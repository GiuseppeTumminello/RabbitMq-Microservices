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
class TaxTest {

    public static final String DESCRIPTION = "description";
    public static final String TAX_DESCRIPTION = "Tax";
    public static final String VALUE = "value";
    private final String TAX_ENDPOINT = "/tax/calculation/";
    @Autowired
    private MockMvc mockMvc;
   @Autowired
   private ObjectMapper objectMapper;




    @ParameterizedTest
    @CsvSource({"6000,391.99", "7000, 457.32", "8555,558.91", "15143.99,1702.88"})
    public void calculateTax(BigDecimal input, String tax) throws Exception {
        var expected = this.objectMapper.writeValueAsString(Map.of(DESCRIPTION, TAX_DESCRIPTION, VALUE, tax));
        this.mockMvc.perform(post(TAX_ENDPOINT + input).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }

}