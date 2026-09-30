package com.supplychain.product_service.serviceImpl;

import com.supplychain.product_service.dto.ProductRequest;
import com.supplychain.product_service.dto.ProductResponse;
import com.supplychain.product_service.dto.ProductSearchRequest;
import com.supplychain.product_service.dto.ProductUpdateRequest;
import com.supplychain.product_service.entity.Category;
import com.supplychain.product_service.entity.Product;
import com.supplychain.product_service.entity.ProductStatus;
import com.supplychain.product_service.exceptions.CategoryNotFoundException;
import com.supplychain.product_service.exceptions.DuplicateSkuException;
import com.supplychain.product_service.exceptions.ProductNotFoundException;
import com.supplychain.product_service.mapper.ProductMapper;
import com.supplychain.product_service.repository.CategoryRepository;
import com.supplychain.product_service.repository.ProductRepository;
import com.supplychain.product_service.service.ProductService;
import com.supplychain.product_service.service.specification.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final CategoryRepository categoryRepository;


    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {

        if (productRepository.existsBySku(request.getSku())) {
            throw new DuplicateSkuException(request.getSku());
        }

        Category category = categoryRepository
                .findById(request.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(
                                request.getCategoryId())
                );

        Product product = productMapper.toEntity(request);

        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        return productMapper.toResponse(savedProduct);
    }

    @Override
    public ProductResponse getProductById(UUID productId) {

        Product product = productRepository
                .findById(productId)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                productId)
                );

        return productMapper.toResponse(product);
    }


    @Override
    public Page<ProductResponse> getAllProducts(Pageable pageable) {

        return productRepository
                .findAll(pageable)
                .map(productMapper::toResponse);
    }

    @Override
    public Page<ProductResponse> searchProducts(
            String name,
            Pageable pageable
    ) {

        return productRepository
                .findByNameContainingIgnoreCase(
                        name,
                        pageable
                )
                .map(productMapper::toResponse);
    }


    @Override
    public ProductResponse updateProduct(
            UUID productId,
            ProductUpdateRequest request
    ) {

        Product product = productRepository
                .findById(productId)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                productId
                        )
                );

        Category category = categoryRepository
                .findById(request.getCategoryId())
                .orElseThrow(() ->
                        new CategoryNotFoundException(
                                request.getCategoryId()
                        )
                );

        productMapper.updateEntity(
                request,
                product
        );

        product.setCategory(category);

        Product updatedProduct =
                productRepository.save(product);

        return productMapper.toResponse(updatedProduct);
    }

    @Override
    @Transactional
    public void deactivateProduct(UUID productId) {

        Product product = productRepository
                .findById(productId)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                productId
                        )
                );

        product.setStatus(ProductStatus.INACTIVE);

        productRepository.save(product);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> searchProducts(
            ProductSearchRequest request,
            Pageable pageable
    ) {

        Specification<Product> specification =
                Specification
                        .where(ProductSpecification.hasName(request.name()))
                        .and(ProductSpecification.hasSku(request.sku()))
                        .and(ProductSpecification.hasCategoryId(request.categoryId()))
                        .and(ProductSpecification.hasBrand(request.brand()))
                        .and(ProductSpecification.hasStatus(request.status()));

        return productRepository
                .findAll(specification, pageable)
                .map(productMapper::toResponse);
    }
}