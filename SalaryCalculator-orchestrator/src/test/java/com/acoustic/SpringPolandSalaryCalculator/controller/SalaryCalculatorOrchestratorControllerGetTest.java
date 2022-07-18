package com.acoustic.SpringPolandSalaryCalculator.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class SalaryCalculatorOrchestratorControllerGetTest {

    private static final String DEPARTMENT_NAME_ENDPOINT = "/salary-calculations/departments";
    private static final String JOB_TITLE_ENDPOINT = "/salary-calculations/jobs/";


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    public SalaryCalculatorOrchestratorControllerGetTest() {
    }


    @Test
    public void getDepartmentName() throws Exception {
        String expected = this.objectMapper.writeValueAsString(List.of("it", "finance", "engineer", "restaurant", "airline"));
        var actual = this.mockMvc.perform(get(DEPARTMENT_NAME_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expected))
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(actual.length()).isEqualTo(expected.length());

    }



    private static  Stream<Arguments> departmentNamesAndJobTitlesData() {
        return Stream.of(
                Arguments.of("it", List.of("DevOps Engineer", "Software Developer", "Cloud System Engineer", "Cloud Architect", "Network Engineer", "IT Support Specialist",
                        "Database Administrator", "System Architect", "Web Administrator", "Software Engineer")),
                Arguments.of("finance", List.of("Depositary", "Fund Accountant", "Accountant", "Tax Analyst", "Auditor", "Risk Analyst", "Business Analyst", "Billing Administrator", "Financial Controller")),
                Arguments.of("engineer",List.of("Civil Engineer", "Project Engineer", "Test Engineer", "Sales Engineer", "R&D Engineer", "Thermal Engineer")),
                Arguments.of("restaurant",List.of("Executive Chef","Assistant Manager","General Manager","Sous Chef","Kitchen Manager","Line Cook","Bartender","Cashier","Dishwasher","Waitress")),
                Arguments.of("airline",List.of("Airline Captain","Airline Pilot", "Airport Manager", "Analyst", "Chief Pilot", "Traffic Manager")));


    }

    @ParameterizedTest
    @MethodSource("departmentNamesAndJobTitlesData")
    void getJobTitles(String departmentName, List<String> jobTitles) throws Exception {
        String expected = this.objectMapper.writeValueAsString(jobTitles);
        System.out.println(departmentName);
        var actual = this.mockMvc.perform(get(JOB_TITLE_ENDPOINT + departmentName).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expected))
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertThat(actual.length()).isEqualTo(expected.length());
    }




}
