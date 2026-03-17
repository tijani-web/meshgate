package com.meshgate.billing_service.controller;

import com.meshgate.billing_service.entity.Invoice;
import com.meshgate.billing_service.entity.Subscription;
import com.meshgate.billing_service.repository.InvoiceRepository;
import com.meshgate.billing_service.repository.SubscriptionRepository;
import com.meshgate.billing_service.service.BillingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/billing")
public class BillingController {

    private final BillingService billingService;
    private final SubscriptionRepository subscriptionRepository;
    private final InvoiceRepository invoiceRepository;

    public BillingController(BillingService billingService,
            SubscriptionRepository subscriptionRepository,
            InvoiceRepository invoiceRepository) {
        this.billingService = billingService;
        this.subscriptionRepository = subscriptionRepository;
        this.invoiceRepository = invoiceRepository;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "billing-service"));
    }

    @GetMapping("/subscriptions")
    public ResponseEntity<List<Subscription>> getAllSubscriptions() {
        return ResponseEntity.ok(subscriptionRepository.findAll());
    }

    @GetMapping("/subscriptions/{userId}")
    public ResponseEntity<Subscription> getSubscription(@PathVariable UUID userId) {
        return ResponseEntity.ok(billingService.getSubscription(userId));
    }

    @PutMapping("/subscriptions/{userId}/upgrade")
    public ResponseEntity<Subscription> upgradeSubscription(
            @PathVariable UUID userId,
            @RequestParam String tier) {
        return ResponseEntity.ok(billingService.upgradeSubscription(userId, tier));
    }

    @GetMapping("/invoices")
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        return ResponseEntity.ok(invoiceRepository.findAll());
    }

    @GetMapping("/invoices/{userId}")
    public ResponseEntity<List<Invoice>> getInvoicesByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(invoiceRepository.findByUserId(userId));
    }

    @PostMapping("/invoices/{userId}")
    public ResponseEntity<Invoice> generateInvoice(
            @PathVariable UUID userId,
            @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(billingService.generateInvoice(userId, amount));
    }
}
