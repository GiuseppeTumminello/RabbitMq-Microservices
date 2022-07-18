package com.acoustic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


@SpringBootApplication
@EnableEurekaClient
public class DisabilityZusApplication {
    public static void main(String[] args) {
        SpringApplication.run(DisabilityZusApplication.class, args);
    }
}