package com.supplychain.product_service.service.specification;

import com.supplychain.product_service.entity.Product;
import com.supplychain.product_service.entity.ProductStatus;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public final class ProductSpecification {

    private ProductSpecification() {
    }

    public static Specification<Product> hasName(String name) {
        return (root, query, cb) -> {

            if (name == null || name.isBlank()) {
                return null;
            }

            return cb.like(
                    cb.lower(root.get("name")),
                    "%" + name.toLowerCase() + "%"
            );
        };
    }

    public static Specification<Product> hasSku(String sku) {
        return (root, query, cb) -> {

            if (sku == null || sku.isBlank()) {
                return null;
            }

            return cb.equal(
                    cb.lower(root.get("sku")),
                    sku.toLowerCase()
            );
        };
    }

    public static Specification<Product> hasCategoryId(UUID categoryId) {
        return (root, query, cb) -> {

            if (categoryId == null) {
                return null;
            }

            return cb.equal(
                    root.get("category").get("id"),
                    categoryId
            );
        };
    }

    public static Specification<Product> hasBrand(String brand) {
        return (root, query, cb) -> {

            if (brand == null || brand.isBlank()) {
                return null;
            }

            return cb.like(
                    cb.lower(root.get("brand")),
                    "%" + brand.toLowerCase() + "%"
            );
        };
    }


    public static Specification<Product> hasStatus(ProductStatus status) {
        return (root, query, cb) -> {

            if (status == null) {
                return null;
            }

            return cb.equal(
                    root.get("status"),
                    status
            );
        };
    }
}