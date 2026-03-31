package com.polaris.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "offer_items")
public class OfferItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offer_db_id")
    @JsonIgnore
    private Offer offer;

    @Column(name = "plan_id")
    private String planId;

    @Column(name = "plan_name")
    private String planName;

    @Column(name = "principals")
    private Integer principals = 0;

    @Column(name = "dependents")
    private Integer dependents = 0;

    @Column(name = "reg_fee")
    private Double regFee = 24.0;

    @Column(name = "fund_deposit")
    private Double fundDeposit = 0.0;

    @Column(name = "subtotal_reg")
    private Double subtotalReg = 0.0;

    @Column(name = "subtotal_fund")
    private Double subtotalFund = 0.0;

    @Column(name = "has_dental")
    private Boolean hasDental = false;

    @Column(name = "dental_fee")
    private Double dentalFee = 9.50;

    @Column(name = "dental_total")
    private Double dentalTotal = 0.0;

    @Column(name = "total_cost")
    private Double totalCost = 0.0;

    @Column(name = "per_member")
    private Double perMember = 0.0;

    // Constructors
    public OfferItem() {}

    public OfferItem(String planName, int principals, int dependents, double regFee, double fundDeposit) {
        this.planName = planName;
        this.principals = principals;
        this.dependents = dependents;
        this.regFee = regFee;
        this.fundDeposit = fundDeposit;
        recalculate();
    }

    public void recalculate() {
        int totalMembers = (principals != null ? principals : 0) + (dependents != null ? dependents : 0);
        this.subtotalReg = (principals != null ? principals : 0) * (regFee != null ? regFee : 24.0);
        this.subtotalFund = totalMembers * (fundDeposit != null ? fundDeposit : 0);
        this.dentalTotal = (hasDental != null && hasDental) ? totalMembers * (dentalFee != null ? dentalFee : 9.50) : 0;
        this.totalCost = this.subtotalReg + this.subtotalFund + this.dentalTotal;
        this.perMember = totalMembers > 0 ? this.totalCost / totalMembers : 0;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Offer getOffer() { return offer; }
    public void setOffer(Offer offer) { this.offer = offer; }

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

    public Double getSubtotalReg() { return subtotalReg; }
    public void setSubtotalReg(Double subtotalReg) { this.subtotalReg = subtotalReg; }

    public Double getSubtotalFund() { return subtotalFund; }
    public void setSubtotalFund(Double subtotalFund) { this.subtotalFund = subtotalFund; }

    public Boolean getHasDental() { return hasDental; }
    public void setHasDental(Boolean hasDental) { this.hasDental = hasDental; }

    public Double getDentalFee() { return dentalFee; }
    public void setDentalFee(Double dentalFee) { this.dentalFee = dentalFee; }

    public Double getDentalTotal() { return dentalTotal; }
    public void setDentalTotal(Double dentalTotal) { this.dentalTotal = dentalTotal; }

    public Double getTotalCost() { return totalCost; }
    public void setTotalCost(Double totalCost) { this.totalCost = totalCost; }

    public Double getPerMember() { return perMember; }
    public void setPerMember(Double perMember) { this.perMember = perMember; }
}
