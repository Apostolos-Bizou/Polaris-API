package com.polaris.api.config;

import com.polaris.api.model.Offer;
import com.polaris.api.model.OfferItem;
import com.polaris.api.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Order(2) // Run after main DataInitializer (Order 1)
public class OfferDataInitializer implements CommandLineRunner {

    @Autowired
    private OfferRepository offerRepository;

    @Override
    public void run(String... args) {
        if (offerRepository.count() > 0) {
            System.out.println("Offers already seeded, skipping...");
            return;
        }

        System.out.println("Seeding offers data...");

        // ═══════════════════════════════════════════════════════════════
        // STANDARD OFFERS - Various statuses across the pipeline
        // ═══════════════════════════════════════════════════════════════

        // 1. CENTROFIN - Contract (completed lifecycle)
        Offer o1 = createOffer("OFF-2025-001", "CLI-001", "CENTROFIN CORP", "standard", "contract",
                LocalDate.of(2025, 1, 15), 30, true, "Worldwide", "Cigna Global", "Marine Elite");
        addItem(o1, "gold", "Gold Plan", 45, 30, 24.0, 55.0, false);
        addItem(o1, "platinum", "Platinum Plan", 20, 15, 24.0, 75.0, false);
        o1.recalculateTotals();
        offerRepository.save(o1);

        // 2.NAVIOS GROUP - Signed (awaiting contract creation)
        Offer o2 = createOffer("OFF-2025-002", "CLI-002", "NAVIOS GROUP", "standard", "signed",
                LocalDate.of(2025, 2, 10), 30, true, "Worldwide", "Allianz Partners", "Marine Premium");
        addItem(o2, "platinum", "Platinum Plan", 60, 40, 24.0, 75.0, true);
        addItem(o2, "diamond", "Diamond Plan", 15, 10, 24.0, 95.0, true);
        o2.recalculateTotals();
        offerRepository.save(o2);

        // 3. MINERVA MARINE - Accepted
        Offer o3 = createOffer("OFF-2025-003", "CLI-003", "MINERVA MARINE", "standard", "accepted",
                LocalDate.of(2025, 3, 5), 30, false, "Worldwide excl. US", "Cigna Global", "Marine Standard");
        addItem(o3, "gold", "Gold Plan", 80, 55, 24.0, 55.0, false);
        o3.recalculateTotals();
        offerRepository.save(o3);

        // 4. THENAMARIS - Sent (waiting for client response)
        Offer o4 = createOffer("OFF-2025-004", "CLI-004", "THENAMARIS", "standard", "sent",
                LocalDate.of(2025, 3, 20), 45, true, "Worldwide", "MetLife International", "Marine Executive");
        addItem(o4, "platinum", "Platinum Plan", 35, 25, 24.0, 75.0, true);
        addItem(o4, "gold", "Gold Plan", 50, 35, 24.0, 55.0, false);
        o4.recalculateTotals();
        offerRepository.save(o4);

        // 5. TSAKOS GROUP - Draft (being prepared)
        Offer o5 = createOffer("OFF-2025-005", "CLI-005", "TSAKOS GROUP", "standard", "draft",
                LocalDate.of(2025, 3, 28), 30, true, "Worldwide", "Cigna Global", "Marine Elite");
        addItem(o5, "diamond", "Diamond Plan", 25, 18, 24.0, 95.0, true);
        addItem(o5, "platinum", "Platinum Plan", 40, 28, 24.0, 75.0, true);
        o5.recalculateTotals();
        offerRepository.save(o5);

        // 6. COSTAMARE - Pending Signature
        Offer o6 = createOffer("OFF-2025-006", "CLI-006", "COSTAMARE INC", "standard", "pending_signature",
                LocalDate.of(2025, 2, 28), 30, false, "Worldwide excl. US", "Allianz Partners", "Marine Standard");
        addItem(o6, "gold", "Gold Plan", 70, 48, 24.0, 55.0, false);
        addItem(o6, "silver", "Silver Plan", 30, 20, 24.0, 42.0, false);
        o6.recalculateTotals();
        offerRepository.save(o6);

        // 7. STAR BULK - Rejected
        Offer o7 = createOffer("OFF-2025-007", "CLI-007", "STAR BULK CARRIERS", "standard", "rejected",
                LocalDate.of(2025, 1, 20), 30, false, "Worldwide", "Cigna Global", "Marine Premium");
        addItem(o7, "platinum", "Platinum Plan", 55, 38, 24.0, 75.0, false);
        o7.setNotes("Client chose competitor offer from Willis Towers Watson");
        o7.recalculateTotals();
        offerRepository.save(o7);

        // 8. EUROSEAS - Draft
        Offer o8 = createOffer("OFF-2025-008", "CLI-008", "EUROSEAS LTD", "standard", "draft",
                LocalDate.of(2025, 3, 30), 30, true, "Worldwide excl. US", "MetLife International", "Marine Standard");
        addItem(o8, "gold", "Gold Plan", 22, 15, 24.0, 55.0, true);
        o8.recalculateTotals();
        offerRepository.save(o8);

        // 9. DIANA SHIPPING - Sent
        Offer o9 = createOffer("OFF-2025-009", "CLI-009", "DIANA SHIPPING", "standard", "sent",
                LocalDate.of(2025, 3, 15), 45, true, "Worldwide", "Allianz Partners", "Marine Executive");
        addItem(o9, "diamond", "Diamond Plan", 18, 12, 24.0, 95.0, true);
        addItem(o9, "platinum", "Platinum Plan", 32, 22, 24.0, 75.0, true);
        o9.recalculateTotals();
        offerRepository.save(o9);

        // 10. ELETSON - Expired
        Offer o10 = createOffer("OFF-2025-010", "CLI-010", "ELETSON CORP", "standard", "expired",
                LocalDate.of(2024, 11, 10), 30, false, "Worldwide", "Cigna Global", "Marine Standard");
        addItem(o10, "gold", "Gold Plan", 40, 28, 24.0, 55.0, false);
        o10.setNotes("Offer expired - client did not respond within validity period");
        o10.recalculateTotals();
        offerRepository.save(o10);

        // ═══════════════════════════════════════════════════════════════
        // COMPARISON OFFERS (CQ) - Multi-plan comparisons
        // ═══════════════════════════════════════════════════════════════

        // 11. DANAOS CORP - Comparison Quote (sent)
        Offer cq1 = createOffer("CQ-2025-001", "CLI-011", "DANAOS CORPORATION", "comparison", "sent",
                LocalDate.of(2025, 3, 18), 30, false, "Worldwide", null, null);
        addItem(cq1, "gold", "Gold Plan", 30, 20, 24.0, 55.0, false);
        addItem(cq1, "platinum", "Platinum Plan", 30, 20, 24.0, 75.0, false);
        addItem(cq1, "diamond", "Diamond Plan", 30, 20, 24.0, 95.0, false);
        cq1.recalculateTotals();
        offerRepository.save(cq1);

        // 12. DORIAN LPG - Comparison Quote (draft)
        Offer cq2 = createOffer("CQ-2025-002", "CLI-012", "DORIAN LPG", "comparison", "draft",
                LocalDate.of(2025, 3, 25), 30, true, "Worldwide excl. US", null, null);
        addItem(cq2, "silver", "Silver Plan", 15, 10, 24.0, 42.0, true);
        addItem(cq2, "gold", "Gold Plan", 15, 10, 24.0, 55.0, true);
        addItem(cq2, "gold_plus", "Gold Plus Plan", 15, 10, 24.0, 65.0, true);
        cq2.recalculateTotals();
        offerRepository.save(cq2);

        // 13. PYXIS TANKERS - Comparison Quote (accepted)
        Offer cq3 = createOffer("CQ-2025-003", "CLI-013", "PYXIS TANKERS", "comparison", "accepted",
                LocalDate.of(2025, 2, 20), 30, true, "Worldwide", null, null);
        addItem(cq3, "gold", "Gold Plan", 12, 8, 24.0, 55.0, true);
        addItem(cq3, "platinum", "Platinum Plan", 12, 8, 24.0, 75.0, true);
        cq3.recalculateTotals();
        offerRepository.save(cq3);

        // 14. GLOBUS MARITIME - Comparison Quote (draft)
        Offer cq4 = createOffer("CQ-2025-004", "CLI-014", "GLOBUS MARITIME", "comparison", "draft",
                LocalDate.of(2025, 3, 29), 45, false, "Worldwide excl. US", null, null);
        addItem(cq4, "bronze", "Bronze Plan", 8, 5, 24.0, 35.0, false);
        addItem(cq4, "silver", "Silver Plan", 8, 5, 24.0, 42.0, false);
        addItem(cq4, "gold", "Gold Plan", 8, 5, 24.0, 55.0, false);
        addItem(cq4, "platinum", "Platinum Plan", 8, 5, 24.0, 75.0, false);
        cq4.recalculateTotals();
        offerRepository.save(cq4);

        // 15. SEANERGY - Standard (contract - older)
        Offer o15 = createOffer("OFF-2024-015", "CLI-015", "SEANERGY MARITIME", "standard", "contract",
                LocalDate.of(2024, 9, 15), 30, true, "Worldwide", "Cigna Global", "Marine Premium");
        addItem(o15, "platinum", "Platinum Plan", 28, 19, 24.0, 75.0, true);
        o15.recalculateTotals();
        offerRepository.save(o15);

        long count = offerRepository.count();
        System.out.println("Offers seeded successfully! Total: " + count +
                " (" + offerRepository.countByStatus("draft") + " draft, " +
                offerRepository.countByStatus("sent") + " sent, " +
                offerRepository.countByStatus("accepted") + " accepted, " +
                offerRepository.countByStatus("signed") + " signed, " +
                offerRepository.countByStatus("contract") + " contract)");
    }

    // ═══════════════════════════════════════════════════════════════════
    // HELPER METHODS
    // ═══════════════════════════════════════════════════════════════════

    private Offer createOffer(String offerId, String clientId, String clientName,
                              String offerType, String status, LocalDate offerDate,
                              int validityDays, boolean includesDental,
                              String coverageArea, String network, String programName) {
        Offer o = new Offer(offerId, clientId, clientName);
        o.setOfferType(offerType);
        o.setStatus(status);
        o.setOfferDate(offerDate);
        o.setCreatedDate(offerDate.atTime(10, 0));
        o.setValidityDays(validityDays);
        o.setExpiryDate(offerDate.plusDays(validityDays));
        o.setIncludesDental(includesDental);
        o.setCoverageArea(coverageArea);
        o.setNetwork(network);
        o.setProgramName(programName);
        o.setCreatedBy("Admin");
        return o;
    }

    private void addItem(Offer offer, String planId, String planName,
                         int principals, int dependents, double regFee, double fundDeposit,
                         boolean hasDental) {
        OfferItem item = new OfferItem();
        item.setPlanId(planId);
        item.setPlanName(planName);
        item.setPrincipals(principals);
        item.setDependents(dependents);
        item.setRegFee(regFee);
        item.setFundDeposit(fundDeposit);
        item.setHasDental(hasDental);
        item.setDentalFee(9.50);
        item.recalculate();
        offer.addItem(item);
    }
}
