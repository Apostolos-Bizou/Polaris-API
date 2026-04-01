const fs = require('fs');
const f = 'C:\\Users\\akage\\Downloads\\polaris-api\\src\\main\\java\\com\\polaris\\api\\config\\DataInitializer.java';
let lines = fs.readFileSync(f, 'utf8').split('\n');

// Find start (line with "String[][] clientData") and end (line with "};" after it)
let startLine = -1, endLine = -1;
for (let i = 0; i < lines.length; i++) {
  if (lines[i].includes('String[][] clientData')) startLine = i;
  if (startLine > 0 && i > startLine && lines[i].trim() === '};') { endLine = i; break; }
}

console.log('Found clientData at lines', startLine+1, 'to', endLine+1);

if (startLine < 0 || endLine < 0) {
  console.log('ERROR: Could not find clientData block');
  process.exit(1);
}

const newBlock = [
  '            String[][] clientData = {',
  '                // DIANA SHIPPING SERVICES SA (4 entities)',
  '                {"CLI-2026-0001", "DIANA SHIPPING SERVICES SA", "parent", null, "Greece", "Maritime"},',
  '                {"CLI-2026-0002", "DIANA GOLD", "subsidiary", "CLI-2026-0001", "Greece", "Maritime"},',
  '                {"CLI-2026-0003", "DIANA PLATINUM", "subsidiary", "CLI-2026-0001", "Greece", "Maritime"},',
  '                {"CLI-2026-0004", "DIANA MARINERS INC", "subsidiary", "CLI-2026-0001", "Greece", "Maritime"},',
  '                // CENTROFIN (10 entities)',
  '                {"CLI-2026-0005", "CENTROFIN", "parent", null, "Greece", "Maritime"},',
  '                {"CLI-2026-0006", "CENTROFIN GOLD", "subsidiary", "CLI-2026-0005", "Greece", "Maritime"},',
  '                {"CLI-2026-0007", "CENTROFIN PLATINUM", "subsidiary", "CLI-2026-0005", "Greece", "Maritime"},',
  '                {"CLI-2026-0008", "CENTROFIN SILVER", "subsidiary", "CLI-2026-0005", "Greece", "Maritime"},',
  '                {"CLI-2026-0009", "CENTROFIN MARINE TRUST GOLD", "subsidiary", "CLI-2026-0005", "Greece", "Maritime"},',
  '                {"CLI-2026-0010", "CENTROFIN MARINE TRUST PLATINUN", "subsidiary", "CLI-2026-0005", "Greece", "Maritime"},',
  '                {"CLI-2026-0011", "CENTROFIN MARINE TRUST SILVER", "subsidiary", "CLI-2026-0005", "Greece", "Maritime"},',
  '                {"CLI-2026-0012", "CENTROFIN TRUST BULKERS GOLD", "subsidiary", "CLI-2026-0005", "Greece", "Maritime"},',
  '                {"CLI-2026-0013", "CENTROFIN TRUST BULKERS PLATINUN", "subsidiary", "CLI-2026-0005", "Greece", "Maritime"},',
  '                {"CLI-2026-0014", "CENTROFIN TRUST BULKERS SILVER", "subsidiary", "CLI-2026-0005", "Greece", "Maritime"},',
  '                // ASTRA SHIPMANAGEMENT INC (3 entities)',
  '                {"CLI-2026-0015", "ASTRA SHIPMANAGEMENT INC", "parent", null, "Greece", "Maritime"},',
  '                {"CLI-2026-0016", "ASTRA SHIPMANAGEMENT GOLD", "subsidiary", "CLI-2026-0015", "Greece", "Maritime"},',
  '                {"CLI-2026-0017", "ASTRA TANKERS GOLD", "subsidiary", "CLI-2026-0015", "Greece", "Maritime"},',
  '                // AIMS ADELE SHIPHOLDING LTD (2 entities)',
  '                {"CLI-2026-0018", "AIMS ADELE SHIPHOLDING LTD", "parent", null, "Greece", "Maritime"},',
  '                {"CLI-2026-0019", "AIMS GOLD", "subsidiary", "CLI-2026-0018", "Greece", "Maritime"},',
  '                // ANOSIS MARITIME SA (2 entities)',
  '                {"CLI-2026-0020", "ANOSIS MARITIME SA", "parent", null, "Greece", "Maritime"},',
  '                {"CLI-2026-0021", "ANOSIS PLATINUM", "subsidiary", "CLI-2026-0020", "Greece", "Maritime"},',
  '                // CROSSWORLD MARINE SERVICES INC (16 entities)',
  '                {"CLI-2026-0022", "CROSSWORLD MARINE SERVICES INC", "parent", null, "Philippines", "Maritime"},',
  '                {"CLI-2026-0023", "CROSSWORLD STAF&FAM GOLD", "subsidiary", "CLI-2026-0022", "Philippines", "Maritime"},',
  '                {"CLI-2026-0024", "CROSSWORLD STAF&FAM PLATINUN", "subsidiary", "CLI-2026-0022", "Philippines", "Maritime"},',
  '                {"CLI-2026-0025", "BOURBON GOLD CW", "subsidiary", "CLI-2026-0022", "Philippines", "Maritime"},',
  '                {"CLI-2026-0026", "BOURBON PLATINUN CW", "subsidiary", "CLI-2026-0022", "Philippines", "Maritime"},',
  '                {"CLI-2026-0027", "CASSIOPEIA GOLD CW", "subsidiary", "CLI-2026-0022", "Philippines", "Maritime"},',
  '                {"CLI-2026-0028", "CASSIOPEIA PLATINUM CW", "subsidiary", "CLI-2026-0022", "Philippines", "Maritime"},',
  '                {"CLI-2026-0029", "PIONEER CW", "subsidiary", "CLI-2026-0022", "Philippines", "Maritime"},',
  '                {"CLI-2026-0030", "VERITAS GOLD CW", "subsidiary", "CLI-2026-0022", "Philippines", "Maritime"},',
  '                {"CLI-2026-0031", "VARSHIP GOLD CW", "subsidiary", "CLI-2026-0022", "Philippines", "Maritime"},',
  '                {"CLI-2026-0032", "POLARIS GOLD CW", "subsidiary", "CLI-2026-0022", "Philippines", "Maritime"},',
  '                {"CLI-2026-0033", "POLARIS PLATINUM CW", "subsidiary", "CLI-2026-0022", "Philippines", "Maritime"},',
  '                {"CLI-2026-0034", "CROSSWORLD KADETS", "subsidiary", "CLI-2026-0022", "Philippines", "Maritime"},',
  '                {"CLI-2026-0035", "CROSSWORLD PROMO GOLD", "subsidiary", "CLI-2026-0022", "Philippines", "Maritime"},',
  '                {"CLI-2026-0036", "CROSSWORLD SPECIAL PROGRAM", "subsidiary", "CLI-2026-0022", "Philippines", "Maritime"},',
  '                {"CLI-2026-0037", "UNION MARINE GOLD CW", "subsidiary", "CLI-2026-0022", "Philippines", "Maritime"},',
  '                // EFNAV COMPANY LIMITED (2 entities)',
  '                {"CLI-2026-0038", "EFNAV COMPANY LIMITED", "parent", null, "Greece", "Maritime"},',
  '                {"CLI-2026-0039", "EFNAV GOLD", "subsidiary", "CLI-2026-0038", "Greece", "Maritime"},',
  '                // GOLDEN UNION SHIPPING CO SA (5 entities)',
  '                {"CLI-2026-0040", "GOLDEN UNION SHIPPING CO SA", "parent", null, "Greece", "Maritime"},',
  '                {"CLI-2026-0041", "GOLDEN UNION SHIPPING GOLD", "subsidiary", "CLI-2026-0040", "Greece", "Maritime"},',
  '                {"CLI-2026-0042", "GOLDEN UNION SHIPPING PLATINUN", "subsidiary", "CLI-2026-0040", "Greece", "Maritime"},',
  '                {"CLI-2026-0043", "GOLDEN UNION WORLDWIDE GOLD", "subsidiary", "CLI-2026-0040", "Greece", "Maritime"},',
  '                {"CLI-2026-0044", "GOLDEN UNION WORLDWIDE PLATINUM", "subsidiary", "CLI-2026-0040", "Greece", "Maritime"},',
  '                // HEALTHPLUS DIAGNOSTIC CLINIC INC (2 entities)',
  '                {"CLI-2026-0045", "HEALTHPLUS DIAGNOSTIC CLINIC INC", "parent", null, "Philippines", "Healthcare"},',
  '                {"CLI-2026-0046", "HEALTHPLUS GOLD", "subsidiary", "CLI-2026-0045", "Philippines", "Healthcare"},',
  '                // MINOA MARINE LIMITED (2 entities)',
  '                {"CLI-2026-0047", "MINOA MARINE LIMITED", "parent", null, "Greece", "Maritime"},',
  '                {"CLI-2026-0048", "MINOA MARINE GOLD", "subsidiary", "CLI-2026-0047", "Greece", "Maritime"},',
  '                // KYKLADES MARITIME CORPORATION (2 entities)',
  '                {"CLI-2026-0049", "KYKLADES MARITIME CORPORATION", "parent", null, "Greece", "Maritime"},',
  '                {"CLI-2026-0050", "KYKLADES PLATINUM", "subsidiary", "CLI-2026-0049", "Greece", "Maritime"},',
  '                // OMICRON SHIP MANAGEMENT INC (2 entities)',
  '                {"CLI-2026-0051", "OMICRON SHIP MANAGEMENT INC", "parent", null, "Greece", "Maritime"},',
  '                {"CLI-2026-0052", "OMICRON GOLD", "subsidiary", "CLI-2026-0051", "Greece", "Maritime"},',
  '                // LEADER MARINE (8 entities)',
  '                {"CLI-2026-0053", "LEADER MARINE", "parent", null, "Greece", "Maritime"},',
  '                {"CLI-2026-0054", "LEADER MARINE AQUILA BULKERS INC GOLD", "subsidiary", "CLI-2026-0053", "Greece", "Maritime"},',
  '                {"CLI-2026-0055", "LEADER MARINE FALCON SHIPHOLDING INC GOLD", "subsidiary", "CLI-2026-0053", "Greece", "Maritime"},',
  '                {"CLI-2026-0056", "LEADER MARINE GOLD", "subsidiary", "CLI-2026-0053", "Greece", "Maritime"},',
  '                {"CLI-2026-0057", "LEADER MARINE INC GOLD", "subsidiary", "CLI-2026-0053", "Greece", "Maritime"},',
  '                {"CLI-2026-0058", "LEADER MARINE NOMADE HOLDING LTD GOLD", "subsidiary", "CLI-2026-0053", "Greece", "Maritime"},',
  '                {"CLI-2026-0059", "LEADER MARINE PEGASUS BULKERS INC GOLD", "subsidiary", "CLI-2026-0053", "Greece", "Maritime"},',
  '                {"CLI-2026-0060", "LEADER MARINE PLATINUM", "subsidiary", "CLI-2026-0053", "Greece", "Maritime"},',
  '                // IONIC (3 entities)',
  '                {"CLI-2026-0061", "IONIC", "parent", null, "Greece", "Maritime"},',
  '                {"CLI-2026-0062", "IONIC GOLD", "subsidiary", "CLI-2026-0061", "Greece", "Maritime"},',
  '                {"CLI-2026-0063", "IONIC PLATINUM", "subsidiary", "CLI-2026-0061", "Greece", "Maritime"},',
  '            };',
];

// Replace lines startLine to endLine with newBlock
const before = lines.slice(0, startLine);
const after = lines.slice(endLine + 1);
const result = [...before, ...newBlock, ...after];

// Also fix client_id column length (20 -> 50) in Client.java model
const modelFile = 'C:\\Users\\akage\\Downloads\\polaris-api\\src\\main\\java\\com\\polaris\\api\\model\\Client.java';
let model = fs.readFileSync(modelFile, 'utf8');
if (model.includes('length = 20')) {
  model = model.replace(/name = "client_id".*?length = 20/s, 'name = "client_id", unique = true, nullable = false, length = 50');
  // Also fix parent_id length
  model = model.replace(/name = "parent_id".*?length = 20/s, 'name = "parent_id", length = 50');
  fs.writeFileSync(modelFile, model, 'utf8');
  console.log('Client.java: client_id and parent_id column length -> 50');
}

fs.writeFileSync(f, result.join('\n'), 'utf8');
console.log('DataInitializer.java: 30 clients -> 63 clients');
console.log('New total lines:', result.length);
console.log('\nNow restart Java API: Stop (Ctrl+C) then: mvn spring-boot:run');
