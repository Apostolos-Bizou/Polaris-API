package com.polaris.api.controller;

import com.polaris.api.model.User;
import com.polaris.api.repository.UserRepository;
import com.polaris.api.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * Integration tests for Polaris API endpoints.
 * Uses Spring Boot Test with H2 in-memory database.
 * Tests cover authentication, dashboard, clients, offers, claims, members, renewals, and admin.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class PolarisApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    private String jwtToken;

    @BeforeEach
    void setUp() {
        // Generate JWT token for authenticated requests
        jwtToken = jwtUtil.generateToken("tolis_admin", "ADMIN");
    }

    // ═══════════════════════════════════════════════════════════════
    // HEALTH CHECK TESTS
    // ═══════════════════════════════════════════════════════════════

    @Test
    void healthCheck_shouldReturnHealthy() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("healthy"))
                .andExpect(jsonPath("$.database").value("connected"))
                .andExpect(jsonPath("$.service").value("polaris-api"));
    }

    // ═══════════════════════════════════════════════════════════════
    // AUTH TESTS
    // ═══════════════════════════════════════════════════════════════

    @Test
    void login_withValidCredentials_shouldReturnToken() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"tolis_admin\", \"password\": \"admin123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").value("tolis_admin"))
                .andExpect(jsonPath("$.role").value("admin"));
    }

    @Test
    void login_withInvalidCredentials_shouldReturn401() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"wrong\", \"password\": \"wrong\"}"))
                .andExpect(status().isUnauthorized());
    }

    // ═══════════════════════════════════════════════════════════════
    // DASHBOARD TESTS
    // ═══════════════════════════════════════════════════════════════

    @Test
    void dashboardKpis_withAuth_shouldReturnKPIs() throws Exception {
        mockMvc.perform(get("/api/dashboard/kpis")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalClients").isNumber())
                .andExpect(jsonPath("$.totalClaims").isNumber())
                .andExpect(jsonPath("$.activeMembers").isNumber());
    }

    @Test
    void dashboardKpis_withoutAuth_shouldReturn403() throws Exception {
        mockMvc.perform(get("/api/dashboard/kpis"))
                .andExpect(status().isForbidden());
    }

    // ═══════════════════════════════════════════════════════════════
    // CLIENT TESTS
    // ═══════════════════════════════════════════════════════════════

    @Test
    void getClients_shouldReturn30Clients() throws Exception {
        mockMvc.perform(get("/api/clients")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(30)));
    }

    @Test
    void getClientById_shouldReturnClient() throws Exception {
        mockMvc.perform(get("/api/clients/CLI-001")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value("CLI-001"));
    }

    @Test
    void getClientKpis_shouldReturnKPIs() throws Exception {
        mockMvc.perform(get("/api/clients/CLI-001/kpis")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(status().isOk());
    }

    // ═══════════════════════════════════════════════════════════════
    // OFFER TESTS
    // ═══════════════════════════════════════════════════════════════

    @Test
    void getOffers_shouldReturn15Offers() throws Exception {
        mockMvc.perform(get("/api/offers")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.count").value(15))
                .andExpect(jsonPath("$.data", hasSize(15)));
    }

    @Test
    void getOfferStats_publicEndpoint_shouldReturnStats() throws Exception {
        mockMvc.perform(get("/api/offers/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.total").value(15))
                .andExpect(jsonPath("$.data.draft").isNumber())
                .andExpect(jsonPath("$.data.revenue_pipeline").isNumber());
    }

    @Test
    void getOfferById_shouldReturnOffer() throws Exception {
        mockMvc.perform(get("/api/offers/OFF-2025-001")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.offer_id").value("OFF-2025-001"))
                .andExpect(jsonPath("$.data.client_name").value("CENTROFIN CORP"));
    }

    @Test
    void createOffer_shouldCreateNewOffer() throws Exception {
        mockMvc.perform(post("/api/offers")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"clientId\": \"CLI-099\", \"clientName\": \"TEST CLIENT\", \"offerType\": \"standard\", \"validityDays\": 30}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.offer_id").exists());
    }

    @Test
    void updateOfferStatus_shouldUpdateStatus() throws Exception {
        mockMvc.perform(put("/api/offers/OFF-2025-005/status")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\": \"sent\", \"notes\": \"Test status update\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.new_status").value("sent"));
    }

    // ═══════════════════════════════════════════════════════════════
    // CLAIMS TESTS
    // ═══════════════════════════════════════════════════════════════

    @Test
    void claimsStats_publicEndpoint_shouldReturnStats() throws Exception {
        mockMvc.perform(get("/api/claims/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.total_claims").value(1774))
                .andExpect(jsonPath("$.data.by_type.inpatient.count").isNumber())
                .andExpect(jsonPath("$.data.by_type.outpatient.count").isNumber());
    }

    @Test
    void claimsCategories_shouldReturnBreakdown() throws Exception {
        mockMvc.perform(get("/api/claims/categories")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }

    // ═══════════════════════════════════════════════════════════════
    // MEMBER TESTS
    // ═══════════════════════════════════════════════════════════════

    @Test
    void memberStats_publicEndpoint_shouldReturnStats() throws Exception {
        mockMvc.perform(get("/api/members/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.total").value(3337))
                .andExpect(jsonPath("$.data.principals").isNumber())
                .andExpect(jsonPath("$.data.dependents").isNumber());
    }

    @Test
    void getMembersByClient_shouldReturnMembers() throws Exception {
        mockMvc.perform(get("/api/members/client/CLI-001")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.client_id").value("CLI-001"));
    }

    // ═══════════════════════════════════════════════════════════════
    // RENEWALS TESTS
    // ═══════════════════════════════════════════════════════════════

    @Test
    void renewalsKpis_publicEndpoint_shouldReturnKPIs() throws Exception {
        mockMvc.perform(get("/api/renewals/kpis"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.total_members").isNumber())
                .andExpect(jsonPath("$.data.pending_offers").isNumber());
    }

    @Test
    void renewalsPipeline_shouldReturnPipeline() throws Exception {
        mockMvc.perform(get("/api/renewals/pipeline")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.pipeline").exists());
    }

    @Test
    void renewalsExpiring_shouldReturnContracts() throws Exception {
        mockMvc.perform(get("/api/renewals/expiring")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }

    // ═══════════════════════════════════════════════════════════════
    // CEO DASHBOARD TESTS
    // ═══════════════════════════════════════════════════════════════

    @Test
    void ceoDashboard_shouldReturnFinancials() throws Exception {
        mockMvc.perform(get("/api/ceo/dashboard")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.summary.gross_revenue").isNumber())
                .andExpect(jsonPath("$.summary.net_profit").isNumber())
                .andExpect(jsonPath("$.taxBreakdown.total_tax").isNumber())
                .andExpect(jsonPath("$.claimTypes").exists());
    }

    // ═══════════════════════════════════════════════════════════════
    // ADMIN TESTS
    // ═══════════════════════════════════════════════════════════════

    @Test
    void getUsers_withAdmin_shouldReturnUsers() throws Exception {
        mockMvc.perform(get("/api/admin/users")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.count").value(greaterThanOrEqualTo(4)));
    }

    @Test
    void createUser_shouldCreateNewUser() throws Exception {
        mockMvc.perform(post("/api/admin/users")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"testuser\", \"password\": \"test123\", \"email\": \"test@polaris.com\", \"role\": \"USER\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.user.username").value("testuser"));
    }
}
