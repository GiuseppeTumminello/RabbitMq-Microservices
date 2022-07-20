package com.acoustic.controller;


import com.acoustic.entity.SalaryCalculatorOrchestratorData;
import com.acoustic.jobcategories.JobCategoriesConfigurationProperties;
import com.acoustic.model.DataProducer;
import com.acoustic.rabbitmqsettings.RabbitMqSettings;
import com.acoustic.repository.DataProducerRepository;
import com.acoustic.repository.DataSalaryCalculatorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/salary-calculations")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@Slf4j
public class SalaryCalculatorOrchestratorController implements RabbitListenerConfigurer {

    private static final int MINIMUM_GROSS = 2000;
    private final JobCategoriesConfigurationProperties jobCategoriesConfigurationProperties;

    private final DataSalaryCalculatorRepository dataSalaryCalculatorRepository;
    private final Map<String, BigDecimal> response;


    private final RabbitTemplate rabbitTemplate;

    private final RabbitMqSettings rabbitMqSettings;

    private final DataProducerRepository dataProducerRepository;


    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {

    }

//    @RabbitListener(queues = "${rabbitmq.queue}")
//    public void receivedMessage(Data data) {
//        this.response.put(data.getDescription(), data.getAmount());
//        System.out.println(response);
//
//
//    }


    @GetMapping("/jobs/{departmentName}")
    public List<String> getJobTitles(@PathVariable String departmentName) {
        return this.jobCategoriesConfigurationProperties.getJobDepartmentAndTitles().get(departmentName);
    }

    @GetMapping("/departments")
    public Set<String> getDepartmentName() {
        return this.jobCategoriesConfigurationProperties.getJobDepartmentAndTitles().keySet();
    }


    @PostMapping("/calculations/{grossMonthlySalary}")
    public Map<String, BigDecimal> calculateSalary(@PathVariable @Min(value = MINIMUM_GROSS, message = "Must be Greater than or equal to 2000.00") @NotNull BigDecimal grossMonthlySalary, @RequestParam(required = false) String departmentName, @RequestParam(required = false) Integer jobTitleId) throws InterruptedException {

        var uuid = getCalculationFromMicroservices(grossMonthlySalary);

        var data = dataProducerRepository.findDataByUuid(uuid);


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


        return getAverage(grossMonthlySalary, jobTitlesList.get(jobTitleId - 1), this.response);
    }


    private UUID getCalculationFromMicroservices(BigDecimal grossMonthlySalary) {
        var uuid = UUID.randomUUID();
        rabbitTemplate.convertAndSend(rabbitMqSettings.getExchange(), "", new DataProducer(grossMonthlySalary, uuid));
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



