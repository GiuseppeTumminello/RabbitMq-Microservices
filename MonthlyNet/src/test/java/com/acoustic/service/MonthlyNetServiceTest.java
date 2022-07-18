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
class MonthlyNetServiceTest {

    public static final double TOTAL_ZUS_RATE = 0.1371;
    public static final double HEALTH_RATE = 0.09;
    public static final int TAX_GROSS_AMOUNT_THRESHOLD = 120000;

    public static final double TAX_AMOUNT_RATE_17 = 0.0832;

    public static final double TAX_AMOUNT_RATE_32 = 0.1432;
    public static final String MONTHLY_NET_DESCRIPTION = "Monthly net";
    @InjectMocks
    private MonthlyNetService monthlyNetService;
    @Mock
    private RatesConfigurationProperties ratesConfigurationProperties;

    @Test
    void getDescription() {
        assertThat(this.monthlyNetService.getDescription()).isEqualTo(MONTHLY_NET_DESCRIPTION);
    }

    @ParameterizedTest
    @CsvSource({"10188.77,6854.93", "15999.72, 10764.50"})
    public void getMonthlyNetBasedOnRate32(BigDecimal input, BigDecimal expected) {
        given(this.ratesConfigurationProperties.getTaxRate32Rate()).willReturn(BigDecimal.valueOf(TAX_AMOUNT_RATE_32));
        given(this.ratesConfigurationProperties.getTotalZusRate()).willReturn(BigDecimal.valueOf(TOTAL_ZUS_RATE));
        given(this.ratesConfigurationProperties.getHealthRate()).willReturn(BigDecimal.valueOf(HEALTH_RATE));
        given(this.ratesConfigurationProperties.getTaxGrossAmountThreshold()).willReturn(BigDecimal.valueOf(TAX_GROSS_AMOUNT_THRESHOLD));
        assertThat(this.monthlyNetService.apply(input)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"6000,4319.44", "7000, 5039.35", "8555,6158.81"})
    public void getMonthlyNetBasedOnRate17(BigDecimal input, BigDecimal expected) {
        given(this.ratesConfigurationProperties.getTaxRate17Rate()).willReturn(BigDecimal.valueOf(TAX_AMOUNT_RATE_17));
        given(this.ratesConfigurationProperties.getTotalZusRate()).willReturn(BigDecimal.valueOf(TOTAL_ZUS_RATE));
        given(this.ratesConfigurationProperties.getHealthRate()).willReturn(BigDecimal.valueOf(HEALTH_RATE));
        given(this.ratesConfigurationProperties.getTaxGrossAmountThreshold()).willReturn(BigDecimal.valueOf(TAX_GROSS_AMOUNT_THRESHOLD));
        assertThat(this.monthlyNetService.apply(input)).isEqualTo(expected);
    }
}

