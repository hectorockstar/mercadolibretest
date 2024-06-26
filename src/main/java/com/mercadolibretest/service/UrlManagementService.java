package com.mercadolibretest.service;

import com.mercadolibretest.dto.UrlUpdateDataRequest;
import com.mercadolibretest.exceptionhandler.custom.DateCustomException;
import com.mercadolibretest.exceptionhandler.custom.UrlConfigActionException;
import com.mercadolibretest.dto.UrlDataRequest;
import com.mercadolibretest.dto.UrlDataResponse;
import com.mercadolibretest.model.UrlEntity;
import com.mercadolibretest.repository.UrlManagementRepository;
import com.mercadolibretest.utils.UrlUtils;
import com.mercadolibretest.utils.Utils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

@Service
public class UrlManagementService {

    private final EncodeUrlService encodeUrlService;
    private final UrlManagementRepository urlManagementRepository;

    @Autowired
    public UrlManagementService(EncodeUrlService encodeUrlService, UrlManagementRepository urlManagementRepository) {
        this.encodeUrlService = encodeUrlService;
        this.urlManagementRepository = urlManagementRepository;
    }

    @SneakyThrows
    public UrlDataResponse createShortUrl(UrlDataRequest urlDataRequest) {
        String shortUrl = encodeUrlService.getEncodedUrl(urlDataRequest.getLongUrl());

        UrlEntity urlEntityExist = urlManagementRepository.findByShortUrl(shortUrl);
        if (urlEntityExist != null) {
            throw UrlConfigActionException.create("URL_EXIST");
        }

        UrlEntity urlEntity = UrlEntity.getUrlEntityBuilder(urlDataRequest, shortUrl);
        if (!Utils.expiredDateValidator(urlEntity.getExpiredAt())) {
            throw DateCustomException.create("DATE_EXPIRED");
        }
        urlManagementRepository.save(urlEntity);
        return UrlDataResponse.getUrlDataResponseBuilder(urlEntity);
    }

    @Cacheable("getLongUrlByShortUrl")
    @SneakyThrows
    public UrlDataResponse getLongUrlByShortUrlCacheable(String url) {
        return this.getLongUrlByShortUrl(url);
    }

    @SneakyThrows
    public UrlDataResponse getLongUrlByShortUrl(String url) {

        String shortUrl = url;
        if (UrlUtils.isLongUrl(url)) {
            shortUrl = encodeUrlService.getEncodedUrl(url);
        }

        UrlEntity urlEntity = urlManagementRepository.findByShortUrl(shortUrl);
        if(urlEntity == null) {
            throw UrlConfigActionException.create("URL_NOT_AVAILABLE");
        }
        return UrlDataResponse.getUrlDataResponseBuilder(urlEntity);
    }

    @SneakyThrows
    public String showUrl(String url, UrlDataResponse urlDataResponse) {
        return UrlUtils.isLongUrl(url) ? urlDataResponse.getShortUrl() : urlDataResponse.getLongUrl();
    }

    @Transactional
    @SneakyThrows
    public void redirectToLongUrlByShortUrl(UrlDataResponse urlDataResponse, HttpServletResponse httpServletResponse) {

        if(!urlDataResponse.getIsAvailable()) {
            throw UrlConfigActionException.create("URL_NOT_AVAILABLE");
        }

        String expiredAt = urlDataResponse.getExpiredAt();
        if(expiredAt == null || !Utils.expiredDateValidator(Utils.stringDateToDateFormatter(expiredAt))){
            throw UrlConfigActionException.create("URL_EXPIRED");
        }

        urlManagementRepository.updateUrlEntity(new BigInteger(urlDataResponse.getId()));

        httpServletResponse.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*");
        httpServletResponse.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "*");
        httpServletResponse.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "*");
        httpServletResponse.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        httpServletResponse.setStatus(HttpStatus.MOVED_TEMPORARILY.value());
        httpServletResponse.setHeader(HttpHeaders.LOCATION, urlDataResponse.getLongUrl());
        httpServletResponse.setHeader(HttpHeaders.CONNECTION, "close");
    }

    @Transactional
    @SneakyThrows
    public UrlDataResponse deleteUrlConfigByShortUrl(String shortUrl) {
        UrlEntity urlEntity = urlManagementRepository.findByShortUrl(shortUrl);

        if(urlEntity == null) {
            throw UrlConfigActionException.create("URL_NOT_EXIST");
        }
        urlManagementRepository.deleteById(urlEntity.getId());
        return UrlDataResponse.getUrlDataResponseBuilder(urlEntity);
    }

    @Transactional
    @SneakyThrows
    public UrlDataResponse updateUrlConfigByShortUrl(String shortUrl, UrlUpdateDataRequest urlUpdateDataRequest) {
        UrlEntity urlEntity = urlManagementRepository.findByShortUrl(shortUrl);
        if(urlEntity == null) {
            throw UrlConfigActionException.create("URL_NOT_EXIST");
        }

        String expiredAt = urlUpdateDataRequest.getExpiredAt();
        if(expiredAt == null || !Utils.expiredDateValidator(Utils.stringDateToDateFormatter(expiredAt))){
            throw DateCustomException.create("DATE_EXPIRED");
        }

        urlEntity.setIsAvailable(urlUpdateDataRequest.getIsAvailable());
        urlEntity.setExpiredAt(Utils.stringDateToDateFormatter(expiredAt));
        urlEntity.setUpdatedAt(Utils.getSystemDate());

        urlManagementRepository.save(urlEntity);
        return UrlDataResponse.getUrlDataResponseBuilder(urlEntity);
    }
}
