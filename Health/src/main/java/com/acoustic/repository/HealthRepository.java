package com.acoustic.repository;


import com.acoustic.entity.Health;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthRepository extends JpaRepository<Health, Integer> {



}
