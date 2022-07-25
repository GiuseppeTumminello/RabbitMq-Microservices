package com.acoustic.repository;

import com.acoustic.entity.MicroservicesData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.UUID;

public interface MicroservicesDataRepository extends JpaRepository<MicroservicesData, Integer> {

    @Query(value = "select * from microservices_data where uuid=:uuid", nativeQuery = true)
    ArrayList<MicroservicesData> findDataByUuid(@Param("uuid") UUID uuid);


}
