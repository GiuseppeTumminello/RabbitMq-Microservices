package com.acoustic.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@ExtendWith(MockitoExtension.class)
class MonthlyGrossServiceTest {

    public static final String MONTHLY_GROSS_DESCRIPTION = "Monthly gross";
    @InjectMocks
    private MonthlyGrossService salaryCalculatorService;


    @Test
    void getDescription() {
        assertThat(this.salaryCalculatorService.getDescription()).isEqualTo(MONTHLY_GROSS_DESCRIPTION);
    }

    @ParameterizedTest
    @CsvSource({"6000, 6000.00", "7000, 7000.00", "15143.99,15143.99"})
    public void getMonthlyGross(BigDecimal input, BigDecimal expected) {
        assertThat(this.salaryCalculatorService.apply(input)).isEqualTo(expected);
    }
}