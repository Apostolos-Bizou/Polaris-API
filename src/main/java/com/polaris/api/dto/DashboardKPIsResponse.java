package com.polaris.api.dto;

public class DashboardKPIsResponse {

    // Core KPIs
    private long totalClients;
    private long activeMembers;
    private long totalClaims;
    private double totalCostUsd;
    private double averageClaimCost;

    // Inpatient / Outpatient / Ex Gratia
    private long inpatientCases;
    private double inpatientCost;
    private long outpatientCases;
    private double outpatientCost;
    private long exGratiaCases;
    private double exGratiaCost;

    // Principal / Dependent
    private long principalCount;
    private long dependentCount;
    private long principalClaims;
    private long dependentClaims;

    // Member Movement
    private long newEnrollments;
    private long cancellations;
    private long netChange;

    // Constructors
    public DashboardKPIsResponse() {}

    // === Getters and Setters ===
    public long getTotalClients() { return totalClients; }
    public void setTotalClients(long totalClients) { this.totalClients = totalClients; }

    public long getActiveMembers() { return activeMembers; }
    public void setActiveMembers(long activeMembers) { this.activeMembers = activeMembers; }

    public long getTotalClaims() { return totalClaims; }
    public void setTotalClaims(long totalClaims) { this.totalClaims = totalClaims; }

    public double getTotalCostUsd() { return totalCostUsd; }
    public void setTotalCostUsd(double totalCostUsd) { this.totalCostUsd = totalCostUsd; }

    public double getAverageClaimCost() { return averageClaimCost; }
    public void setAverageClaimCost(double averageClaimCost) { this.averageClaimCost = averageClaimCost; }

    public long getInpatientCases() { return inpatientCases; }
    public void setInpatientCases(long inpatientCases) { this.inpatientCases = inpatientCases; }

    public double getInpatientCost() { return inpatientCost; }
    public void setInpatientCost(double inpatientCost) { this.inpatientCost = inpatientCost; }

    public long getOutpatientCases() { return outpatientCases; }
    public void setOutpatientCases(long outpatientCases) { this.outpatientCases = outpatientCases; }

    public double getOutpatientCost() { return outpatientCost; }
    public void setOutpatientCost(double outpatientCost) { this.outpatientCost = outpatientCost; }

    public long getExGratiaCases() { return exGratiaCases; }
    public void setExGratiaCases(long exGratiaCases) { this.exGratiaCases = exGratiaCases; }

    public double getExGratiaCost() { return exGratiaCost; }
    public void setExGratiaCost(double exGratiaCost) { this.exGratiaCost = exGratiaCost; }

    public long getPrincipalCount() { return principalCount; }
    public void setPrincipalCount(long principalCount) { this.principalCount = principalCount; }

    public long getDependentCount() { return dependentCount; }
    public void setDependentCount(long dependentCount) { this.dependentCount = dependentCount; }

    public long getPrincipalClaims() { return principalClaims; }
    public void setPrincipalClaims(long principalClaims) { this.principalClaims = principalClaims; }

    public long getDependentClaims() { return dependentClaims; }
    public void setDependentClaims(long dependentClaims) { this.dependentClaims = dependentClaims; }

    public long getNewEnrollments() { return newEnrollments; }
    public void setNewEnrollments(long newEnrollments) { this.newEnrollments = newEnrollments; }

    public long getCancellations() { return cancellations; }
    public void setCancellations(long cancellations) { this.cancellations = cancellations; }

    public long getNetChange() { return netChange; }
    public void setNetChange(long netChange) { this.netChange = netChange; }
}
