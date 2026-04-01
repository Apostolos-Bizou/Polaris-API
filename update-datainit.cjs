const fs = require('fs');
const f = 'C:\\Users\\akage\\Downloads\\polaris-api\\src\\main\\java\\com\\polaris\\api\\config\\DataInitializer.java';
let c = fs.readFileSync(f, 'utf8');

const oldData = `String[][] clientData = {
            {"CLI-001", "OLYMPIC MARITIME", "parent", null, "Greece", "Shipping"},
            {"CLI-002", "OLYMPIC CARRIER", "subsidiary", "CLI-001", "Greece", "Shipping"},
            {"CLI-003", "OLYMPIC DIGNITY", "subsidiary", "CLI-001", "Greece", "Shipping"},
            {"CLI-004", "CENTROFIN", "parent", null, "Greece", "Shipping"},
            {"CLI-005", "CENTROFIN MANAGEMENT", "subsidiary", "CLI-004", "Greece", "Shipping"},
            {"CLI-006", "TSAKOS ENERGY", "parent", null, "Greece", "Shipping"},
            {"CLI-007", "TSAKOS COLUMBIA", "subsidiary", "CLI-006", "Greece", "Shipping"},
            {"CLI-008", "MINERVA MARINE", "parent", null, "Greece", "Shipping"},
            {"CLI-009", "MINERVA DRY", "subsidiary", "CLI-008", "Greece", "Shipping"},
            {"CLI-010", "THENAMARIS", "parent", null, "Greece", "Shipping"},
            {"CLI-011", "ARCADIA SHIPMANAGEMENT", "parent", null, "Greece", "Shipping"},
            {"CLI-012", "ANGELICOUSSIS GROUP", "parent", null, "Greece", "Shipping"},
            {"CLI-013", "MARAN TANKERS", "subsidiary", "CLI-012", "Greece", "Shipping"},
            {"CLI-014", "MARAN GAS", "subsidiary", "CLI-012", "Greece", "Shipping"},
            {"CLI-015", "ANANGEL", "subsidiary", "CLI-012", "Greece", "Shipping"},
            {"CLI-016", "NAVIOS GROUP", "parent", null, "Greece", "Shipping"},
            {"CLI-017", "NAVIOS MARITIME", "subsidiary", "CLI-016", "Greece", "Shipping"},
            {"CLI-018", "DYNACOM TANKERS", "parent", null, "Greece", "Shipping"},
            {"CLI-019", "SEA TRADERS", "parent", null, "Greece", "Shipping"},
            {"CLI-020", "DANAOS CORPORATION", "parent", null, "Greece", "Shipping"},
            {"CLI-021", "COSTAMARE", "parent", null, "Greece", "Shipping"},
            {"CLI-022", "STAR BULK CARRIERS", "parent", null, "Greece", "Shipping"},
            {"CLI-023", "EUROSEAS", "parent", null, "Greece", "Shipping"},
            {"CLI-024", "SEANERGY MARITIME", "parent", null, "Greece", "Shipping"},
            {"CLI-025", "PERFORMANCE SHIPPING", "parent", null, "Greece", "Shipping"},
            {"CLI-026", "CAPITAL PRODUCT PARTNERS", "parent", null, "Greece", "Shipping"},
            {"CLI-027", "GASLOG", "parent", null, "Greece", "Shipping"},
            {"CLI-028", "DORIAN LPG", "parent", null, "USA", "Shipping"},
            {"CLI-029", "SAFE BULKERS", "parent", null, "Greece", "Shipping"},
            {"CLI-030", "GENCO SHIPPING", "parent", null, "USA", "Shipping"},`;

const newData = `String[][] clientData = {
            // DIANA SHIPPING SERVICES SA (4 entities)
            {"CLI-2026-0001", "DIANA SHIPPING SERVICES SA", "parent", null, "Greece", "Maritime"},
            {"CLI-2026-0002", "DIANA GOLD", "subsidiary", "CLI-2026-0001", "Greece", "Maritime"},
            {"CLI-2026-0003", "DIANA PLATINUM", "subsidiary", "CLI-2026-0001", "Greece", "Maritime"},
            {"CLI-2026-0004", "DIANA MARINERS INC", "subsidiary", "CLI-2026-0001", "Greece", "Maritime"},
            // CENTROFIN (10 entities)
            {"CLI-2026-0005", "CENTROFIN", "parent", null, "Greece", "Maritime"},
            {"CLI-2026-0006", "CENTROFIN GOLD", "subsidiary", "CLI-2026-0005", "Greece", "Maritime"},
            {"CLI-2026-0007", "CENTROFIN PLATINUM", "subsidiary", "CLI-2026-0005", "Greece", "Maritime"},
            {"CLI-2026-0008", "CENTROFIN SILVER", "subsidiary", "CLI-2026-0005", "Greece", "Maritime"},
            {"CLI-2026-0009", "CENTROFIN MARINE TRUST GOLD", "subsidiary", "CLI-2026-0005", "Greece", "Maritime"},
            {"CLI-2026-0010", "CENTROFIN MARINE TRUST PLATINUN", "subsidiary", "CLI-2026-0005", "Greece", "Maritime"},
            {"CLI-2026-0011", "CENTROFIN MARINE TRUST SILVER", "subsidiary", "CLI-2026-0005", "Greece", "Maritime"},
            {"CLI-2026-0012", "CENTROFIN TRUST BULKERS GOLD", "subsidiary", "CLI-2026-0005", "Greece", "Maritime"},
            {"CLI-2026-0013", "CENTROFIN TRUST BULKERS PLATINUN", "subsidiary", "CLI-2026-0005", "Greece", "Maritime"},
            {"CLI-2026-0014", "CENTROFIN TRUST BULKERS SILVER", "subsidiary", "CLI-2026-0005", "Greece", "Maritime"},
            // ASTRA SHIPMANAGEMENT INC (3 entities)
            {"CLI-2026-0015", "ASTRA SHIPMANAGEMENT INC", "parent", null, "Greece", "Maritime"},
            {"CLI-2026-0016", "ASTRA SHIPMANAGEMENT GOLD", "subsidiary", "CLI-2026-0015", "Greece", "Maritime"},
            {"CLI-2026-0017", "ASTRA TANKERS GOLD", "subsidiary", "CLI-2026-0015", "Greece", "Maritime"},
            // AIMS ADELE SHIPHOLDING LTD (2 entities)
            {"CLI-2026-0018", "AIMS ADELE SHIPHOLDING LTD", "parent", null, "Greece", "Maritime"},
            {"CLI-2026-0019", "AIMS GOLD", "subsidiary", "CLI-2026-0018", "Greece", "Maritime"},
            // ANOSIS MARITIME SA (2 entities)
            {"CLI-2026-0020", "ANOSIS MARITIME SA", "parent", null, "Greece", "Maritime"},
            {"CLI-2026-0021", "ANOSIS PLATINUM", "subsidiary", "CLI-2026-0020", "Greece", "Maritime"},
            // CROSSWORLD MARINE SERVICES INC (16 entities)
            {"CLI-2026-0022", "CROSSWORLD MARINE SERVICES INC", "parent", null, "Philippines", "Maritime"},
            {"CLI-2026-0023", "CROSSWORLD STAF&FAM GOLD", "subsidiary", "CLI-2026-0022", "Philippines", "Maritime"},
            {"CLI-2026-0024", "CROSSWORLD STAF&FAM PLATINUN", "subsidiary", "CLI-2026-0022", "Philippines", "Maritime"},
            {"CLI-2026-0025", "BOURBON GOLD CW", "subsidiary", "CLI-2026-0022", "Philippines", "Maritime"},
            {"CLI-2026-0026", "BOURBON PLATINUN CW", "subsidiary", "CLI-2026-0022", "Philippines", "Maritime"},
            {"CLI-2026-0027", "CASSIOPEIA GOLD CW", "subsidiary", "CLI-2026-0022", "Philippines", "Maritime"},
            {"CLI-2026-0028", "CASSIOPEIA PLATINUM CW", "subsidiary", "CLI-2026-0022", "Philippines", "Maritime"},
            {"CLI-2026-0029", "PIONEER CW", "subsidiary", "CLI-2026-0022", "Philippines", "Maritime"},
            {"CLI-2026-0030", "VERITAS GOLD CW", "subsidiary", "CLI-2026-0022", "Philippines", "Maritime"},
            {"CLI-2026-0031", "VARSHIP GOLD CW", "subsidiary", "CLI-2026-0022", "Philippines", "Maritime"},
            {"CLI-2026-0032", "POLARIS GOLD CW", "subsidiary", "CLI-2026-0022", "Philippines", "Maritime"},
            {"CLI-2026-0033", "POLARIS PLATINUM CW", "subsidiary", "CLI-2026-0022", "Philippines", "Maritime"},
            {"CLI-2026-0034", "CROSSWORLD KADETS", "subsidiary", "CLI-2026-0022", "Philippines", "Maritime"},
            {"CLI-2026-0035", "CROSSWORLD PROMO GOLD", "subsidiary", "CLI-2026-0022", "Philippines", "Maritime"},
            {"CLI-2026-0036", "CROSSWORLD SPECIAL PROGRAM", "subsidiary", "CLI-2026-0022", "Philippines", "Maritime"},
            {"CLI-2026-0037", "UNION MARINE GOLD CW", "subsidiary", "CLI-2026-0022", "Philippines", "Maritime"},
            // EFNAV COMPANY LIMITED (2 entities)
            {"CLI-2026-0038", "EFNAV COMPANY LIMITED", "parent", null, "Greece", "Maritime"},
            {"CLI-2026-0039", "EFNAV GOLD", "subsidiary", "CLI-2026-0038", "Greece", "Maritime"},
            // GOLDEN UNION SHIPPING CO SA (5 entities)
            {"CLI-2026-0040", "GOLDEN UNION SHIPPING CO SA", "parent", null, "Greece", "Maritime"},
            {"CLI-2026-0041", "GOLDEN UNION SHIPPING GOLD", "subsidiary", "CLI-2026-0040", "Greece", "Maritime"},
            {"CLI-2026-0042", "GOLDEN UNION SHIPPING PLATINUN", "subsidiary", "CLI-2026-0040", "Greece", "Maritime"},
            {"CLI-2026-0043", "GOLDEN UNION WORLDWIDE GOLD", "subsidiary", "CLI-2026-0040", "Greece", "Maritime"},
            {"CLI-2026-0044", "GOLDEN UNION WORLDWIDE PLATINUM", "subsidiary", "CLI-2026-0040", "Greece", "Maritime"},
            // HEALTHPLUS DIAGNOSTIC CLINIC INC (2 entities)
            {"CLI-2026-0045", "HEALTHPLUS DIAGNOSTIC CLINIC INC", "parent", null, "Philippines", "Healthcare"},
            {"CLI-2026-0046", "HEALTHPLUS GOLD", "subsidiary", "CLI-2026-0045", "Philippines", "Healthcare"},
            // MINOA MARINE LIMITED (2 entities)
            {"CLI-2026-0047", "MINOA MARINE LIMITED", "parent", null, "Greece", "Maritime"},
            {"CLI-2026-0048", "MINOA MARINE GOLD", "subsidiary", "CLI-2026-0047", "Greece", "Maritime"},
            // KYKLADES MARITIME CORPORATION (2 entities)
            {"CLI-2026-0049", "KYKLADES MARITIME CORPORATION", "parent", null, "Greece", "Maritime"},
            {"CLI-2026-0050", "KYKLADES PLATINUM", "subsidiary", "CLI-2026-0049", "Greece", "Maritime"},
            // OMICRON SHIP MANAGEMENT INC (2 entities)
            {"CLI-2026-0051", "OMICRON SHIP MANAGEMENT INC", "parent", null, "Greece", "Maritime"},
            {"CLI-2026-0052", "OMICRON GOLD", "subsidiary", "CLI-2026-0051", "Greece", "Maritime"},
            // LEADER MARINE (8 entities)
            {"CLI-2026-0053", "LEADER MARINE", "parent", null, "Greece", "Maritime"},
            {"CLI-2026-0054", "LEADER MARINE AQUILA BULKERS INC GOLD", "subsidiary", "CLI-2026-0053", "Greece", "Maritime"},
            {"CLI-2026-0055", "LEADER MARINE FALCON SHIPHOLDING INC GOLD", "subsidiary", "CLI-2026-0053", "Greece", "Maritime"},
            {"CLI-2026-0056", "LEADER MARINE GOLD", "subsidiary", "CLI-2026-0053", "Greece", "Maritime"},
            {"CLI-2026-0057", "LEADER MARINE INC GOLD", "subsidiary", "CLI-2026-0053", "Greece", "Maritime"},
            {"CLI-2026-0058", "LEADER MARINE NOMADE HOLDING LTD GOLD", "subsidiary", "CLI-2026-0053", "Greece", "Maritime"},
            {"CLI-2026-0059", "LEADER MARINE PEGASUS BULKERS INC GOLD", "subsidiary", "CLI-2026-0053", "Greece", "Maritime"},
            {"CLI-2026-0060", "LEADER MARINE PLATINUM", "subsidiary", "CLI-2026-0053", "Greece", "Maritime"},
            // IONIC (3 entities)
            {"CLI-2026-0061", "IONIC", "parent", null, "Greece", "Maritime"},
            {"CLI-2026-0062", "IONIC GOLD", "subsidiary", "CLI-2026-0061", "Greece", "Maritime"},
            {"CLI-2026-0063", "IONIC PLATINUM", "subsidiary", "CLI-2026-0061", "Greece", "Maritime"},`;

if (c.includes(oldData)) {
  c = c.replace(oldData, newData);
  // Also update comment
  c = c.replace('// === CLIENTS (sample of the 63 real clients) ===', '// === CLIENTS (all 63 production clients) ===');
  // Also need to increase client_id column length from 20 to 30 since CLI-2026-0001 is longer
  fs.writeFileSync(f, c, 'utf8');
  console.log('SUCCESS! DataInitializer.java updated with 63 clients');
} else {
  console.log('Pattern not found - checking...');
  if (c.includes('CLI-001')) console.log('Found CLI-001 format');
  if (c.includes('CLI-030')) console.log('Found CLI-030');
  // Show lines 43-76
  const lines = c.split('\n');
  for (let i = 42; i < 76; i++) console.log((i+1) + ': ' + lines[i]);
}
