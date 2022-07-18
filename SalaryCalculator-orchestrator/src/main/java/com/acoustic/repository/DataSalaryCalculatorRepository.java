package com.acoustic.repository;


import com.acoustic.entity.SalaryCalculatorOrchestratorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface DataSalaryCalculatorRepository extends JpaRepository<SalaryCalculatorOrchestratorData, Integer> {

    @Query(value = "select avg(gross_monthly_salary) from salary_calculator_orchestrator_data where job_title=:jobTitle", nativeQuery = true)
    BigDecimal findAverageByJobTitle(@Param("jobTitle") String jobTitle);


}
