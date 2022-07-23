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
class AnnualGrossApplicationServiceTest {

    public static final String ANNUAL_GROSS_DESCRIPTION = "Annual gross";
    @InjectMocks
    private AnnualGrossAmount salaryCalculatorService;

    @Test
    void getDescription() {
        assertThat(this.salaryCalculatorService.getDescription()).isEqualTo(ANNUAL_GROSS_DESCRIPTION);
    }

    @ParameterizedTest
    @CsvSource({"6000, 72000.00", "7000, 84000.00", "15143.99,181727.88"})
    public void getAnnualGross(BigDecimal input, BigDecimal expected) {
        assertThat(this.salaryCalculatorService.apply(input)).isEqualTo(expected);
    }
}