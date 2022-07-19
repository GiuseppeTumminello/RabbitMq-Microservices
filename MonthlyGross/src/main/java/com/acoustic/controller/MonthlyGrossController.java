package com.acoustic.controller;


import com.acoustic.entity.DataProducer;
import com.acoustic.entity.MonthlyGross;
import com.acoustic.repository.MonthlyGrossRepository;
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
@RequestMapping("/monthly-gross")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@Slf4j
public class MonthlyGrossController {

    public static final int MINIMUM_GROSS = 2000;

    private final MonthlyGrossRepository monthlyGrossRepository;
    private final SalaryCalculatorService salaryCalculatorService;


    @RabbitListener(queues = "${rabbitmq.queueProducers}")
    public void receivedMessage(DataProducer dataProducer) {
        log.warn(dataProducer.getUuid().toString());
        sendAnnualGrossDataToReceiver(dataProducer.getAmount(), dataProducer.getUuid());

    }


    @PostMapping("/calculation/{grossMonthlySalary}")
    public ResponseEntity<Map<String, String>> calculateAnnualNetEndpoint(@PathVariable @Min(MINIMUM_GROSS) BigDecimal grossMonthlySalary) {
        var annualNet = calculateAnnualGross(grossMonthlySalary);
        saveAnnualGross(annualNet, UUID.randomUUID());
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(this.salaryCalculatorService.getDescription(), String.valueOf(annualNet)));
    }

    private void sendAnnualGrossDataToReceiver(BigDecimal grossMonthlySalary, UUID uuid) {
        var monthlyGross = calculateAnnualGross(grossMonthlySalary);
        var monthlyGrossData = saveAnnualGross(monthlyGross, uuid);
        this.salaryCalculatorService.sendMonthlyGross(monthlyGrossData);
    }

    private MonthlyGross saveAnnualGross(BigDecimal monthlyGross, UUID uuid) {
        return this.monthlyGrossRepository.saveAndFlush(MonthlyGross.builder().description(this.salaryCalculatorService.getDescription()).amount(monthlyGross).uuid(uuid).build());
    }

    private BigDecimal calculateAnnualGross(BigDecimal grossMonthlySalary) {
        return this.salaryCalculatorService.apply(grossMonthlySalary);
    }
}
