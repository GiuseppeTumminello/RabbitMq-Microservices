package com.acoustic.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.acoustic.entity.DisabilityZus;

@Repository
public interface DisabilityZusRepository extends JpaRepository<DisabilityZus, Integer> {



}
