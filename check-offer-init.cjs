const fs = require('fs');
const f = 'C:\\Users\\akage\\Downloads\\polaris-api\\src\\main\\java\\com\\polaris\\api\\config\\OfferDataInitializer.java';
let c = fs.readFileSync(f, 'utf8');
let lines = c.split('\n');

console.log('Total lines:', lines.length);

// Find where offers are seeded
let seedStart = -1, seedEnd = -1;
for (let i = 0; i < lines.length; i++) {
  if (lines[i].includes('Seeding offers data') || lines[i].includes('offerRepo.count()') || lines[i].includes('offers already seeded')) {
    console.log('Found at line', i+1, ':', lines[i].trim());
  }
}

// Strategy: Find the run() method and replace the offer creation block
// Let's find the class structure first
for (let i = 0; i < lines.length; i++) {
  if (lines[i].includes('offer.set') || lines[i].includes('new Offer') || lines[i].includes('Offer offer')) {
    if (seedStart < 0) seedStart = i;
    seedEnd = i;
  }
}

console.log('Offer creation block: lines', seedStart+1, 'to', seedEnd+1);

// Show some context
for (let i = Math.max(0, seedStart-5); i < Math.min(lines.length, seedStart+10); i++) {
  console.log((i+1) + ': ' + lines[i]);
}

// Now let's find the pattern more precisely
let runStart = -1, saveStart = -1;
for (let i = 0; i < lines.length; i++) {
  if (lines[i].includes('System.out.println("Seeding offers data')) runStart = i;
  if (lines[i].includes('offerRepo.save(offer)')) saveStart = i;
}

console.log('\nSeed start:', runStart+1);
console.log('Last save:', saveStart+1);

// Let's see the full run method
console.log('\n--- Full file context ---');
for (let i = 0; i < Math.min(lines.length, 20); i++) {
  console.log((i+1) + ': ' + lines[i]);
}
