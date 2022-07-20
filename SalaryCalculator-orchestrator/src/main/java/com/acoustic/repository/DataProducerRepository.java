package com.acoustic.repository;

import com.acoustic.model.MicroservicesData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface DataProducerRepository extends JpaRepository<MicroservicesData, Integer> {

    @Query(value = "select * from data where uuid=:uuid", nativeQuery = true)
    CompletableFuture<List<MicroservicesData>> findDataByUuid(@Param("uuid") UUID uuid);


}
