const fs = require('fs');
const f = 'C:\\Users\\akage\\Downloads\\polaris-api\\src\\main\\java\\com\\polaris\\api\\config\\OfferDataInitializer.java';
let lines = fs.readFileSync(f, 'utf8').split('\n');

// Find and replace lines 130-138 (the broken summary block)
for (let i = 0; i < lines.length; i++) {
  if (lines[i].includes('Offers seeded successfully') || 
      lines[i].includes('countByStatus')) {
    console.log('Found at', i+1, ':', lines[i].trim());
  }
}

// Replace the multi-line println with simple one
let content = lines.join('\n');
// Find from "System.out.println("Offers seeded" to the closing ");"
const startMatch = content.indexOf('System.out.println("Offers seeded');
if (startMatch > 0) {
  // Find the closing ");  after it
  const endMatch = content.indexOf('");', startMatch);
  if (endMatch > 0) {
    const oldBlock = content.substring(startMatch, endMatch + 3);
    console.log('\nReplacing block:\n' + oldBlock.substring(0, 200));
    content = content.replace(oldBlock, 'System.out.println("Offers seeded successfully! Total: " + offerRepository.count() + " real production offers");');
    fs.writeFileSync(f, content, 'utf8');
    console.log('\nFIXED! Simple summary line');
  }
} else {
  // Try alternate format
  const alt = content.indexOf('Offers seeded successfully! Total: 12');
  if (alt > 0) {
    console.log('Already fixed');
  } else {
    console.log('Could not find summary line');
    // Show lines 128-145
    for (let i = 127; i < 145; i++) {
      console.log((i+1) + ': ' + lines[i]);
    }
  }
}
