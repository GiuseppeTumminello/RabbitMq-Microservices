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
class SicknessZusControllerTest {

    public static final String DESCRIPTION = "description";
    public static final String SICKNESS_ZUS_DESCRIPTION = "Sickness zus";
    public static final String VALUE = "value";
    private final String SICKNESS_ZUS_ENDPOINT = "/sickness-zus/calculation/";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    @ParameterizedTest
    @CsvSource({"6000, 147.00", "7000, 171.50", "8555,209.60", "15143.99,371.03"})
    public void calculateSicknessZus(BigDecimal input, String sicknessZus) throws Exception {
        var expected = this.objectMapper.writeValueAsString(Map.of(DESCRIPTION, SICKNESS_ZUS_DESCRIPTION, VALUE, sicknessZus));
        this.mockMvc.perform(post(SICKNESS_ZUS_ENDPOINT + input).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }

}