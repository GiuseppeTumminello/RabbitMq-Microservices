package com.acoustic.configuration;

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
    public Queue taxQueue() {
        return new Queue(rabbitMqSettings.getQueueTax(), rabbitMqSettings.isDurable());
    }

    @Bean
    public Queue salaryCalculatorOrchestratorQueue(){
        return new Queue(rabbitMqSettings.getQueueSalaryCalculator(), rabbitMqSettings.isDurable());
    }

    @Bean
    public Exchange salaryCalculatorOrchestratorExchange() {
        return ExchangeBuilder.directExchange(rabbitMqSettings.getExchangeSalaryCalculator()).durable(rabbitMqSettings.isDurable()).build();
    }

    @Bean
    public FanoutExchange microservicesExchange() {
        return ExchangeBuilder.fanoutExchange(rabbitMqSettings.getExchange()).durable(rabbitMqSettings.isDurable()).build();
    }

    @Bean
    public Binding taxBinding() {
        return BindingBuilder
                .bind(taxQueue())
                .to(microservicesExchange());
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
