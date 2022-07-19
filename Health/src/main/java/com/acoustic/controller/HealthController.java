package com.acoustic.controller;


import com.acoustic.entity.DataProducer;
import com.acoustic.entity.Health;
import com.acoustic.repository.HealthRepository;
import com.acoustic.service.SalaryCalculatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/health")
@CrossOrigin
@Slf4j
public class HealthController {


    public static final int MINIMUM_GROSS = 2000;
    private final HealthRepository healthRepository;
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
        var health = calculateAnnualGross(grossMonthlySalary);
        var healthData = saveAnnualGross(health, uuid);
        this.salaryCalculatorService.sendAnnualNet(healthData);
    }

    private Health saveAnnualGross(BigDecimal health, UUID uuid) {
        return this.healthRepository.saveAndFlush(Health.builder().description(this.salaryCalculatorService.getDescription()).amount(health).uuid(uuid).build());
    }

    private BigDecimal calculateAnnualGross(BigDecimal grossMonthlySalary) {
        return this.salaryCalculatorService.apply(grossMonthlySalary);
    }
}
