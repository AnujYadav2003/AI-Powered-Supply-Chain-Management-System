package com.supplychain.supplier_service.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierRequest {

    @NotBlank(message = "Supplier code is required")
    @Size(
            max = 30,
            message = "Supplier code must not exceed 30 characters"
    )
    private String supplierCode;

    @NotBlank(message = "Supplier name is required")
    @Size(
            max = 150,
            message = "Supplier name must not exceed 150 characters"
    )
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone is required")
    @Size(
            min = 10,
            max = 20,
            message = "Invalid phone number"
    )
    private String phone;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Country is required")
    private String country;

    @NotBlank(message = "Pincode is required")
    private String pincode;
}