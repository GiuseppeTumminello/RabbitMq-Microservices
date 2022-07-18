package com.acoustic.SpringPolandSalaryCalculator.calculator;

import com.acoustic.SpringPolandSalaryCalculator.rates.RatesConfigurationPropertiesTest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.UnaryOperator;


@Component
public class SalaryCalculatorResponse {

    private static final int MONTHS_NUMBER = 12;
    private final RatesConfigurationPropertiesTest ratesConfigurationPropertiesTest;


    public SalaryCalculatorResponse(final RatesConfigurationPropertiesTest ratesConfigurationPropertiesTest) {
        this.ratesConfigurationPropertiesTest = ratesConfigurationPropertiesTest;

    }

    private UnaryOperator<BigDecimal> getAmountByRate(BigDecimal rate) {
        return gross -> gross.multiply(rate).setScale(2, RoundingMode.HALF_EVEN);
    }

    private UnaryOperator<BigDecimal> getHealth() {
        return gross -> gross.subtract(getAmountByRate(ratesConfigurationPropertiesTest.getTotalZusRate()).apply(gross))
                .multiply(ratesConfigurationPropertiesTest.getHealthRate())
                .setScale(2, RoundingMode.HALF_EVEN);
    }

    private UnaryOperator<BigDecimal> getNet() {
        return gross -> gross.subtract(getAmountByRate(ratesConfigurationPropertiesTest.getTotalZusRate()).apply(gross)
                .add((getTaxAmount().apply(gross)).add(getHealth().apply(gross)))).setScale(2, RoundingMode.HALF_EVEN);
    }


    private UnaryOperator<BigDecimal> getNetYearly() {
        return gross -> getNet().apply(gross)
                .multiply(BigDecimal.valueOf(MONTHS_NUMBER))
                .setScale(2, RoundingMode.HALF_EVEN);
    }

    private UnaryOperator<BigDecimal> getTaxAmount() {
        return gross -> (gross.multiply(BigDecimal.valueOf(MONTHS_NUMBER))
                .compareTo(ratesConfigurationPropertiesTest.getTaxGrossAmountThreshold()) < 0)
                ? getTaxAmountBasedOnRate(
                gross,
                ratesConfigurationPropertiesTest.getTaxRate17Rate())
                : getTaxAmountBasedOnRate(gross, ratesConfigurationPropertiesTest.getTaxRate32Rate());
    }


    private BigDecimal getTaxAmountBasedOnRate(BigDecimal gross, BigDecimal rate) {
        return gross.subtract(getAmountByRate(ratesConfigurationPropertiesTest.getTotalZusRate()).apply(gross))
                .subtract(getHealth().apply(gross))
                .multiply(rate)
                .setScale(2, RoundingMode.HALF_EVEN);
    }


    public Map<String, BigDecimal> expectedValue(BigDecimal grossMonthlySalary, boolean average) {
        Map<String, BigDecimal> expected = new LinkedHashMap<>();
        expected.put("Total zus", getAmountByRate(ratesConfigurationPropertiesTest.getTotalZusRate()).apply(grossMonthlySalary));
        expected.put("Annual gross", getAmountByRate(BigDecimal.valueOf(MONTHS_NUMBER)).apply(grossMonthlySalary));
        expected.put("Pension zus", getAmountByRate(ratesConfigurationPropertiesTest.getPensionZusRate()).apply(grossMonthlySalary));
        expected.put("Annual net", getNetYearly().apply(grossMonthlySalary));
        expected.put("Disability zus", getAmountByRate(ratesConfigurationPropertiesTest.getDisabilityZusRate()).apply(grossMonthlySalary));
        expected.put("Health", getHealth().apply(grossMonthlySalary));
        expected.put("Monthly gross", grossMonthlySalary.setScale(2, RoundingMode.HALF_EVEN));
        expected.put("Monthly net", getNet().apply(grossMonthlySalary));
        expected.put("Sickness zus", getAmountByRate(ratesConfigurationPropertiesTest.getSicknessZusRate()).apply(grossMonthlySalary));
        expected.put("Tax", getTaxAmount().apply(grossMonthlySalary));
        return checkIfAverage(grossMonthlySalary, average, expected);
    }

    private Map<String, BigDecimal> checkIfAverage(
            BigDecimal grossMonthlySalary, boolean average, Map<String, BigDecimal> expected) {
        if (average) {
            expected.put("Average", grossMonthlySalary.setScale(2, RoundingMode.HALF_EVEN));
            return expected;
        }
        expected.put("Total zus", getAmountByRate(ratesConfigurationPropertiesTest.getTotalZusRate()).apply(grossMonthlySalary));

        return expected;
    }
}






