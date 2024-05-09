package com.mercadolibretest.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheck {

    @GetMapping("/healthCheck")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Mercado Libre App is UP");
    }
}
