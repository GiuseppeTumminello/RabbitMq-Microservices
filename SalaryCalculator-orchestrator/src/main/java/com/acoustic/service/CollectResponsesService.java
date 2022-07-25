package com.acoustic.service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;


public interface CollectResponsesService {
    void sendDataToMicroservices(BigDecimal grossMonthlySalary, UUID uuid);

    Map<String, BigDecimal> collectMicroservicesResponse(UUID uuid);
}

