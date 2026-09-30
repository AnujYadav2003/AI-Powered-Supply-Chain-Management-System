package com.supplychain.inventory_service.specification;

import com.supplychain.inventory_service.entity.Inventory;
import com.supplychain.inventory_service.entity.InventoryStatus;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public final class InventorySpecification {

    private InventorySpecification() {
    }

    public static Specification<Inventory> hasProductId(UUID productId) {

        return (root, query, cb) -> {

            if (productId == null) {
                return null;
            }

            return cb.equal(root.get("productId"), productId);
        };
    }

    public static Specification<Inventory> hasWarehouseId(UUID warehouseId) {

        return (root, query, cb) -> {

            if (warehouseId == null) {
                return null;
            }

            return cb.equal(root.get("warehouseId"), warehouseId);
        };
    }

    public static Specification<Inventory> hasStatus(InventoryStatus status) {

        return (root, query, cb) -> {

            if (status == null) {
                return null;
            }

            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<Inventory> hasAvailableQuantityLessThanOrEqualTo(Integer quantity) {

        return (root, query, cb) -> {

            if (quantity == null) {
                return null;
            }

            return cb.lessThanOrEqualTo(root.get("availableQuantity"), quantity);
        };
    }

    public static Specification<Inventory> hasTotalQuantityLessThanOrEqualTo(Integer quantity) {

        return (root, query, cb) -> {

            if (quantity == null) {
                return null;
            }

            return cb.lessThanOrEqualTo(root.get("totalQuantity"), quantity);
        };
    }
}