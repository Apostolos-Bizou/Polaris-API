package com.polaris.api.config;

import com.polaris.api.model.Claim;
import com.polaris.api.model.Client;
import com.polaris.api.model.Member;
import com.polaris.api.model.User;
import com.polaris.api.repository.ClaimRepository;
import com.polaris.api.repository.ClientRepository;
import com.polaris.api.repository.MemberRepository;
import com.polaris.api.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Random;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepo,
                                    ClientRepository clientRepo,
                                    MemberRepository memberRepo,
                                    ClaimRepository claimRepo,
                                    PasswordEncoder encoder) {
        return args -> {
            // Only seed if database is empty
            if (userRepo.count() > 0) {
                System.out.println("Database already seeded. Skipping initialization.");
                return;
            }

            System.out.println("Seeding database with Polaris data...");

            // === USERS ===
            userRepo.save(new User("tolis_admin", encoder.encode("admin123"), "admin"));
            userRepo.save(new User("nik_admin", encoder.encode("admin123"), "admin"));
            userRepo.save(new User("tolis", encoder.encode("admin123"), "client"));
            userRepo.save(new User("apostolos", encoder.encode("5404775"), "client"));

            // === CLIENTS (sample of the 63 real clients) ===
            String[][] clientData = {
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
                {"CLI-030", "GENCO SHIPPING", "parent", null, "USA", "Shipping"},
            };

            Random rand = new Random(42);
            String[] plans = {"Gold", "Silver", "Platinum", "Diamond"};
            String[] categories = {"Hospitalization", "Surgery", "Outpatient Visit", "Dental", "Maternity", "Pharmacy", "Diagnostics", "Emergency"};
            String[] hospitals = {"Athens Medical Center", "Hygeia Hospital", "Metropolitan Hospital", "Evangelismos", "AHEPA Thessaloniki", "Mediterranean Hospital", "Iatrikó Kentro", "Creta InterClinic", "Dubai Hospital", "Singapore General"};
            String[] countries = {"Greece", "Cyprus", "UAE", "Singapore", "USA", "UK", "Germany"};

            for (String[] cd : clientData) {
                Client client = new Client(cd[0], cd[1], cd[2]);
                client.setParentId(cd[3]);
                client.setCountry(cd[4]);
                client.setSector(cd[5]);
                client.setStatus("active");

                int memberCount = 20 + rand.nextInt(180);
                int claimCount = 10 + rand.nextInt(90);
                client.setTotalMembers(memberCount);
                client.setTotalClaims(claimCount);
                client.setTotalPremium(50000.0 + rand.nextInt(450000));
                client.setContactEmail(cd[1].toLowerCase().replace(" ", ".") + "@polaris.com");
                clientRepo.save(client);

                // Create members for this client
                for (int m = 0; m < memberCount; m++) {
                    Member member = new Member();
                    member.setMemberId(cd[0] + "-M" + String.format("%04d", m + 1));
                    member.setClientId(cd[0]);
                    member.setFullName("Member " + (m + 1) + " of " + cd[1]);
                    member.setMemberType(m < memberCount * 0.6 ? "principal" : "dependent");
                    member.setPlan(plans[rand.nextInt(plans.length)]);
                    member.setStatus(rand.nextDouble() > 0.1 ? "active" : "cancelled");
                    member.setEnrollmentDate(LocalDate.of(2024, 1 + rand.nextInt(12), 1 + rand.nextInt(28)));
                    member.setGender(rand.nextBoolean() ? "Male" : "Female");
                    member.setNationality(countries[rand.nextInt(countries.length)]);
                    if ("cancelled".equals(member.getStatus())) {
                        member.setCancellationDate(LocalDate.of(2025, 1 + rand.nextInt(12), 1 + rand.nextInt(28)));
                    }
                    memberRepo.save(member);
                }

                // Create claims for this client
                for (int c = 0; c < claimCount; c++) {
                    Claim claim = new Claim();
                    claim.setClaimId(cd[0] + "-C" + String.format("%04d", c + 1));
                    claim.setClientId(cd[0]);
                    claim.setMemberId(cd[0] + "-M" + String.format("%04d", 1 + rand.nextInt(memberCount)));
                    claim.setCategory(categories[rand.nextInt(categories.length)]);

                    double r = rand.nextDouble();
                    claim.setClaimType(r < 0.4 ? "inpatient" : r < 0.85 ? "outpatient" : "ex_gratia");

                    claim.setAmountUsd(200.0 + rand.nextInt(49800));
                    claim.setStatus(rand.nextDouble() > 0.1 ? "approved" : "pending");
                    claim.setHospital(hospitals[rand.nextInt(hospitals.length)]);
                    claim.setHospitalCountry(countries[rand.nextInt(countries.length)]);
                    claim.setClaimDate(LocalDate.of(2025, 1 + rand.nextInt(12), 1 + rand.nextInt(28)));
                    claim.setMemberType(rand.nextDouble() < 0.6 ? "principal" : "dependent");
                    claim.setPlan(plans[rand.nextInt(plans.length)]);
                    claimRepo.save(claim);
                }
            }

            System.out.println("Database seeded successfully!");
            System.out.println("  Users: " + userRepo.count());
            System.out.println("  Clients: " + clientRepo.count());
            System.out.println("  Members: " + memberRepo.count());
            System.out.println("  Claims: " + claimRepo.count());
        };
    }
}
