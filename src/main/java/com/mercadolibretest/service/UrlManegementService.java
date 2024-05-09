package com.mercadolibretest.service;

import com.mercadolibretest.exceptionhandler.custom.URLException;
import com.mercadolibretest.model.UrlEntity;
import com.mercadolibretest.repository.UrlManagementRepository;
import lombok.SneakyThrows;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Date;

@Service
public class UrlManegementService {

    private final UrlManagementRepository urlManagementRepository;

    @Autowired
    public UrlManegementService(UrlManagementRepository urlManagementRepository) {
        this.urlManagementRepository = urlManagementRepository;
    }

    @SneakyThrows
    public UrlEntity getShortUrl(String url) {

        UrlEntity urlEntity = UrlEntity.getUrlEntityBuilder(new BigInteger("1"), url, "short url", new Date());

        urlManagementRepository.save(urlEntity);
        System.out.println(url);

        return urlEntity;
    }


}
