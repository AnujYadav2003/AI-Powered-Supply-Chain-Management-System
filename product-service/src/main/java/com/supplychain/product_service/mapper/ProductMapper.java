package com.supplychain.product_service.mapper;

import com.supplychain.product_service.dto.ProductRequest;
import com.supplychain.product_service.dto.ProductResponse;
import com.supplychain.product_service.dto.ProductUpdateRequest;
import com.supplychain.product_service.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "category", ignore = true)
    Product toEntity(ProductRequest request);

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    ProductResponse toResponse(Product product);

    @Mapping(target = "category", ignore = true)
    void updateEntity(ProductUpdateRequest request, @MappingTarget Product product);
}