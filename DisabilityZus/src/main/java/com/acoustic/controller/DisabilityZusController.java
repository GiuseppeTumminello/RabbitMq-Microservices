package com.acoustic.controller;


import com.acoustic.entity.DisabilityZus;
import com.acoustic.repository.DisabilityZusRepository;
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
@RequestMapping("/disability-zus")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@Slf4j
public class DisabilityZusController {

    public static final String DESCRIPTION = "description";
    public static final String VALUE = "value";
    public static final int MINIMUM_GROSS = 2000;
    private final DisabilityZusRepository disabilityZusRepository;
    private final SalaryCalculatorService salaryCalculatorService;


    @PostMapping("/calculation/{grossMonthlySalary}")
    public ResponseEntity<Map<String, String>> calculateDisabilityZus(@PathVariable @Min(MINIMUM_GROSS) BigDecimal grossMonthlySalary) {
        var disabilityZus = salaryCalculatorService.apply(grossMonthlySalary);
        var disabilityZusData = this.disabilityZusRepository.saveAndFlush(DisabilityZus.builder().description(this.salaryCalculatorService.getDescription()).amount(String.valueOf(disabilityZus)).build());
        this.salaryCalculatorService.sendDisabilityZus(disabilityZusData);
        log.info(disabilityZusData.toString());
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(DESCRIPTION,this.salaryCalculatorService.getDescription(), VALUE, String.valueOf(disabilityZus)));
    }
}
