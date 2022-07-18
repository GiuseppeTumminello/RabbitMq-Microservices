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
class AnnualNetControllerTest {

    public static final String ANNUAL_NET_DESCRIPTION = "Annual net";
    public static final String DESCRIPTION = "description";
    public static final String VALUE = "value";
    private final String ANNUAL_NET_ENDPOINT = "/annual-net/calculation/";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    @ParameterizedTest
    @CsvSource({"6000,51833.28", "7000, 60472.20", "8555,73905.72", "15143.99,122265.24"})
    public void calculateAnnualNet(BigDecimal input, String annualNet) throws Exception {
        var expected = this.objectMapper.writeValueAsString(Map.of(DESCRIPTION, ANNUAL_NET_DESCRIPTION, VALUE, annualNet));
        this.mockMvc.perform(post(ANNUAL_NET_ENDPOINT + input).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }

}