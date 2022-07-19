package com.acoustic.model;

import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DataProducer {

    private BigDecimal amount;
    private String uuid;
}
