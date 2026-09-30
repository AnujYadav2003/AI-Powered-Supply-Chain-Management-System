package com.supplychain.logistics_service.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShipmentItemResponse {

    private UUID id;

    private UUID productId;

    private Integer quantity;
}