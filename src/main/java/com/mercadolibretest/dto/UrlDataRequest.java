package com.mercadolibretest.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UrlDataRequest {

    private String longUrl;
    private String expiredAt;
    private Boolean isAvailable;

}
