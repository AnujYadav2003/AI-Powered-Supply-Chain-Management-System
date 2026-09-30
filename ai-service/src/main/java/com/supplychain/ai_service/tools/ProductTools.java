
package com.supplychain.ai_service.tools;

import com.supplychain.ai_service.client.ServiceClient;
import com.supplychain.ai_service.util.ValidationUtils;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Component
public class ProductTools {

    private final RestClient restClient;
    private final ServiceClient serviceClient;

    public ProductTools(
            @Value("${services.product.url}") String productServiceUrl,
            ServiceClient serviceClient) {

        this.restClient = RestClient.builder()
                .baseUrl(productServiceUrl)
                .build();

        this.serviceClient = serviceClient;
    }

    @Tool(
            name = "getProductById",
            description = """
                Retrieve complete information about a single product
                using its exact product UUID.

                PURPOSE:
                Use this tool when the user wants details about one
                specific product.

                WHEN TO USE:
                - The user provides an exact product UUID.
                - The user asks for details about a specific product.
                - The user wants to retrieve a product by its ID.

                WHEN NOT TO USE:
                - The user wants to search products by name, SKU, brand,
                  category, or status.
                - The user provides only a product name or partial identifier.
                - The user wants multiple matching products.

                INPUT:
                - productId: A valid UUID identifying the product.

                BEHAVIOR:
                - Validate the supplied UUID before making the request.
                - Retrieve the product from Product Service.
                - Return the actual service response.
                - Never fabricate product information.

                IMPORTANT:
                If the user does not provide an exact product ID,
                use searchProducts instead.
                """
    )
    public String getProductById(String productId) {

        UUID id = ValidationUtils.parseUuid(productId, "productId");

        return serviceClient.execute(
                restClient.get()
                        .uri("/api/v1/products/{productId}", id)
                        .retrieve()
        );
    }

    @Tool(
            name = "searchProducts",
            description = """
                Search and filter products using optional search criteria.

                PURPOSE:
                Find one or more products matching the user's requirements.

                WHEN TO USE:
                - The user wants to find or search for products.
                - The user provides a product name, SKU, brand, or category.
                - The user wants products filtered by status.
                - The user wants to combine multiple search criteria.
                - The user asks for products matching specific conditions.

                SUPPORTED SEARCH CRITERIA:
                - name: Partial or complete product name.
                - sku: Exact or partial product SKU, according to
                  Product Service search behavior.
                - categoryId: UUID of the product category.
                - brand: Product brand.
                - status: Product status, such as ACTIVE or INACTIVE.

                INPUT RULES:
                - All search criteria are optional.
                - Leave a parameter empty when the user has not specified it.
                - Multiple criteria can be combined.
                - Validate categoryId when provided.
                - Normalize status to uppercase before sending the request.

                BEHAVIOR:
                - Send the supplied filters to Product Service.
                - Return matching product records.
                - Use the service's pagination metadata to determine
                  the total number of matching records.
                - Do not assume that the number of records in the current
                  page represents the total matching count.
                - Return the actual service response without inventing data.

                TOOL SELECTION:
                - Use getProductById for a specific product UUID.
                - Use this tool for searching, filtering, or discovering
                  products using business attributes.

                IMPORTANT:
                This tool only retrieves product information.
                It does not create, update, or delete products.
                """
    )
    public String searchProducts(
            String name,
            String sku,
            String categoryId,
            String brand,
            String status) {

        UUID categoryUUID = null;

        if (categoryId != null && !categoryId.isBlank()) {
            categoryUUID = ValidationUtils.parseUuid(
                    categoryId,
                    "categoryId"
            );
        }

        UUID finalCategoryUUID = categoryUUID;

        return serviceClient.execute(
                restClient.get()
                        .uri(uriBuilder -> {

                            uriBuilder.path("/api/v1/products/search");

                            if (name != null && !name.isBlank()) {
                                uriBuilder.queryParam("name", name);
                            }

                            if (sku != null && !sku.isBlank()) {
                                uriBuilder.queryParam("sku", sku);
                            }

                            if (finalCategoryUUID != null) {
                                uriBuilder.queryParam(
                                        "categoryId",
                                        finalCategoryUUID
                                );
                            }

                            if (brand != null && !brand.isBlank()) {
                                uriBuilder.queryParam("brand", brand);
                            }

                            if (status != null && !status.isBlank()) {
                                uriBuilder.queryParam(
                                        "status",
                                        status.toUpperCase()
                                );
                            }

                            uriBuilder.queryParam("page", 0);
                            uriBuilder.queryParam("size", 10);

                            return uriBuilder.build();
                        })
                        .retrieve()
        );
    }
}