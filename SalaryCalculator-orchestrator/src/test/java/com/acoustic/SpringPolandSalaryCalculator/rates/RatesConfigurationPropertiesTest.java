package com.acoustic.SpringPolandSalaryCalculator.rates;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.math.BigDecimal;


@ConfigurationProperties(prefix = "response")
@PropertySource("classpath:response-test.properties")
@Configuration
@Getter
@Setter
public class RatesConfigurationPropertiesTest {

    private BigDecimal pensionZusRate;

    private BigDecimal disabilityZusRate;

    private BigDecimal sicknessZusRate;

    private BigDecimal totalZusRate;

    private BigDecimal healthRate;

    private BigDecimal taxRate17Rate;

    private BigDecimal taxRate32Rate;

    private BigDecimal taxGrossAmountThreshold;

    private BigDecimal minimumSalary;

}
