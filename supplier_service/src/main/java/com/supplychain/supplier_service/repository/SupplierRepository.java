package com.supplychain.supplier_service.repository;

import com.supplychain.supplier_service.entity.Supplier;
import com.supplychain.supplier_service.entity.SupplierStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SupplierRepository
        extends JpaRepository<Supplier, UUID> {

    Optional<Supplier> findBySupplierCode(
            String supplierCode
    );

    Optional<Supplier> findByEmailIgnoreCase(
            String email
    );

    boolean existsBySupplierCode(
            String supplierCode
    );

    boolean existsByEmailIgnoreCase(
            String email
    );

    Page<Supplier> findByNameContainingIgnoreCase(
            String name,
            Pageable pageable
    );

    Page<Supplier> findByCityContainingIgnoreCase(
            String city,
            Pageable pageable
    );

    Page<Supplier> findByStatus(
            SupplierStatus status,
            Pageable pageable
    );
}