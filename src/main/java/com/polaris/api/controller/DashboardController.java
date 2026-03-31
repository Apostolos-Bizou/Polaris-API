package com.polaris.api.controller;

import com.polaris.api.dto.DashboardKPIsResponse;
import com.polaris.api.repository.ClaimRepository;
import com.polaris.api.repository.ClientRepository;
import com.polaris.api.repository.MemberRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "Dashboard KPIs and analytics")
@SecurityRequirement(name = "bearerAuth")
public class DashboardController {

    private final ClientRepository clientRepository;
    private final ClaimRepository claimRepository;
    private final MemberRepository memberRepository;

    public DashboardController(ClientRepository clientRepository,
                                ClaimRepository claimRepository,
                                MemberRepository memberRepository) {
        this.clientRepository = clientRepository;
        this.claimRepository = claimRepository;
        this.memberRepository = memberRepository;
    }

    @GetMapping("/kpis")
    @Operation(summary = "Get Dashboard KPIs", description = "Returns all KPI data for the main admin dashboard")
    public ResponseEntity<DashboardKPIsResponse> getDashboardKPIs() {
        DashboardKPIsResponse kpis = new DashboardKPIsResponse();

        // Core KPIs
        kpis.setTotalClients(clientRepository.count());
        kpis.setActiveMembers(memberRepository.countActiveMembers());
        kpis.setTotalClaims(claimRepository.count());

        double totalCost = claimRepository.findAll().stream()
                .mapToDouble(c -> c.getAmountUsd() != null ? c.getAmountUsd() : 0)
                .sum();
        kpis.setTotalCostUsd(totalCost);
        kpis.setAverageClaimCost(kpis.getTotalClaims() > 0 ? totalCost / kpis.getTotalClaims() : 0);

        // Inpatient / Outpatient / Ex Gratia
        kpis.setInpatientCases(claimRepository.countByClaimType("inpatient"));
        kpis.setInpatientCost(claimRepository.sumCostByClaimType("inpatient"));
        kpis.setOutpatientCases(claimRepository.countByClaimType("outpatient"));
        kpis.setOutpatientCost(claimRepository.sumCostByClaimType("outpatient"));
        kpis.setExGratiaCases(claimRepository.countByClaimType("ex_gratia"));
        kpis.setExGratiaCost(claimRepository.sumCostByClaimType("ex_gratia"));

        // Principal / Dependent
        kpis.setPrincipalCount(memberRepository.countActiveByMemberType("principal"));
        kpis.setDependentCount(memberRepository.countActiveByMemberType("dependent"));
        kpis.setPrincipalClaims(claimRepository.countByMemberType("principal"));
        kpis.setDependentClaims(claimRepository.countByMemberType("dependent"));

        // Member Movement (simplified)
        kpis.setNewEnrollments(memberRepository.countActiveMembers());
        kpis.setCancellations(memberRepository.findByStatus("cancelled").size());
        kpis.setNetChange(kpis.getNewEnrollments() - kpis.getCancellations());

        return ResponseEntity.ok(kpis);
    }
}
