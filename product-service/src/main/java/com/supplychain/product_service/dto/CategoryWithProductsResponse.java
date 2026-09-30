package com.supplychain.product_service.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryWithProductsResponse {

    private UUID categoryId;

    private String categoryName;

    private String description;

    private List<ProductResponse> products;
}