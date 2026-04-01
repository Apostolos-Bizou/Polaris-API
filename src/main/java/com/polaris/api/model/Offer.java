package com.polaris.api.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "offers")
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "offer_id", unique = true, nullable = false)
    private String offerId;

    @Column(name = "client_id", nullable = false)
    private String clientId;

    @Column(name = "client_name")
    private String clientName;

    // "standard" or "comparison"
    @Column(name = "offer_type")
    private String offerType = "standard";

    // draft, sent, accepted, pending_signature, signed, contract, rejected, expired
    @Column(name = "status")
    private String status = "draft";

    @Column(name = "offer_date")
    private LocalDate offerDate;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "validity_days")
    private Integer validityDays = 30;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    // Member counts
    @Column(name = "total_principals")
    private Integer totalPrincipals = 0;

    @Column(name = "total_dependents")
    private Integer totalDependents = 0;

    @Column(name = "total_members")
    private Integer totalMembers = 0;

    // Financial totals
    @Column(name = "subtotal_reg_fees")
    private Double subtotalRegFees = 0.0;

    @Column(name = "subtotal_fund_deposit")
    private Double subtotalFundDeposit = 0.0;

    @Column(name = "subtotal_dental")
    private Double subtotalDental = 0.0;

    @Column(name = "grand_total_usd")
    private Double grandTotalUsd = 0.0;

    @Column(name = "includes_dental")
    private Boolean includesDental = false;

    // Coverage
    @Column(name = "coverage_area")
    private String coverageArea = "Worldwide";

    @Column(name = "network")
    private String network;

    @Column(name = "program_name")
    private String programName;

    // Metadata
    @Column(name = "created_by")
    private String createdBy = "Admin";

    @Column(name = "notes", length = 2000)
    private String notes;

    @Column(name = "contact_name", length = 100)
    private String contactName;

    @Column(name = "contact_email", length = 100)
    private String contactEmail;

    // Offer items (line items / plan options)
    @OneToMany(mappedBy = "offer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("id ASC")
    private List<OfferItem> items = new ArrayList<>();

    // Constructors
    public Offer() {}

    public Offer(String offerId, String clientId, String clientName) {
        this.offerId = offerId;
        this.clientId = clientId;
        this.clientName = clientName;
        this.createdDate = LocalDateTime.now();
        this.offerDate = LocalDate.now();
    }

    // Helper methods
    public void addItem(OfferItem item) {
        items.add(item);
        item.setOffer(this);
    }

    public void removeItem(OfferItem item) {
        items.remove(item);
        item.setOffer(null);
    }

    public void recalculateTotals() {
        int principals = 0, dependents = 0;
        double regFees = 0, fundDeposit = 0, dental = 0;

        for (OfferItem item : items) {
            principals += item.getPrincipals() != null ? item.getPrincipals() : 0;
            dependents += item.getDependents() != null ? item.getDependents() : 0;
            regFees += item.getSubtotalReg() != null ? item.getSubtotalReg() : 0;
            fundDeposit += item.getSubtotalFund() != null ? item.getSubtotalFund() : 0;
        }

        this.totalPrincipals = principals;
        this.totalDependents = dependents;
        this.totalMembers = principals + dependents;

        if (this.includesDental != null && this.includesDental) {
            dental = this.totalMembers * 9.50; // DENTAL_FEE
        }

        this.subtotalRegFees = regFees;
        this.subtotalFundDeposit = fundDeposit;
        this.subtotalDental = dental;
        this.grandTotalUsd = regFees + fundDeposit + dental;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOfferId() { return offerId; }
    public void setOfferId(String offerId) { this.offerId = offerId; }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public String getOfferType() { return offerType; }
    public void setOfferType(String offerType) { this.offerType = offerType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getOfferDate() { return offerDate; }
    public void setOfferDate(LocalDate offerDate) { this.offerDate = offerDate; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public Integer getValidityDays() { return validityDays; }
    public void setValidityDays(Integer validityDays) { this.validityDays = validityDays; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public Integer getTotalPrincipals() { return totalPrincipals; }
    public void setTotalPrincipals(Integer totalPrincipals) { this.totalPrincipals = totalPrincipals; }

    public Integer getTotalDependents() { return totalDependents; }
    public void setTotalDependents(Integer totalDependents) { this.totalDependents = totalDependents; }

    public Integer getTotalMembers() { return totalMembers; }
    public void setTotalMembers(Integer totalMembers) { this.totalMembers = totalMembers; }

    public Double getSubtotalRegFees() { return subtotalRegFees; }
    public void setSubtotalRegFees(Double subtotalRegFees) { this.subtotalRegFees = subtotalRegFees; }

    public Double getSubtotalFundDeposit() { return subtotalFundDeposit; }
    public void setSubtotalFundDeposit(Double subtotalFundDeposit) { this.subtotalFundDeposit = subtotalFundDeposit; }

    public Double getSubtotalDental() { return subtotalDental; }
    public void setSubtotalDental(Double subtotalDental) { this.subtotalDental = subtotalDental; }

    public Double getGrandTotalUsd() { return grandTotalUsd; }
    public void setGrandTotalUsd(Double grandTotalUsd) { this.grandTotalUsd = grandTotalUsd; }

    public Boolean getIncludesDental() { return includesDental; }
    public void setIncludesDental(Boolean includesDental) { this.includesDental = includesDental; }

    public String getCoverageArea() { return coverageArea; }
    public void setCoverageArea(String coverageArea) { this.coverageArea = coverageArea; }

    public String getNetwork() { return network; }
    public void setNetwork(String network) { this.network = network; }

    public String getProgramName() { return programName; }
    public void setProgramName(String programName) { this.programName = programName; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public List<OfferItem> getItems() { return items; }
    public void setItems(List<OfferItem> items) { this.items = items; }

    public String getContactName() { return contactName; }
    public void setContactName(String contactName) { this.contactName = contactName; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

}
