package com.acoustic.controller;


import com.acoustic.entity.Tax;
import com.acoustic.repository.TaxRepository;
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
@RequestMapping("/tax")
@CrossOrigin
@Slf4j
public class TaxController {

    private static final String DESCRIPTION = "description";
    private static final String VALUE = "value";
    public static final int MINIMUM_GROSS = 2000;
    private final TaxRepository taxRepository;
    private final SalaryCalculatorService salaryCalculatorService;


    @PostMapping("/calculation/{grossMonthlySalary}")
    public ResponseEntity<Map<String, String>> calculateTax(@PathVariable @Min(MINIMUM_GROSS) BigDecimal grossMonthlySalary) {
        var tax = this.salaryCalculatorService.apply(grossMonthlySalary);
        var taxData = this.taxRepository.saveAndFlush(Tax.builder().description(this.salaryCalculatorService.getDescription()).amount(String.valueOf(tax)).build());
        this.salaryCalculatorService.sendTax(taxData);
        log.warn(taxData.toString());
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(DESCRIPTION, this.salaryCalculatorService.getDescription(), VALUE, String.valueOf(tax)));

    }


}
