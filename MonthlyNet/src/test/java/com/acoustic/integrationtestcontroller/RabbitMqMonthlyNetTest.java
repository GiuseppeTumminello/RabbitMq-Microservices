package com.acoustic.integrationtestcontroller;

import com.acoustic.controller.MonthlyNetController;
import com.acoustic.entity.MonthlyNet;
import com.acoustic.rabbitmqsettings.RabbitMqSettings;
import com.acoustic.rabbitmqtestconfiguration.RabbitTestConfiguration;
import com.acoustic.service.SalaryCalculatorService;
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
public class RabbitMqMonthlyNetTest {
    @Autowired
    private MonthlyNetController monthlyNetZusController;

    @Autowired
    private RabbitListenerTestHarness harness;

    @Autowired
    private TestRabbitTemplate testRabbitTemplate;

    @Autowired
    private RabbitMqSettings rabbitMqSettings;
    @Autowired
    private SalaryCalculatorService salaryCalculatorService;

    @ParameterizedTest
    @CsvSource({"6000", "7000", "8555", "15143.99"})
    public void receiveMessageTest(BigDecimal grossMonthlySalary) {
        this.monthlyNetZusController = this.harness.getSpy(this.rabbitMqSettings.getReceiverId());
        assertNotNull(monthlyNetZusController);
        var monthlyNetData = MonthlyNet.builder().description(this.salaryCalculatorService.getDescription()).amount(grossMonthlySalary).uuid(UUID.randomUUID()).build();
        this.testRabbitTemplate.convertAndSend(this.rabbitMqSettings.getQueueMonthlyNet(), monthlyNetData);
        verify(this.monthlyNetZusController).receiveMessage(monthlyNetData);
    }

}