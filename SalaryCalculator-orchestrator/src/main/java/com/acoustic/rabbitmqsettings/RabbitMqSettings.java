package com.acoustic.rabbitmqsettings;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Setter
@Configuration
@PropertySource("classpath:rabbitmq.properties")
@ConfigurationProperties(prefix = "rabbitmq")
public class RabbitMqSettings {

    private String exchange;
    private String routingKey;
    private boolean durable;
    private String receiverId;
    private String queueSalaryCalculator;
    private String routingKeySalaryCalculator;
    private String exchangeSalaryCalculator;
}
