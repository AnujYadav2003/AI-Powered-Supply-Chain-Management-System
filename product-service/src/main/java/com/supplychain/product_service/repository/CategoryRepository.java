package com.supplychain.product_service.repository;

import com.supplychain.product_service.entity.Category;
import com.supplychain.product_service.entity.CategoryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository
        extends JpaRepository<Category, UUID>,
        JpaSpecificationExecutor<Category> {

    Optional<Category> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    Page<Category> findByStatus(
            CategoryStatus status,
            Pageable pageable
    );

    @Query("""
            SELECT DISTINCT c
            FROM Category c
            LEFT JOIN FETCH c.products
            """)
    List<Category> findAllWithProducts();
}