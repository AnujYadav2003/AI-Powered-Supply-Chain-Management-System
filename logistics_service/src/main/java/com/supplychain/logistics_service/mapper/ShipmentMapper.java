package com.supplychain.logistics_service.mapper;

import com.supplychain.logistics_service.dto.ShipmentItemResponse;
import com.supplychain.logistics_service.dto.ShipmentResponse;
import com.supplychain.logistics_service.entity.Shipment;
import com.supplychain.logistics_service.entity.ShipmentItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShipmentMapper {

    ShipmentResponse toResponse(Shipment shipment);

    ShipmentItemResponse toResponse(ShipmentItem item);
}