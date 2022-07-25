package com.acoustic.service;

import com.acoustic.entity.PensionZus;

import java.math.BigDecimal;
import java.util.function.UnaryOperator;



public interface SalaryCalculatorService extends UnaryOperator<BigDecimal> {

     String getDescription();


     void sendPensionZus(PensionZus monthlyNet);


}
