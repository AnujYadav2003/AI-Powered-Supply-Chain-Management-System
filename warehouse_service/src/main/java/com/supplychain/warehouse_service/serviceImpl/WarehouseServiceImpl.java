package com.supplychain.warehouse_service.serviceImpl;

import com.supplychain.warehouse_service.repository.WarehouseRepository;
import com.supplychain.warehouse_service.specification.WarehouseSpecification;
import com.supplychain.warehouse_service.dto.*;
import com.supplychain.warehouse_service.entity.*;
import com.supplychain.warehouse_service.exception.*;
import com.supplychain.warehouse_service.mapper.WarehouseMapper;
import com.supplychain.warehouse_service.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository repository;

    private final WarehouseMapper mapper;

    @Override
    @Transactional
    public WarehouseResponse createWarehouse(WarehouseRequest request) {

        if (repository.existsByCode(request.getCode())) {
            throw new DuplicateWarehouseException(request.getCode());
        }

        Warehouse warehouse = mapper.toEntity(request);

        warehouse.setCurrentUtilization(0);
        warehouse.setStatus(WarehouseStatus.ACTIVE);

        return mapper.toResponse(repository.save(warehouse));
    }

    @Override
    public WarehouseResponse getWarehouseById(UUID id) {

        return mapper.toResponse(
                repository.findById(id)
                        .orElseThrow(() -> new WarehouseNotFoundException(id))
        );
    }

    @Override
    public WarehouseResponse getWarehouseByCode(String code) {

        Warehouse warehouse = repository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));

        return mapper.toResponse(warehouse);
    }

    @Override
    public Page<WarehouseResponse> getAllWarehouses(Pageable pageable) {

        return repository.findAll(pageable)
                .map(mapper::toResponse);
    }

    @Override
    public Page<WarehouseResponse> searchByCity(String city, Pageable pageable) {

        return repository.findByCityContainingIgnoreCase(city, pageable)
                .map(mapper::toResponse);
    }

    @Override
    public Page<WarehouseResponse> getByStatus(WarehouseStatus status, Pageable pageable) {

        return repository.findByStatus(status, pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional
    public WarehouseResponse updateWarehouse(
            UUID id,
            WarehouseUpdateRequest request
    ) {

        Warehouse warehouse = repository.findById(id)
                .orElseThrow(() -> new WarehouseNotFoundException(id));

        mapper.updateEntity(request, warehouse);

        return mapper.toResponse(warehouse);
    }

    @Override
    @Transactional
    public WarehouseResponse updateWarehouseStatus(
            UUID id,
            WarehouseStatus status
    ) {

        Warehouse warehouse = repository.findById(id)
                .orElseThrow(() -> new WarehouseNotFoundException(id));

        warehouse.setStatus(status);

        return mapper.toResponse(warehouse);
    }


    @Override
    public Page<WarehouseResponse> searchWarehouses(
            WarehouseSearchRequest request,
            Pageable pageable) {

        Specification<Warehouse> spec = Specification.where((Specification<Warehouse>) null);

        if (request.code() != null && !request.code().isBlank()) {
            spec = spec.and(
                    WarehouseSpecification.hasCode(request.code().trim())
            );
        }

        if (request.name() != null && !request.name().isBlank()) {
            spec = spec.and(
                    WarehouseSpecification.hasName(request.name().trim())
            );
        }

        if (request.city() != null && !request.city().isBlank()) {
            spec = spec.and(
                    WarehouseSpecification.hasCity(request.city().trim())
            );
        }

        if (request.state() != null && !request.state().isBlank()) {
            spec = spec.and(
                    WarehouseSpecification.hasState(request.state().trim())
            );
        }

        if (request.country() != null && !request.country().isBlank()) {
            spec = spec.and(
                    WarehouseSpecification.hasCountry(request.country().trim())
            );
        }

        if (request.pincode() != null && !request.pincode().isBlank()) {
            spec = spec.and(
                    WarehouseSpecification.hasPincode(request.pincode().trim())
            );
        }

        if (request.status() != null) {
            spec = spec.and(
                    WarehouseSpecification.hasStatus(request.status())
            );
        }

        if (request.minCapacity() != null) {
            spec = spec.and(
                    WarehouseSpecification.capacityGreaterThanOrEqualTo(
                            request.minCapacity()
                    )
            );
        }

        if (request.maxCapacity() != null) {
            spec = spec.and(
                    WarehouseSpecification.capacityLessThanOrEqualTo(
                            request.maxCapacity()
                    )
            );
        }

        if (request.minCurrentUtilization() != null) {
            spec = spec.and(
                    WarehouseSpecification.utilizationGreaterThanOrEqualTo(
                            request.minCurrentUtilization()
                    )
            );
        }

        if (request.maxCurrentUtilization() != null) {
            spec = spec.and(
                    WarehouseSpecification.utilizationLessThanOrEqualTo(
                            request.maxCurrentUtilization()
                    )
            );
        }

        return repository.findAll(spec, pageable)
                .map(mapper::toResponse);
    }
}