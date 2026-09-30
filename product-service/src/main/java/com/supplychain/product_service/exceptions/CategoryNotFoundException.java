package com.supplychain.product_service.exceptions;
import java.util.UUID;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(UUID categoryId) {
        super("Category not found with id: " + categoryId);
    }

    public CategoryNotFoundException(String categoryName) {
        super("Category not found with name: " + categoryName);
    }
}
