package com.acoustic.service;

import com.acoustic.entity.MicroservicesData;
import com.acoustic.rabbitmqsettings.RabbitMqSettings;
import com.acoustic.repository.MicroservicesDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CollectResponsesServiceImplementation implements CollectResponsesService{

    public static final int NUMBER_OF_CHECKS = 50;
    public static final int MICROSERVICE_COUNT = 10;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMqSettings rabbitMqSettings;
    private final MicroservicesDataRepository microservicesDataRepository;

    @Override
    public void sendDataToMicroservices(BigDecimal grossMonthlySalary, UUID uuid) {
        this.rabbitTemplate.convertAndSend(this.rabbitMqSettings.getExchange(), this.rabbitMqSettings.getRoutingKey(), MicroservicesData.builder().amount(grossMonthlySalary).uuid(uuid).build());
    }

    @Override
    public Map<String, BigDecimal> collectMicroservicesResponse(UUID uuid) {
        Map<String, BigDecimal> response = new HashMap<>();
        List<MicroservicesData> data = new ArrayList<>();
        var count = 0;
        while (data.size() < MICROSERVICE_COUNT) {
            data = this.microservicesDataRepository.findDataByUuid(uuid);
            count++;
            if (count == NUMBER_OF_CHECKS) {
                break;
            }
        }
        data.forEach(microservicesData -> response.put(microservicesData.getDescription(), microservicesData.getAmount()));
        return response;
    }
}
