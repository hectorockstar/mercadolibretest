package com.mercadolibretest.repository;

import com.mercadolibretest.model.UrlEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UrlManagementRepository extends CrudRepository<UrlEntity, UUID> {

}
