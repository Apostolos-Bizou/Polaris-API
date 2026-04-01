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
                {"CLI-2026-0063", "IONIC PLATINUM", "subsidiary", "CLI-2026-0061", "Greece", "Maritime"},
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
