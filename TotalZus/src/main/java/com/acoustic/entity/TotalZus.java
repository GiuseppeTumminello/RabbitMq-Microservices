package com.acoustic.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TotalZus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private BigDecimal totalZusAmount;
    private BigDecimal totalZusRate;


}
