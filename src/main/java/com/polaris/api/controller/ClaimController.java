package com.polaris.api.controller;

import com.polaris.api.model.Claim;
import com.polaris.api.repository.ClaimRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/claims")
@Tag(name = "Claims", description = "Claims management and categories breakdown")
@SecurityRequirement(name = "bearerAuth")
public class ClaimController {

    @Autowired
    private ClaimRepository claimRepository;

    // ═══════════════════════════════════════════════════════════════════
    // GET /api/claims/categories - Categories breakdown
    // Matches Google Script getCategoriesBreakdown response format
    // ═══════════════════════════════════════════════════════════════════
    @GetMapping("/categories")
    @Operation(summary = "Get claims categories breakdown", description = "Returns claims grouped by category with cost_usd and cases count")
    public ResponseEntity<?> getCategoriesBreakdown(
            @RequestParam(required = false) String clientId,
            @RequestParam(required = false, defaultValue = "2025") String year) {

        List<Claim> claims;

        if (clientId != null && !clientId.isEmpty()) {
            claims = claimRepository.findByClientId(clientId);
        } else {
            claims = claimRepository.findAll();
        }

        // Group by category
        Map<String, List<Claim>> grouped = claims.stream()
                .collect(Collectors.groupingBy(c -> c.getCategory() != null ? c.getCategory() : "Other"));

        List<Map<String, Object>> categories = new ArrayList<>();

        for (Map.Entry<String, List<Claim>> entry : grouped.entrySet()) {
            Map<String, Object> cat = new LinkedHashMap<>();
            cat.put("category", entry.getKey());
            cat.put("cases", entry.getValue().size()); // "cases" not "count" - matches Google Script
            cat.put("cost_usd", entry.getValue().stream()
                    .mapToDouble(c -> c.getAmountUsd() != null ? c.getAmountUsd() : 0)
                    .sum()); // "cost_usd" not "cost" - matches Google Script
            cat.put("avg_cost", entry.getValue().stream()
                    .mapToDouble(c -> c.getAmountUsd() != null ? c.getAmountUsd() : 0)
                    .average().orElse(0));
            categories.add(cat);
        }

        // Sort by cost descending
        categories.sort((a, b) -> Double.compare((Double) b.get("cost_usd"), (Double) a.get("cost_usd")));

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("count", categories.size());
        response.put("total_claims", claims.size());
        response.put("total_cost_usd", claims.stream()
                .mapToDouble(c -> c.getAmountUsd() != null ? c.getAmountUsd() : 0).sum());
        response.put("data", categories);

        return ResponseEntity.ok(response);
    }

    // ═══════════════════════════════════════════════════════════════════
    // GET /api/claims - List claims with optional filters
    // ═══════════════════════════════════════════════════════════════════
    @GetMapping
    @Operation(summary = "Get all claims", description = "Returns claims with optional filtering")
    public ResponseEntity<?> getAllClaims(
            @RequestParam(required = false) String clientId,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String claimType,
            @RequestParam(required = false, defaultValue = "100") int limit) {

        List<Claim> claims;

        if (clientId != null && !clientId.isEmpty()) {
            claims = claimRepository.findByClientId(clientId);
        } else {
            claims = claimRepository.findAll();
        }

        // Apply filters
        if (category != null && !category.isEmpty()) {
            claims = claims.stream()
                    .filter(c -> category.equalsIgnoreCase(c.getCategory()))
                    .collect(Collectors.toList());
        }
        if (claimType != null && !claimType.isEmpty()) {
            claims = claims.stream()
                    .filter(c -> claimType.equalsIgnoreCase(c.getClaimType()))
                    .collect(Collectors.toList());
        }

        int totalCount = claims.size();

        if (claims.size() > limit) {
            claims = claims.subList(0, limit);
        }

        List<Map<String, Object>> data = claims.stream()
                .map(this::claimToMap)
                .collect(Collectors.toList());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("count", data.size());
        response.put("total", totalCount);
        response.put("data", data);

        return ResponseEntity.ok(response);
    }

    // ═══════════════════════════════════════════════════════════════════
    // GET /api/claims/stats - Claims statistics
    // ═══════════════════════════════════════════════════════════════════
    @GetMapping("/stats")
    @Operation(summary = "Get claims statistics", description = "Returns total claims, cost, and breakdown by type")
    public ResponseEntity<?> getClaimsStats() {

        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("total_claims", claimRepository.count());
        stats.put("total_cost_usd", Math.round(claimRepository.totalClaimsCost()));
        stats.put("avg_claim_cost", Math.round(claimRepository.averageClaimCost()));

        // By type
        Map<String, Object> byType = new LinkedHashMap<>();

        Map<String, Object> inpatient = new LinkedHashMap<>();
        inpatient.put("count", claimRepository.countByClaimType("Inpatient"));
        inpatient.put("cost", Math.round(claimRepository.sumCostByClaimType("Inpatient")));
        byType.put("inpatient", inpatient);

        Map<String, Object> outpatient = new LinkedHashMap<>();
        outpatient.put("count", claimRepository.countByClaimType("Outpatient"));
        outpatient.put("cost", Math.round(claimRepository.sumCostByClaimType("Outpatient")));
        byType.put("outpatient", outpatient);

        Map<String, Object> exgratia = new LinkedHashMap<>();
        exgratia.put("count", claimRepository.countByClaimType("Ex-Gratia"));
        exgratia.put("cost", Math.round(claimRepository.sumCostByClaimType("Ex-Gratia")));
        byType.put("exgratia", exgratia);

        stats.put("by_type", byType);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("data", stats);

        return ResponseEntity.ok(response);
    }

    private Map<String, Object> claimToMap(Claim c) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("claim_id", c.getClaimId());
        map.put("client_id", c.getClientId());
        map.put("category", c.getCategory());
        map.put("claim_type", c.getClaimType());
        map.put("amount_usd", c.getAmountUsd());
        map.put("hospital", c.getHospital());
        return map;
    }
}
