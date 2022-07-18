package com.acoustic.service;

import com.acoustic.rate.RatesConfigurationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class TaxService implements SalaryCalculatorService {

    private static final int MONTHS_NUMBER = 12;
    private final RatesConfigurationProperties rate;


    @Override
    public BigDecimal apply(BigDecimal grossMonthlySalary) {
        if (grossMonthlySalary.multiply(BigDecimal.valueOf(MONTHS_NUMBER)).compareTo(this.rate.getTaxGrossAmountThreshold()) < 0) {
            return getTaxAmountBasedOnRate(grossMonthlySalary, this.rate.getTaxRate17Rate());
        } else {
            return getTaxAmountBasedOnRate(grossMonthlySalary, this.rate.getTaxRate32Rate());
        }
    }

    @Override
    public String getDescription() {
        return "Tax";
    }
    private BigDecimal calculateTotalZus(BigDecimal grossMonthlySalary) {
        return grossMonthlySalary.subtract(grossMonthlySalary.multiply(this.rate.getTotalZusRate()));
    }

    private BigDecimal calculateHealth(BigDecimal grossMonthlySalary) {
        return grossMonthlySalary.subtract(grossMonthlySalary.multiply(this.rate.getHealthRate()));
    }


    private BigDecimal getTaxAmountBasedOnRate(BigDecimal grossMonthlySalary, BigDecimal rate) {
        var salaryMinusTotalZus = calculateTotalZus(grossMonthlySalary);
        var salaryMinusHealth = calculateHealth(salaryMinusTotalZus);
        return salaryMinusHealth.multiply(rate).setScale(2, RoundingMode.HALF_EVEN);

    }
}
