package com.acoustic.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.acoustic.entity.PensionZus;

@Repository
public interface PensionZusRepository extends JpaRepository<PensionZus, Integer> {



}
