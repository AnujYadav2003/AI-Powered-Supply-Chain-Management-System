package com.supplychain.procurement_service.repository;

import com.supplychain.procurement_service.entity.PurchaseOrder;
import com.supplychain.procurement_service.entity.PurchaseOrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PurchaseOrderRepository
        extends JpaRepository<PurchaseOrder, UUID> {

    boolean existsByPurchaseOrderNumber(String purchaseOrderNumber);

    Page<PurchaseOrder> findBySupplierId(
            UUID supplierId,
            Pageable pageable
    );

    Page<PurchaseOrder> findByStatus(
            PurchaseOrderStatus status,
            Pageable pageable
    );
}