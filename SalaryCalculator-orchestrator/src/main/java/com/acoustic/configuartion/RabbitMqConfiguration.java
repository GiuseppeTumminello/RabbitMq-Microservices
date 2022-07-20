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

    private final RabbitMqSettings rabbitMqValues;

    @Bean
    public Queue queueAnnualNet() {
        return new Queue(rabbitMqValues.getQueueAnnualNet(), true);
    }

    @Bean
    public Queue queueAnnualGross() {
        return new Queue(rabbitMqValues.getQueueAnnualGross(), true);
    }

    @Bean
    public FanoutExchange myExchange() {
        return ExchangeBuilder.fanoutExchange(rabbitMqValues.getExchange()).durable(true).build();
    }

    @Bean
    public Binding bindingAnnualNet() {
        return BindingBuilder
                .bind(queueAnnualNet())
                .to(myExchange());
    }

    @Bean
    public Binding bindingAnnualGross() {
        return BindingBuilder
                .bind(queueAnnualGross())
                .to(myExchange());
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
