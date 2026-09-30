package com.supplychain.product_service.service;

import com.supplychain.product_service.dto.ProductRequest;
import com.supplychain.product_service.dto.ProductResponse;
import com.supplychain.product_service.dto.ProductSearchRequest;
import com.supplychain.product_service.dto.ProductUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);

    ProductResponse getProductById(UUID productId);

    Page<ProductResponse> getAllProducts(Pageable pageable);

    Page<ProductResponse> searchProducts(
            String name,
            Pageable pageable
    );

    ProductResponse updateProduct(
            UUID productId,
            ProductUpdateRequest request
    );

    void deactivateProduct(UUID productId);

    Page<ProductResponse> searchProducts(ProductSearchRequest request,
            Pageable pageable);
}