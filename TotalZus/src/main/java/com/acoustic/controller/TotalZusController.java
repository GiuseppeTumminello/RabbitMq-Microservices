package com.acoustic.controller;


import com.acoustic.entity.DataProducer;
import com.acoustic.entity.TotalZus;
import com.acoustic.repository.TotalZusRepository;
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
@RequestMapping("/total-zus")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@Slf4j
public class TotalZusController {


    public static final int MINIMUM_GROSS = 2000;
    private final TotalZusRepository totalZusRepository;
    private final SalaryCalculatorService salaryCalculatorService;


    @RabbitListener(queues = "${rabbitmq.queueProducers}")
    public void receivedMessage(DataProducer dataProducer) {
        log.warn(dataProducer.getUuid().toString());
        sendTotalZusToReceiver(dataProducer.getAmount(), dataProducer.getUuid());

    }


    @PostMapping("/calculation/{grossMonthlySalary}")
    public ResponseEntity<Map<String, String>> calculateTotalZusEndpoint(@PathVariable @Min(MINIMUM_GROSS) BigDecimal grossMonthlySalary) {
        var totalZus = calculateTotalZus(grossMonthlySalary);
        saveTotalZus(totalZus, UUID.randomUUID());
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(this.salaryCalculatorService.getDescription(), String.valueOf(totalZus)));
    }

    private void sendTotalZusToReceiver(BigDecimal grossMonthlySalary, UUID uuid) {
        var totalZus = calculateTotalZus(grossMonthlySalary);
        var totalZusData = saveTotalZus(totalZus, uuid);
        this.salaryCalculatorService.sendTotalZus(totalZusData);
    }

    private TotalZus saveTotalZus(BigDecimal totalZus, UUID uuid) {
        return this.totalZusRepository.saveAndFlush(TotalZus.builder().description(this.salaryCalculatorService.getDescription()).amount(totalZus).uuid(uuid).build());
    }

    private BigDecimal calculateTotalZus(BigDecimal grossMonthlySalary) {
        return this.salaryCalculatorService.apply(grossMonthlySalary);
    }
}