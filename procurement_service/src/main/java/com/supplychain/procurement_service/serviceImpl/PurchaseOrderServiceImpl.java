package com.supplychain.procurement_service.serviceImpl;

import com.supplychain.procurement_service.config.ProductClient;
import com.supplychain.procurement_service.config.SupplierClient;
import com.supplychain.procurement_service.dto.*;
import com.supplychain.procurement_service.entity.PurchaseOrder;
import com.supplychain.procurement_service.entity.PurchaseOrderItem;
import com.supplychain.procurement_service.entity.PurchaseOrderStatus;
import com.supplychain.procurement_service.exception.PurchaseOrderNotFoundException;
import com.supplychain.procurement_service.mapper.PurchaseOrderMapper;
import com.supplychain.procurement_service.repository.PurchaseOrderRepository;
import com.supplychain.procurement_service.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PurchaseOrderServiceImpl
        implements PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderMapper purchaseOrderMapper;

    private final ProductClient productClient;
    private final SupplierClient supplierClient;

    @Override
    @Transactional
    public PurchaseOrderResponse createPurchaseOrder(
            PurchaseOrderRequest request
    ) {

        // Validate supplier
        SupplierResponse supplier =
                supplierClient.getSupplierById(
                        request.getSupplierId()
                );

        if (!"ACTIVE".equalsIgnoreCase(supplier.getStatus())) {
            throw new IllegalStateException(
                    "Supplier is not active: " +
                            request.getSupplierId()
            );
        }

        String purchaseOrderNumber =
                "PO-" + System.currentTimeMillis();

        PurchaseOrder purchaseOrder =
                PurchaseOrder.builder()
                        .purchaseOrderNumber(purchaseOrderNumber)
                        .supplierId(request.getSupplierId())
                        .status(PurchaseOrderStatus.CREATED)
                        .expectedDeliveryDate(
                                request.getExpectedDeliveryDate()
                        )
                        .notes(request.getNotes())
                        .totalAmount(BigDecimal.ZERO)
                        .items(new ArrayList<>())
                        .build();

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (PurchaseOrderItemRequest itemRequest :
                request.getItems()) {

            // Validate product
            ProductResponse product =
                    productClient.getProductById(
                            itemRequest.getProductId()
                    );

            if (!"ACTIVE".equalsIgnoreCase(product.getStatus())) {
                throw new IllegalStateException(
                        "Product is not active: " +
                                itemRequest.getProductId()
                );
            }

            BigDecimal totalPrice =
                    itemRequest.getUnitPrice()
                            .multiply(
                                    BigDecimal.valueOf(
                                            itemRequest.getQuantity()
                                    )
                            );

            PurchaseOrderItem item =
                    PurchaseOrderItem.builder()
                            .productId(itemRequest.getProductId())
                            .quantity(itemRequest.getQuantity())
                            .receivedQuantity(0)
                            .unitPrice(itemRequest.getUnitPrice())
                            .totalPrice(totalPrice)
                            .purchaseOrder(purchaseOrder)
                            .build();

            purchaseOrder.getItems().add(item);

            totalAmount = totalAmount.add(totalPrice);
        }

        purchaseOrder.setTotalAmount(totalAmount);

        PurchaseOrder savedPurchaseOrder =
                purchaseOrderRepository.save(purchaseOrder);

        return purchaseOrderMapper.toResponse(
                savedPurchaseOrder
        );
    }

    @Override
    public PurchaseOrderResponse getPurchaseOrderById(
            UUID purchaseOrderId
    ) {

        PurchaseOrder purchaseOrder =
                purchaseOrderRepository.findById(purchaseOrderId)
                        .orElseThrow(
                                () -> new PurchaseOrderNotFoundException(
                                        purchaseOrderId
                                )
                        );

        return purchaseOrderMapper.toResponse(purchaseOrder);
    }

    @Override
    public Page<PurchaseOrderResponse> getAllPurchaseOrders(
            Pageable pageable
    ) {

        return purchaseOrderRepository
                .findAll(pageable)
                .map(purchaseOrderMapper::toResponse);
    }

    @Override
    public Page<PurchaseOrderResponse> getPurchaseOrdersBySupplier(
            UUID supplierId,
            Pageable pageable
    ) {

        return purchaseOrderRepository
                .findBySupplierId(supplierId, pageable)
                .map(purchaseOrderMapper::toResponse);
    }

    @Override
    public Page<PurchaseOrderResponse> getPurchaseOrdersByStatus(
            PurchaseOrderStatus status,
            Pageable pageable
    ) {

        return purchaseOrderRepository
                .findByStatus(status, pageable)
                .map(purchaseOrderMapper::toResponse);
    }

    @Override
    @Transactional
    public PurchaseOrderResponse updateStatus(
            UUID purchaseOrderId,
            PurchaseOrderStatus status
    ) {

        PurchaseOrder purchaseOrder =
                purchaseOrderRepository.findById(purchaseOrderId)
                        .orElseThrow(
                                () -> new PurchaseOrderNotFoundException(
                                        purchaseOrderId
                                )
                        );

        purchaseOrder.setStatus(status);

        return purchaseOrderMapper.toResponse(
                purchaseOrderRepository.save(purchaseOrder)
        );
    }

    @Override
    @Transactional
    public void cancelPurchaseOrder(
            UUID purchaseOrderId
    ) {

        PurchaseOrder purchaseOrder =
                purchaseOrderRepository.findById(purchaseOrderId)
                        .orElseThrow(
                                () -> new PurchaseOrderNotFoundException(
                                        purchaseOrderId
                                )
                        );

        purchaseOrder.setStatus(
                PurchaseOrderStatus.CANCELLED
        );

        purchaseOrderRepository.save(purchaseOrder);
    }
}