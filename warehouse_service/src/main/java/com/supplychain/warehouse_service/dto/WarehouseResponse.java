package com.supplychain.warehouse_service.dto;

import com.supplychain.warehouse_service.entity.WarehouseStatus;
import lombok.*;

import java.time.LocalDateTime;
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
    private String address;
    private String city;
    private String state;
    private String country;
    private String pincode;
    private Integer capacity;
    private Integer currentUtilization;
    private WarehouseStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}