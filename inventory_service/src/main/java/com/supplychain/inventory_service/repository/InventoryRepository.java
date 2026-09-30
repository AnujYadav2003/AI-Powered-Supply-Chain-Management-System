package com.supplychain.inventory_service.repository;
import com.supplychain.inventory_service.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface InventoryRepository extends JpaRepository<Inventory, UUID>, JpaSpecificationExecutor<Inventory> {

    Optional<Inventory> findByProductIdAndWarehouseId(
            UUID productId,
            UUID warehouseId
    );
}