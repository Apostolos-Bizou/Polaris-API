package com.polaris.api.dto;

import java.util.List;

/**
 * DTO for creating a new offer via POST /api/offers
 */
public class CreateOfferRequest {

    private String clientId;
    private String clientName;
    private String offerType = "standard"; // "standard" or "comparison"
    private Integer validityDays = 30;
    private Boolean includesDental = false;
    private String coverageArea = "Worldwide";
    private String network;
    private String programName;
    private String notes;
    private String createdBy = "Admin";
    private List<OfferItemRequest> items;

    // Inner class for offer items
    public static class OfferItemRequest {
        private String planId;
        private String planName;
        private Integer principals = 0;
        private Integer dependents = 0;
        private Double regFee = 24.0;
        private Double fundDeposit = 0.0;
        private Boolean hasDental = false;

        // Getters and Setters
        public String getPlanId() { return planId; }
        public void setPlanId(String planId) { this.planId = planId; }
        public String getPlanName() { return planName; }
        public void setPlanName(String planName) { this.planName = planName; }
        public Integer getPrincipals() { return principals; }
        public void setPrincipals(Integer principals) { this.principals = principals; }
        public Integer getDependents() { return dependents; }
        public void setDependents(Integer dependents) { this.dependents = dependents; }
        public Double getRegFee() { return regFee; }
        public void setRegFee(Double regFee) { this.regFee = regFee; }
        public Double getFundDeposit() { return fundDeposit; }
        public void setFundDeposit(Double fundDeposit) { this.fundDeposit = fundDeposit; }
        public Boolean getHasDental() { return hasDental; }
        public void setHasDental(Boolean hasDental) { this.hasDental = hasDental; }
    }

    // Getters and Setters
    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }
    public String getOfferType() { return offerType; }
    public void setOfferType(String offerType) { this.offerType = offerType; }
    public Integer getValidityDays() { return validityDays; }
    public void setValidityDays(Integer validityDays) { this.validityDays = validityDays; }
    public Boolean getIncludesDental() { return includesDental; }
    public void setIncludesDental(Boolean includesDental) { this.includesDental = includesDental; }
    public String getCoverageArea() { return coverageArea; }
    public void setCoverageArea(String coverageArea) { this.coverageArea = coverageArea; }
    public String getNetwork() { return network; }
    public void setNetwork(String network) { this.network = network; }
    public String getProgramName() { return programName; }
    public void setProgramName(String programName) { this.programName = programName; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public List<OfferItemRequest> getItems() { return items; }
    public void setItems(List<OfferItemRequest> items) { this.items = items; }
}
