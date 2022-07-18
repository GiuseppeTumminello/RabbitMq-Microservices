package com.acoustic.controller;


import com.acoustic.entity.PensionZus;
import com.acoustic.repository.PensionZusRepository;
import com.acoustic.service.SalaryCalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Map;


@RestController
@RequestMapping("/pension-zus")
@RequiredArgsConstructor
@Validated
@CrossOrigin
public class PensionZusController {

    public static final String DESCRIPTION = "description";
    public static final String VALUE = "value";
    public static final int MINIMUM_SALARY = 2000;
    private final PensionZusRepository pensionZusRepository;
    private final SalaryCalculatorService salaryCalculatorService;


    @PostMapping("/calculation/{grossMonthlySalary}")
    public Map<String, String> calculatePensionZus(@PathVariable @Min(MINIMUM_SALARY) BigDecimal grossMonthlySalary) {
        var pensionZus = this.salaryCalculatorService.apply(grossMonthlySalary);
        this.pensionZusRepository.save(PensionZus.builder().pensionZusAmount(pensionZus).build());
        return Map.of(DESCRIPTION, this.salaryCalculatorService.getDescription(), VALUE, String.valueOf(pensionZus));
    }
}
