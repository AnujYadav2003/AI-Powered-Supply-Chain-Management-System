package com.supplychain.product_service.serviceImpl;
import com.supplychain.product_service.dto.*;
import com.supplychain.product_service.entity.Category;
import com.supplychain.product_service.entity.CategoryStatus;
import com.supplychain.product_service.exceptions.CategoryNotFoundException;
import com.supplychain.product_service.exceptions.DuplicateCategoryException;
import com.supplychain.product_service.mapper.CategoryMapper;
import com.supplychain.product_service.mapper.ProductMapper;
import com.supplychain.product_service.repository.CategoryRepository;
import com.supplychain.product_service.service.CategoryService;

import com.supplychain.product_service.service.specification.CategorySpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ProductMapper productMapper;
    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {

        if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateCategoryException(request.getName());
        }

        Category category = categoryMapper.toEntity(request);

        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.toResponse(savedCategory);
    }

    @Override
    public CategoryResponse getCategoryById(UUID categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new CategoryNotFoundException(categoryId)
                );

        return categoryMapper.toResponse(category);
    }


    @Override
    public Page<CategoryResponse> getAllCategories(
            Pageable pageable
    ) {

        return categoryRepository
                .findAll(pageable)
                .map(categoryMapper::toResponse);
    }


    @Override
    @Transactional
    public CategoryResponse updateCategory(UUID categoryId, CategoryUpdateRequest request) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new CategoryNotFoundException(categoryId)
                );

        if (!category.getName().equalsIgnoreCase(request.getName())
                && categoryRepository.existsByNameIgnoreCase(
                request.getName())) {

            throw new DuplicateCategoryException(
                    request.getName()
            );
        }

        categoryMapper.updateEntity(request, category);

        Category updatedCategory =
                categoryRepository.save(category);

        return categoryMapper.toResponse(updatedCategory);
    }

    @Override
    @Transactional
    public void deactivateCategory(UUID categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() ->
                        new CategoryNotFoundException(categoryId)
                );

        category.setStatus(CategoryStatus.INACTIVE);

        categoryRepository.save(category);
    }

    @Override
    public List<CategoryWithProductsResponse> getCategoriesWithProducts() {

        List<Category> categories =
                categoryRepository.findAllWithProducts();

        return categories.stream()
                .map(category -> {

                    List<ProductResponse> products =
                            category.getProducts()
                                    .stream()
                                    .map(product ->
                                            productMapper.toResponse(product)
                                    )
                                    .toList();

                    return CategoryWithProductsResponse.builder()
                            .categoryId(category.getId())
                            .categoryName(category.getName())
                            .description(category.getDescription())
                            .products(products)
                            .build();
                })
                .toList();
    }

    @Override
    public Page<CategoryResponse> searchCategories(
            CategorySearchRequest request,
            Pageable pageable) {

        Specification<Category> specification =
                Specification
                        .where(CategorySpecification.hasName(request.name()))
                        .and(CategorySpecification.hasStatus(request.status()));

        return categoryRepository
                .findAll(specification, pageable)
                .map(categoryMapper::toResponse);
    }
}