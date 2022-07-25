package com.acoustic.service;

import com.acoustic.entity.Health;

import java.math.BigDecimal;
import java.util.function.UnaryOperator;



public interface SalaryCalculatorService extends UnaryOperator<BigDecimal> {

     String getDescription();

     void sendHealth(Health health);


}
