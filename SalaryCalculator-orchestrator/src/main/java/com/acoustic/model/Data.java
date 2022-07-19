package com.acoustic.model;


import lombok.*;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Data {

    private int id;

    private String description;
    private String amount;




}
