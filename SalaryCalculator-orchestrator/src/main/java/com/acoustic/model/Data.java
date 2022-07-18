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
public class Data {

    private int id;

    private String description;
    private BigDecimal value;




}
