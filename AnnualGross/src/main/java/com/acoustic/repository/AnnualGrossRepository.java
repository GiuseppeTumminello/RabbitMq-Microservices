package com.acoustic.repository;

import com.acoustic.entity.AnnualGross;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnualGrossRepository extends JpaRepository<AnnualGross, Integer> {
}
