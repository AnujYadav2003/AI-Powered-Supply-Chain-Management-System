package com.supplychain.supplier_service.service;

import com.supplychain.supplier_service.dto.SupplierRequest;
import com.supplychain.supplier_service.dto.SupplierResponse;
import com.supplychain.supplier_service.dto.SupplierUpdateRequest;
import com.supplychain.supplier_service.entity.SupplierStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface SupplierService {

    SupplierResponse createSupplier(
            SupplierRequest request
    );

    SupplierResponse getSupplierById(
            UUID supplierId
    );

    SupplierResponse getSupplierByCode(
            String supplierCode
    );

    SupplierResponse getSupplierByEmail(
            String email
    );

    Page<SupplierResponse> getAllSuppliers(
            Pageable pageable
    );

    Page<SupplierResponse> searchByName(
            String name,
            Pageable pageable
    );

    Page<SupplierResponse> searchByCity(
            String city,
            Pageable pageable
    );

    Page<SupplierResponse> getByStatus(
            SupplierStatus status,
            Pageable pageable
    );

    SupplierResponse updateSupplier(
            UUID supplierId,
            SupplierUpdateRequest request
    );

    SupplierResponse updateSupplierStatus(
            UUID supplierId,
            SupplierStatus status
    );
}