package com.mercadolibretest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class UrlDataRequest {

    @NotBlank(message="longUrl is mandatory")
    private String longUrl;
    @NotBlank(message="expiredAt is mandatory")
    private String expiredAt;
    @NotNull(message="isAvailable is mandatory")
    private Boolean isAvailable;

}
