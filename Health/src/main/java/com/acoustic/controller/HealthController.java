package com.acoustic.controller;


import com.acoustic.entity.Health;
import com.acoustic.repository.HealthRepository;
import com.acoustic.service.SalaryCalculatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/health")
@CrossOrigin
@Slf4j
public class HealthController {

    public static final String DESCRIPTION = "description";
    public static final String VALUE = "value";
    public static final int MINIMUM_GROSS = 2000;
    private final HealthRepository healthRepository;
    private final SalaryCalculatorService salaryCalculatorService;


    @PostMapping("/calculation/{grossMonthlySalary}")
    public ResponseEntity<Map<String, String>> calculateHealth(@PathVariable @Min(MINIMUM_GROSS) BigDecimal grossMonthlySalary) {
        var health = this.salaryCalculatorService.apply(grossMonthlySalary);
        var healthData = this.healthRepository.saveAndFlush(Health.builder().description(this.salaryCalculatorService.getDescription()).value(String.valueOf(health)).build());
        this.salaryCalculatorService.sendAnnualNet(healthData);
        log.warn(healthData.toString());
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(DESCRIPTION,this.salaryCalculatorService.getDescription(), VALUE, String.valueOf(health)));
    }
}
