package com.acoustic.service;

import com.acoustic.rate.RatesConfigurationProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class TotalZusServiceTest {

    public static final String TOTAL_ZUS_DESCRIPTION = "Total zus";
    @InjectMocks
    private  TotalZusService salaryCalculatorService;

    @Mock
    private RatesConfigurationProperties ratesConfigurationProperties;

    @Test
    void getDescription() {
        assertThat(this.salaryCalculatorService.getDescription()).isEqualTo(TOTAL_ZUS_DESCRIPTION);
    }

    @ParameterizedTest
    @CsvSource({"6000, 822.60, 0.1371", "7000, 959.70, 0.1371", "15891.68, 2178.75, 0.1371"})
    public void getTotalZus(BigDecimal input, BigDecimal expected, BigDecimal rate) {
        given(this.ratesConfigurationProperties.getTotalZusRate()).willReturn(rate);
        assertThat(this.salaryCalculatorService.apply(input)).isEqualTo(expected);
    }
}