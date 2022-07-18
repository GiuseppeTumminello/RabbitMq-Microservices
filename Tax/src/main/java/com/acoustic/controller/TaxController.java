package com.acoustic.controller;


import com.acoustic.entity.Tax;
import com.acoustic.repository.TaxRepository;
import com.acoustic.service.SalaryCalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tax")
@CrossOrigin
public class TaxController {

    private static final String DESCRIPTION = "description";
    private static final String VALUE = "value";
    public static final int MINIMUM_GROSS = 2000;
    private final TaxRepository taxRepository;
    private final SalaryCalculatorService salaryCalculatorService;


    @PostMapping("/calculation/{grossMonthlySalary}")
    public Map<String, String> calculateTax(@PathVariable @Min(MINIMUM_GROSS) BigDecimal grossMonthlySalary) {
        var tax = this.salaryCalculatorService.apply(grossMonthlySalary);
        this.taxRepository.save(Tax.builder().taxAmount(tax).build());
        return Map.of(DESCRIPTION,this.salaryCalculatorService.getDescription(), VALUE, String.valueOf(tax));
    }


}
