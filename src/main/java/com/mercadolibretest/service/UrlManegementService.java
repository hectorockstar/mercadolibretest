package com.mercadolibretest.service;

import com.mercadolibretest.constants.MercadoLibreTestConstants;
import com.mercadolibretest.exceptionhandler.custom.DateCustomException;
import com.mercadolibretest.exceptionhandler.custom.UrlConfigActionException;
import com.mercadolibretest.dto.UrlDataRequest;
import com.mercadolibretest.dto.UrlDataResponse;
import com.mercadolibretest.model.UrlEntity;
import com.mercadolibretest.repository.UrlManagementRepository;
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
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class UrlManegementService {

    private final UrlManagementRepository urlManagementRepository;

    @Autowired
    public UrlManegementService(UrlManagementRepository urlManagementRepository) {
        this.urlManagementRepository = urlManagementRepository;
    }

    @SneakyThrows
    public UrlDataResponse createShortUrl(UrlDataRequest urlDataRequest) {
        Utils.urlValidator(urlDataRequest.getLongUrl());
        String shortUrl = this.getEncodedUrl(urlDataRequest.getLongUrl());

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
    public UrlDataResponse getLongUrlByShortUrl(String url) {

        String shortUrl = url;
        if (Utils.isLongUrl(url)) {
            shortUrl = this.getEncodedUrl(url);
        }

        UrlEntity urlEntity = urlManagementRepository.findByShortUrl(shortUrl);
        if(urlEntity == null) {
            throw UrlConfigActionException.create("URL_NOT_AVAILABLE");
        }
        return UrlDataResponse.getUrlDataResponseBuilder(urlEntity);
    }

    @SneakyThrows
    public String showUrl(String url, UrlDataResponse urlDataResponse) {
        return Utils.isLongUrl(url) ? urlDataResponse.getShortUrl() : urlDataResponse.getLongUrl();
    }

    @Transactional
    @SneakyThrows
    public void redirectToLonglUrlbyShortUrl(UrlDataResponse urlDataResponse, HttpServletResponse httpServletResponse) {
        urlManagementRepository.updateUrlEntity(new BigInteger(urlDataResponse.getId()));
        httpServletResponse.setStatus(HttpStatus.MOVED_TEMPORARILY.value());
        httpServletResponse.setHeader(HttpHeaders.LOCATION, urlDataResponse.getLongUrl());
        httpServletResponse.setHeader(HttpHeaders.CONNECTION, "close");
    }

    @Transactional
    @SneakyThrows
    public UrlEntity deleteUrlConfigByShortUrl(String shortUrl) {
        UrlEntity urlEntity = urlManagementRepository.findByShortUrl(shortUrl);

        if(urlEntity == null) {
            throw UrlConfigActionException.create("URL_NOT_EXIST");
        }
        urlManagementRepository.deleteById(urlEntity.getId());
        return urlEntity;
    }

    @Transactional
    @SneakyThrows
    public UrlDataResponse updateUrlConfigByShortUrl(String shortUrl, UrlDataRequest urlDataRequest) {
        UrlEntity urlEntity = urlManagementRepository.findByShortUrl(shortUrl);
        if(urlEntity == null) {
            throw UrlConfigActionException.create("URL_NOT_EXIST");
        }

        String expiredAt = urlDataRequest.getExpiredAt();
        if(expiredAt != null && !Utils.expiredDateValidator(Utils.stringDateToDateFormatter(expiredAt))){
            throw DateCustomException.create("DATE_EXPIRED");
        }

        urlEntity.setIsAvailable(urlDataRequest.getIsAvailable());
        urlEntity.setExpiredAt(Utils.stringDateToDateFormatter(expiredAt));
        urlEntity.setUpdatedAt(Utils.getSystemDate());

        urlManagementRepository.save(urlEntity);
        return UrlDataResponse.getUrlDataResponseBuilder(urlEntity);
    }

    private String getEncodedUrl(String url) throws NoSuchAlgorithmException {
        byte[] sha256EncodedHash = MessageDigest.getInstance(MercadoLibreTestConstants.SHA256)
                                        .digest(url.getBytes(StandardCharsets.UTF_8));
        String base64Encoded = Base64.getEncoder().encodeToString(sha256EncodedHash);

        StringBuilder sb = new StringBuilder();
        sb.append(base64Encoded);

        return sb.substring(0, MercadoLibreTestConstants.URL_MAX_SIZE).toUpperCase();
    }


}
