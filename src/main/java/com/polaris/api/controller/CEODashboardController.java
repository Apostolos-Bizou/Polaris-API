package com.polaris.api.controller;

import com.polaris.api.repository.ClientRepository;
import com.polaris.api.repository.ClaimRepository;
import com.polaris.api.repository.MemberRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/ceo")
@Tag(name = "CEO Dashboard", description = "CEO financial dashboard with revenue, expenses, tax and ratios")
@SecurityRequirement(name = "bearerAuth")
public class CEODashboardController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClaimRepository claimRepository;

    @Autowired
    private MemberRepository memberRepository;

    // ═══════════════════════════════════════════════════════════════════
    // GET /api/ceo/dashboard - Full CEO financial dashboard
    // ═══════════════════════════════════════════════════════════════════
    @GetMapping("/dashboard")
    @Operation(summary = "Get CEO Dashboard", description = "Returns comprehensive financial data: revenue, expenses, tax breakdown, ratios, claim types, member types")
    public ResponseEntity<?> getCEODashboard(
            @RequestParam(required = false) String clientId,
            @RequestParam(required = false, defaultValue = "2025") String year) {

        // Aggregate all claims data
        double totalClaimsCost = claimRepository.totalClaimsCost();
        long totalClaims = claimRepository.count();
        long totalMembers = memberRepository.count();
        long activeMembers = memberRepository.countActiveMembers();

        // Claims by type
        double inpatientCost = claimRepository.sumCostByClaimType("Inpatient");
        long inpatientCount = claimRepository.countByClaimType("Inpatient");
        double outpatientCost = claimRepository.sumCostByClaimType("Outpatient");
        long outpatientCount = claimRepository.countByClaimType("Outpatient");
        double exgratiaCost = claimRepository.sumCostByClaimType("Ex-Gratia");
        long exgratiaCount = claimRepository.countByClaimType("Ex-Gratia");

        // Members by type
        long principals = memberRepository.countByMemberType("Principal");
        long dependents = memberRepository.countByMemberType("Dependent");

        // ═══════════════════════════════════════════════════════════════
        // FINANCIAL CALCULATIONS (matching admin-dashboard.html logic)
        // ═══════════════════════════════════════════════════════════════

        // Polaris TPA Fees (real fee structure)
        double auditFeeRate = 0.15; // 15% audit fee
        double regFeePerMember = 24.0; // $24/member/year
        double monthlyFeePerMember = 9.0; // $9/member/month

        double feeRevenue = (activeMembers * regFeePerMember) + (activeMembers * monthlyFeePerMember * 12);
        double premiumRevenue = totalClaimsCost * 1.35; // 35% markup on claims
        double grossRevenue = feeRevenue + premiumRevenue;

        // Expenses
        double operatingExpenses = grossRevenue * 0.15;
        double bankFees = grossRevenue * 0.01;
        double providerCosts = grossRevenue * 0.07;
        double totalExpenses = totalClaimsCost + operatingExpenses + bankFees;

        // EBIT & Profit
        double ebit = grossRevenue - totalExpenses;

        // Cyprus Tax (CIT 12.5% + SDC 2.65% + GHS 2.9%)
        double citRate = 0.125;
        double sdcRate = 0.0265;
        double ghsRate = 0.029;
        double corporateTax = ebit > 0 ? Math.round(ebit * citRate) : 0;
        double sdc = ebit > 0 ? Math.round(ebit * sdcRate) : 0;
        double ghs = ebit > 0 ? Math.round(ebit * ghsRate) : 0;
        double totalTax = corporateTax + sdc + ghs;

        double netProfit = ebit - totalTax;

        // Key Ratios
        double grossMarginPct = grossRevenue > 0 ? ((grossRevenue - totalClaimsCost) / grossRevenue * 100) : 0;
        double lossRatioPct = grossRevenue > 0 ? (totalClaimsCost / grossRevenue * 100) : 0;
        double profitMarginPct = grossRevenue > 0 ? (netProfit / grossRevenue * 100) : 0;
        double arpm = activeMembers > 0 ? Math.round(grossRevenue / activeMembers) : 0;

        // Cash Flow
        double stopLossRevenue = grossRevenue * 0.08;
        double networkRevenue = grossRevenue * 0.05;
        double processingRevenue = grossRevenue * 0.02;

        // ═══════════════════════════════════════════════════════════════
        // BUILD RESPONSE (matching Google Script getCEODashboard format)
        // ═══════════════════════════════════════════════════════════════

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("gross_revenue", Math.round(grossRevenue));
        summary.put("total_claims_cost", Math.round(totalClaimsCost));
        summary.put("net_profit", Math.round(netProfit));
        summary.put("ebit", Math.round(ebit));
        summary.put("cyprus_tax", Math.round(totalTax));
        summary.put("fee_revenue", Math.round(feeRevenue));
        summary.put("premium_revenue", Math.round(premiumRevenue));
        summary.put("stop_loss_revenue", Math.round(stopLossRevenue));
        summary.put("network_revenue", Math.round(networkRevenue));
        summary.put("processing_revenue", Math.round(processingRevenue));
        summary.put("operating_expenses", Math.round(operatingExpenses));
        summary.put("bank_fees", Math.round(bankFees));
        summary.put("provider_costs", Math.round(providerCosts));
        summary.put("total_members", totalMembers);
        summary.put("active_members", activeMembers);
        summary.put("total_claims", totalClaims);
        summary.put("gross_margin_pct", Math.round(grossMarginPct * 10) / 10.0);
        summary.put("loss_ratio_pct", Math.round(lossRatioPct * 10) / 10.0);
        summary.put("profit_margin_pct", Math.round(profitMarginPct * 10) / 10.0);
        summary.put("arpm", Math.round(arpm));
        // Enrollments/Cancellations
        summary.put("new_enrollments", memberRepository.countByStatus("Active"));
        summary.put("cancellations", memberRepository.countByStatus("Cancelled"));
        summary.put("net_change", memberRepository.countByStatus("Active") - memberRepository.countByStatus("Cancelled"));

        // Claim types breakdown
        Map<String, Object> claimTypes = new LinkedHashMap<>();
        Map<String, Object> costs = new LinkedHashMap<>();
        costs.put("inpatient", Math.round(inpatientCost));
        costs.put("outpatient", Math.round(outpatientCost));
        costs.put("exgratia", Math.round(exgratiaCost));
        claimTypes.put("costs", costs);
        Map<String, Object> counts = new LinkedHashMap<>();
        counts.put("inpatient", inpatientCount);
        counts.put("outpatient", outpatientCount);
        counts.put("exgratia", exgratiaCount);
        claimTypes.put("counts", counts);

        // Member types
        Map<String, Object> memberTypes = new LinkedHashMap<>();
        memberTypes.put("principals", principals);
        memberTypes.put("dependents", dependents);

        // Tax breakdown
        Map<String, Object> taxBreakdown = new LinkedHashMap<>();
        taxBreakdown.put("taxable_income", Math.round(ebit));
        taxBreakdown.put("corporate_tax_12_5", Math.round(corporateTax));
        taxBreakdown.put("sdc_2_65", Math.round(sdc));
        taxBreakdown.put("ghs_2_9", Math.round(ghs));
        taxBreakdown.put("total_tax", Math.round(totalTax));

        // Full response
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("success", true);
        response.put("period", year);
        response.put("summary", summary);
        response.put("claimTypes", claimTypes);
        response.put("memberTypes", memberTypes);
        response.put("taxBreakdown", taxBreakdown);

        return ResponseEntity.ok(response);
    }
}
