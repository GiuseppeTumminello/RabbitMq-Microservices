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
class DisabilityZusControllerTest {
    public static final String DISABILITY_ZUS_DESCRIPTION = "Disability zus";
    private final String DISABILITY_ZUS_ENDPOINT = "/disability-zus/calculation/";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @ParameterizedTest
    @CsvSource({"6000, 90.00", "7000, 105.00", "8555,128.32", "15143.99,227.16"})
    public void calculateDisabilityZus(BigDecimal input, String pensionZus) throws Exception {
        var expected = this.objectMapper.writeValueAsString(Map.of(DISABILITY_ZUS_DESCRIPTION, pensionZus));
        this.mockMvc.perform(post(DISABILITY_ZUS_ENDPOINT + input).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }

}