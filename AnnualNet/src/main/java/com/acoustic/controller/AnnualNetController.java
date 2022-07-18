package com.acoustic.controller;


import com.acoustic.entity.AnnualNet;
import com.acoustic.repository.AnnualNetRepository;
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
@RequestMapping("/annual-net")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@Slf4j
public class AnnualNetController {

    public static final String DESCRIPTION = "description";
    public static final String VALUE = "value";
    public static final int MINIMUM_GROSS = 2000;
    private final AnnualNetRepository annualNetRepository;
    private final SalaryCalculatorService salaryCalculatorService;




    @PostMapping("/calculation/{grossMonthlySalary}")
    public ResponseEntity<Map<String, String>> calculateAnnualNet(@PathVariable @Min(MINIMUM_GROSS)BigDecimal grossMonthlySalary){
        var annualNet = this.salaryCalculatorService.apply(grossMonthlySalary);
        var annualNetData = this.annualNetRepository.saveAndFlush(AnnualNet.builder().description(this.salaryCalculatorService.getDescription()).value(String.valueOf(annualNet)).build());
        this.salaryCalculatorService.sendHealth(annualNetData);
        log.info(annualNetData.toString());
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(DESCRIPTION,this.salaryCalculatorService.getDescription(), VALUE, String.valueOf(annualNet)));
    }



}
