package com.acoustic.repository;


import com.acoustic.entity.TotalZus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TotalZusRepository extends JpaRepository<TotalZus, Integer> {



}
