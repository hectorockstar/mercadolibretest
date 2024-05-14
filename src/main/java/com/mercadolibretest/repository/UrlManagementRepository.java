package com.mercadolibretest.repository;

import com.mercadolibretest.model.UrlEntity;
import org.springframework.data.repository.CrudRepository;

import java.math.BigInteger;
import java.util.Optional;
import java.util.UUID;

public interface UrlManagementRepository extends CrudRepository<UrlEntity, UUID> {

    UrlEntity findByShortUrl(String shortUrl);

    Long deleteByShortUrl(String shortUrl);

}
