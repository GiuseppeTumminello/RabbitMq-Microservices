package com.acoustic.configuartion;

import com.acoustic.rabbitmqsettings.RabbitMqSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitMqConfiguration {
    private final RabbitMqSettings rabbitMqSettings;
    @Bean
    public Queue salaryCalculatorQueue(){
        return new Queue(this.rabbitMqSettings.getQueueSalaryCalculator(), this.rabbitMqSettings.isDurable());
    }
    @Bean
    public Exchange salaryCalculatorExchange() {
        return ExchangeBuilder.directExchange(this.rabbitMqSettings.getExchangeSalaryCalculator()).durable(this.rabbitMqSettings.isDurable()).build();
    }

    @Bean
    public Binding bindingSalaryCalculator() {
        return BindingBuilder
                .bind(salaryCalculatorQueue())
                .to(salaryCalculatorExchange()).with(this.rabbitMqSettings.getRoutingKeySalaryCalculator()).noargs();
    }

    @Bean
    public FanoutExchange exchange() {
        return ExchangeBuilder.fanoutExchange(this.rabbitMqSettings.getExchange()).durable(this.rabbitMqSettings.isDurable()).build();
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
