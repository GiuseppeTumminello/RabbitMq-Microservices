package com.acoustic.controller;


import com.acoustic.entity.MonthlyNet;
import com.acoustic.repository.MonthlyNetRepository;
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
@RequestMapping("/monthly-net")
@CrossOrigin
@Slf4j
public class MonthlyNetController {


    public static final int MINIMUM_GROSS = 2000;
    private final MonthlyNetRepository monthlyNetRepository;
    private final SalaryCalculatorService salaryCalculatorService;



    @RabbitListener(queues = "${rabbitmq.queueMonthlyNet}")
    public void receivedMessage(MonthlyNet monthlyNet) {
        log.warn(monthlyNet.getUuid().toString());
        sendMonthlyNetDataToSalaryCalculatorOrchestrator(monthlyNet.getAmount(), monthlyNet.getUuid());

    }


    @PostMapping("/calculation/{grossMonthlySalary}")
    public ResponseEntity<Map<String, String>> calculateMonthlyNetEndpoint(@PathVariable @Min(MINIMUM_GROSS) BigDecimal grossMonthlySalary) {
        var annualNet = calculateMonthlyData(grossMonthlySalary);
        saveMonthlyData(annualNet, UUID.randomUUID());
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(this.salaryCalculatorService.getDescription(), String.valueOf(annualNet)));
    }

    private void sendMonthlyNetDataToSalaryCalculatorOrchestrator(BigDecimal grossMonthlySalary, UUID uuid) {
        var monthlyNet = calculateMonthlyData(grossMonthlySalary);
        var monthlyNetData = saveMonthlyData(monthlyNet, uuid);
        this.salaryCalculatorService.sendMonthlyNet(monthlyNetData);
    }

    private MonthlyNet saveMonthlyData(BigDecimal monthlyNet, UUID uuid) {
        return this.monthlyNetRepository.saveAndFlush(MonthlyNet.builder().description(this.salaryCalculatorService.getDescription()).amount(monthlyNet).uuid(uuid).build());
    }

    private BigDecimal calculateMonthlyData(BigDecimal grossMonthlySalary) {
        return this.salaryCalculatorService.apply(grossMonthlySalary);
    }


}
