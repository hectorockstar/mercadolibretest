package com.mercadolibretest.repository;

import com.mercadolibretest.model.UrlEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UrlManagementRepository extends CrudRepository<UrlEntity, UUID> {

    //@Cacheable("findByShortUrl")
    UrlEntity findByShortUrl(String shortUrl);

    Long deleteByShortUrl(String shortUrl);

}
