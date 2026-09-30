package com.supplychain.product_service.controller;

import com.supplychain.product_service.dto.ProductRequest;
import com.supplychain.product_service.dto.ProductResponse;
import com.supplychain.product_service.dto.ProductSearchRequest;
import com.supplychain.product_service.dto.ProductUpdateRequest;
import com.supplychain.product_service.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest request) {

        ProductResponse response = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(
            @PathVariable UUID productId) {

        return ResponseEntity.ok(productService.getProductById(productId));
    }



    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            Pageable pageable) {
        return ResponseEntity.ok(productService.getAllProducts(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponse>> searchProducts(ProductSearchRequest request,
            Pageable pageable) {

        return ResponseEntity.ok(
                productService.searchProducts(request, pageable)
        );
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable UUID productId,
            @Valid @RequestBody ProductUpdateRequest request
    ) {

        return ResponseEntity.ok(productService.updateProduct(productId, request)
        );
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deactivateProduct(
            @PathVariable UUID productId
    ) {

        productService.deactivateProduct(productId);
        return ResponseEntity.noContent().build();
    }
}