package com.supplychain.procurement_service.dto;

import lombok.*;

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

    private String status;
}