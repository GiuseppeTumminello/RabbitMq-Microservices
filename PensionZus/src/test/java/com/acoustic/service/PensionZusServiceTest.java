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
class PensionZusServiceTest {

    public static final String PENSION_ZUS_DESCRIPTION = "Pension zus";
    @InjectMocks
    private PensionZusService salaryCalculatorService;

    @Mock
    private RatesConfigurationProperties ratesConfigurationProperties;

    @Test
    void getDescription() {
        assertThat(this.salaryCalculatorService.getDescription()).isEqualTo(PENSION_ZUS_DESCRIPTION);
    }

    @ParameterizedTest
    @CsvSource({"6000, 585.60, 0.0976", "7000, 683.20, 0.0976", "15143.99,1478.05, 0.0976"})
    public void getPensionZus(BigDecimal input, BigDecimal expected, BigDecimal rate) {
        given(this.ratesConfigurationProperties.getPensionZusRate()).willReturn(rate);
        assertThat(this.salaryCalculatorService.apply(input)).isEqualTo(expected);
    }
}