package com.acoustic.service;

import com.acoustic.entity.MonthlyNet;
import com.acoustic.rabbitmqsettings.RabbitMqSettings;
import com.acoustic.rate.RatesConfigurationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class MonthlyNetService implements SalaryCalculatorService {

    private static final int MONTHS_NUMBER = 12;
    private final RatesConfigurationProperties rate;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMqSettings rabbitMqSettings;

    @Override
    public BigDecimal apply(BigDecimal grossMonthlySalary) {
        if (grossMonthlySalary.multiply(BigDecimal.valueOf(MONTHS_NUMBER)).compareTo(this.rate.getTaxGrossAmountThreshold()) < 0) {
            return getMonthlyNetBasedOnRate(grossMonthlySalary, this.rate.getTaxRate17Rate());
        } else {
            return getMonthlyNetBasedOnRate(grossMonthlySalary, this.rate.getTaxRate32Rate());
        }
    }

    @Override
    public String getDescription() {
        return "Monthly net";
    }

    @Override
    public void sendMonthlyNet(MonthlyNet monthlyNet) {
        this.rabbitTemplate.convertAndSend(this.rabbitMqSettings.getExchangeSalaryCalculator(), this.rabbitMqSettings.getRoutingKeySalaryCalculator(), monthlyNet);
    }

    private BigDecimal calculateTotalZus(BigDecimal grossMonthlySalary) {
        return grossMonthlySalary.subtract(grossMonthlySalary.multiply(this.rate.getTotalZusRate()));
    }

    private BigDecimal calculateHealth(BigDecimal grossMonthlySalary) {
        return grossMonthlySalary.subtract(grossMonthlySalary.multiply(this.rate.getHealthRate()));
    }

    private BigDecimal getMonthlyNetBasedOnRate(BigDecimal grossMonthlySalary, BigDecimal rate) {
        var salaryMinusTotalZus = calculateTotalZus(grossMonthlySalary);
        var salaryMinusHealth = calculateHealth(salaryMinusTotalZus);
        return salaryMinusHealth.subtract(salaryMinusHealth.multiply(rate)).setScale(2, RoundingMode.HALF_EVEN);

    }

}
