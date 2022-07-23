package com.acoustic.controller;


import com.acoustic.entity.SicknessZus;
import com.acoustic.repository.SicknessZusRepository;
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
@RequestMapping("/sickness-zus")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@Slf4j
public class SicknessZusController {

    private  static final String SICKNESS_ZUS_RECEIVER_ID = "sicknessZusReceiverId";

    public static final int MINIMUM_GROSS = 2000;

    private final SicknessZusRepository sicknessZusRepository;
    private final SalaryCalculatorService salaryCalculatorService;



    @RabbitListener(id = SICKNESS_ZUS_RECEIVER_ID, queues = "${rabbitmq.queueSicknessZus}")
    public void receiveMessage(SicknessZus sicknessZus) {
        log.warn(sicknessZus.getUuid().toString());
        sendSicknessZusDataToSalaryCalculatorOrchestrator(sicknessZus.getAmount(), sicknessZus.getUuid());

    }


    @PostMapping("/calculation/{grossMonthlySalary}")
    public ResponseEntity<Map<String, String>> calculationSicknessZusEndpoint(@PathVariable @Min(MINIMUM_GROSS) BigDecimal grossMonthlySalary) {
        var sicknessZus = calculateSicknessZus(grossMonthlySalary);
        saveSicknessZus(sicknessZus, UUID.randomUUID());
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(this.salaryCalculatorService.getDescription(), String.valueOf(sicknessZus)));
    }

    private void sendSicknessZusDataToSalaryCalculatorOrchestrator(BigDecimal grossMonthlySalary, UUID uuid) {
        var sicknessZus = calculateSicknessZus(grossMonthlySalary);
        var sicknessZusData = saveSicknessZus(sicknessZus, uuid);
        this.salaryCalculatorService.sendSicknessZus(sicknessZusData);
    }

    private SicknessZus saveSicknessZus(BigDecimal sicknessZus, UUID uuid) {
        return this.sicknessZusRepository.saveAndFlush(SicknessZus.builder().description(this.salaryCalculatorService.getDescription()).amount(sicknessZus).uuid(uuid).build());
    }

    private BigDecimal calculateSicknessZus(BigDecimal grossMonthlySalary) {
        return this.salaryCalculatorService.apply(grossMonthlySalary);
    }

}
