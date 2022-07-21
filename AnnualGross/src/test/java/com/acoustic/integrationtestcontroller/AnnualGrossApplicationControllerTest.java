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
class AnnualGrossApplicationControllerTest {

    public static final String ANNUAL_GROSS_DESCRIPTION = "Annual gross";
    private final String ANNUAL_GROSS_ENDPOINT = "/annual-gross/calculation/";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;




    @ParameterizedTest
    @CsvSource({"6000, 72000.00", "7000, 84000.00", "8555,102660.00", "15143.99,181727.88"})
    public void calculateAnnualGross(BigDecimal input, String annualGross) throws Exception {
        var expected = this.objectMapper.writeValueAsString(Map.of(ANNUAL_GROSS_DESCRIPTION, annualGross));
        this.mockMvc.perform(post(ANNUAL_GROSS_ENDPOINT + input).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }

}