package com.polaris.api.controller;

import com.polaris.api.dto.CreateOfferRequest;
import com.polaris.api.dto.UpdateOfferStatusRequest;
import com.polaris.api.model.Offer;
import com.polaris.api.model.OfferItem;
import com.polaris.api.repository.OfferRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/offers")
@Tag(name = "Offers", description = "Offer management - full lifecycle from draft to contract")
@SecurityRequirement(name = "bearerAuth")
public class OfferController {

    @Autowired
    private OfferRepository offerRepository;

    // ═══════════════════════════════════════════════════════════════════
    // GET /api/offers - List all offers with optional filters
    // ═══════════════════════════════════════════════════════════════════
    @GetMapping
    @Operation(summary = "Get all offers", description = "Returns all offers with optional filtering by status, type, and client name")
    public ResponseEntity<?> getAllOffers(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String client) {

        List<Offer> offers;

        if (client != null && !client.isEmpty()) {
            offers = offerRepository.searchByClientName(client);
        } else if (status != null && type != null) {
            offers = offerRepository.findByStatusAndType(status, type);
        } else if (status != null) {
            offers = offerRepository.findByStatusOrderByCreatedDateDesc(status);
        } else if (type != null) {
            offers = offerRepository.findByOfferTypeOrderByCreatedDateDesc(type);
        } else {
            offers = offerRepository.findAllOrderByCreatedDateDesc();
        }

        // Build response matching Google Script format
        List<Map<String, Object>> data = offers.stream()
                .map(this::offerToMap)
                .collect(Collectors.toList());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("count", data.size());
        response.put("data", data);

        return ResponseEntity.ok(response);
    }

    // ═══════════════════════════════════════════════════════════════════
    // GET /api/offers/{offerId} - Get single offer with items
    // ═══════════════════════════════════════════════════════════════════
    @GetMapping("/{offerId}")
    @Operation(summary = "Get offer by ID", description = "Returns offer details with all items/plan options")
    public ResponseEntity<?> getOfferById(@PathVariable String offerId) {

        Optional<Offer> optOffer = offerRepository.findByOfferId(offerId);

        if (optOffer.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "error", "Offer not found: " + offerId
            ));
        }

        Offer offer = optOffer.get();
        Map<String, Object> data = offerToMap(offer);

        // Add items
        List<Map<String, Object>> itemsList = offer.getItems().stream()
                .map(this::itemToMap)
                .collect(Collectors.toList());
        data.put("items", itemsList);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("data", data);

        return ResponseEntity.ok(response);
    }

    // ═══════════════════════════════════════════════════════════════════
    // POST /api/offers - Create new offer
    // ═══════════════════════════════════════════════════════════════════
    @PostMapping
    @Operation(summary = "Create new offer", description = "Creates a new standard or comparison offer with items")
    public ResponseEntity<?> createOffer(@RequestBody CreateOfferRequest request) {

        // Generate offer ID
        String prefix = "comparison".equals(request.getOfferType()) ? "CQ-" : "OFF-";
        String offerId = prefix + System.currentTimeMillis();

        Offer offer = new Offer(offerId, request.getClientId(), request.getClientName());
        offer.setOfferType(request.getOfferType() != null ? request.getOfferType() : "standard");
        offer.setStatus("draft");
        offer.setValidityDays(request.getValidityDays() != null ? request.getValidityDays() : 30);
        offer.setExpiryDate(LocalDate.now().plusDays(offer.getValidityDays()));
        offer.setIncludesDental(request.getIncludesDental());
        offer.setCoverageArea(request.getCoverageArea());
        offer.setNetwork(request.getNetwork());
        offer.setProgramName(request.getProgramName());
        offer.setNotes(request.getNotes());
        offer.setCreatedBy(request.getCreatedBy() != null ? request.getCreatedBy() : "Admin");

        // Add items
        if (request.getItems() != null) {
            for (CreateOfferRequest.OfferItemRequest itemReq : request.getItems()) {
                OfferItem item = new OfferItem();
                item.setPlanId(itemReq.getPlanId());
                item.setPlanName(itemReq.getPlanName());
                item.setPrincipals(itemReq.getPrincipals() != null ? itemReq.getPrincipals() : 0);
                item.setDependents(itemReq.getDependents() != null ? itemReq.getDependents() : 0);
                item.setRegFee(itemReq.getRegFee() != null ? itemReq.getRegFee() : 24.0);
                item.setFundDeposit(itemReq.getFundDeposit() != null ? itemReq.getFundDeposit() : 0);
                item.setHasDental(itemReq.getHasDental() != null ? itemReq.getHasDental() : false);
                item.recalculate();
                offer.addItem(item);
            }
        }

        // Recalculate totals
        offer.recalculateTotals();

        // Save
        Offer saved = offerRepository.save(offer);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("offer_id", saved.getOfferId());
        response.put("message", "Offer created successfully");
        response.put("data", offerToMap(saved));

        return ResponseEntity.ok(response);
    }

    // ═══════════════════════════════════════════════════════════════════
    // PUT /api/offers/{offerId}/status - Update offer status
    // ═══════════════════════════════════════════════════════════════════
    @PutMapping("/{offerId}/status")
    @Operation(summary = "Update offer status", description = "Updates offer status. Valid: draft, sent, accepted, pending_signature, signed, contract, rejected, expired")
    public ResponseEntity<?> updateOfferStatus(
            @PathVariable String offerId,
            @RequestBody UpdateOfferStatusRequest request) {

        Optional<Offer> optOffer = offerRepository.findByOfferId(offerId);

        if (optOffer.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "error", "Offer not found: " + offerId
            ));
        }

        Offer offer = optOffer.get();
        String newStatus = request.getStatus();

        // Validate status
        List<String> validStatuses = Arrays.asList(
                "draft", "sent", "accepted", "pending_signature", "signed", "contract", "rejected", "expired"
        );

        if (!validStatuses.contains(newStatus)) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", "Invalid status: " + newStatus + ". Valid: " + String.join(", ", validStatuses)
            ));
        }

        String oldStatus = offer.getStatus();
        offer.setStatus(newStatus);

        // Add notes if provided
        if (request.getNotes() != null && !request.getNotes().isEmpty()) {
            String existingNotes = offer.getNotes() != null ? offer.getNotes() : "";
            String timestamp = LocalDateTime.now().toString().substring(0, 16);
            offer.setNotes(existingNotes + "\n[" + timestamp + "] " + oldStatus + " -> " + newStatus + ": " + request.getNotes());
        }

        offerRepository.save(offer);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("offer_id", offerId);
        response.put("old_status", oldStatus);
        response.put("new_status", newStatus);
        response.put("message", "Status updated: " + oldStatus + " -> " + newStatus);

        return ResponseEntity.ok(response);
    }

    // ═══════════════════════════════════════════════════════════════════
    // GET /api/offers/stats - Offer statistics for analytics dashboard
    // ═══════════════════════════════════════════════════════════════════
    @GetMapping("/stats")
    @Operation(summary = "Get offer statistics", description = "Returns counts by status, revenue pipeline, and pending value")
    public ResponseEntity<?> getOfferStats() {

        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("total", offerRepository.count());
        stats.put("draft", offerRepository.countByStatus("draft"));
        stats.put("sent", offerRepository.countByStatus("sent"));
        stats.put("accepted", offerRepository.countByStatus("accepted"));
        stats.put("pending_signature", offerRepository.countByStatus("pending_signature"));
        stats.put("signed", offerRepository.countByStatus("signed"));
        stats.put("contract", offerRepository.countByStatus("contract"));
        stats.put("rejected", offerRepository.countByStatus("rejected"));
        stats.put("expired", offerRepository.countByStatus("expired"));
        stats.put("revenue_pipeline", offerRepository.totalRevenuePipeline());
        stats.put("pending_value", offerRepository.totalPendingValue());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("data", stats);

        return ResponseEntity.ok(response);
    }

    // ═══════════════════════════════════════════════════════════════════
    // GET /api/offers/client/{clientId} - Offers for specific client
    // ═══════════════════════════════════════════════════════════════════
    @GetMapping("/client/{clientId}")
    @Operation(summary = "Get offers by client", description = "Returns all offers for a specific client")
    public ResponseEntity<?> getOffersByClient(@PathVariable String clientId) {

        List<Offer> offers = offerRepository.findByClientIdOrderByCreatedDateDesc(clientId);

        List<Map<String, Object>> data = offers.stream()
                .map(this::offerToMap)
                .collect(Collectors.toList());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("count", data.size());
        response.put("client_id", clientId);
        response.put("data", data);

        return ResponseEntity.ok(response);
    }

    // ═══════════════════════════════════════════════════════════════════
    // DELETE /api/offers/{offerId} - Delete offer (only drafts)
    // ═══════════════════════════════════════════════════════════════════
    @DeleteMapping("/{offerId}")
    @Operation(summary = "Delete offer", description = "Deletes an offer (only draft offers can be deleted)")
    public ResponseEntity<?> deleteOffer(@PathVariable String offerId) {

        Optional<Offer> optOffer = offerRepository.findByOfferId(offerId);

        if (optOffer.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "error", "Offer not found: " + offerId
            ));
        }

        Offer offer = optOffer.get();

        if (!"draft".equals(offer.getStatus())) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", "Only draft offers can be deleted. Current status: " + offer.getStatus()
            ));
        }

        offerRepository.delete(offer);

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Offer " + offerId + " deleted successfully"
        ));
    }

    // ═══════════════════════════════════════════════════════════════════
    // HELPER: Convert Offer entity to Map (matching Google Script format)
    // ═══════════════════════════════════════════════════════════════════
    private Map<String, Object> offerToMap(Offer o) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("offer_id", o.getOfferId());
        map.put("client_id", o.getClientId());
        map.put("client_name", o.getClientName());
        map.put("offer_type", o.getOfferType());
        map.put("status", o.getStatus());
        map.put("offer_date", o.getOfferDate() != null ? o.getOfferDate().toString() : null);
        map.put("created_date", o.getCreatedDate() != null ? o.getCreatedDate().toString() : null);
        map.put("validity_days", o.getValidityDays());
        map.put("expiry_date", o.getExpiryDate() != null ? o.getExpiryDate().toString() : null);
        map.put("total_principals", o.getTotalPrincipals());
        map.put("total_dependents", o.getTotalDependents());
        map.put("total_members", o.getTotalMembers());
        map.put("subtotal_reg_fees", o.getSubtotalRegFees());
        map.put("subtotal_fund_deposit", o.getSubtotalFundDeposit());
        map.put("subtotal_dental", o.getSubtotalDental());
        map.put("grand_total_usd", o.getGrandTotalUsd());
        map.put("includes_dental", o.getIncludesDental());
        map.put("coverage_area", o.getCoverageArea());
        map.put("network", o.getNetwork());
        map.put("program_name", o.getProgramName());
        map.put("created_by", o.getCreatedBy());
        map.put("notes", o.getNotes());

        // Include items in list response too (for compatibility with Google Script getOffers)
        if (o.getItems() != null && !o.getItems().isEmpty()) {
            List<Map<String, Object>> itemsList = o.getItems().stream()
                    .map(this::itemToMap)
                    .collect(Collectors.toList());
            map.put("items", itemsList);
        }

        return map;
    }

    private Map<String, Object> itemToMap(OfferItem i) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("plan_id", i.getPlanId());
        map.put("plan_name", i.getPlanName());
        map.put("principals", i.getPrincipals());
        map.put("dependents", i.getDependents());
        map.put("num_principals", i.getPrincipals()); // alias for compatibility
        map.put("num_dependents", i.getDependents()); // alias for compatibility
        map.put("reg_fee", i.getRegFee());
        map.put("fund_deposit", i.getFundDeposit());
        map.put("subtotal_reg", i.getSubtotalReg());
        map.put("subtotal_fund", i.getSubtotalFund());
        map.put("has_dental", i.getHasDental());
        map.put("dental_fee", i.getDentalFee());
        map.put("dental_total", i.getDentalTotal());
        map.put("total_cost", i.getTotalCost());
        map.put("per_member", i.getPerMember());
        return map;
    }
}
