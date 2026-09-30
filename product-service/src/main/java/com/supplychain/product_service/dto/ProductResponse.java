package com.supplychain.product_service.dto;

import com.supplychain.product_service.entity.ProductStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

    private UUID id;
    private String sku;
    private String name;
    private String description;
    private UUID categoryId;
    private String categoryName;
    private String brand;
    private BigDecimal unitPrice;
    private String currency;
    private BigDecimal weight;
    private String weightUnit;
    private String unitOfMeasure;
    private ProductStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
