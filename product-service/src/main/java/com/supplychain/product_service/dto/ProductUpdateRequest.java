package com.supplychain.product_service.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductUpdateRequest {

    @NotBlank(message = "Product name is required")
    @Size(max = 150)
    private String name;

    @Size(max = 1000)
    private String description;

    @NotNull(message = "Category ID is required")
    private UUID categoryId;

    @Size(max = 100)
    private String brand;

    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.01")
    private BigDecimal unitPrice;

    @Size(min = 3, max = 3)
    private String currency;

    @DecimalMin(value = "0.001")
    private BigDecimal weight;

    @Size(max = 10)
    private String weightUnit;

    @NotBlank
    @Size(max = 20)
    private String unitOfMeasure;
}