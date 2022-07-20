package com.acoustic.controller;


import com.acoustic.entity.AnnualNet;
import com.acoustic.repository.AnnualNetRepository;
import com.acoustic.service.SalaryCalculatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/annual-net")
@RequiredArgsConstructor
@Validated
@CrossOrigin
@Slf4j
public class AnnualNetController implements RabbitListenerConfigurer {


    public static final int MINIMUM_GROSS = 2000;
    private final AnnualNetRepository annualNetRepository;
    private final SalaryCalculatorService salaryCalculatorService;




    @RabbitListener(queues = "${rabbitmq.queueAnnualNet}")
    public void receivedMessage(AnnualNet annualNet) {
        log.warn(annualNet.getUuid().toString());
        sendAnnualNetEndpointDataToReceiver(annualNet.getAmount(),annualNet.getUuid());

    }


    @PostMapping("/calculation/{grossMonthlySalary}")
    public ResponseEntity<Map<String, String>> calculateAnnualNetEndpoint(@PathVariable @Min(MINIMUM_GROSS)BigDecimal grossMonthlySalary){
        var annualNet = calculateAnnualNet(grossMonthlySalary);
        saveAnnualNet(annualNet, UUID.randomUUID());
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(this.salaryCalculatorService.getDescription(), String.valueOf(annualNet)));
    }

    private void sendAnnualNetEndpointDataToReceiver(BigDecimal grossMonthlySalary, UUID uuid) {
        var annualGrossSalary = calculateAnnualNet(grossMonthlySalary);
        this.salaryCalculatorService.sendAnnualNet(AnnualNet.builder().description(this.salaryCalculatorService.getDescription()).amount(annualGrossSalary).uuid(uuid).build());
    }

    private void saveAnnualNet(BigDecimal annualNet, UUID uuid) {
        this.annualNetRepository.saveAndFlush(AnnualNet.builder().description(this.salaryCalculatorService.getDescription()).amount(annualNet).uuid(uuid).build());
    }

    private BigDecimal calculateAnnualNet(BigDecimal grossMonthlySalary) {
        return this.salaryCalculatorService.apply(grossMonthlySalary);
    }


    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {

    }
}
