package com.supplychain.product_service.controller;

import com.supplychain.product_service.dto.*;
import com.supplychain.product_service.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid @RequestBody CategoryRequest request
    ) {

        CategoryResponse response =
                categoryService.createCategory(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> getCategoryById(
            @PathVariable UUID categoryId
    ) {

        return ResponseEntity.ok(
                categoryService.getCategoryById(categoryId)
        );
    }



    @GetMapping
    public ResponseEntity<Page<CategoryResponse>> getAllCategories(
            Pageable pageable
    ) {

        return ResponseEntity.ok(
                categoryService.getAllCategories(pageable)
        );
    }


    @GetMapping("/with-products")
    public ResponseEntity<List<CategoryWithProductsResponse>> getCategoriesWithProducts() {

        return ResponseEntity.ok(
                categoryService.getCategoriesWithProducts()
        );
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable UUID categoryId,
            @Valid @RequestBody CategoryUpdateRequest request
    ) {

        return ResponseEntity.ok(
                categoryService.updateCategory(
                        categoryId,
                        request
                )
        );
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deactivateCategory(
            @PathVariable UUID categoryId
    ) {

        categoryService.deactivateCategory(categoryId);

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/search")
    public ResponseEntity<Page<CategoryResponse>> searchCategories(
            CategorySearchRequest request,
            Pageable pageable) {

        return ResponseEntity.ok(
                categoryService.searchCategories(request, pageable)
        );
    }
}