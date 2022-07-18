package com.acoustic.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.acoustic.entity.MonthlyGross;

@Repository
public interface MonthlyGrossRepository extends JpaRepository<MonthlyGross, Long> {



}
