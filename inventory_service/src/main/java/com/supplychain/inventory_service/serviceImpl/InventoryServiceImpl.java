package com.supplychain.inventory_service.serviceImpl;

import com.supplychain.inventory_service.client.ProductClient;
import com.supplychain.inventory_service.client.WarehouseClient;
import com.supplychain.inventory_service.dto.*;
import com.supplychain.inventory_service.entity.Inventory;
import com.supplychain.inventory_service.entity.InventoryStatus;
import com.supplychain.inventory_service.entity.WarehouseStatus;
import com.supplychain.inventory_service.exception.DuplicateInventoryException;
import com.supplychain.inventory_service.exception.InsufficientStockException;
import com.supplychain.inventory_service.exception.InventoryNotFoundException;
import com.supplychain.inventory_service.mapper.InventoryMapper;
import com.supplychain.inventory_service.repository.InventoryRepository;
import com.supplychain.inventory_service.service.InventoryService;
import com.supplychain.inventory_service.specification.InventorySpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InventoryServiceImpl
        implements InventoryService {

    private final InventoryRepository inventoryRepository;

    private final InventoryMapper inventoryMapper;
    private final ProductClient productClient;
    private final WarehouseClient warehouseClient;

    @Override
    @Transactional
    public InventoryResponse createInventory(
            InventoryRequest request
    ) {

        // 1. Check Product Service
        ProductResponse product =
                productClient.
                        getProductById(
                        request.getProductId()
                );


        // 2. Check product status
        if (!"ACTIVE".equalsIgnoreCase(product.getStatus())) {

            throw new IllegalStateException(
                    "Product is not active: "
                            + request.getProductId()
            );
        }


        // 3. Check Warehouse Service
        WarehouseResponse warehouse =
                warehouseClient.getWarehouseById(
                        request.getWarehouseId()
                );


        // 4. Check warehouse status
        if (warehouse.getStatus()
                != WarehouseStatus.ACTIVE) {

            throw new IllegalStateException(
                    "Warehouse is not active: "
                            + request.getWarehouseId()
            );
        }


        // 5. Check duplicate inventory
        if (
                inventoryRepository
                        .findByProductIdAndWarehouseId(
                                request.getProductId(),
                                request.getWarehouseId()
                        )
                        .isPresent()
        ) {

            throw new DuplicateInventoryException(
                    request.getProductId(),
                    request.getWarehouseId()
            );
        }


        // 6. Create inventory
        Inventory inventory =
                Inventory.builder()
                        .productId(request.getProductId())
                        .warehouseId(request.getWarehouseId())
                        .totalQuantity(
                                request.getTotalQuantity()
                        )
                        .reservedQuantity(0)
                        .reorderLevel(
                                request.getReorderLevel()
                        )
                        .status(
                                InventoryStatus.AVAILABLE
                        )
                        .build();


        // 7. Calculate status
        updateInventoryStatus(inventory);


        // 8. Save
        Inventory savedInventory =
                inventoryRepository.save(inventory);


        return inventoryMapper.toResponse(
                savedInventory
        );
    }

    @Override
    public InventoryResponse getInventoryById(
            UUID inventoryId
    ) {

        Inventory inventory =
                inventoryRepository.findById(inventoryId)
                        .orElseThrow(
                                () -> new InventoryNotFoundException(
                                        inventoryId
                                )
                        );

        return inventoryMapper.toResponse(inventory);
    }

    @Override
    public Page<InventoryResponse> getAllInventory(
            Pageable pageable
    ) {

        return inventoryRepository
                .findAll(pageable)
                .map(inventoryMapper::toResponse);
    }


    @Override
    @Transactional
    public InventoryResponse addStock(
            UUID inventoryId,
            Integer quantity
    ) {

        Inventory inventory =
                getInventoryEntity(inventoryId);

        inventory.setTotalQuantity(
                inventory.getTotalQuantity()
                        + quantity
        );

        updateInventoryStatus(inventory);

        return inventoryMapper.toResponse(inventory);
    }


    @Override
    @Transactional
    public InventoryResponse removeStock(
            UUID inventoryId,
            Integer quantity
    ) {

        Inventory inventory =
                getInventoryEntity(inventoryId);

        int availableQuantity =
                getAvailableQuantity(inventory);

        if (quantity > availableQuantity) {
            throw new InsufficientStockException();
        }

        inventory.setTotalQuantity(
                inventory.getTotalQuantity()
                        - quantity
        );

        updateInventoryStatus(inventory);

        return inventoryMapper.toResponse(inventory);
    }


    @Override
    @Transactional
    public InventoryResponse reserveStock(
            UUID inventoryId,
            Integer quantity
    ) {

        Inventory inventory =
                getInventoryEntity(inventoryId);

        int availableQuantity =
                getAvailableQuantity(inventory);

        if (quantity > availableQuantity) {
            throw new InsufficientStockException();
        }

        inventory.setReservedQuantity(
                inventory.getReservedQuantity()
                        + quantity
        );

        updateInventoryStatus(inventory);

        return inventoryMapper.toResponse(inventory);
    }


    @Override
    @Transactional
    public InventoryResponse releaseStock(
            UUID inventoryId,
            Integer quantity
    ) {

        Inventory inventory =
                getInventoryEntity(inventoryId);

        if (
                quantity
                        > inventory.getReservedQuantity()
        ) {

            throw new IllegalArgumentException(
                    "Release quantity cannot exceed reserved quantity"
            );
        }

        inventory.setReservedQuantity(
                inventory.getReservedQuantity()
                        - quantity
        );

        updateInventoryStatus(inventory);

        return inventoryMapper.toResponse(inventory);
    }


    private Inventory getInventoryEntity(
            UUID inventoryId
    ) {

        return inventoryRepository
                .findById(inventoryId)
                .orElseThrow(
                        () -> new InventoryNotFoundException(
                                inventoryId
                        )
                );
    }


    private int getAvailableQuantity(
            Inventory inventory
    ) {

        return inventory.getTotalQuantity()
                - inventory.getReservedQuantity();
    }


    private void updateInventoryStatus(
            Inventory inventory
    ) {

        int availableQuantity =
                getAvailableQuantity(inventory);

        if (availableQuantity == 0) {

            inventory.setStatus(
                    InventoryStatus.OUT_OF_STOCK
            );

        } else if (
                availableQuantity
                        <= inventory.getReorderLevel()
        ) {

            inventory.setStatus(
                    InventoryStatus.LOW_STOCK
            );

        } else {

            inventory.setStatus(
                    InventoryStatus.AVAILABLE
            );
        }
    }

    @Override
    public Page<InventoryResponse> searchInventory(
            InventorySearchRequest request, Pageable pageable) {

        Specification<Inventory> specification =
                Specification
                        .where(
                                InventorySpecification.hasProductId(
                                        request.productId()
                                )
                        )
                        .and(
                                InventorySpecification.hasWarehouseId(
                                        request.warehouseId()
                                )
                        )
                        .and(
                                InventorySpecification.hasStatus(
                                        request.status()
                                )
                        )
                        .and(
                                InventorySpecification
                                        .hasAvailableQuantityLessThanOrEqualTo(
                                                request.maxAvailableQuantity()
                                        )
                        )
                        .and(
                                InventorySpecification
                                        .hasTotalQuantityLessThanOrEqualTo(
                                                request.maxTotalQuantity()
                                        )
                        );

        return inventoryRepository
                .findAll(specification, pageable)
                .map(inventoryMapper::toResponse);
    }
}