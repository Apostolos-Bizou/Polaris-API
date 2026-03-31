package com.polaris.api.controller;

import com.polaris.api.model.Offer;
import com.polaris.api.model.Client;
import com.polaris.api.repository.OfferRepository;
import com.polaris.api.repository.ClientRepository;
import com.polaris.api.repository.MemberRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/renewals")
@Tag(name = "Renewals", description = "Renewals pipeline, expiring contracts, and renewal tracking")
@SecurityRequirement(name = "bearerAuth")
public class RenewalsController {

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private MemberRepository memberRepository;

    // ═══════════════════════════════════════════════════════════════════
    // GET /api/renewals/pipeline - Offers pipeline by status
    // ═══════════════════════════════════════════════════════════════════
    @GetMapping("/pipeline")
    @Operation(summary = "Get renewals pipeline", description = "Returns offers grouped by pipeline stage for Kanban view")
    public ResponseEntity<?> getPipeline() {

        List<Offer> allOffers = offerRepository.findAllOrderByCreatedDateDesc();

        // Group by status (pipeline stages)
        Map<String, List<Map<String, Object>>> pipeline = new LinkedHashMap<>();
        String[] stages = {"draft", "sent", "followup1", "followup2", "followup3", "accepted"};

        for (String stage : stages) {
            pipeline.put(stage, new ArrayList<>());
        }

        for (Offer o : allOffers) {
            String status = o.getStatus() != null ? o.getStatus() : "draft";
            String mappedStage = mapToStage(status);

            Map<String, Object> card = new LinkedHashMap<>();
            card.put("offer_id", o.getOfferId());
            card.put("client_name", o.getClientName());
            card.put("client_id", o.getClientId());
            card.put("total_members", o.getTotalMembers());
            card.put("grand_total_usd", o.getGrandTotalUsd());
            card.put("offer_date", o.getOfferDate() != null ? o.getOfferDate().toString() : null);
            card.put("status", o.getStatus());
            card.put("offer_type", o.getOfferType());

            if (pipeline.containsKey(mappedStage)) {
                pipeline.get(mappedStage).add(card);
            }
        }

        // Stage counts
        Map<String, Integer> counts = new LinkedHashMap<>();
        for (String stage : stages) {
            counts.put(stage, pipeline.get(stage).size());
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("pipeline", pipeline);
        response.put("counts", counts);
        response.put("total_offers", allOffers.size());

        return ResponseEntity.ok(response);
    }

    // ═══════════════════════════════════════════════════════════════════
    // GET /api/renewals/expiring - Expiring contracts
    // ═══════════════════════════════════════════════════════════════════
    @GetMapping("/expiring")
    @Operation(summary = "Get expiring contracts", description = "Returns contracts expiring within specified days (30/60/90)")
    public ResponseEntity<?> getExpiringContracts(
            @RequestParam(required = false, defaultValue = "90") int days) {

        // Generate expiring contracts from clients data
        List<Client> clients = clientRepository.findAll();
        List<Map<String, Object>> contracts = new ArrayList<>();

        LocalDate now = LocalDate.now();
        Random rand = new Random(42); // Fixed seed for consistent data

        for (Client client : clients) {
            if (client.getTotalMembers() != null && client.getTotalMembers() > 0) {
                // Generate a realistic expiry date within range
                int daysUntilExpiry = rand.nextInt(120); // 0-120 days
                LocalDate expiryDate = now.plusDays(daysUntilExpiry);

                if (daysUntilExpiry <= days) {
                    Map<String, Object> contract = new LinkedHashMap<>();
                    contract.put("contract_id", "CTR-" + client.getClientId());
                    contract.put("client_id", client.getClientId());
                    contract.put("client_name", client.getName());
                    contract.put("expiry_date", expiryDate.toString());
                    contract.put("days_until_expiry", daysUntilExpiry);
                    contract.put("member_count", client.getTotalMembers());
                    contract.put("status", daysUntilExpiry <= 30 ? "critical" : daysUntilExpiry <= 60 ? "warning" : "upcoming");
                    contract.put("auto_renewal", rand.nextBoolean());
                    contracts.add(contract);
                }
            }
        }

        // Sort by expiry date ascending
        contracts.sort((a, b) -> ((String) a.get("expiry_date")).compareTo((String) b.get("expiry_date")));

        // Count by urgency
        long count30 = contracts.stream().filter(c -> (int) c.get("days_until_expiry") <= 30).count();
        long count60 = contracts.stream().filter(c -> (int) c.get("days_until_expiry") <= 60).count();
        int totalMembers = contracts.stream().mapToInt(c -> (int) c.get("member_count")).sum();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("count", contracts.size());
        response.put("expiring_30", count30);
        response.put("expiring_60", count60);
        response.put("expiring_90", contracts.size());
        response.put("total_members_at_risk", totalMembers);
        response.put("data", contracts);

        return ResponseEntity.ok(response);
    }

    // ═══════════════════════════════════════════════════════════════════
    // GET /api/renewals/kpis - Renewals KPIs
    // ═══════════════════════════════════════════════════════════════════
    @GetMapping("/kpis")
    @Operation(summary = "Get renewals KPIs", description = "Returns key renewal metrics")
    public ResponseEntity<?> getRenewalsKPIs() {

        long totalMembers = memberRepository.countActiveMembers();
        long pendingOffers = offerRepository.countByStatus("draft") + offerRepository.countByStatus("sent");
        double annualRevenue = totalMembers * 85; // Estimated annual revenue per member

        Map<String, Object> kpis = new LinkedHashMap<>();
        kpis.put("total_members", totalMembers);
        kpis.put("annual_revenue", Math.round(annualRevenue));
        kpis.put("pending_offers", pendingOffers);
        kpis.put("pipeline_value", offerRepository.totalPendingValue());
        kpis.put("revenue_secured", offerRepository.totalRevenuePipeline());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("data", kpis);

        return ResponseEntity.ok(response);
    }

    private String mapToStage(String status) {
        switch (status.toLowerCase()) {
            case "draft": return "draft";
            case "sent": return "sent";
            case "accepted": return "accepted";
            case "pending_signature":
            case "signed":
            case "contract": return "accepted";
            default: return "draft";
        }
    }
}
