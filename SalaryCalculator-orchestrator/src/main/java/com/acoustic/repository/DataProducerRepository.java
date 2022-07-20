package com.acoustic.repository;

import com.acoustic.model.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface DataProducerRepository extends JpaRepository<Data, Integer> {

    @Query(value = "select * from data where uuid=:uuid", nativeQuery = true)
    List<Data> findDataByUuid(@Param("uuid") UUID uuid);


}
