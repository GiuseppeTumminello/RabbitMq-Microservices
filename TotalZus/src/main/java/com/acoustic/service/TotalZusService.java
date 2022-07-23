package com.acoustic.service;

import com.acoustic.entity.TotalZus;
import com.acoustic.rabbitmqsettings.RabbitMqSettings;
import com.acoustic.rate.RatesConfigurationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class TotalZusService implements SalaryCalculatorService{

    private final RatesConfigurationProperties ratesConfigurationProperties;

    private final RabbitTemplate rabbitTemplate;

    private final RabbitMqSettings rabbitMqSettings;


    @Override
    public String getDescription() {
        return "Total zus";
    }

    @Override
    public void sendTotalZus(TotalZus totalZus) {
        this.rabbitTemplate.convertAndSend(this.rabbitMqSettings.getExchangeSalaryCalculator(), this.rabbitMqSettings.getRoutingKeySalaryCalculator(), totalZus);
    }

    @Override
    public BigDecimal apply(final BigDecimal grossMonthlySalary) {
        return grossMonthlySalary.multiply(this.ratesConfigurationProperties.getTotalZusRate()).setScale(2, RoundingMode.HALF_EVEN);
    }
}
