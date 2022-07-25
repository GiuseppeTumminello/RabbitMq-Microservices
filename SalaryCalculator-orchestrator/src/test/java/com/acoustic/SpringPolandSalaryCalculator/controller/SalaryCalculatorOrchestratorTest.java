package com.acoustic.SpringPolandSalaryCalculator.controller;

import com.acoustic.controller.SalaryCalculatorOrchestratorController;
import com.acoustic.entity.SalaryCalculatorOrchestratorData;
import com.acoustic.repository.SalaryCalculatorOrchestratorDataRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
public class SalaryCalculatorOrchestratorTest {


    @MockBean
    private SalaryCalculatorOrchestratorDataRepository salaryCalculatorOrchestratorDataRepository;

    @Autowired
    private SalaryCalculatorOrchestratorController salaryCalculatorOrchestratorController;


    @ParameterizedTest
    @CsvSource({"6000.00, DevOps Engineer, Average", "7000.00, Software Developer, Average", "15891.68, Software Engineer, Average"})
    public void getAverage(BigDecimal grossMonthlySalary, String jobTitleName, String Description) {
        given(this.salaryCalculatorOrchestratorDataRepository.findAverageByJobTitle(any())).willReturn(grossMonthlySalary);
        given(this.salaryCalculatorOrchestratorDataRepository.save(any())).willReturn(SalaryCalculatorOrchestratorData.builder().grossMonthlySalary(grossMonthlySalary).build());
        given(this.salaryCalculatorOrchestratorController.statistic(jobTitleName, any())).willReturn(grossMonthlySalary);
        assertThat(this.salaryCalculatorOrchestratorController.getAverage(grossMonthlySalary, jobTitleName, new LinkedHashMap<>(Map.of(jobTitleName, grossMonthlySalary))))
                .isEqualTo(Map.of(jobTitleName, grossMonthlySalary, Description, grossMonthlySalary));
    }

    @ParameterizedTest
    @CsvSource({"6000.00, DevOps Engineer", "7000.00, Software Developer", "15891.68, Software Engineer"})
    public void statistic(BigDecimal grossMonthlySalary, String jobTitleName) {
        given(this.salaryCalculatorOrchestratorDataRepository.findAverageByJobTitle(any())).willReturn(grossMonthlySalary);
        given(this.salaryCalculatorOrchestratorDataRepository.save(any())).willReturn(SalaryCalculatorOrchestratorData.builder().grossMonthlySalary(grossMonthlySalary).jobTitle("DevOps Engineer").build());
        Assertions.assertEquals(grossMonthlySalary, salaryCalculatorOrchestratorController.statistic(jobTitleName, grossMonthlySalary));
        assertThat(this.salaryCalculatorOrchestratorController.statistic(jobTitleName, grossMonthlySalary))
                .isEqualTo(grossMonthlySalary);
    }




}
