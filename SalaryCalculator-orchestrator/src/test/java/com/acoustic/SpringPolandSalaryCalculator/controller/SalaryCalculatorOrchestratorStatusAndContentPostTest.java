package com.acoustic.SpringPolandSalaryCalculator.controller;


import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class SalaryCalculatorOrchestratorStatusAndContentPostTest {
    public static final String CALCULATOR_ENDPOINTS = "/salary-calculations/calculations/";
    public static final String DEPARTMENT_NAME_REQUEST_PARAM = "?departmentName=";
    public static final String JOB_TITLE_ID_REQUEST_PARAM = "&jobTitleId=";
    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @CsvSource({"6000.00, finance, 1",
            "7000.00, it, 2, true",
            "15891.68, airline, 1",
            "7700.00, restaurant, 6",
            "12191.68, it, 10",
            "185891.68, finance, 2"})
    public void calculateSalaryVerifyContentAndStatus(BigDecimal input, String departmentName, int jobTitleId) throws Exception {
        this.mockMvc.perform(post(CALCULATOR_ENDPOINTS + input + DEPARTMENT_NAME_REQUEST_PARAM + departmentName + JOB_TITLE_ID_REQUEST_PARAM + jobTitleId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

    }

}