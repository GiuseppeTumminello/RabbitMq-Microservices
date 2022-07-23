package com.acoustic.controller;


import com.acoustic.entity.Tax;
import com.acoustic.repository.TaxRepository;
import com.acoustic.service.SalaryCalculatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tax")
@CrossOrigin
@Slf4j
public class TaxController {

    public static final int MINIMUM_GROSS = 2000;
    private  static final String TAX_RECEIVER_ID = "taxReceiverId";
    private final TaxRepository taxRepository;
    private final SalaryCalculatorService salaryCalculatorService;


    @RabbitListener(id = TAX_RECEIVER_ID,queues = "${rabbitmq.queueTax}")
    public void receiveMessage(Tax tax) {
        sendTaxDataToSalaryCalculatorOrchestrator(tax.getAmount(), tax.getUuid());

    }


    @PostMapping("/calculation/{grossMonthlySalary}")
    public ResponseEntity<Map<String, String>> calculationTaxEndpoint(@PathVariable @Min(MINIMUM_GROSS) BigDecimal grossMonthlySalary) {
        var taxZus = calculateTax(grossMonthlySalary);
        saveTax(taxZus, UUID.randomUUID());
        return ResponseEntity.status(HttpStatus.OK).body(Map.of(this.salaryCalculatorService.getDescription(), String.valueOf(taxZus)));
    }

    private void sendTaxDataToSalaryCalculatorOrchestrator(BigDecimal grossMonthlySalary, UUID uuid) {
        var tax = calculateTax(grossMonthlySalary);
        var taxData = saveTax(tax, uuid);
        this.salaryCalculatorService.sendTax(taxData);
    }

    private Tax saveTax(BigDecimal tax, UUID uuid) {
        return this.taxRepository.saveAndFlush(Tax.builder().description(this.salaryCalculatorService.getDescription()).amount(tax).uuid(uuid).build());
    }

    private BigDecimal calculateTax(BigDecimal grossMonthlySalary) {
        return this.salaryCalculatorService.apply(grossMonthlySalary);
    }


}
