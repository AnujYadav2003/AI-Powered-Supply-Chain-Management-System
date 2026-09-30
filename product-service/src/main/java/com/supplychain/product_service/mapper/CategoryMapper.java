package com.supplychain.product_service.mapper;

import com.supplychain.product_service.dto.CategoryRequest;
import com.supplychain.product_service.dto.CategoryResponse;
import com.supplychain.product_service.dto.CategoryUpdateRequest;
import com.supplychain.product_service.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toEntity(CategoryRequest request);

    CategoryResponse toResponse(Category category);

    void updateEntity(CategoryUpdateRequest request,
            @MappingTarget Category category
    );
}
