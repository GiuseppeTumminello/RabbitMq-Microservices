package com.acoustic.service;

import com.acoustic.entity.MonthlyGross;
import com.acoustic.rabbitmqsettings.RabbitMqSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class MonthlyGrossService implements SalaryCalculatorService {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMqSettings rabbitMqSettings;


    @Override
    public String getDescription() {
        return "Monthly gross";
    }

    @Override
    public void sendMonthlyGross(MonthlyGross monthlyGross) {
        this.rabbitTemplate.convertAndSend(this.rabbitMqSettings.getExchangeSalaryCalculator(), this.rabbitMqSettings.getRoutingKeySalaryCalculator(), monthlyGross);
    }

    @Override
    public BigDecimal apply(final BigDecimal grossMonthlySalary) {
        return grossMonthlySalary.setScale(2, RoundingMode.HALF_EVEN);
    }
}
