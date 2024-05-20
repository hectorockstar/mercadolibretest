package com.mercadolibretest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UrlUpdateDataRequest {

    @NotBlank(message="expiredAt is mandatory")
    private String expiredAt;
    @NotNull(message="isAvailable is mandatory")
    private Boolean isAvailable;
}
