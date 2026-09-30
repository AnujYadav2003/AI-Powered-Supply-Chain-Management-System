package com.supplychain.supplier_service.serviceImpl;

import com.supplychain.supplier_service.dto.SupplierRequest;
import com.supplychain.supplier_service.dto.SupplierResponse;
import com.supplychain.supplier_service.dto.SupplierUpdateRequest;
import com.supplychain.supplier_service.entity.Supplier;
import com.supplychain.supplier_service.entity.SupplierStatus;
import com.supplychain.supplier_service.exception.DuplicateSupplierException;
import com.supplychain.supplier_service.exception.SupplierNotFoundException;
import com.supplychain.supplier_service.mapper.SupplierMapper;
import com.supplychain.supplier_service.repository.SupplierRepository;
import com.supplychain.supplier_service.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SupplierServiceImpl
        implements SupplierService {

    private final SupplierRepository supplierRepository;

    private final SupplierMapper supplierMapper;


    @Override
    @Transactional
    public SupplierResponse createSupplier(
            SupplierRequest request
    ) {

        if (
                supplierRepository
                        .existsBySupplierCode(
                                request.getSupplierCode()
                        )
        ) {

            throw new DuplicateSupplierException(
                    "Supplier code already exists: "
                            + request.getSupplierCode()
            );
        }


        if (
                supplierRepository
                        .existsByEmailIgnoreCase(
                                request.getEmail()
                        )
        ) {

            throw new DuplicateSupplierException(
                    "Supplier email already exists: "
                            + request.getEmail()
            );
        }


        Supplier supplier =
                supplierMapper.toEntity(request);


        supplier.setRating(0.0);

        supplier.setStatus(
                SupplierStatus.ACTIVE
        );


        Supplier savedSupplier =
                supplierRepository.save(supplier);


        return supplierMapper.toResponse(
                savedSupplier
        );
    }


    @Override
    public SupplierResponse getSupplierById(
            UUID supplierId
    ) {

        Supplier supplier =
                supplierRepository.findById(supplierId)
                        .orElseThrow(
                                () ->
                                        new SupplierNotFoundException(
                                                supplierId
                                        )
                        );

        return supplierMapper.toResponse(
                supplier
        );
    }


    @Override
    public SupplierResponse getSupplierByCode(
            String supplierCode
    ) {

        Supplier supplier =
                supplierRepository
                        .findBySupplierCode(
                                supplierCode
                        )
                        .orElseThrow(
                                () ->
                                        new SupplierNotFoundException(
                                                supplierCode
                                        )
                        );

        return supplierMapper.toResponse(
                supplier
        );
    }


    @Override
    public SupplierResponse getSupplierByEmail(
            String email
    ) {

        Supplier supplier =
                supplierRepository
                        .findByEmailIgnoreCase(email)
                        .orElseThrow(
                                () ->
                                        new RuntimeException(
                                                "Supplier not found"
                                        )
                        );

        return supplierMapper.toResponse(
                supplier
        );
    }


    @Override
    public Page<SupplierResponse> getAllSuppliers(
            Pageable pageable
    ) {

        return supplierRepository
                .findAll(pageable)
                .map(supplierMapper::toResponse);
    }


    @Override
    public Page<SupplierResponse> searchByName(
            String name,
            Pageable pageable
    ) {

        return supplierRepository
                .findByNameContainingIgnoreCase(
                        name,
                        pageable
                )
                .map(supplierMapper::toResponse);
    }


    @Override
    public Page<SupplierResponse> searchByCity(
            String city,
            Pageable pageable
    ) {

        return supplierRepository
                .findByCityContainingIgnoreCase(
                        city,
                        pageable
                )
                .map(supplierMapper::toResponse);
    }


    @Override
    public Page<SupplierResponse> getByStatus(
            SupplierStatus status,
            Pageable pageable
    ) {

        return supplierRepository
                .findByStatus(
                        status,
                        pageable
                )
                .map(supplierMapper::toResponse);
    }


    @Override
    @Transactional
    public SupplierResponse updateSupplier(
            UUID supplierId,
            SupplierUpdateRequest request
    ) {

        Supplier supplier =
                supplierRepository.findById(supplierId)
                        .orElseThrow(
                                () ->
                                        new SupplierNotFoundException(
                                                supplierId
                                        )
                        );


        if (
                !supplier.getEmail()
                        .equalsIgnoreCase(
                                request.getEmail()
                        )
                        &&
                        supplierRepository
                                .existsByEmailIgnoreCase(
                                        request.getEmail()
                                )
        ) {

            throw new DuplicateSupplierException(
                    "Supplier email already exists: "
                            + request.getEmail()
            );
        }


        supplierMapper.updateEntity(
                request,
                supplier
        );


        return supplierMapper.toResponse(
                supplier
        );
    }


    @Override
    @Transactional
    public SupplierResponse updateSupplierStatus(
            UUID supplierId,
            SupplierStatus status
    ) {

        Supplier supplier =
                supplierRepository.findById(supplierId)
                        .orElseThrow(
                                () ->
                                        new SupplierNotFoundException(
                                                supplierId
                                        )
                        );


        supplier.setStatus(status);


        return supplierMapper.toResponse(
                supplier
        );
    }
}