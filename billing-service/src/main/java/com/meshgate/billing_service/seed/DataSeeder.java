package com.meshgate.billing_service.seed;

import com.meshgate.billing_service.entity.Invoice;
import com.meshgate.billing_service.entity.Subscription;
import com.meshgate.billing_service.repository.InvoiceRepository;
import com.meshgate.billing_service.repository.SubscriptionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class DataSeeder implements CommandLineRunner {

    private final SubscriptionRepository subscriptionRepository;
    private final InvoiceRepository invoiceRepository;

    public DataSeeder(SubscriptionRepository subscriptionRepository, InvoiceRepository invoiceRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public void run(String... args) {
        if (subscriptionRepository.count() > 0) {
            System.out.println("[Seed] Billing data already exists, skipping seed.");
            return;
        }

        System.out.println("[Seed] Seeding billing data...");

        // userId, tier, status
        String[][] subs = {
                { "00000000-0000-0000-0000-000000000001", "ENTERPRISE", "ACTIVE" },
                { "00000000-0000-0000-0000-000000000002", "PRO", "ACTIVE" },
                { "00000000-0000-0000-0000-000000000003", "PRO", "ACTIVE" },
                { "00000000-0000-0000-0000-000000000004", "FREE", "ACTIVE" },
                { "00000000-0000-0000-0000-000000000005", "FREE", "ACTIVE" },
                { "00000000-0000-0000-0000-000000000006", "ENTERPRISE", "ACTIVE" },
                { "00000000-0000-0000-0000-000000000007", "PRO", "ACTIVE" },
                { "00000000-0000-0000-0000-000000000008", "FREE", "ACTIVE" },
                { "00000000-0000-0000-0000-000000000009", "FREE", "ACTIVE" },
                { "00000000-0000-0000-0000-000000000010", "PRO", "ACTIVE" },
        };

        for (int i = 0; i < subs.length; i++) {
            Subscription sub = new Subscription();
            sub.setUserId(UUID.fromString(subs[i][0]));
            sub.setTier(subs[i][1]);
            sub.setStatus(subs[i][2]);
            sub.setStartDate(LocalDateTime.now().minusDays(30 - i));
            subscriptionRepository.save(sub);
        }

        // Generate invoices for paid tiers
        // userId, amount, status, daysAgo
        Object[][] invoices = {
                { "00000000-0000-0000-0000-000000000001", new BigDecimal("99.99"), "PAID", 25 },
                { "00000000-0000-0000-0000-000000000001", new BigDecimal("99.99"), "PENDING", 0 },
                { "00000000-0000-0000-0000-000000000002", new BigDecimal("29.99"), "PAID", 20 },
                { "00000000-0000-0000-0000-000000000002", new BigDecimal("29.99"), "PENDING", 0 },
                { "00000000-0000-0000-0000-000000000003", new BigDecimal("29.99"), "PAID", 18 },
                { "00000000-0000-0000-0000-000000000006", new BigDecimal("99.99"), "PAID", 22 },
                { "00000000-0000-0000-0000-000000000006", new BigDecimal("99.99"), "PAID", 10 },
                { "00000000-0000-0000-0000-000000000006", new BigDecimal("99.99"), "PENDING", 0 },
                { "00000000-0000-0000-0000-000000000007", new BigDecimal("29.99"), "PAID", 15 },
                { "00000000-0000-0000-0000-000000000007", new BigDecimal("29.99"), "OVERDUE", 5 },
                { "00000000-0000-0000-0000-000000000010", new BigDecimal("29.99"), "PAID", 12 },
                { "00000000-0000-0000-0000-000000000010", new BigDecimal("29.99"), "PENDING", 0 },
        };

        for (Object[] inv : invoices) {
            Invoice invoice = new Invoice();
            invoice.setUserId(UUID.fromString((String) inv[0]));
            invoice.setAmount((BigDecimal) inv[1]);
            invoice.setStatus((String) inv[2]);
            int daysAgo = (int) inv[3];
            invoice.setDueDate(LocalDateTime.now().minusDays(daysAgo).plusDays(30));
            if ("PAID".equals(inv[2])) {
                invoice.setPaidAt(LocalDateTime.now().minusDays(daysAgo).plusDays(5));
            }
            invoiceRepository.save(invoice);
        }

        System.out.println("[Seed] Seeded 10 subscriptions and 12 invoices.");
    }
}
