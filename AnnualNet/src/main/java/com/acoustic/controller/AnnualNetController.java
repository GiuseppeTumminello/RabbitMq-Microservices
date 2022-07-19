package com.acoustic.controller;


import com.acoustic.entity.AnnualNet;
import com.acoustic.entity.DataProducer;
import com.acoustic.repository.AnnualNetRepository;
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
@RequestMapping("/annual-net")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@Slf4j
public class AnnualNetController {


    public static final int MINIMUM_GROSS = 2000;
    private final AnnualNetRepository annualNetRepository;
    private final SalaryCalculatorService salaryCalculatorService;




    @RabbitListener(queues = "${rabbitmq.queueProducers}")
    public void receivedMessage(DataProducer dataProducer) {
        log.warn(dataProducer.getUuid().toString());
        sendAnnualGrossDataToReceiver(dataProducer.getAmount(),dataProducer.getUuid());

    }


    @PostMapping("/calculation/{grossMonthlySalary}")
    public ResponseEntity<Map<String, String>> calculateAnnualNetEndpoint(@PathVariable @Min(MINIMUM_GROSS)BigDecimal grossMonthlySalary){
        var annualNet = calculateAnnualGross(grossMonthlySalary);
        saveAnnualGross(annualNet, UUID.randomUUID());
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(this.salaryCalculatorService.getDescription(), String.valueOf(annualNet)));
    }

    private void sendAnnualGrossDataToReceiver(BigDecimal grossMonthlySalary, UUID uuid) {
        var annualGrossSalary = calculateAnnualGross(grossMonthlySalary);
        var AnnualGrossData = saveAnnualGross(annualGrossSalary, uuid);
        this.salaryCalculatorService.sendAnnualNet(AnnualGrossData);
    }

    private AnnualNet saveAnnualGross(BigDecimal annualNet, UUID uuid) {
        return this.annualNetRepository.saveAndFlush(AnnualNet.builder().description(this.salaryCalculatorService.getDescription()).amount(annualNet).uuid(uuid).build());
    }

    private BigDecimal calculateAnnualGross(BigDecimal grossMonthlySalary) {
        return this.salaryCalculatorService.apply(grossMonthlySalary);
    }



}
