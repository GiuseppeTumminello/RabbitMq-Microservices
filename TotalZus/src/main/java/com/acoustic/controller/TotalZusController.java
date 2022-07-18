package com.acoustic.controller;


import com.acoustic.entity.TotalZus;
import com.acoustic.rate.RatesConfigurationProperties;
import com.acoustic.repository.TotalZusRepository;
import com.acoustic.service.SalaryCalculatorService;
import lombok.RequiredArgsConstructor;
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
public class TotalZusController {

    public static final String DESCRIPTION = "description";
    public static final String VALUE = "value";
    public static final int MINIMUM_GROSS = 2000;
    private final TotalZusRepository totalZusRepository;
    private final SalaryCalculatorService salaryCalculatorService;
    private final RatesConfigurationProperties ratesConfigurationProperties;


    @PostMapping("/calculation/{grossMonthlySalary}")
    public Map<String, String> calculateTotalZus(@PathVariable @Min(MINIMUM_GROSS)BigDecimal grossMonthlySalary){
        var totalZus = this.salaryCalculatorService.apply(grossMonthlySalary);
        this.totalZusRepository.save(TotalZus.builder().totalZusAmount(totalZus).totalZusRate(this.ratesConfigurationProperties.getTotalZusRate()).build());
        return Map.of(DESCRIPTION,this.salaryCalculatorService.getDescription(), VALUE, String.valueOf(totalZus));
    }
}
