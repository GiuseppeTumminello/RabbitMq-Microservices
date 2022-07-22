package com.acoustic.service;

import com.acoustic.entity.AnnualNet;
import com.acoustic.rabbitmqsettings.RabbitMqSettings;
import com.acoustic.rate.RatesConfigurationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class AnnualNetService implements SalaryCalculatorService {

    public static final int MONTHS_NUMBER = 12;
    private final RatesConfigurationProperties rate;

    private final RabbitTemplate rabbitTemplate;

    private final RabbitMqSettings rabbitMqSettings;

    @Override
    public BigDecimal apply(BigDecimal grossMonthlySalary) {
        if (grossMonthlySalary.multiply(BigDecimal.valueOf(MONTHS_NUMBER)).compareTo(this.rate.getTaxGrossAmountThreshold()) < 0) {
            return getAnnualNetBasedOnRate(grossMonthlySalary, this.rate.getTaxRate17Rate());
        } else {
            return getAnnualNetBasedOnRate(grossMonthlySalary, this.rate.getTaxRate32Rate());
        }
    }

    @Override
    public String getDescription() {
        return "Annual net";
    }



    private BigDecimal calculateTotalZus(BigDecimal grossMonthlySalary) {
        return grossMonthlySalary.subtract(grossMonthlySalary.multiply(this.rate.getTotalZusRate())).setScale(2, RoundingMode.HALF_EVEN);
    }

    private BigDecimal calculateHealth(BigDecimal grossMonthlySalary) {
        return grossMonthlySalary.subtract(grossMonthlySalary.multiply(this.rate.getHealthRate())).setScale(2, RoundingMode.HALF_EVEN);
    }

    private BigDecimal calculateMonthlyNet(BigDecimal rate, BigDecimal salaryMinusHealth) {
        return salaryMinusHealth.subtract(salaryMinusHealth.multiply(rate)).setScale(2, RoundingMode.HALF_EVEN);
    }


    private BigDecimal getAnnualNetBasedOnRate(BigDecimal grossMonthlySalary, BigDecimal rate) {
        var salaryMinusTotalZus = calculateTotalZus(grossMonthlySalary);
        var salaryMinusHealth = calculateHealth(salaryMinusTotalZus);
        var salaryMonthlyNet = calculateMonthlyNet(rate, salaryMinusHealth);
        return salaryMonthlyNet.multiply(BigDecimal.valueOf(MONTHS_NUMBER).setScale(2, RoundingMode.HALF_EVEN)).setScale(2, RoundingMode.HALF_EVEN);

    }

    @Override
    public void sendAnnualNet(AnnualNet annualNet) {
        this.rabbitTemplate.convertAndSend(this.rabbitMqSettings.getExchangeSalaryCalculator(),this.rabbitMqSettings.getRoutingKeySalaryCalculator(), annualNet);
    }



}
