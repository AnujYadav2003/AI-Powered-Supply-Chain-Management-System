package com.supplychain.procurement_service.service;

import com.supplychain.procurement_service.dto.PurchaseOrderRequest;
import com.supplychain.procurement_service.dto.PurchaseOrderResponse;
import com.supplychain.procurement_service.entity.PurchaseOrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PurchaseOrderService {

    PurchaseOrderResponse createPurchaseOrder(
            PurchaseOrderRequest request
    );

    PurchaseOrderResponse getPurchaseOrderById(
            UUID purchaseOrderId
    );

    Page<PurchaseOrderResponse> getAllPurchaseOrders(
            Pageable pageable
    );

    Page<PurchaseOrderResponse> getPurchaseOrdersBySupplier(
            UUID supplierId,
            Pageable pageable
    );

    Page<PurchaseOrderResponse> getPurchaseOrdersByStatus(
            PurchaseOrderStatus status,
            Pageable pageable
    );

    PurchaseOrderResponse updateStatus(
            UUID purchaseOrderId,
            PurchaseOrderStatus status
    );

    void cancelPurchaseOrder(UUID purchaseOrderId);
}