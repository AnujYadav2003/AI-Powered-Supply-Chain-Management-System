package com.supplychain.supplier_service.mapper;

import com.supplychain.supplier_service.dto.SupplierRequest;
import com.supplychain.supplier_service.dto.SupplierResponse;
import com.supplychain.supplier_service.dto.SupplierUpdateRequest;
import com.supplychain.supplier_service.entity.Supplier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SupplierMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Supplier toEntity(SupplierRequest request);

    SupplierResponse toResponse(Supplier supplier);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "supplierCode", ignore = true)
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(SupplierUpdateRequest request, @MappingTarget Supplier supplier);
}