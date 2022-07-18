package com.acoustic.configuartion;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title("Salary Calculator Microservice")
                .version("1.0")
                .description("The microservice calculates all the taxation from the gross monthly salary, the user can also participate to a statistics to check if its salary is above or below the average")
                .contact(new Contact().name("Giuseppe Tumminello"))
                .license(new License().name("Apache 2.0")));
    }

}

