package com.acoustic.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.acoustic.entity.SicknessZus;

@Repository
public interface SicknessZusRepository extends JpaRepository<SicknessZus, Integer> {



}
