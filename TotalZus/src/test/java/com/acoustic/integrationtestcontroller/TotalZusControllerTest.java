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
class TotalZusControllerTest {


    public static final String TOTAL_ZUS_DESCRIPTION = "Total zus";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private final String TOTAL_ZUS_ENDPOINT = "/total-zus/calculation/";


    @ParameterizedTest
    @CsvSource({"6000,822.60", "7000, 959.70", "8555,1172.89", "15143.99,2076.24"})
    public void calculateTotalZus(BigDecimal input, String totalZus) throws Exception {
        var expected = this.objectMapper.writeValueAsString(Map.of(TOTAL_ZUS_DESCRIPTION, totalZus));
        this.mockMvc.perform(post(TOTAL_ZUS_ENDPOINT + input).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }

}