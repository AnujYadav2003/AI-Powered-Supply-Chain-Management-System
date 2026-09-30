package com.supplychain.warehouse_service.repository;

import com.supplychain.warehouse_service.entity.Warehouse;
import com.supplychain.warehouse_service.entity.WarehouseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface WarehouseRepository extends JpaRepository<Warehouse, UUID>,
        JpaSpecificationExecutor<Warehouse> {

    Optional<Warehouse> findByCode(String code);

    Page<Warehouse> findByCityContainingIgnoreCase(
            String city,
            Pageable pageable
    );

    Page<Warehouse> findByStatus(
            WarehouseStatus status,
            Pageable pageable
    );

    boolean existsByCode(String code);
}