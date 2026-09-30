
package com.supplychain.ai_service.tools;

import com.supplychain.ai_service.client.ServiceClient;
import com.supplychain.ai_service.util.ValidationUtils;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Component
public class CategoryTools {

    private final RestClient restClient;
    private final ServiceClient serviceClient;

    public CategoryTools(
            @Value("${services.category.url}") String categoryServiceUrl,
            ServiceClient serviceClient) {

        this.restClient = RestClient.builder()
                .baseUrl(categoryServiceUrl)
                .build();

        this.serviceClient = serviceClient;
    }

    @Tool(
            name = "getCategoryById",
            description = """
                    Retrieve complete information about a single category
                    using its exact category UUID.

                    PURPOSE:
                    Fetch detailed information about a specific category.

                    WHEN TO USE:
                    - The user provides an exact category UUID.
                    - The user wants details about a particular category.
                    - The user asks to retrieve a category by its ID.

                    WHEN NOT TO USE:
                    - The user provides only a category name.
                    - The user wants to search multiple categories.
                    - The user wants to filter categories by status.

                    INPUT:
                    - categoryId: A valid UUID identifying the category.

                    BEHAVIOR:
                    - Validate the supplied UUID before making the request.
                    - Retrieve the category from Category Service.
                    - Return the actual service response.
                    - Never fabricate category information.

                    IMPORTANT:
                    If the user does not provide an exact category UUID,
                    use searchCategories instead.
                    """
    )
    public String getCategoryById(String categoryId) {

        UUID id = ValidationUtils.parseUuid(
                categoryId,
                "categoryId"
        );

        return serviceClient.execute(
                restClient.get()
                        .uri("/api/v1/categories/{categoryId}", id)
                        .retrieve()
        );
    }

    @Tool(
            name = "searchCategories",
            description = """
                    Search and filter categories using optional criteria.

                    PURPOSE:
                    Find categories matching the user's requirements.

                    WHEN TO USE:
                    - The user wants to find or search categories.
                    - The user provides a category name.
                    - The user wants categories filtered by status.
                    - The user wants to combine name and status filters.
                    - The user asks whether a category exists by name.

                    SUPPORTED SEARCH CRITERIA:
                    - name: Partial or complete category name.
                    - status: Category status, such as ACTIVE or INACTIVE.

                    INPUT RULES:
                    - All search criteria are optional.
                    - Leave parameters empty when the user has not specified them.
                    - Multiple criteria can be combined.
                    - Normalize status to uppercase before sending the request.

                    BEHAVIOR:
                    - Send the supplied filters to Category Service.
                    - Return matching category records.
                    - Respect pagination metadata.
                    - Use totalElements to determine the total number
                      of matching records.
                    - Do not assume the current page contains every match.
                    - Never invent category records.

                    TOOL SELECTION:
                    - Use getCategoryById for an exact category UUID.
                    - Use this tool for searching or filtering categories.

                    IMPORTANT:
                    This tool retrieves category information only.
                    It does not create, update, or delete categories.
                    """
    )
    public String searchCategories(
            String name,
            String status) {

        return serviceClient.execute(
                restClient.get()
                        .uri(uriBuilder -> {

                            uriBuilder.path("/api/v1/categories/search");

                            if (name != null && !name.isBlank()) {
                                uriBuilder.queryParam("name", name.trim());
                            }

                            if (status != null && !status.isBlank()) {
                                uriBuilder.queryParam(
                                        "status",
                                        status.trim().toUpperCase()
                                );
                            }

                            uriBuilder.queryParam("page", 0);
                            uriBuilder.queryParam("size", 20);

                            return uriBuilder.build();
                        })
                        .retrieve()
        );
    }

    @Tool(
            name = "getAllCategories",
            description = """
                    Retrieve a paginated list of categories without
                    applying any search filters.

                    PURPOSE:
                    Provide a general overview of categories available
                    in Category Service.

                    WHEN TO USE:
                    - The user explicitly asks to view all categories.
                    - The user requests a general category overview.
                    - The user wants categories without specifying filters.

                    WHEN NOT TO USE:
                    - The user specifies a category name.
                    - The user requests filtering by category status.
                    - The user asks for one specific category.
                    - The user wants categories with their associated products.

                    BEHAVIOR:
                    - Retrieve the first page of categories.
                    - Return the actual service response.
                    - Use totalElements to determine the total number
                      of category records.
                    - Do not claim that all records were returned if
                      additional pages exist.

                    TOOL SELECTION:
                    - Use getCategoryById for an exact category UUID.
                    - Use searchCategories when filtering is required.
                    - Use getCategoriesWithProducts when product associations
                      are explicitly requested.

                    IMPORTANT:
                    This tool is read-only and does not modify categories.
                    """
    )
    public String getAllCategories() {

        return serviceClient.execute(
                restClient.get()
                        .uri("/api/v1/categories?page=0&size=20")
                        .retrieve()
        );
    }

    @Tool(
            name = "getCategoriesWithProducts",
            description = """
                    Retrieve categories along with their associated products.

                    PURPOSE:
                    Provide category information together with product
                    associations.

                    WHEN TO USE:
                    - The user asks which products belong to each category.
                    - The user wants to see categories with their products.
                    - The user wants an overview of category-product relationships.
                    - The user wants to understand which products are grouped
                      under particular categories.

                    WHEN NOT TO USE:
                    - The user only wants basic category information.
                    - The user wants to search categories by name or status.
                    - The user wants details about one category by UUID.

                    BEHAVIOR:
                    - Retrieve category data with associated product information.
                    - Return the actual service response.
                    - Use only the product associations returned by the service.
                    - Do not invent products or category relationships.

                    IMPORTANT:
                    This tool retrieves information only.
                    It does not modify categories or products.
                    """
    )
    public String getCategoriesWithProducts() {

        return serviceClient.execute(
                restClient.get()
                        .uri("/api/v1/categories/with-products")
                        .retrieve()
        );
    }
}