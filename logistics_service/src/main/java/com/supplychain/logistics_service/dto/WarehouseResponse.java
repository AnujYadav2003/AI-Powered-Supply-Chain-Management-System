package com.supplychain.logistics_service.dto;

import com.supplychain.logistics_service.entity.WarehouseStatus;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseResponse {

    private UUID id;

    private String code;

    private String name;

    private WarehouseStatus status;
}