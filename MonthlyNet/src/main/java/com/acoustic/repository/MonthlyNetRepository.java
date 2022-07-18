package com.acoustic.repository;


import com.acoustic.entity.MonthlyNet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonthlyNetRepository extends JpaRepository<MonthlyNet, Integer> {


}
