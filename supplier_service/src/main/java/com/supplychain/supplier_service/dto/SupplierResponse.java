package com.supplychain.supplier_service.dto;

import com.supplychain.supplier_service.entity.SupplierStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierResponse {

    private UUID id;

    private String supplierCode;

    private String name;

    private String email;

    private String phone;

    private String address;

    private String city;

    private String state;

    private String country;

    private String pincode;

    private Double rating;

    private SupplierStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}