package com.meshgate.billing_service.repository;

import com.meshgate.billing_service.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
    List<Invoice> findByUserId(UUID userId);
}
