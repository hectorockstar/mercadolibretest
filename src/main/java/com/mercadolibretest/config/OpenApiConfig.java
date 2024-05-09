package com.mercadolibretest.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition (
        info = @Info(
                title = "Mercado Libre Tests",
                version = "1.0.0",
                description = "Este es un proyecto de prueba para Mercado Libre"
        )
)
public class OpenApiConfig {


}
