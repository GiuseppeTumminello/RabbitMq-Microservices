package com.acoustic.service;

import com.acoustic.entity.DisabilityZus;
import com.acoustic.rabbitmqsettings.RabbitMqSettings;
import com.acoustic.rate.RatesConfigurationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class DisabilityZuzService implements SalaryCalculatorService{
    private final RatesConfigurationProperties ratesConfigurationProperties;
    private final RabbitTemplate rabbitTemplate;

    private final RabbitMqSettings rabbitMqSettings;


    @Override
    public String getDescription() {
        return "Disability zus";
    }

    @Override
    public void sendDisabilityZus(DisabilityZus disabilityZus) {
        this.rabbitTemplate.convertAndSend(this.rabbitMqSettings.getExchangeSalaryCalculator(), this.rabbitMqSettings.getRoutingKeySalaryCalculator(), disabilityZus);
    }

    @Override
    public BigDecimal apply(final BigDecimal grossMonthlySalary) {
        return grossMonthlySalary.multiply(this.ratesConfigurationProperties.getDisabilityZusRate()).setScale(2, RoundingMode.HALF_EVEN);
    }
}
