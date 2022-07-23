package com.acoustic.controller;


import com.acoustic.entity.AnnualGross;
import com.acoustic.repository.AnnualGrossRepository;
import com.acoustic.service.SalaryCalculatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/annual-gross")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@Slf4j
public class AnnualGrossController {

    public static final int MINIMUM_GROSS = 2000;
    private static final String ANNUAL_GROSS_RECEIVER_ID = "AnnualGrossReceiverId";
    private final SalaryCalculatorService salaryCalculatorService;
    private final AnnualGrossRepository annualGrossRepository;


    @RabbitListener(id = ANNUAL_GROSS_RECEIVER_ID, queues = "${rabbitmq.queueAnnualGross}")
    public void receiveMessages(AnnualGross dataProducer) {
        log.warn(dataProducer.getDescription() + " " + dataProducer.getAmount() + " " + dataProducer.getUuid() + " " + dataProducer.getId());
        sendAnnualGrossDataToSalaryCalculatorOrchestrator(dataProducer.getAmount(),dataProducer.getUuid());

    }


    @PostMapping("/calculation/{grossMonthlySalary}")
    public ResponseEntity<Map<String, String>> calculationAnnualGrossEndpoint(@PathVariable @Min(MINIMUM_GROSS)BigDecimal grossMonthlySalary){
        var annualGross = calculateAnnualGross(grossMonthlySalary);
        saveAnnualGross(annualGross, UUID.randomUUID());
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(this.salaryCalculatorService.getDescription(), String.valueOf(annualGross)));
    }

    private void sendAnnualGrossDataToSalaryCalculatorOrchestrator(BigDecimal grossMonthlySalary, UUID uuid) {
        var annualGrossSalary = calculateAnnualGross(grossMonthlySalary);
        var annualGrossData = saveAnnualGross(annualGrossSalary, uuid);
        this.salaryCalculatorService.sendAnnualGross(annualGrossData);
    }

    private AnnualGross saveAnnualGross(BigDecimal annualGross, UUID uuid) {
        return this.annualGrossRepository.saveAndFlush(AnnualGross.builder().description(this.salaryCalculatorService.getDescription()).amount(annualGross).uuid(uuid).build());
    }

    private BigDecimal calculateAnnualGross(BigDecimal grossMonthlySalary) {
        return this.salaryCalculatorService.apply(grossMonthlySalary);
    }
}
