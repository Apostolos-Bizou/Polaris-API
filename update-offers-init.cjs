const fs = require('fs');
const f = 'C:\\Users\\akage\\Downloads\\polaris-api\\src\\main\\java\\com\\polaris\\api\\config\\OfferDataInitializer.java';
let lines = fs.readFileSync(f, 'utf8').split('\n');

// Find the line after "Seeding offers data..."
let seedLine = -1;
let lastSaveLine = -1;
for (let i = 0; i < lines.length; i++) {
  if (lines[i].includes('Seeding offers data')) seedLine = i;
  if (lines[i].includes('offerRepository.save(')) lastSaveLine = i;
}

// Find the summary print line after all saves
let summaryLine = -1;
for (let i = lastSaveLine; i < lines.length; i++) {
  if (lines[i].includes('System.out.println') && lines[i].includes('Offers seeded')) {
    summaryLine = i;
    break;
  }
}

console.log('Seed start:', seedLine + 1);
console.log('Last save:', lastSaveLine + 1);
console.log('Summary:', summaryLine + 1);

// Replace everything between seedLine+1 and summaryLine with real offers
const newOffers = `
        // ═══════════════════════════════════════════════════════════════
        // REAL OFFERS FROM PRODUCTION (12 offers)
        // ═══════════════════════════════════════════════════════════════

        // 1. AIMS ADELE SHIPHOLDING LTD - Draft
        Offer o1 = createOffer("OFF-2026-0008", "CLI-2026-0018", "AIMS ADELE SHIPHOLDING LTD", "standard", "draft",
                LocalDate.of(2026, 3, 27), 30, true, "Worldwide", "MetLife International", "Marine Elite");
        addItem(o1, "gold", "Gold Plan", 1200, 800, 24.0, 42.0, true);
        o1.setContactName("GIANNHS KAKKARIS");
        o1.setContactEmail("manning@aims-shipping.com");
        o1.recalculateTotals();
        offerRepository.save(o1);

        // 2. Prospective Client - Draft
        Offer o2 = createOffer("OFF-2026-0007", "PROSPECT-001", "Prospective Client", "standard", "draft",
                LocalDate.of(2026, 3, 27), 30, true, "Worldwide", "Cigna Global", "Marine Premium");
        addItem(o2, "platinum", "Platinum Plan", 800, 1400, 24.0, 47.0, true);
        o2.recalculateTotals();
        offerRepository.save(o2);

        // 3. CROSSWORLD MARINE SERVICES INC - Pending Signature
        Offer o3 = createOffer("OFF-2026-0006", "CLI-2026-0022", "CROSSWORLD MARINE SERVICES INC", "standard", "pending_signature",
                LocalDate.of(2026, 2, 16), 30, true, "Worldwide", "Allianz Partners", "Marine Standard");
        addItem(o3, "gold", "Gold Plan", 500, 1000, 24.0, 42.0, true);
        o3.setContactName("SIMOS VARIAS");
        o3.setContactEmail("simos@crossworld.ph");
        o3.recalculateTotals();
        offerRepository.save(o3);

        // 4. Prospective Client - Draft
        Offer o4 = createOffer("OFF-2026-0005", "PROSPECT-002", "Prospective Client", "standard", "draft",
                LocalDate.of(2026, 2, 16), 30, true, "Worldwide excl. US", "MetLife International", "Marine Standard");
        addItem(o4, "gold", "Gold Plan", 500, 600, 24.0, 42.0, true);
        o4.recalculateTotals();
        offerRepository.save(o4);

        // 5. ASTRA SHIPMANAGEMENT INC - Pending Signature
        Offer o5 = createOffer("OFF-2026-0004", "CLI-2026-0015", "ASTRA SHIPMANAGEMENT INC", "standard", "pending_signature",
                LocalDate.of(2026, 2, 9), 30, true, "Worldwide", "Cigna Global", "Marine Elite");
        addItem(o5, "gold", "Gold Plan", 500, 400, 24.0, 42.0, true);
        o5.setContactName("NIKOS TROUSAS");
        o5.setContactEmail("nikos@astra-ship.gr");
        o5.recalculateTotals();
        offerRepository.save(o5);

        // 6. CROSSWORLD MARINE SERVICES INC - Signed
        Offer o6 = createOffer("OFF-2026-0003", "CLI-2026-0022", "CROSSWORLD MARINE SERVICES INC", "standard", "signed",
                LocalDate.of(2026, 2, 9), 30, true, "Worldwide", "Allianz Partners", "Marine Premium");
        addItem(o6, "platinum", "Platinum Plan", 500, 1500, 24.0, 47.0, true);
        o6.setContactName("SIMOS VARIAS");
        o6.setContactEmail("simos@crossworld.ph");
        o6.recalculateTotals();
        offerRepository.save(o6);

        // 7. Prospective Client - Draft
        Offer o7 = createOffer("OFF-2026-0002", "PROSPECT-003", "Prospective Client", "standard", "draft",
                LocalDate.of(2026, 2, 9), 30, true, "Worldwide", "Cigna Global", "Marine Standard");
        addItem(o7, "platinum", "Platinum Plan", 500, 1500, 24.0, 47.0, true);
        o7.recalculateTotals();
        offerRepository.save(o7);

        // 8. CROSSWORLD MARINE SERVICES INC - Pending Signature
        Offer o8 = createOffer("OFF-2026-0001", "CLI-2026-0022", "CROSSWORLD MARINE SERVICES INC", "standard", "pending_signature",
                LocalDate.of(2026, 2, 9), 30, true, "Worldwide", "Allianz Partners", "Marine Standard");
        addItem(o8, "gold", "Gold Plan", 500, 500, 24.0, 42.0, true);
        o8.setContactName("SIMOS VARIAS");
        o8.setContactEmail("simos@crossworld.ph");
        o8.recalculateTotals();
        offerRepository.save(o8);

        // 9. CROSSWORLD (Group) - Draft
        Offer o9 = createOffer("OFF-2026-0009", "CLI-2026-0022", "CROSSWORLD MARINE SERVICES INC", "standard", "draft",
                LocalDate.of(2026, 2, 9), 30, true, "Worldwide", "MetLife International", "Marine Standard");
        addItem(o9, "gold", "Gold Plan", 567, 456, 24.0, 42.0, true);
        o9.setContactName("SIMOS VARIAS");
        o9.recalculateTotals();
        offerRepository.save(o9);

        // 10. ASTRA SHIPMANAGEMENT INC (Group) - Signed
        Offer o10 = createOffer("OFF-2026-0010", "CLI-2026-0015", "ASTRA SHIPMANAGEMENT INC", "standard", "signed",
                LocalDate.of(2026, 2, 9), 30, true, "Worldwide", "Cigna Global", "Marine Elite");
        addItem(o10, "silver", "Silver Plan", 1000, 500, 24.0, 18.0, false);
        addItem(o10, "platinum", "Platinum Plan", 200, 300, 24.0, 47.0, false);
        o10.setContactName("NIKOS TROUSAS");
        o10.recalculateTotals();
        offerRepository.save(o10);

        // 11. CROSSWORLD (Group) - Accepted
        Offer o11 = createOffer("OFF-2026-0011", "CLI-2026-0022", "CROSSWORLD MARINE SERVICES INC", "standard", "accepted",
                LocalDate.of(2026, 2, 9), 30, true, "Worldwide", "Allianz Partners", "Marine Premium");
        addItem(o11, "gold", "Gold Plan", 300, 300, 24.0, 42.0, true);
        o11.setContactName("SIMOS VARIAS");
        o11.recalculateTotals();
        offerRepository.save(o11);

        // 12. ASTRA TANKERS GOLD - Draft
        Offer o12 = createOffer("OFF-2026-0012", "CLI-2026-0017", "ASTRA TANKERS GOLD", "standard", "draft",
                LocalDate.of(2026, 2, 9), 30, false, "Worldwide excl. US", "Cigna Global", "Marine Standard");
        addItem(o12, "gold", "Gold Plan", 800, 600, 24.0, 42.0, false);
        o12.recalculateTotals();
        offerRepository.save(o12);

`;

const before = lines.slice(0, seedLine + 1);
const after = lines.slice(summaryLine);

const result = [...before, ...newOffers.split('\n'), ...after];
fs.writeFileSync(f, result.join('\n'), 'utf8');

// Also need to add contactName and contactEmail to Offer model if missing
const modelFile = 'C:\\Users\\akage\\Downloads\\polaris-api\\src\\main\\java\\com\\polaris\\api\\model\\Offer.java';
let model = fs.readFileSync(modelFile, 'utf8');

if (!model.includes('contactName')) {
  // Add contactName and contactEmail fields
  model = model.replace(
    'private String notes;',
    'private String notes;\n\n    @Column(name = "contact_name", length = 100)\n    private String contactName;\n\n    @Column(name = "contact_email", length = 100)\n    private String contactEmail;'
  );

  // Add getters/setters before the last closing brace
  const lastBrace = model.lastIndexOf('}');
  const gettersSetters = `
    public String getContactName() { return contactName; }
    public void setContactName(String contactName) { this.contactName = contactName; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

`;
  model = model.substring(0, lastBrace) + gettersSetters + model.substring(lastBrace);

  fs.writeFileSync(modelFile, model, 'utf8');
  console.log('[OK] Added contactName/contactEmail to Offer model');
}

// Update summary line count
let finalContent = fs.readFileSync(f, 'utf8');
finalContent = finalContent.replace(
  /Offers seeded successfully!.*/,
  'Offers seeded successfully! Total: 12 real production offers");'
);
fs.writeFileSync(f, finalContent, 'utf8');

console.log('[OK] OfferDataInitializer updated with 12 real offers');
console.log('Now restart Java API to load new data');
