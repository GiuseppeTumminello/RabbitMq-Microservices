package com.acoustic.repository;


import com.acoustic.entity.TotalZus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TotalZusRepository extends CrudRepository<TotalZus, Integer> {



}
