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
class MonthlyNetTest {

    public static final String MONTHLY_NET_DESCRIPTION = "Monthly net";
    public static final String DESCRIPTION = "description";
    public static final String VALUE = "value";
    private final String MONTHLY_NET_ENDPOINT = "/monthly-net/calculation/";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    @ParameterizedTest
    @CsvSource({"6000,4319.44", "7000, 5039.35", "8555,6158.81", "15143.99,10188.77"})
    public void calculateMonthlyNet(BigDecimal input, String netMonthly) throws Exception {
        var expected = this.objectMapper.writeValueAsString(Map.of(DESCRIPTION, MONTHLY_NET_DESCRIPTION, VALUE, netMonthly));
        this.mockMvc.perform(post(MONTHLY_NET_ENDPOINT + input).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }

}