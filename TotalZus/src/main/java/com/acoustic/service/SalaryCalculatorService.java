package com.acoustic.service;

import com.acoustic.entity.TotalZus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.function.UnaryOperator;


@Service
public interface SalaryCalculatorService extends UnaryOperator<BigDecimal> {
     String getDescription();

     void sendTotalZus(TotalZus totalZus);

}
