package com.polaris.api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id", unique = true, nullable = false, length = 50)
    private String clientId; // e.g., "CLI-001"

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "client_type", length = 20)
    private String clientType; // "parent" or "subsidiary"

    @Column(name = "parent_id", length = 50)
    private String parentId; // parent's clientId for subsidiaries

    @Column(length = 50)
    private String country;

    @Column(length = 50)
    private String sector;

    @Column(length = 20)
    private String status; // "active", "inactive"

    @Column(name = "total_members")
    private Integer totalMembers;

    @Column(name = "total_claims")
    private Integer totalClaims;

    @Column(name = "total_premium")
    private Double totalPremium;

    @Column(name = "contact_email", length = 100)
    private String contactEmail;

    @Column(name = "contact_phone", length = 30)
    private String contactPhone;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // === Constructors ===
    public Client() {}

    public Client(String clientId, String name, String clientType) {
        this.clientId = clientId;
        this.name = name;
        this.clientType = clientType;
    }

    // === Getters and Setters ===
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getClientType() { return clientType; }
    public void setClientType(String clientType) { this.clientType = clientType; }

    public String getParentId() { return parentId; }
    public void setParentId(String parentId) { this.parentId = parentId; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getSector() { return sector; }
    public void setSector(String sector) { this.sector = sector; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getTotalMembers() { return totalMembers; }
    public void setTotalMembers(Integer totalMembers) { this.totalMembers = totalMembers; }

    public Integer getTotalClaims() { return totalClaims; }
    public void setTotalClaims(Integer totalClaims) { this.totalClaims = totalClaims; }

    public Double getTotalPremium() { return totalPremium; }
    public void setTotalPremium(Double totalPremium) { this.totalPremium = totalPremium; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
