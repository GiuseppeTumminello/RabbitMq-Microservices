package com.acoustic.controller;


import com.acoustic.entity.MicroservicesData;
import com.acoustic.entity.SalaryCalculatorOrchestratorData;
import com.acoustic.jobcategories.JobCategoriesConfigurationProperties;
import com.acoustic.rabbitmqsettings.RabbitMqSettings;
import com.acoustic.repository.DataProducerRepository;
import com.acoustic.repository.DataSalaryCalculatorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
    public static final int NUMBER_OF_CHECKS = 50;
    public static final int MICROSERVICE_COUNT = 10;
    private final JobCategoriesConfigurationProperties jobCategoriesConfigurationProperties;
    private final DataSalaryCalculatorRepository dataSalaryCalculatorRepository;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitMqSettings rabbitMqSettings;
    private final DataProducerRepository dataProducerRepository;

    @RabbitListener(id = SALARY_CALCULATOR_RECEIVER_ID, queues = "${rabbitmq.queueSalaryCalculator}")
    public void messageReceiver(MicroservicesData microservicesData) {
        this.dataProducerRepository.save(microservicesData);
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
        getCalculationFromMicroservices(grossMonthlySalary, uuid);
        var response = collectMicroservicesResponse(uuid);
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

    public Map<String, BigDecimal> collectMicroservicesResponse(UUID uuid) {
        Map<String, BigDecimal> response = new HashMap<>();
        List<MicroservicesData> data = new ArrayList<>();
        var count = 0;
        while (data.size() < MICROSERVICE_COUNT) {
            data = this.dataProducerRepository.findDataByUuid(uuid);
            count++;
            if (count == NUMBER_OF_CHECKS) {
                break;
            }
        }
        data.forEach(microservicesData -> response.put(microservicesData.getDescription(), microservicesData.getAmount()));
        return response;
    }


    public void getCalculationFromMicroservices(BigDecimal grossMonthlySalary, UUID uuid) {
        this.rabbitTemplate.convertAndSend(this.rabbitMqSettings.getExchange(), this.rabbitMqSettings.getRoutingKey(), MicroservicesData.builder().amount(grossMonthlySalary).description(this.getClass().getSimpleName()).uuid(uuid).build());
    }


    public Map<String, BigDecimal> getAverage(BigDecimal grossMonthlySalary, String jobTitleId, Map<String, BigDecimal> response) {
        BigDecimal average = statistic(jobTitleId, grossMonthlySalary);
        response.put("Average", average.setScale(2, RoundingMode.HALF_EVEN));
        return response;

    }
    public BigDecimal statistic(String jobTitleId, BigDecimal grossMonthlySalary) {
        this.dataSalaryCalculatorRepository.save(SalaryCalculatorOrchestratorData.builder().grossMonthlySalary(grossMonthlySalary).jobTitle(jobTitleId).build());
        return this.dataSalaryCalculatorRepository.findAverageByJobTitle((jobTitleId)).setScale(2, RoundingMode.HALF_EVEN);
    }
}



