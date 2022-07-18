package com.acoustic.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.acoustic.entity.AnnualNet;

@Repository
public interface AnnualNetRepository extends JpaRepository<AnnualNet, Integer> {



}
