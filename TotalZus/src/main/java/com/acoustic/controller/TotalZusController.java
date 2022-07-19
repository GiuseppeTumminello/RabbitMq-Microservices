package com.acoustic.controller;


import com.acoustic.entity.TotalZus;
import com.acoustic.repository.TotalZusRepository;
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
@RequestMapping("/total-zus")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@Slf4j
public class TotalZusController {

    public static final String DESCRIPTION = "description";
    public static final String VALUE = "value";
    public static final int MINIMUM_GROSS = 2000;
    private final TotalZusRepository totalZusRepository;
    private final SalaryCalculatorService salaryCalculatorService;


    @PostMapping("/calculation/{grossMonthlySalary}")
    public ResponseEntity<Map<String, String>> calculateTotalZus(@PathVariable @Min(MINIMUM_GROSS) BigDecimal grossMonthlySalary) {
        var totalZus = this.salaryCalculatorService.apply(grossMonthlySalary);
        var totalZusData = this.totalZusRepository.saveAndFlush(TotalZus.builder().description(this.salaryCalculatorService.getDescription()).amount(String.valueOf(totalZus)).build());
        this.salaryCalculatorService.sendTotalZus(totalZusData);
        log.warn(totalZusData.toString());
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(DESCRIPTION, this.salaryCalculatorService.getDescription(), VALUE, String.valueOf(totalZus)));
    }
}
