package com.acoustic.controller;


import com.acoustic.entity.MicroservicesData;
import com.acoustic.entity.SalaryCalculatorOrchestratorData;
import com.acoustic.jobcategories.JobCategoriesConfigurationProperties;
import com.acoustic.repository.MicroservicesDataRepository;
import com.acoustic.repository.SalaryCalculatorOrchestratorDataRepository;
import com.acoustic.service.CollectResponsesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@RestController
@RequestMapping("/salary-calculations")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@Slf4j
public class SalaryCalculatorOrchestratorController {
    private static final int MINIMUM_GROSS = 2000;
    private static final String SALARY_CALCULATOR_RECEIVER_ID = "salaryCalculatorReceiverId";
    private final JobCategoriesConfigurationProperties jobCategoriesConfigurationProperties;
    private final SalaryCalculatorOrchestratorDataRepository salaryCalculatorOrchestratorDataRepository;
    private final CollectResponsesService collectResponsesService;
    private final MicroservicesDataRepository microservicesDataRepository;

    @RabbitListener(id = SALARY_CALCULATOR_RECEIVER_ID, queues = "${rabbitmq.queueSalaryCalculator}")
    public void messageReceiver(MicroservicesData microservicesData) {
        this.microservicesDataRepository.save(microservicesData);
    }


    @GetMapping("/jobs/{departmentName}")
    public ResponseEntity<List<String>> getJobTitles(@PathVariable String departmentName) {
        return ResponseEntity.status(HttpStatus.OK).body(this.jobCategoriesConfigurationProperties.getJobDepartmentAndTitles().get(departmentName));
    }

    @GetMapping("/departments")
    public ResponseEntity<Set<String>> getDepartmentName() {
        return ResponseEntity.status(HttpStatus.OK).body(this.jobCategoriesConfigurationProperties.getJobDepartmentAndTitles().keySet());

    }

    @PostMapping("/calculations/{grossMonthlySalary}")
    public ResponseEntity<Map<String, BigDecimal>> calculateSalary(@PathVariable @Min(value = MINIMUM_GROSS, message = "Must be Greater than or equal to 2000.00") @NotNull BigDecimal grossMonthlySalary, @RequestParam(required = false) String departmentName, @RequestParam(required = false) Integer jobTitleId) {
        var uuid = UUID.randomUUID();
        this.collectResponsesService.sendDataToMicroservices(grossMonthlySalary, uuid);
        var response = this.collectResponsesService.collectMicroservicesResponse(uuid);
        if (departmentName == null || jobTitleId == null) {
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        List<String> jobTitlesList = this.jobCategoriesConfigurationProperties.getJobDepartmentAndTitles().get(departmentName);
        if (!this.jobCategoriesConfigurationProperties.getJobDepartmentAndTitles().containsKey(departmentName.toLowerCase())) {
            throw new IllegalArgumentException("Invalid department name");
        }
        if (jobTitleId > jobTitlesList.size() || jobTitleId <= 0) {
            throw new IllegalArgumentException("Wrong job id");
        }
        return ResponseEntity.status(HttpStatus.OK).body(getAverage(grossMonthlySalary, jobTitlesList.get(jobTitleId - 1), response));
    }


    public Map<String, BigDecimal> getAverage(BigDecimal grossMonthlySalary, String jobTitleId, Map<String, BigDecimal> response) {
        BigDecimal average = statistic(jobTitleId, grossMonthlySalary);
        response.put("Average", average.setScale(2, RoundingMode.HALF_EVEN));
        return response;

    }

    public BigDecimal statistic(String jobTitleId, BigDecimal grossMonthlySalary) {
        this.salaryCalculatorOrchestratorDataRepository.save(SalaryCalculatorOrchestratorData.builder().grossMonthlySalary(grossMonthlySalary).jobTitle(jobTitleId).build());
        return this.salaryCalculatorOrchestratorDataRepository.findAverageByJobTitle((jobTitleId)).setScale(2, RoundingMode.HALF_EVEN);
    }
}



