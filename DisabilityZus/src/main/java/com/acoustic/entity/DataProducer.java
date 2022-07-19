package com.acoustic.entity;

import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DataProducer {
    private BigDecimal amount;
    private UUID uuid;
}
