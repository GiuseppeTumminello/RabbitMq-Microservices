package com.acoustic.integrationtestcontroller;

import com.acoustic.MicroservicesData;
import com.acoustic.controller.TotalZusController;
import com.acoustic.rabbitmqsettings.RabbitMqSettings;
import com.acoustic.service.SalaryCalculatorService;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test")
public class ListenerIT {
    @Autowired
    private TotalZusController totalZusController;


    @Autowired
    private RabbitTemplate testRabbitTemplate;

    @Autowired
    private RabbitMqSettings myConfigParameters;
    @Autowired
    private SalaryCalculatorService salaryCalculatorService;

    @Test
    public void testSimpleSends() {
        this.testRabbitTemplate.convertAndSend(this.myConfigParameters.getQueueTotalZus(), MicroservicesData.builder().description(this.salaryCalculatorService.getDescription()).amount(BigDecimal.valueOf(6000)).uuid(UUID.randomUUID()).build());
        assertThat(this.totalZusController.getCounter()).isEqualTo(2);
//        this.testRabbitTemplate.convertAndSend("bar", "hello2");
//        assertThat(this.config.barIn, equalTo("bar:hello2"));
//        assertThat(this.config.smlc1In, equalTo("smlc1:"));
//        this.testRabbitTemplate.convertAndSend("foo", "hello3");
//        assertThat(this.config.fooIn, equalTo("foo:hello1"));
//        this.testRabbitTemplate.convertAndSend("bar", "hello4");
//        assertThat(this.config.barIn, equalTo("bar:hello2"));
//        assertThat(this.config.smlc1In, equalTo("smlc1:hello3hello4"));
//
//        this.testRabbitTemplate.setBroadcast(true);
//        this.testRabbitTemplate.convertAndSend("foo", "hello5");
//        assertThat(this.config.fooIn, equalTo("foo:hello1foo:hello5"));
//        this.testRabbitTemplate.convertAndSend("bar", "hello6");
//        assertThat(this.config.barIn, equalTo("bar:hello2bar:hello6"));
//        assertThat(this.config.smlc1In, equalTo("smlc1:hello3hello4hello5hello6"));
    }

//    @SneakyThrows
//    private String getMessage() {
//        Map<String, String> bean = new HashMap<>();
//        bean.put("key1","value1");
//        bean.put("key2","value2");
//        return new ObjectMapper().writeValueAsString(bean);
//    }
}