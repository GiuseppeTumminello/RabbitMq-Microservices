package com.acoustic.controller;


import com.acoustic.entity.SicknessZus;
import com.acoustic.repository.SicknessZusRepository;
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
@RequestMapping("/sickness-zus")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@Slf4j
public class SicknessZusController {

    private static final String DESCRIPTION = "description";
    static final String VALUE = "value";
    public static final int MINIMUM_GROSS = 2000;

    private final SicknessZusRepository sicknessZusRepository;
    private final SalaryCalculatorService salaryCalculatorService;



    @PostMapping("/calculation/{grossMonthlySalary}")
    public ResponseEntity<Map<String, String>>calculateSicknessZus(@PathVariable @Min(MINIMUM_GROSS)BigDecimal grossMonthlySalary){
        var sicknessZus = this.salaryCalculatorService.apply(grossMonthlySalary);
        var sicknessZusData = this.sicknessZusRepository.saveAndFlush(SicknessZus.builder().description(this.salaryCalculatorService.getDescription()).amount(String.valueOf(sicknessZus)).build());
        this.salaryCalculatorService.sendSicknessZus(sicknessZusData);
        log.warn(sicknessZusData.toString());
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(DESCRIPTION, this.salaryCalculatorService.getDescription(), VALUE, String.valueOf(sicknessZus)));

    }
}
