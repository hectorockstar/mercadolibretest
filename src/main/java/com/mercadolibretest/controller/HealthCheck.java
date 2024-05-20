package com.mercadolibretest.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheck {

    @Operation(summary = "Endpoint verificador si la appa esta en ejecucion", description = "Con este endpoint podras verificar si la aplicacion esta en ejecucion actualmente")
    @GetMapping("/healthCheck")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Mercado Libre App is UP");
    }
}
