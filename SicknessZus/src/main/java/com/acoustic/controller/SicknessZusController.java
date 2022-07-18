package com.acoustic.controller;


import com.acoustic.entity.SicknessZus;
import com.acoustic.repository.SicknessZusRepository;
import com.acoustic.service.SalaryCalculatorService;
import lombok.RequiredArgsConstructor;
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
public class SicknessZusController {

    private static final String DESCRIPTION = "description";
    static final String VALUE = "value";
    public static final int MINIMUM_GROSS = 2000;

    private final SicknessZusRepository sicknessZusRepository;
    private final SalaryCalculatorService salaryCalculatorService;



    @PostMapping("/calculation/{grossMonthlySalary}")
    public Map<String, String> calculateSicknessZus(@PathVariable @Min(MINIMUM_GROSS)BigDecimal grossMonthlySalary){
        var sicknessZus = this.salaryCalculatorService.apply(grossMonthlySalary);
        this.sicknessZusRepository.save(SicknessZus.builder().sicknessZusAmount(sicknessZus).build());
        return  Map.of(DESCRIPTION,this.salaryCalculatorService.getDescription(), VALUE, String.valueOf(sicknessZus));
    }
}
