package com.acoustic.controller;


import com.acoustic.entity.SalaryCalculatorOrchestratorData;
import com.acoustic.jobcategories.JobCategoriesConfigurationProperties;
import com.acoustic.model.MicroservicesData;
import com.acoustic.rabbitmqsettings.RabbitMqSettings;
import com.acoustic.repository.DataProducerRepository;
import com.acoustic.repository.DataSalaryCalculatorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
public class SalaryCalculatorOrchestratorController{

    private static final int MINIMUM_GROSS = 2000;
    private final JobCategoriesConfigurationProperties jobCategoriesConfigurationProperties;

    private final DataSalaryCalculatorRepository dataSalaryCalculatorRepository;

    private final RabbitTemplate rabbitTemplate;

    private final RabbitMqSettings rabbitMqSettings;

    private final DataProducerRepository dataProducerRepository;




    @RabbitListener(queues = "${rabbitmq.queueSalaryCalculator}")
    public void receivedMessage(MicroservicesData microservicesData) {
        dataProducerRepository.save(microservicesData);
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
        var uuid = getCalculationFromMicroservices(grossMonthlySalary);
        var response = collectMicroservicesData(uuid);
        if (departmentName == null || jobTitleId == null) {
            return response;
        }
        List<String> jobTitlesList = this.jobCategoriesConfigurationProperties.getJobDepartmentAndTitles().get(departmentName);
        if (!this.jobCategoriesConfigurationProperties.getJobDepartmentAndTitles().containsKey(departmentName.toLowerCase())) {
            throw new IllegalArgumentException("Invalid department name");
        }

        if (jobTitleId > jobTitlesList.size() || jobTitleId <= 0) {
            throw new IllegalArgumentException("Wrong job id");
        }
        return getAverage(grossMonthlySalary, jobTitlesList.get(jobTitleId - 1), response);
    }

    private Map<String, BigDecimal> collectMicroservicesData(UUID uuid) {
        var start = System.currentTimeMillis();
        Map<String, BigDecimal> response = new HashMap<>();
        List<MicroservicesData> data = new ArrayList<>();
        var count = 0;
        while (data.size() < 10) {
            data = dataProducerRepository.findDataByUuid(uuid);
            count++;
            if (count == 40) {
                break;
            }
        }
        log.info(data.toString() + " - length: " + data.size());
        data.forEach(microservicesData -> response.put(microservicesData.getDescription(), microservicesData.getAmount()));
        var end = System.currentTimeMillis();
        log.warn("Total time taken: " + (end - start));
        return response;
    }


    private UUID getCalculationFromMicroservices(BigDecimal grossMonthlySalary) {
        var uuid = UUID.randomUUID();
        rabbitTemplate.convertAndSend(rabbitMqSettings.getExchange(), "", MicroservicesData.builder().amount(grossMonthlySalary).description(this.getClass().getSimpleName()).uuid(uuid).build());
        return uuid;

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



