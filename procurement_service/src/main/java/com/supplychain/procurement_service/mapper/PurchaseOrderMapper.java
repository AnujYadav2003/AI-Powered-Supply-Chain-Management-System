package com.supplychain.procurement_service.mapper;

import com.supplychain.procurement_service.dto.PurchaseOrderItemResponse;
import com.supplychain.procurement_service.dto.PurchaseOrderResponse;
import com.supplychain.procurement_service.entity.PurchaseOrder;
import com.supplychain.procurement_service.entity.PurchaseOrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PurchaseOrderMapper {

    PurchaseOrderResponse toResponse(PurchaseOrder purchaseOrder);

    PurchaseOrderItemResponse toResponse(PurchaseOrderItem item);
}