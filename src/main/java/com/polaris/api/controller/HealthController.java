package com.polaris.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Health", description = "System health checks")
public class HealthController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Returns system health status")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new LinkedHashMap<>();
        health.put("status", "healthy");
        health.put("timestamp", LocalDateTime.now().toString());
        health.put("service", "polaris-api");
        health.put("version", "1.0.0");

        // Check database
        try (Connection conn = dataSource.getConnection()) {
            health.put("database", conn.isValid(2) ? "connected" : "error");
        } catch (Exception e) {
            health.put("database", "error: " + e.getMessage());
        }

        return ResponseEntity.ok(health);
    }
}
