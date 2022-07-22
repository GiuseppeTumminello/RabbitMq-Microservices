package com.acoustic.integrationtestcontroller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class HealthTest {
    public static final String HEALTH_ZUS_DESCRIPTION = "Health";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private final String HEALTH_ENDPOINT = "/health/calculation/";

    @ParameterizedTest
    @CsvSource({"6000,465.97", "7000, 543.63", "8555,664.39", "15143.99,1176.10"})
    public void calculateHealth(BigDecimal input, String health) throws Exception {
        var expected = this.objectMapper.writeValueAsString(Map.of(HEALTH_ZUS_DESCRIPTION, health));
        this.mockMvc.perform(post(HEALTH_ENDPOINT + input).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }

}