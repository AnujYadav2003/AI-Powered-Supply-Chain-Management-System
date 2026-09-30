package com.supplychain.warehouse_service.controller;

import com.supplychain.warehouse_service.dto.*;
import com.supplychain.warehouse_service.entity.WarehouseStatus;
import com.supplychain.warehouse_service.service.WarehouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/warehouses")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService service;

    @PostMapping
    public ResponseEntity<WarehouseResponse> create(
            @Valid @RequestBody WarehouseRequest request
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createWarehouse(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseResponse> getById(
            @PathVariable UUID id
    ) {

        return ResponseEntity.ok(service.getWarehouseById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<WarehouseResponse> getByCode(
            @PathVariable String code
    ) {

        return ResponseEntity.ok(service.getWarehouseByCode(code));
    }

    @GetMapping
    public ResponseEntity<Page<WarehouseResponse>> getAll(
            Pageable pageable
    ) {

        return ResponseEntity.ok(service.getAllWarehouses(pageable));
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<Page<WarehouseResponse>> searchByCity(
            @PathVariable String city,
            Pageable pageable
    ) {

        return ResponseEntity.ok(service.searchByCity(city, pageable));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<WarehouseResponse>> getByStatus(
            @PathVariable WarehouseStatus status,
            Pageable pageable
    ) {

        return ResponseEntity.ok(service.getByStatus(status, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WarehouseResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody WarehouseUpdateRequest request
    ) {

        return ResponseEntity.ok(service.updateWarehouse(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<WarehouseResponse> updateStatus(
            @PathVariable UUID id,
            @RequestParam WarehouseStatus status
    ) {

        return ResponseEntity.ok(
                service.updateWarehouseStatus(id, status)
        );
    }
}