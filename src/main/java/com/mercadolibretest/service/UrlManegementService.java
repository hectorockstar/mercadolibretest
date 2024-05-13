package com.mercadolibretest.service;

import com.mercadolibretest.model.UrlEntity;
import com.mercadolibretest.repository.UrlManagementRepository;
import com.mercadolibretest.utils.Utils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        String shorUrl = encodeUrl(url);

        UrlEntity urlEntity = UrlEntity.getUrlEntityBuilder(new BigInteger("1"), url, shorUrl, new Date());

        urlManagementRepository.save(urlEntity);
        System.out.println(shorUrl);

        return urlEntity;
    }

    public String encodeUrl(String url) throws NoSuchAlgorithmException {
        MessageDigest sha256Hash = MessageDigest.getInstance("SHA-256");
        byte[] sha256EncodedHash = sha256Hash.digest(url.getBytes(StandardCharsets.UTF_8));
        String encoded = Base64.getEncoder().encodeToString(sha256EncodedHash);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(encoded.replace("/", ""));

        return stringBuilder.substring(0, 7).toUpperCase();
    }


}
