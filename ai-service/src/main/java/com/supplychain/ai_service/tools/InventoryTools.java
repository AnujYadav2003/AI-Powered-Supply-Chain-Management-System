
package com.supplychain.ai_service.tools;

import com.supplychain.ai_service.client.ServiceClient;
import com.supplychain.ai_service.util.ValidationUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Component
public class InventoryTools {

    private final RestClient restClient;
    private final ServiceClient serviceClient;

    public InventoryTools(
            @Value("${services.inventory.url}") String inventoryServiceUrl,
            ServiceClient serviceClient) {

        this.restClient = RestClient.builder()
                .baseUrl(inventoryServiceUrl)
                .build();

        this.serviceClient = serviceClient;
    }

    @Tool(
            name = "getInventoryById",
            description = """
                    Retrieve complete information about a single inventory
                    record using its exact inventory UUID.

                    PURPOSE:
                    Fetch detailed information about a specific inventory
                    record.

                    WHEN TO USE:
                    - The user provides an exact inventory UUID.
                    - The user wants details about a specific inventory record.
                    - The user asks to retrieve inventory by its ID.

                    WHEN NOT TO USE:
                    - The user wants to search inventory using product or
                      warehouse identifiers.
                    - The user wants to filter inventory by quantity or status.
                    - The user wants to retrieve multiple inventory records.

                    INPUT:
                    - inventoryId: A valid UUID identifying the inventory record.

                    BEHAVIOR:
                    - Validate the supplied UUID before making the request.
                    - Retrieve the inventory record from Inventory Service.
                    - Return the actual service response.
                    - Never fabricate inventory information.

                    IMPORTANT:
                    If the user does not provide an exact inventory UUID,
                    use searchInventory instead.
                    """
    )
    public String getInventoryById(String inventoryId) {

        UUID id = ValidationUtils.parseUuid(
                inventoryId,
                "inventoryId"
        );

        return serviceClient.execute(
                restClient.get()
                        .uri("/api/v1/inventory/{inventoryId}", id)
                        .retrieve()
        );
    }

    @Tool(
            name = "searchInventory",
            description = """
                    Search and filter inventory records using optional
                    search criteria.

                    PURPOSE:
                    Find inventory records matching the user's requirements
                    across products, warehouses, stock quantities, and status.

                    WHEN TO USE:
                    - The user wants to find or search inventory.
                    - The user asks for inventory associated with a product.
                    - The user asks for inventory available at a warehouse.
                    - The user wants to identify low-stock inventory.
                    - The user wants to filter inventory by status.
                    - The user wants to compare inventory quantities.
                    - The user provides multiple inventory search conditions.

                    SUPPORTED SEARCH CRITERIA:
                    - productId: UUID identifying the product.
                    - warehouseId: UUID identifying the warehouse.
                    - status: Inventory status, using a valid InventoryStatus value.
                    - maxAvailableQuantity: Maximum available stock quantity.
                    - maxTotalQuantity: Maximum total stock quantity.

                    INPUT RULES:
                    - All search criteria are optional.
                    - Leave parameters empty when the user has not specified them.
                    - Multiple criteria can be combined.
                    - Validate productId and warehouseId when provided.
                    - Normalize status to uppercase before sending the request.
                    - Quantity filters must be interpreted as upper bounds.

                    QUANTITY INTERPRETATION:
                    - totalQuantity represents the total stock recorded.
                    - reservedQuantity represents stock reserved for operations.
                    - availableQuantity represents stock available for use.
                    - Never treat reserved quantity as available stock.
                    - Use maxAvailableQuantity when the user asks for low
                      available stock.
                    - Use maxTotalQuantity when the user asks to filter by
                      total stock.

                    BEHAVIOR:
                    - Send the supplied filters to Inventory Service.
                    - Return matching inventory records.
                    - Use the actual service response for analysis.
                    - Respect pagination metadata.
                    - Use totalElements to determine the total number of
                      matching records.
                    - Do not assume the current page contains every match.
                    - Never invent inventory records or quantities.

                    TOOL SELECTION:
                    - Use getInventoryById for a specific inventory UUID.
                    - Use this tool for searching, filtering, or discovering
                      inventory records.

                    IMPORTANT:
                    This tool retrieves inventory information only.
                    It does not add stock, remove stock, reserve stock,
                    or release reserved stock.
                    """
    )
    public String searchInventory(
            String productId,
            String warehouseId,
            String status,
            Integer maxAvailableQuantity,
            Integer maxTotalQuantity) {

        UUID productUUID = parseOptionalUuid(
                productId,
                "productId"
        );

        UUID warehouseUUID = parseOptionalUuid(
                warehouseId,
                "warehouseId"
        );

        return serviceClient.execute(
                restClient.get()
                        .uri(uriBuilder -> {

                            uriBuilder.path("/api/v1/inventory/search");

                            if (productUUID != null) {
                                uriBuilder.queryParam(
                                        "productId",
                                        productUUID
                                );
                            }

                            if (warehouseUUID != null) {
                                uriBuilder.queryParam(
                                        "warehouseId",
                                        warehouseUUID
                                );
                            }

                            if (status != null && !status.isBlank()) {
                                uriBuilder.queryParam(
                                        "status",
                                        status.trim().toUpperCase()
                                );
                            }

                            if (maxAvailableQuantity != null) {
                                uriBuilder.queryParam(
                                        "maxAvailableQuantity",
                                        maxAvailableQuantity
                                );
                            }

                            if (maxTotalQuantity != null) {
                                uriBuilder.queryParam(
                                        "maxTotalQuantity",
                                        maxTotalQuantity
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
            name = "getAllInventory",
            description = """
                    Retrieve a paginated list of inventory records without
                    applying any search filters.

                    PURPOSE:
                    Provide a general overview of inventory records stored
                    in Inventory Service.

                    WHEN TO USE:
                    - The user explicitly asks to view all inventory.
                    - The user requests a general inventory overview.
                    - The user wants to retrieve inventory without specifying
                      any filtering conditions.

                    WHEN NOT TO USE:
                    - The user provides a product ID or warehouse ID.
                    - The user requests low-stock inventory.
                    - The user specifies a status or quantity condition.
                    - The user wants a specific inventory record.

                    BEHAVIOR:
                    - Retrieve the first page of inventory records.
                    - Return the actual service response.
                    - Respect the pagination metadata.
                    - Use totalElements to determine the total number
                      of inventory records.
                    - Do not claim that all records were returned if
                      additional pages exist.

                    TOOL SELECTION:
                    - Use getInventoryById for an exact inventory UUID.
                    - Use searchInventory when any filtering condition
                      is provided.

                    IMPORTANT:
                    This tool is read-only and does not modify inventory.
                    """
    )
    public String getAllInventory() {

        return serviceClient.execute(
                restClient.get()
                        .uri("/api/v1/inventory?page=0&size=20")
                        .retrieve()
        );
    }

    private UUID parseOptionalUuid(
            String value,
            String fieldName) {

        if (value == null || value.isBlank()) {
            return null;
        }

        return ValidationUtils.parseUuid(
                value,
                fieldName
        );
    }
}