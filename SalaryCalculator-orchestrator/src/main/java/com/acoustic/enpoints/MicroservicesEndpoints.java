package com.acoustic.enpoints;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "microservice")
@PropertySource("classpath:microservices-endpoints.properties")
@Getter
@Setter
public class MicroservicesEndpoints {

    private List<String> endpoints;
}
