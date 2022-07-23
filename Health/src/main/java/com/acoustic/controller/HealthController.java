package com.acoustic.controller;


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
    private static final String HEALTH_RECEIVER_ID = "healthReceiverId";
    private final HealthRepository healthRepository;
    private final SalaryCalculatorService salaryCalculatorService;


    @RabbitListener(id = HEALTH_RECEIVER_ID,queues = "${rabbitmq.queueHealth}")
    public void receiveMessage(Health health) {
        log.warn(health.getUuid().toString());
        sendHealthDataToSalaryCalculatorOrchestrator(health.getAmount(), health.getUuid());

    }


    @PostMapping("/calculation/{grossMonthlySalary}")
    public ResponseEntity<Map<String, String>> calculationHealthEndpoint(@PathVariable @Min(MINIMUM_GROSS) BigDecimal grossMonthlySalary) {
        var health = calculateHealth(grossMonthlySalary);
        saveHealth(health, UUID.randomUUID());
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(this.salaryCalculatorService.getDescription(), String.valueOf(health)));
    }

    private void sendHealthDataToSalaryCalculatorOrchestrator(BigDecimal grossMonthlySalary, UUID uuid) {
        var health = calculateHealth(grossMonthlySalary);
        var healthData = saveHealth(health, uuid);
        this.salaryCalculatorService.sendHealth(healthData);
    }

    private Health saveHealth(BigDecimal health, UUID uuid) {
        return this.healthRepository.saveAndFlush(Health.builder().description(this.salaryCalculatorService.getDescription()).amount(health).uuid(uuid).build());
    }

    private BigDecimal calculateHealth(BigDecimal grossMonthlySalary) {
        return this.salaryCalculatorService.apply(grossMonthlySalary);
    }
}
