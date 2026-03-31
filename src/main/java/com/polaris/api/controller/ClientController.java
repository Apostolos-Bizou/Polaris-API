package com.polaris.api.controller;

import com.polaris.api.model.Client;
import com.polaris.api.repository.ClientRepository;
import com.polaris.api.repository.ClaimRepository;
import com.polaris.api.repository.MemberRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
@Tag(name = "Clients", description = "Client management and data")
@SecurityRequirement(name = "bearerAuth")
public class ClientController {

    private final ClientRepository clientRepository;
    private final ClaimRepository claimRepository;
    private final MemberRepository memberRepository;

    public ClientController(ClientRepository clientRepository,
                            ClaimRepository claimRepository,
                            MemberRepository memberRepository) {
        this.clientRepository = clientRepository;
        this.claimRepository = claimRepository;
        this.memberRepository = memberRepository;
    }

    @GetMapping
    @Operation(summary = "Get all clients", description = "Returns all 63 clients with parent/subsidiary hierarchy")
    public ResponseEntity<List<Map<String, Object>>> getAllClients() {
        List<Client> clients = clientRepository.findAllOrderByName();

        List<Map<String, Object>> result = clients.stream().map(client -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("clientId", client.getClientId());
            map.put("name", client.getName());
            map.put("clientType", client.getClientType());
            map.put("parentId", client.getParentId());
            map.put("country", client.getCountry());
            map.put("sector", client.getSector());
            map.put("status", client.getStatus());
            map.put("totalMembers", client.getTotalMembers());
            map.put("totalClaims", client.getTotalClaims());
            map.put("totalPremium", client.getTotalPremium());
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{clientId}")
    @Operation(summary = "Get client by ID", description = "Returns detailed client information")
    public ResponseEntity<?> getClient(@PathVariable String clientId) {
        Optional<Client> clientOpt = clientRepository.findByClientId(clientId);

        if (clientOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Client client = clientOpt.get();
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("clientId", client.getClientId());
        result.put("name", client.getName());
        result.put("clientType", client.getClientType());
        result.put("parentId", client.getParentId());
        result.put("country", client.getCountry());
        result.put("sector", client.getSector());
        result.put("status", client.getStatus());
        result.put("contactEmail", client.getContactEmail());
        result.put("contactPhone", client.getContactPhone());
        result.put("totalMembers", client.getTotalMembers());
        result.put("totalClaims", client.getTotalClaims());
        result.put("totalPremium", client.getTotalPremium());

        // If parent, include subsidiaries
        if ("parent".equals(client.getClientType())) {
            List<Client> subsidiaries = clientRepository.findByParentId(client.getClientId());
            result.put("subsidiaries", subsidiaries.stream().map(s -> {
                Map<String, Object> sub = new LinkedHashMap<>();
                sub.put("clientId", s.getClientId());
                sub.put("name", s.getName());
                sub.put("status", s.getStatus());
                sub.put("totalMembers", s.getTotalMembers());
                return sub;
            }).collect(Collectors.toList()));
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{clientId}/kpis")
    @Operation(summary = "Get client KPIs", description = "Returns KPI summary for a specific client")
    public ResponseEntity<?> getClientKPIs(@PathVariable String clientId) {
        Optional<Client> clientOpt = clientRepository.findByClientId(clientId);

        if (clientOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var claims = claimRepository.findByClientId(clientId);
        var members = memberRepository.findByClientId(clientId);

        long activeMembers = members.stream().filter(m -> "active".equals(m.getStatus())).count();
        double totalCost = claims.stream().mapToDouble(c -> c.getAmountUsd() != null ? c.getAmountUsd() : 0).sum();
        long inpatientCases = claims.stream().filter(c -> "inpatient".equals(c.getClaimType())).count();
        long outpatientCases = claims.stream().filter(c -> "outpatient".equals(c.getClaimType())).count();
        long principalMembers = members.stream().filter(m -> "principal".equals(m.getMemberType()) && "active".equals(m.getStatus())).count();
        long dependentMembers = members.stream().filter(m -> "dependent".equals(m.getMemberType()) && "active".equals(m.getStatus())).count();

        Map<String, Object> kpis = new LinkedHashMap<>();
        kpis.put("clientId", clientId);
        kpis.put("totalClaims", claims.size());
        kpis.put("totalCostUsd", totalCost);
        kpis.put("activeMembers", activeMembers);
        kpis.put("averageClaimCost", claims.size() > 0 ? totalCost / claims.size() : 0);
        kpis.put("inpatientCases", inpatientCases);
        kpis.put("outpatientCases", outpatientCases);
        kpis.put("principalMembers", principalMembers);
        kpis.put("dependentMembers", dependentMembers);

        // Categories breakdown
        List<Object[]> categories = claimRepository.getCategoriesBreakdown(clientId);
        List<Map<String, Object>> catList = categories.stream().map(cat -> {
            Map<String, Object> c = new LinkedHashMap<>();
            c.put("category", cat[0]);
            c.put("cases", cat[1]);
            c.put("cost_usd", cat[2]);
            return c;
        }).collect(Collectors.toList());
        kpis.put("categories", catList);

        return ResponseEntity.ok(kpis);
    }
}
