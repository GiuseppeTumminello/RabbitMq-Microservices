package com.acoustic.SpringPolandSalaryCalculator.rabbittest;

import com.acoustic.SpringPolandSalaryCalculator.rabbitmqtestconfiguration.RabbitTestConfiguration;
import com.acoustic.controller.SalaryCalculatorOrchestratorController;
import com.acoustic.entity.MicroservicesData;
import com.acoustic.rabbitmqsettings.RabbitMqSettings;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.amqp.rabbit.test.TestRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;


@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = RabbitTestConfiguration.class)
public class RabbitMqSalaryCalculatorOrchestratorTest {
    @Autowired
    private SalaryCalculatorOrchestratorController salaryCalculatorOrchestratorController;

    @Autowired
    private RabbitListenerTestHarness harness;

    @Autowired
    private TestRabbitTemplate testRabbitTemplate;

    @Autowired
    private RabbitMqSettings rabbitMqSettings;


    @ParameterizedTest
    @CsvSource({"6000,1", "7000,1", "8555,1", "15143.99,1"})
    public void receiveMessageTest(BigDecimal grossMonthlySalary, int id) {
        this.salaryCalculatorOrchestratorController = this.harness.getSpy(this.rabbitMqSettings.getReceiverId());
        assertNotNull(this.salaryCalculatorOrchestratorController);
        var microservicesData = MicroservicesData.builder().id(id).amount(grossMonthlySalary).uuid(UUID.randomUUID()).build();
        this.testRabbitTemplate.convertAndSend(this.rabbitMqSettings.getQueueSalaryCalculator(), microservicesData);
        verify(this.salaryCalculatorOrchestratorController).messageReceiver(microservicesData);
    }

}