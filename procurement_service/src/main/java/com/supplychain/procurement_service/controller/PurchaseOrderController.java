package com.supplychain.procurement_service.controller;

import com.supplychain.procurement_service.dto.PurchaseOrderRequest;
import com.supplychain.procurement_service.dto.PurchaseOrderResponse;
import com.supplychain.procurement_service.entity.PurchaseOrderStatus;
import com.supplychain.procurement_service.service.PurchaseOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PurchaseOrderResponse createPurchaseOrder(
            @Valid @RequestBody PurchaseOrderRequest request
    ) {
        return purchaseOrderService.createPurchaseOrder(request);
    }

    @GetMapping("/{purchaseOrderId}")
    public PurchaseOrderResponse getPurchaseOrderById(
            @PathVariable UUID purchaseOrderId
    ) {
        return purchaseOrderService.getPurchaseOrderById(
                purchaseOrderId
        );
    }

    @GetMapping
    public Page<PurchaseOrderResponse> getAllPurchaseOrders(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return purchaseOrderService.getAllPurchaseOrders(pageable);
    }

    @GetMapping("/supplier/{supplierId}")
    public Page<PurchaseOrderResponse> getBySupplier(
            @PathVariable UUID supplierId,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return purchaseOrderService.getPurchaseOrdersBySupplier(
                supplierId,
                pageable
        );
    }

    @GetMapping("/status/{status}")
    public Page<PurchaseOrderResponse> getByStatus(
            @PathVariable PurchaseOrderStatus status,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return purchaseOrderService.getPurchaseOrdersByStatus(
                status,
                pageable
        );
    }

    @PatchMapping("/{purchaseOrderId}/status")
    public PurchaseOrderResponse updateStatus(
            @PathVariable UUID purchaseOrderId,
            @RequestParam PurchaseOrderStatus status
    ) {
        return purchaseOrderService.updateStatus(
                purchaseOrderId,
                status
        );
    }

    @PatchMapping("/{purchaseOrderId}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelPurchaseOrder(
            @PathVariable UUID purchaseOrderId
    ) {
        purchaseOrderService.cancelPurchaseOrder(
                purchaseOrderId
        );
    }
}