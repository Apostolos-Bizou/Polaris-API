const fs = require('fs');
const f = 'C:\\Users\\akage\\Downloads\\polaris-api\\src\\main\\java\\com\\polaris\\api\\config\\OfferDataInitializer.java';
let lines = fs.readFileSync(f, 'utf8').split('\n');

// Remove lines 134-138 (index 133-137) - leftover countByStatus lines
lines.splice(133, 5);

fs.writeFileSync(f, lines.join('\n'), 'utf8');
console.log('FIXED - removed 5 leftover lines (134-138)');
console.log('New line 134:', lines[133]);
