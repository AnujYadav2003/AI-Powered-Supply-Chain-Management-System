package com.supplychain.product_service.service;

import com.supplychain.product_service.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    CategoryResponse createCategory(CategoryRequest request);

    CategoryResponse getCategoryById(UUID categoryId);


    Page<CategoryResponse> getAllCategories(Pageable pageable);


    CategoryResponse updateCategory(
            UUID categoryId,
            CategoryUpdateRequest request
    );

    void deactivateCategory(UUID categoryId);

    List<CategoryWithProductsResponse> getCategoriesWithProducts();

    Page<CategoryResponse> searchCategories(CategorySearchRequest request, Pageable pageable);
}