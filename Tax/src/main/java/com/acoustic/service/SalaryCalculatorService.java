package com.acoustic.service;

import com.acoustic.entity.Tax;

import java.math.BigDecimal;
import java.util.function.UnaryOperator;



public interface SalaryCalculatorService extends UnaryOperator<BigDecimal> {

    String getDescription();

    void sendTax(Tax tax);




}
