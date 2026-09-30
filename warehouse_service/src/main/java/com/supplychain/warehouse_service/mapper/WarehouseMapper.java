package com.supplychain.warehouse_service.mapper;

import com.supplychain.warehouse_service.dto.*;
import com.supplychain.warehouse_service.entity.Warehouse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface WarehouseMapper {

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "currentUtilization", ignore = true)
//    @Mapping(target = "status", ignore = true)
//    @Mapping(target = "createdAt", ignore = true)
//    @Mapping(target = "updatedAt", ignore = true)
    Warehouse toEntity(WarehouseRequest request);

    WarehouseResponse toResponse(Warehouse warehouse);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", ignore = true)
    @Mapping(target = "currentUtilization", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(
            WarehouseUpdateRequest request,
            @MappingTarget Warehouse warehouse
    );
}