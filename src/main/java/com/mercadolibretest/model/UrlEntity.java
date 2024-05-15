package com.mercadolibretest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mercadolibretest.utils.Utils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigInteger;
import java.util.Date;
import java.util.UUID;

@Data
@SuperBuilder
@Entity
@Table(name = "urls")
@AllArgsConstructor
@NoArgsConstructor
public class UrlEntity {

    @Id
    @JsonIgnore()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    private String urlKeyId;
    private String longUrl;
    private String shortUrl;
    private Date createdAt;
    private Date expiredAt;
    private Boolean isAvailable;
    private BigInteger visitsNumber;
    private Date lastVisitedAt;

    public static UrlEntity getUrlEntityBuilder(UrlDataRequest urlDataRequest, String shortUrl) {
        Date expiredAt = Utils.stringDateToDateFormatter(urlDataRequest.getExpiredAt());

        return UrlEntity.builder()
                .urlKeyId(UUID.randomUUID().toString())
                .longUrl(urlDataRequest.getLongUrl())
                .shortUrl(shortUrl)
                .createdAt(new Date())
                .expiredAt(expiredAt)
                .isAvailable(urlDataRequest.getIsAvailable())
                .visitsNumber(BigInteger.ZERO)
                .build();

    }

}
