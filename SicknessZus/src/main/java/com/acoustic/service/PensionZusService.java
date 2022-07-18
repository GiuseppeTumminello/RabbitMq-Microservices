package com.acoustic.service;

import com.acoustic.rate.RatesConfigurationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class PensionZusService implements SalaryCalculatorService{

    private final RatesConfigurationProperties ratesConfigurationProperties;


    @Override
    public String getDescription() {
        return "Sickness zus";
    }

    @Override
    public BigDecimal apply(final BigDecimal grossMonthlySalary) {
        return grossMonthlySalary.multiply(this.ratesConfigurationProperties.getSicknessZusRate()).setScale(2, RoundingMode.HALF_EVEN);
    }
}
