package com.acoustic;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class AnnualNetApplication {
    public static void main(String[] args) {
        SpringApplication.run(AnnualNetApplication.class, args);
    }
}
