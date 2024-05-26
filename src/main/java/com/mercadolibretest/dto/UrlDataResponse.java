package com.mercadolibretest.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mercadolibretest.model.UrlEntity;
import com.mercadolibretest.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UrlDataResponse implements Serializable {

    @JsonIgnore()
    private String id;
    private String urlKeyId;
    private String longUrl;
    private String shortUrl;
    private String createdAt;
    private String expiredAt;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lastVisitedAt;
    private BigInteger visitsNumber;
    private Boolean isAvailable;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String updatedAt;

    public static UrlDataResponse getUrlDataResponseBuilder(UrlEntity urlEntity) {
        Date lastVisitedAt = urlEntity.getLastVisitedAt();
        Date updatedAt = urlEntity.getUpdatedAt();

        return UrlDataResponse.builder()
                .id(urlEntity.getId() != null ? urlEntity.getId().toString() : null)
                .urlKeyId(urlEntity.getUrlKeyId())
                .longUrl(urlEntity.getLongUrl())
                .shortUrl(urlEntity.getShortUrl())
                .createdAt(Utils.dateToStringDateFormatter(urlEntity.getCreatedAt()))
                .expiredAt(Utils.dateToStringDateFormatter(urlEntity.getExpiredAt()))
                .lastVisitedAt(lastVisitedAt != null ? Utils.dateToStringDateFormatter(urlEntity.getLastVisitedAt()) : null)
                .isAvailable(urlEntity.getIsAvailable())
                .visitsNumber(urlEntity.getVisitsNumber())
                .updatedAt(updatedAt != null ? Utils.dateToStringDateFormatter(urlEntity.getUpdatedAt()) : null)
                .build();

    }

}
