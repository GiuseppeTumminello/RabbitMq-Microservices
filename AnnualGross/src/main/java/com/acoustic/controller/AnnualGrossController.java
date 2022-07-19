package com.acoustic.controller;


import com.acoustic.entity.AnnualGross;
import com.acoustic.repository.AnnualGrossRepository;
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
@RequestMapping("/annual-gross")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@Slf4j
public class AnnualGrossController {

    public static final String DESCRIPTION = "description";
    public static final String VALUE = "value";
    public static final int MINIMUM_GROSS = 2000;

    private final SalaryCalculatorService salaryCalculatorService;

    private final AnnualGrossRepository annualGrossRepository;



    @PostMapping("/calculation/{grossMonthlySalary}")
    public ResponseEntity<Map<String, String>> calculateAnnualGross(@PathVariable @Min(MINIMUM_GROSS)BigDecimal grossMonthlySalary){
        var annualGross = this.salaryCalculatorService.apply(grossMonthlySalary);
        var annualGrossData = this.annualGrossRepository.saveAndFlush(AnnualGross.builder().description(salaryCalculatorService.getDescription()).amount(String.valueOf(annualGross)).build());
        this.salaryCalculatorService.sendAnnualGross(annualGrossData);
        log.info(annualGrossData.toString());
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(DESCRIPTION,this.salaryCalculatorService.getDescription(), VALUE, String.valueOf(annualGross)));
    }

}
