package com.mercadolibretest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private BigInteger idSecuence;
    private UUID urlKeyId;
    private String longUrl;
    private String shortUrl;
    private Date createdAt;
    private Date expiredAt;
    private Boolean isAvailable;

    public static UrlEntity getUrlEntityBuilder(BigInteger idSecuence, String url, String shortUrl, Date expiredAt) {
        return UrlEntity.builder()
                .urlKeyId(UUID.randomUUID())
                .longUrl(url)
                .shortUrl(shortUrl)
                .createdAt(new Date())
                .expiredAt(expiredAt)
                .isAvailable(Boolean.TRUE)
                .idSecuence(idSecuence)
                .build();

    }

}
