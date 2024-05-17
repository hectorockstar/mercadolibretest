package com.mercadolibretest.repository;

import com.mercadolibretest.model.UrlEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.math.BigInteger;
import java.util.UUID;

public interface UrlManagementRepository extends CrudRepository<UrlEntity, UUID> {

    UrlEntity findByShortUrl(String shortUrl);

    void deleteById(BigInteger id);

    @Modifying
    @Query(value = "UPDATE URLS SET LAST_VISITED_AT = CURRENT_TIMESTAMP, VISITS_NUMBER = VISITS_NUMBER+1 WHERE ID = ?1 ",
            nativeQuery = true)
    void updateUrlEntity(BigInteger id);

}
