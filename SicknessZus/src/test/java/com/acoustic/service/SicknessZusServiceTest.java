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
class SicknessZusServiceTest {

    public static final String SICKNESS_ZUS_DESCRIPTION = "Sickness zus";
    @InjectMocks
    private PensionZusService salaryCalculatorService;

    @Mock
    private RatesConfigurationProperties ratesConfigurationProperties;

    @Test
    void getDescription() {
        assertThat(this.salaryCalculatorService.getDescription()).isEqualTo(SICKNESS_ZUS_DESCRIPTION);
    }

    @ParameterizedTest
    @CsvSource({"6000, 147.00, 0.0245", "7000, 171.50, 0.0245", "15143.99,371.03, 0.0245"})
    public void getSicknessZus(BigDecimal input, BigDecimal expected, BigDecimal rate) {
        given(this.ratesConfigurationProperties.getSicknessZusRate()).willReturn(rate);
        assertThat(this.salaryCalculatorService.apply(input)).isEqualTo(expected);
    }
}