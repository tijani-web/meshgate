package com.meshgate.billing_service.service;

import com.meshgate.billing_service.dto.UserRegisteredEvent;
import com.meshgate.billing_service.entity.Invoice;
import com.meshgate.billing_service.entity.Subscription;
import com.meshgate.billing_service.repository.InvoiceRepository;
import com.meshgate.billing_service.repository.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class BillingService {

    private final SubscriptionRepository subscriptionRepository;
    private final InvoiceRepository invoiceRepository;

    public BillingService(SubscriptionRepository subscriptionRepository, InvoiceRepository invoiceRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.invoiceRepository = invoiceRepository;
    }

    public void processUserRegisteredEvent(UserRegisteredEvent event) {
        Optional<Subscription> existing = subscriptionRepository.findByUserId(event.getAuthId());
        if (existing.isEmpty()) {
            Subscription subscription = new Subscription();
            subscription.setUserId(event.getAuthId());
            subscription.setTier("FREE");
            subscription.setStatus("ACTIVE");
            subscription.setStartDate(LocalDateTime.now());
            subscriptionRepository.save(subscription);
            System.out.println("Created FREE subscription for user: " + event.getAuthId());
        }
    }

    public Subscription getSubscription(UUID userId) {
        return subscriptionRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Subscription not found for user: " + userId));
    }

    public Subscription upgradeSubscription(UUID userId, String tier) {
        Subscription subscription = getSubscription(userId);
        subscription.setTier(tier);
        return subscriptionRepository.save(subscription);
    }

    public Invoice generateInvoice(UUID userId, BigDecimal amount) {
        Invoice invoice = new Invoice();
        invoice.setUserId(userId);
        invoice.setAmount(amount);
        invoice.setStatus("PENDING");
        invoice.setDueDate(LocalDateTime.now().plusDays(30));
        return invoiceRepository.save(invoice);
    }
}
