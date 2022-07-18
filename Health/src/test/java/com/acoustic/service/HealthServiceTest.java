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
class HealthServiceTest {

    public static final String HEALTH_DESCRIPTION = "Health";
    public static final double TOTAL_ZUS_RATE = 0.1371;
    public static final double HEALTH_RATE = 0.09;
    @InjectMocks
    private HealthService healthService;

    @Mock
    private RatesConfigurationProperties ratesConfigurationProperties;

    @Test
    void getDescription() {
        assertThat(this.healthService.getDescription()).isEqualTo(HEALTH_DESCRIPTION);
    }

    @ParameterizedTest
    @CsvSource({"6000, 465.97", "7000, 543.63", "15143.99, 1176.10"})
    public void getHealth(BigDecimal input, BigDecimal expected) {
        given(this.ratesConfigurationProperties.getHealthRate()).willReturn(BigDecimal.valueOf(HEALTH_RATE));
        given(this.ratesConfigurationProperties.getTotalZusRate()).willReturn(BigDecimal.valueOf(TOTAL_ZUS_RATE));
        assertThat(this.healthService.apply(input)).isEqualTo(expected);
    }
}