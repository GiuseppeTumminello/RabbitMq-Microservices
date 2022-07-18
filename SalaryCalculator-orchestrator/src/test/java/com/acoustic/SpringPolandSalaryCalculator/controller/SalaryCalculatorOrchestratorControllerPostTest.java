package com.acoustic.SpringPolandSalaryCalculator.controller;


import com.acoustic.SpringPolandSalaryCalculator.calculator.SalaryCalculatorResponse;
import com.acoustic.controller.SalaryCalculatorOrchestratorController;
import com.acoustic.repository.DataSalaryCalculatorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.NestedServletException;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class SalaryCalculatorOrchestratorControllerPostTest {


    public static final String CALCULATOR_ENDPOINTS = "/salary-calculations/calculations/";
    public static final String DEPARTMENT_NAME_REQUEST_PARAM = "?departmentName=";
    public static final String JOB_TITLE_ID_REQUEST_PARAM = "&jobTitleId=";

    private boolean average;


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SalaryCalculatorResponse salaryCalculatorResponse;

    @MockBean
    private DataSalaryCalculatorRepository dataSalaryCalculatorRepository;

    @MockBean
    private SalaryCalculatorOrchestratorController salaryCalculatorOrchestratorController;


    @ParameterizedTest
    @CsvSource({"6000, finance, 1",
            "7000, it, 2, true",
            "15891.68, airline, 1",
            "7700, restaurant, 6",
            "12191.68, it, 10",
            "185891.68, finance, 2"})
    public void getSalaryCalculation(BigDecimal grossMonthlySalary, String departmentName, int jobTitleId) throws Exception {
        this.average = true;
        given(this.dataSalaryCalculatorRepository.findAverageByJobTitle(any())).willReturn(grossMonthlySalary);
        given(this.salaryCalculatorOrchestratorController.calculateSalary(grossMonthlySalary, departmentName, jobTitleId)).willReturn(this.salaryCalculatorResponse.expectedValue(grossMonthlySalary, average));
        this.mockMvc.perform(post(
                        CALCULATOR_ENDPOINTS + grossMonthlySalary + DEPARTMENT_NAME_REQUEST_PARAM + departmentName +
                                JOB_TITLE_ID_REQUEST_PARAM + jobTitleId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string(this.objectMapper.writeValueAsString(this.salaryCalculatorResponse.expectedValue(grossMonthlySalary,
                                average))));


    }


    @ParameterizedTest
    @CsvSource({"6000, finance, -10",
            "7000, it, -1000",
            "15891.68, airline, 11",
            "7700, restaurant, 20",
            "12191.68, it, 30",
            "185891.68, finance, 40"})
    public void getSalaryCalculationIdOutOfBounds(
            BigDecimal grossMonthlySalary, String departmentName, int jobTitleId) {
        this.average = true;
        given(this.dataSalaryCalculatorRepository.findAverageByJobTitle(any())).willReturn(grossMonthlySalary);
        when(this.salaryCalculatorOrchestratorController.calculateSalary(grossMonthlySalary, departmentName, jobTitleId)).thenThrow(new RuntimeException("Exception"));
        Assertions.assertThrows(NestedServletException.class,
                () -> this.mockMvc.perform(post(
                                CALCULATOR_ENDPOINTS + grossMonthlySalary + DEPARTMENT_NAME_REQUEST_PARAM + departmentName +
                                        JOB_TITLE_ID_REQUEST_PARAM + jobTitleId))
                        .andExpect(status().isOk())
                        .andExpect(MockMvcResultMatchers.content()
                                .string(this.objectMapper.writeValueAsString(this.salaryCalculatorResponse.expectedValue(
                                        grossMonthlySalary,
                                        average)))));

    }


    @ParameterizedTest
    @CsvSource({"6000, fff, 1",
            "7000, its, 1",
            "15891.68, airlines, 1",
            "7700, restaurants, 1",
            "12191.68, rest, 2",
            "185891.68, finances, 3"})
    public void getSalaryCalculationWrongDepartmentName(
            BigDecimal grossMonthlySalary, String departmentName, int jobTitleId) {
        this.average = true;
        given(this.dataSalaryCalculatorRepository.findAverageByJobTitle(any())).willReturn(grossMonthlySalary);
        when(this.salaryCalculatorOrchestratorController.calculateSalary(grossMonthlySalary, departmentName, jobTitleId)).thenThrow(new RuntimeException("Exception"));
        Assertions.assertThrows(NestedServletException.class,
                () -> this.mockMvc.perform(post(
                                CALCULATOR_ENDPOINTS + grossMonthlySalary + DEPARTMENT_NAME_REQUEST_PARAM + departmentName +
                                        JOB_TITLE_ID_REQUEST_PARAM + jobTitleId))
                        .andExpect(status().isOk())
                        .andExpect(MockMvcResultMatchers.content()
                                .string(this.objectMapper.writeValueAsString(this.salaryCalculatorResponse.expectedValue(
                                        grossMonthlySalary,
                                        average)))));
    }


    @ParameterizedTest
    @CsvSource({"-6000, finance, 1",
            "-7000, it, 1",
            "-15891.68, airline, 1",
            "-7700, restaurant, 1",
            "1999.9999, it, 2",
            "0, finance, 3"})
    public void getSalaryCalculationGrossBelowTrashHold(
            BigDecimal grossMonthlySalary, String departmentName, int jobTitleId) {
        this.average = true;
        given(this.dataSalaryCalculatorRepository.findAverageByJobTitle(any())).willReturn(grossMonthlySalary);
        when(this.salaryCalculatorOrchestratorController.calculateSalary(grossMonthlySalary, departmentName, jobTitleId)).thenThrow(new RuntimeException("Exception"));
        Assertions.assertThrows(NestedServletException.class,
                () -> this.mockMvc.perform(post(
                                CALCULATOR_ENDPOINTS + grossMonthlySalary + DEPARTMENT_NAME_REQUEST_PARAM + departmentName +
                                        JOB_TITLE_ID_REQUEST_PARAM + jobTitleId))
                        .andExpect(status().isOk())
                        .andExpect(MockMvcResultMatchers.content()
                                .string(this.objectMapper.writeValueAsString(this.salaryCalculatorResponse.expectedValue(
                                        grossMonthlySalary,
                                        average)))));
    }


    @ParameterizedTest
    @CsvSource({"6000", "7000", "15891.68", "7700", "2999.9999"})
    public void getSalaryCalculationGrossNoStatistic(BigDecimal grossMonthlySalary) throws Exception {
        this.average = false;
        String department = null;
        Integer jobId = null;
        given(this.dataSalaryCalculatorRepository.findAverageByJobTitle(any())).willReturn(grossMonthlySalary);
        given(this.salaryCalculatorOrchestratorController.calculateSalary(grossMonthlySalary, department, jobId)).willReturn(this.salaryCalculatorResponse.expectedValue(grossMonthlySalary, average));
        this.mockMvc.perform(post(CALCULATOR_ENDPOINTS + grossMonthlySalary))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string(this.objectMapper.writeValueAsString(this.salaryCalculatorResponse.expectedValue(grossMonthlySalary,
                                average))));

    }

}
