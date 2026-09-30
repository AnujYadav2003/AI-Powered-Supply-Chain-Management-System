package com.supplychain.supplier_service.controller;

import com.supplychain.supplier_service.dto.SupplierRequest;
import com.supplychain.supplier_service.dto.SupplierResponse;
import com.supplychain.supplier_service.dto.SupplierUpdateRequest;
import com.supplychain.supplier_service.entity.SupplierStatus;
import com.supplychain.supplier_service.service.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;


    @PostMapping
    public ResponseEntity<SupplierResponse> createSupplier(
            @Valid @RequestBody SupplierRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        supplierService.createSupplier(
                                request
                        )
                );
    }


    @GetMapping("/{supplierId}")
    public ResponseEntity<SupplierResponse> getSupplierById(
            @PathVariable UUID supplierId
    ) {

        return ResponseEntity.ok(
                supplierService.getSupplierById(
                        supplierId
                )
        );
    }


    @GetMapping("/code/{supplierCode}")
    public ResponseEntity<SupplierResponse> getSupplierByCode(
            @PathVariable String supplierCode
    ) {

        return ResponseEntity.ok(
                supplierService.getSupplierByCode(
                        supplierCode
                )
        );
    }


    @GetMapping("/email/{email}")
    public ResponseEntity<SupplierResponse> getSupplierByEmail(
            @PathVariable String email
    ) {

        return ResponseEntity.ok(
                supplierService.getSupplierByEmail(
                        email
                )
        );
    }


    @GetMapping
    public ResponseEntity<Page<SupplierResponse>> getAllSuppliers(
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                supplierService.getAllSuppliers(
                        pageable
                )
        );
    }


    @GetMapping("/search/name")
    public ResponseEntity<Page<SupplierResponse>> searchByName(
            @RequestParam String name,
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                supplierService.searchByName(
                        name,
                        pageable
                )
        );
    }


    @GetMapping("/search/city")
    public ResponseEntity<Page<SupplierResponse>> searchByCity(
            @RequestParam String city,
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                supplierService.searchByCity(
                        city,
                        pageable
                )
        );
    }


    @GetMapping("/status/{status}")
    public ResponseEntity<Page<SupplierResponse>> getByStatus(
            @PathVariable SupplierStatus status,
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                supplierService.getByStatus(
                        status,
                        pageable
                )
        );
    }


    @PutMapping("/{supplierId}")
    public ResponseEntity<SupplierResponse> updateSupplier(
            @PathVariable UUID supplierId,
            @Valid @RequestBody SupplierUpdateRequest request
    ) {

        return ResponseEntity.ok(
                supplierService.updateSupplier(
                        supplierId,
                        request
                )
        );
    }


    @PatchMapping("/{supplierId}/status")
    public ResponseEntity<SupplierResponse> updateStatus(
            @PathVariable UUID supplierId,
            @RequestParam SupplierStatus status
    ) {

        return ResponseEntity.ok(
                supplierService.updateSupplierStatus(
                        supplierId,
                        status
                )
        );
    }
}