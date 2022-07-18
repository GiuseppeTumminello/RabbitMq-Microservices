package com.acoustic.controller;


import com.acoustic.entity.MonthlyNet;
import com.acoustic.repository.MonthlyNetRepository;
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
@RequestMapping("/monthly-net")
@CrossOrigin
@Slf4j
public class MonthlyNetController {

    public static final String DESCRIPTION = "description";
    public static final String VALUE = "value";
    public static final int MINIMUM_GROSS = 2000;
    private final MonthlyNetRepository monthlyNetRepository;
    private final SalaryCalculatorService salaryCalculatorService;



    @PostMapping("/calculation/{grossMonthlySalary}")
    public ResponseEntity<Map<String, String>> calculateMonthlyNet(@PathVariable @Min(MINIMUM_GROSS) BigDecimal grossMonthlySalary) {
        var monthlyNetSalary = this.salaryCalculatorService.apply(grossMonthlySalary);
        var monthlyNetData = this.monthlyNetRepository.saveAndFlush(MonthlyNet.builder().description(this.salaryCalculatorService.getDescription()).value(String.valueOf(monthlyNetSalary)).build());
        this.salaryCalculatorService.sendMonthlyNet(monthlyNetData);
        log.warn(monthlyNetData.toString());
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(DESCRIPTION,this.salaryCalculatorService.getDescription(), VALUE, String.valueOf(monthlyNetSalary)));

    }


}
