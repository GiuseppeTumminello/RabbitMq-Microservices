package com.acoustic.rabbitmqtestconfiguration;

import com.acoustic.configuration.RabbitMqConfiguration;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.test.RabbitListenerTest;
import org.springframework.amqp.rabbit.test.TestRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;

@RabbitListenerTest(capture = true)
@TestConfiguration
public class RabbitTestConfiguration {
    @Autowired
    private RabbitMqConfiguration rabbitTestConfiguration;

    @Bean
    public ConnectionFactory mockConnectionFactory(){
        ConnectionFactory factory = mock(ConnectionFactory.class);
        Connection connection = mock(Connection.class);
        Channel channel = mock(Channel.class);
        willReturn(connection).given(factory).createConnection();
        willReturn(channel).given(connection).createChannel(anyBoolean());
        given(channel.isOpen()).willReturn(true);
        return factory;

    }

    @Bean
    public SimpleRabbitListenerContainerFactory testRabbitListenerContainerFactory()  {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(mockConnectionFactory());
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return factory;
    }


    @Bean
    public TestRabbitTemplate testRabbitTemplate(ConnectionFactory connectionFactory) {
        final TestRabbitTemplate rabbitTemplate = new TestRabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(this.rabbitTestConfiguration.jsonMessageConverter());
        return rabbitTemplate;
    }

}
