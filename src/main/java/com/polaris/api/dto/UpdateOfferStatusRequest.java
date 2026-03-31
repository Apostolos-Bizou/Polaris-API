package com.polaris.api.dto;

/**
 * DTO for updating offer status via PUT /api/offers/{id}/status
 */
public class UpdateOfferStatusRequest {

    private String status;
    private String notes;

    // Valid status transitions:
    // draft -> sent -> accepted -> pending_signature -> signed -> contract
    // Any status -> rejected
    // Any status -> expired

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
