package com.supplychain.ai_service.tools;

import com.supplychain.ai_service.client.ServiceClient;
import com.supplychain.ai_service.util.ValidationUtils;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class WarehouseTools {

    private final RestClient restClient;
    private final ServiceClient serviceClient;

    public WarehouseTools(
            RestClient.Builder builder,
            ServiceClient serviceClient,
            @Value("${services.warehouse.url}") String warehouseServiceUrl) {

        this.restClient = builder
                .baseUrl(warehouseServiceUrl)
                .build();

        this.serviceClient = serviceClient;
    }

    @Tool(
            name = "getWarehouseById",
            description = """
            Retrieve a specific warehouse using its unique UUID.

            Use this tool when the user asks for details about a particular
            warehouse and provides its warehouse ID.

            Input:
            - warehouseId: UUID of the warehouse.

            Behavior:
            - Validates the warehouse ID.
            - Fetches the warehouse from Warehouse Service.
            - Returns warehouse details, including code, name, location,
              capacity, current utilization, and status.

            Do not use this tool for searching warehouses by name, city,
            status, or capacity. Use searchWarehouses for those queries.
            """
    )
    public String getWarehouseById(String warehouseId) {

        String validatedId = ValidationUtils.parseUuid(
                warehouseId,
                "warehouseId"
        ).toString();

        return serviceClient.execute(
                restClient.get()
                        .uri("/api/v1/warehouses/{id}", validatedId)
                        .retrieve()
        );
    }

    @Tool(
            name = "searchWarehouses",
            description = """
            Search warehouses using optional filters.

            Use this tool when the user wants to find, filter, or analyze
            warehouses based on their attributes.

            Supported filters:
            - code: Partial, case-insensitive warehouse code.
            - name: Partial, case-insensitive warehouse name.
            - city: Partial, case-insensitive city.
            - state: Partial, case-insensitive state.
            - country: Partial, case-insensitive country.
            - pincode: Exact pincode.
            - status: Warehouse status, such as ACTIVE or INACTIVE.
            - minCapacity: Minimum warehouse capacity.
            - maxCapacity: Maximum warehouse capacity.
            - minCurrentUtilization: Minimum current utilization.
            - maxCurrentUtilization: Maximum current utilization.

            Behavior:
            - All filters are optional.
            - Multiple filters are combined using AND.
            - Results are paginated.
            - The tool requests the first page with up to 20 records.
            - An empty result means no warehouse matched the supplied filters.

            Important:
            - Do not invent filter values.
            - Do not use this tool when the user provides an exact warehouse ID.
            - Capacity and utilization are absolute values, not percentages.
            - This is a read-only search operation.
            """
    )
    public String searchWarehouses(
            String code,
            String name,
            String city,
            String state,
            String country,
            String pincode,
            String status,
            Integer minCapacity,
            Integer maxCapacity,
            Integer minCurrentUtilization,
            Integer maxCurrentUtilization) {

        RestClient.ResponseSpec responseSpec = restClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/api/v1/warehouses/search")
                            .queryParam("page", 0)
                            .queryParam("size", 20);

                    addParam(uriBuilder, "code", code);
                    addParam(uriBuilder, "name", name);
                    addParam(uriBuilder, "city", city);
                    addParam(uriBuilder, "state", state);
                    addParam(uriBuilder, "country", country);
                    addParam(uriBuilder, "pincode", pincode);
                    addParam(uriBuilder, "status", status);
                    addParam(uriBuilder, "minCapacity", minCapacity);
                    addParam(uriBuilder, "maxCapacity", maxCapacity);
                    addParam(uriBuilder, "minCurrentUtilization", minCurrentUtilization);
                    addParam(uriBuilder, "maxCurrentUtilization", maxCurrentUtilization);

                    return uriBuilder.build();
                })
                .retrieve();

        return serviceClient.execute(responseSpec);
    }

    @Tool(
            name = "getAllWarehouses",
            description = """
            Retrieve a paginated list of warehouses without applying filters.

            Use this tool when the user asks to:
            - List all warehouses.
            - Show available warehouses.
            - Get an overview of warehouse records.

            Behavior:
            - Fetches the first page with up to 20 records.
            - Returns warehouse data with pagination metadata.
            - Use searchWarehouses instead when the user specifies filters.

            This is a read-only operation.
            """
    )
    public String getAllWarehouses() {

        return serviceClient.execute(
                restClient.get()
                        .uri("/api/v1/warehouses?page=0&size=20")
                        .retrieve()
        );
    }

    private void addParam(
            org.springframework.web.util.UriBuilder uriBuilder,
            String name,
            Object value) {

        if (value != null && !value.toString().isBlank()) {
            uriBuilder.queryParam(name, value);
        }
    }
}