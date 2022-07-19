package com.acoustic.controller;


import com.acoustic.entity.MonthlyGross;
import com.acoustic.repository.MonthlyGrossRepository;
import com.acoustic.service.SalaryCalculatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Map;


@RestController
@RequestMapping("/monthly-gross")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@Slf4j
public class MonthlyGrossController {

    public static final String DESCRIPTION = "description";
    public static final String VALUE = "value";
    public static final int MINIMUM_GROSS = 2000;

    private final MonthlyGrossRepository monthlyGrossRepository;
    private final SalaryCalculatorService salaryCalculatorService;


    @PostMapping("/calculation/{grossMonthlySalary}")
    public ResponseEntity<Map<String, String>> calculateMonthlyGross(@PathVariable @Min(MINIMUM_GROSS)BigDecimal grossMonthlySalary){
        var monthlyGross = this.salaryCalculatorService.apply(grossMonthlySalary);
        var monthlyGrossData = this.monthlyGrossRepository.saveAndFlush(MonthlyGross.builder().description(this.salaryCalculatorService.getDescription()).amount(String.valueOf(monthlyGross)).build());
        this.salaryCalculatorService.sendMonthlyGross(monthlyGrossData);
        log.warn(monthlyGrossData.toString());
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(DESCRIPTION,this.salaryCalculatorService.getDescription(), VALUE, String.valueOf(monthlyGross)));
    }
}
