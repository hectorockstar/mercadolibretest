package com.mercadolibretest.service;

import com.mercadolibretest.constants.MercadoLibreTestConstants;
import com.mercadolibretest.exceptionhandler.custom.UrlConfigActionException;
import com.mercadolibretest.model.UrlEntity;
import com.mercadolibretest.repository.UrlManagementRepository;
import com.mercadolibretest.utils.Utils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;

@Service
public class UrlManegementService {

    private final UrlManagementRepository urlManagementRepository;

    @Autowired
    public UrlManegementService(UrlManagementRepository urlManagementRepository) {
        this.urlManagementRepository = urlManagementRepository;
    }

    @SneakyThrows
    public UrlEntity createShortUrl(String url) {
        Utils.urlValidator(url);
        String shortUrl = encodeUrl(url);
        UrlEntity urlEntity = UrlEntity.getUrlEntityBuilder(new BigInteger("1"), url, shortUrl, new Date());
        urlManagementRepository.save(urlEntity);

        return urlEntity;
    }

    public UrlEntity getLongUrlByShortUrl(String shortUrl) {
        return urlManagementRepository.findByShortUrl(shortUrl);
    }

    public void redirectToLonglUrlbyShortUrl(String shortUrl, HttpServletResponse httpServletResponse) {

        UrlEntity urlEntity = urlManagementRepository.findByShortUrl(shortUrl);

        httpServletResponse.setStatus(HttpStatus.MOVED_TEMPORARILY.value());
        httpServletResponse.setHeader(HttpHeaders.LOCATION, urlEntity.getLongUrl());
        httpServletResponse.setHeader(HttpHeaders.CONNECTION, "close");
    }

    @Transactional
    @SneakyThrows
    public UrlEntity deleteUrlConfigByShortUrl(String shortUrl) {
        UrlEntity urlEntity = urlManagementRepository.findByShortUrl(shortUrl);

        if(urlEntity == null) {
            throw UrlConfigActionException.create("DELETE");
        }
        urlManagementRepository.deleteByShortUrl(urlEntity.getShortUrl());
        return urlEntity;
    }

    private String encodeUrl(String url) throws NoSuchAlgorithmException {
        byte[] sha256EncodedHash = MessageDigest.getInstance(MercadoLibreTestConstants.SHA256)
                                        .digest(url.getBytes(StandardCharsets.UTF_8));
        String base64Encoded = Base64.getEncoder().encodeToString(sha256EncodedHash);

        StringBuilder sb = new StringBuilder();
        sb.append(base64Encoded.replace("/", ""));

        return sb.substring(0, 7).toUpperCase();
    }


}
