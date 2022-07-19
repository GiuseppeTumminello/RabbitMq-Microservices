package com.acoustic.controller;


import com.acoustic.enpoints.MicroservicesEndpoints;
import com.acoustic.entity.SalaryCalculatorOrchestratorData;
import com.acoustic.jobcategories.JobCategoriesConfigurationProperties;
import com.acoustic.model.Data;
import com.acoustic.repository.DataSalaryCalculatorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.http.HttpMethod;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/salary-calculations")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@Slf4j
public class SalaryCalculatorOrchestratorController implements RabbitListenerConfigurer {

    private static final int MINIMUM_GROSS = 2000;
    private final MicroservicesEndpoints microservicesEndpoints;
    private final JobCategoriesConfigurationProperties jobCategoriesConfigurationProperties;
    private final RestTemplate restTemplate;
    private final DataSalaryCalculatorRepository dataSalaryCalculatorRepository;
    private final Map<String, BigDecimal> response;


    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {

    }

    @RabbitListener(queues = "${rabbitmq.queue}")
    public void receivedMessage(Data data) {
        this.response.put(data.getDescription(), BigDecimal.valueOf(Double.parseDouble(data.getAmount())));
        log.warn(this.response.toString());

    }



    @GetMapping("/jobs/{departmentName}")
    public List<String> getJobTitles(@PathVariable String departmentName) {
        return this.jobCategoriesConfigurationProperties.getJobDepartmentAndTitles().get(departmentName);
    }

    @GetMapping("/departments")
    public Set<String> getDepartmentName() {
        return this.jobCategoriesConfigurationProperties.getJobDepartmentAndTitles().keySet();
    }


    @PostMapping("/calculations/{grossMonthlySalary}")
    public Map<String, BigDecimal> calculateSalary(@PathVariable @Min(value = MINIMUM_GROSS, message = "Must be Greater than or equal to 2000.00") @NotNull BigDecimal grossMonthlySalary, @RequestParam(required = false) String departmentName, @RequestParam(required = false) Integer jobTitleId) {
        response.clear();
        getCalculationFromMicroservices(grossMonthlySalary);
        if (departmentName == null || jobTitleId == null) {
            return this.response;
        }
        List<String> jobTitlesList = this.jobCategoriesConfigurationProperties.getJobDepartmentAndTitles().get(departmentName);
        if (!this.jobCategoriesConfigurationProperties.getJobDepartmentAndTitles().containsKey(departmentName.toLowerCase())) {
            throw new IllegalArgumentException("Invalid department name");
        }

        if (jobTitleId > jobTitlesList.size() || jobTitleId <= 0) {
            throw new IllegalArgumentException("Wrong job id");
        }

        return getAverage(grossMonthlySalary, jobTitlesList.get(jobTitleId - 1), this.response);
    }


    private void getCalculationFromMicroservices(BigDecimal grossMonthlySalary) {
        for (var endpoint : this.microservicesEndpoints.getEndpoints()) {
            this.restTemplate.postForEntity(endpoint + grossMonthlySalary, HttpMethod.POST, Data.class);
        }


    }


    private Map<String, BigDecimal> getAverage(BigDecimal grossMonthlySalary, String jobTitleId, Map<String, BigDecimal> response) {
        BigDecimal average = statistic(jobTitleId, grossMonthlySalary);
        response.put("Average", average.setScale(2, RoundingMode.HALF_EVEN));
        return response;

    }

    private BigDecimal statistic(String jobTitleId, BigDecimal grossMonthlySalary) {
        this.dataSalaryCalculatorRepository.save(SalaryCalculatorOrchestratorData.builder().grossMonthlySalary(grossMonthlySalary).jobTitle(jobTitleId).build());
        return this.dataSalaryCalculatorRepository.findAverageByJobTitle((jobTitleId));
    }


}



