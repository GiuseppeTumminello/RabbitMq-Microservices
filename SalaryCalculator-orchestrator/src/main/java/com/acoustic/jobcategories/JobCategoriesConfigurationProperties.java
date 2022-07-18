package com.acoustic.jobcategories;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;
import java.util.Map;


@Getter
@PropertySource("classpath:jobs.properties")
@ConfigurationProperties(prefix = "jobs")
@Configuration
public class JobCategoriesConfigurationProperties {


    @Value("#{${jobs.jobTitles}}")
    private Map<String, List<String>> jobDepartmentAndTitles;

}
