package com.supplychain.product_service.repository;

import com.supplychain.product_service.entity.Product;
import com.supplychain.product_service.entity.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    boolean existsBySku(String sku);

    Page<Product> findByNameContainingIgnoreCase(
            String name,
            Pageable pageable
    );

}