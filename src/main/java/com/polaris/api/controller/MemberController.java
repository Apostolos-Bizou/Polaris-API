package com.polaris.api.controller;

import com.polaris.api.model.Member;
import com.polaris.api.repository.MemberRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/members")
@Tag(name = "Members", description = "Member management - principals and dependents")
@SecurityRequirement(name = "bearerAuth")
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;

    // ═══════════════════════════════════════════════════════════════════
    // GET /api/members - List all members with optional filters
    // ═══════════════════════════════════════════════════════════════════
    @GetMapping
    @Operation(summary = "Get all members", description = "Returns all members with optional filtering by client, type, plan, status")
    public ResponseEntity<?> getAllMembers(
            @RequestParam(required = false) String clientId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String plan,
            @RequestParam(required = false) String status,
            @RequestParam(required = false, defaultValue = "100") int limit) {

        List<Member> members;

        if (clientId != null && !clientId.isEmpty()) {
            members = memberRepository.findByClientId(clientId);
        } else {
            members = memberRepository.findAll();
        }

        // Apply filters
        if (type != null && !type.isEmpty()) {
            members = members.stream()
                    .filter(m -> type.equalsIgnoreCase(m.getMemberType()))
                    .collect(Collectors.toList());
        }
        if (plan != null && !plan.isEmpty()) {
            members = members.stream()
                    .filter(m -> plan.equalsIgnoreCase(m.getPlan()))
                    .collect(Collectors.toList());
        }
        if (status != null && !status.isEmpty()) {
            members = members.stream()
                    .filter(m -> status.equalsIgnoreCase(m.getStatus()))
                    .collect(Collectors.toList());
        }

        int totalCount = members.size();

        // Limit results
        if (members.size() > limit) {
            members = members.subList(0, limit);
        }

        List<Map<String, Object>> data = members.stream()
                .map(this::memberToMap)
                .collect(Collectors.toList());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("count", data.size());
        response.put("total", totalCount);
        response.put("data", data);

        return ResponseEntity.ok(response);
    }

    // ═══════════════════════════════════════════════════════════════════
    // GET /api/members/{memberId} - Get single member
    // ═══════════════════════════════════════════════════════════════════
    @GetMapping("/{memberId}")
    @Operation(summary = "Get member by ID", description = "Returns member details")
    public ResponseEntity<?> getMemberById(@PathVariable String memberId) {

        Optional<Member> optMember = memberRepository.findByMemberId(memberId);

        if (optMember.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of(
                    "success", false,
                    "error", "Member not found: " + memberId
            ));
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("data", memberToMap(optMember.get()));

        return ResponseEntity.ok(response);
    }

    // ═══════════════════════════════════════════════════════════════════
    // GET /api/members/stats - Member statistics
    // ═══════════════════════════════════════════════════════════════════
    @GetMapping("/stats")
    @Operation(summary = "Get member statistics", description = "Returns counts by type, plan, and status")
    public ResponseEntity<?> getMemberStats() {

        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("total", memberRepository.count());
        stats.put("active", memberRepository.countActiveMembers());
        stats.put("principals", memberRepository.countByMemberType("Principal"));
        stats.put("dependents", memberRepository.countByMemberType("Dependent"));
        stats.put("cancelled", memberRepository.countByStatus("Cancelled"));

        // Plan distribution
        Map<String, Long> plans = new LinkedHashMap<>();
        plans.put("gold", memberRepository.countByPlan("Gold"));
        plans.put("platinum", memberRepository.countByPlan("Platinum"));
        plans.put("diamond", memberRepository.countByPlan("Diamond"));
        plans.put("silver", memberRepository.countByPlan("Silver"));
        stats.put("plans", plans);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("data", stats);

        return ResponseEntity.ok(response);
    }

    // ═══════════════════════════════════════════════════════════════════
    // GET /api/members/client/{clientId} - Members for specific client
    // ═══════════════════════════════════════════════════════════════════
    @GetMapping("/client/{clientId}")
    @Operation(summary = "Get members by client", description = "Returns all members for a specific client")
    public ResponseEntity<?> getMembersByClient(@PathVariable String clientId) {

        List<Member> members = memberRepository.findByClientId(clientId);

        List<Map<String, Object>> data = members.stream()
                .map(this::memberToMap)
                .collect(Collectors.toList());

        // Summary
        long principals = members.stream().filter(m -> "Principal".equals(m.getMemberType())).count();
        long dependents = members.stream().filter(m -> "Dependent".equals(m.getMemberType())).count();
        long active = members.stream().filter(m -> "Active".equals(m.getStatus())).count();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("count", data.size());
        response.put("principals", principals);
        response.put("dependents", dependents);
        response.put("active", active);
        response.put("client_id", clientId);
        response.put("data", data);

        return ResponseEntity.ok(response);
    }

    private Map<String, Object> memberToMap(Member m) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("member_id", m.getMemberId());
        map.put("client_id", m.getClientId());
        map.put("member_type", m.getMemberType());
        map.put("plan", m.getPlan());
        map.put("status", m.getStatus());
        map.put("enrollment_date", m.getEnrollmentDate() != null ? m.getEnrollmentDate().toString() : null);
        return map;
    }
}
