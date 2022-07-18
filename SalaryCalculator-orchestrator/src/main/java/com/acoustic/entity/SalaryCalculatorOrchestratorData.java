package com.acoustic.entity;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalaryCalculatorOrchestratorData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private BigDecimal grossMonthlySalary;

    private String jobTitle;
}
