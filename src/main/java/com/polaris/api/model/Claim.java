package com.polaris.api.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "claims")
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "claim_id", unique = true, length = 20)
    private String claimId;

    @Column(name = "client_id", nullable = false, length = 20)
    private String clientId;

    @Column(name = "member_id", length = 20)
    private String memberId;

    @Column(length = 50)
    private String category; // Inpatient, Outpatient, Dental, Maternity, etc.

    @Column(name = "claim_type", length = 20)
    private String claimType; // "inpatient", "outpatient", "ex_gratia"

    @Column(name = "amount_usd")
    private Double amountUsd;

    @Column(length = 20)
    private String status; // "approved", "pending", "rejected"

    @Column(length = 100)
    private String hospital;

    @Column(name = "hospital_country", length = 50)
    private String hospitalCountry;

    @Column(name = "claim_date")
    private LocalDate claimDate;

    @Column(name = "member_type", length = 20)
    private String memberType; // "principal", "dependent"

    @Column(length = 50)
    private String plan; // "Gold", "Silver", "Platinum"

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // === Constructors ===
    public Claim() {}

    // === Getters and Setters ===
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getClaimId() { return claimId; }
    public void setClaimId(String claimId) { this.claimId = claimId; }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }

    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getClaimType() { return claimType; }
    public void setClaimType(String claimType) { this.claimType = claimType; }

    public Double getAmountUsd() { return amountUsd; }
    public void setAmountUsd(Double amountUsd) { this.amountUsd = amountUsd; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getHospital() { return hospital; }
    public void setHospital(String hospital) { this.hospital = hospital; }

    public String getHospitalCountry() { return hospitalCountry; }
    public void setHospitalCountry(String hospitalCountry) { this.hospitalCountry = hospitalCountry; }

    public LocalDate getClaimDate() { return claimDate; }
    public void setClaimDate(LocalDate claimDate) { this.claimDate = claimDate; }

    public String getMemberType() { return memberType; }
    public void setMemberType(String memberType) { this.memberType = memberType; }

    public String getPlan() { return plan; }
    public void setPlan(String plan) { this.plan = plan; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
