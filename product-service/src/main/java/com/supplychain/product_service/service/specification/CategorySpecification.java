package com.supplychain.product_service.service.specification;
import com.supplychain.product_service.entity.Category;
import com.supplychain.product_service.entity.CategoryStatus;
import org.springframework.data.jpa.domain.Specification;

public final class CategorySpecification {

    private CategorySpecification() {
    }

    public static Specification<Category> hasName(String name) {

        return (root, query, cb) -> {

            if (name == null || name.isBlank()) {
                return null;
            }

            return cb.like(
                    cb.lower(root.get("name")),
                    "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Category> hasStatus(CategoryStatus status) {

        return (root, query, cb) -> {

            if (status == null) {
                return null;
            }
            return cb.equal(root.get("status"), status);
        };
    }
}