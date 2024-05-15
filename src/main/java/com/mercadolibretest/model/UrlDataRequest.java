package com.mercadolibretest.model;

import lombok.Data;

import java.util.Date;

@Data
public class UrlDataRequest {

    private String longUrl;
    private String expiredAt;
    private Boolean isAvailable;

}
