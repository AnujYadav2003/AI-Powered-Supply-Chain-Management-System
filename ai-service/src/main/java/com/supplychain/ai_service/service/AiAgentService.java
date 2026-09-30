
package com.supplychain.ai_service.service;

import com.supplychain.ai_service.controller.AiChatResponse;
import com.supplychain.ai_service.exception.ToolExecutionException;
import com.supplychain.ai_service.tools.CategoryTools;
import com.supplychain.ai_service.tools.InventoryTools;
import com.supplychain.ai_service.tools.ProductTools;
import com.supplychain.ai_service.tools.WarehouseTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiAgentService {

    private final ChatClient chatClient;
    private final ProductTools productTools;
    private final InventoryTools inventoryTools;
    private final CategoryTools categoryTools;
    private final WarehouseTools warehouseTools;

    public AiAgentService(
            ChatClient.Builder chatClientBuilder,
            ProductTools productTools,
            InventoryTools inventoryTools,
            CategoryTools categoryTools,
            WarehouseTools warehouseTools){



        this.chatClient = chatClientBuilder.build();
        this.productTools = productTools;
        this.inventoryTools = inventoryTools;
        this.categoryTools=categoryTools;
        this.warehouseTools=warehouseTools;

    }

    public AiChatResponse ask(String message) {

        try {

            return chatClient
                    .prompt()
                    .system("""
                        You are an AI Supply Chain Assistant.

                        Your responsibility is to understand user queries,
                        select appropriate tools, analyze their results,
                        and return accurate structured responses.

                        TOOL USAGE:
                        - Use available tools whenever business data is required.
                        - Select tools based on their descriptions and user intent.
                        - Never invent, assume, or fabricate business data.
                        - Use exact-ID tools only when the user provides an exact ID.
                        - Use search tools for filtering and discovering records.
                        - Combine multiple tools when a query requires information
                          from different services.
                        - Never claim an operation was performed unless a tool
                          actually executed that operation.

                        RESULT ANALYSIS:
                        - Analyze actual tool responses before generating conclusions.
                        - Respect pagination and use totalElements as the total
                          matching record count when available.
                        - Never report zero results when the response contains
                          matching records.
                        - Distinguish between an empty result and a tool failure.
                        - Do not interpret missing information as proof that
                          a record does not exist.
                        - Keep conclusions factual and concise.

                        STATUS RULES:
                        - SUCCESS: The requested operation or data retrieval
                          completed successfully.
                        - NOT_FOUND: The tool executed successfully but no
                          matching records were found.
                        - ERROR: A technical failure or tool execution error
                          prevented the request from completing.
                        - Do not use ERROR for a valid empty search result.

                        RESPONSE FORMAT:
                        Return only a response matching the AiChatResponse
                        structure.

                        Required fields:
                        - status
                        - query
                        - summary
                        - searchCriteria
                        - investigation
                        - conclusion
                        - nextAction

                        Investigation fields:
                        - searchPerformed
                        - resultsFound
                        - errors

                        Error fields:
                        - code
                        - message

                        RESPONSE CONSTRAINTS:
                        - Use the exact field names defined in AiChatResponse.
                        - Use the correct data types for every field.
                        - Use null when an optional field has no applicable value.
                        - Use an empty list for errors when no errors occurred.
                        - Use an empty map for searchCriteria when no filters apply.
                        - Set searchPerformed to false when no tool was called.
                        - Set resultsFound to the actual number of matching records.
                        - Do not include explanations outside the response structure.
                        - Do not return Markdown or JSON code fences.
                        """)
                    .user(message)
                    .tools(productTools, inventoryTools,categoryTools,warehouseTools)
                    .call()
                    .entity(AiChatResponse.class);

        } catch (ToolExecutionException ex) {

            return buildErrorResponse(
                    ex.getCode(),
                    ex.getMessage()
            );

        } catch (Exception ex) {

            return buildErrorResponse(
                    "AI_AGENT_ERROR",
                    "The AI agent could not complete the request."
            );
        }
    }

    private AiChatResponse buildErrorResponse(
            String code,
            String message) {

        return new AiChatResponse(
                "ERROR",
                null,
                message,
                null,
                new AiChatResponse.Investigation(
                        true,
                        0,
                        List.of(
                                new AiChatResponse.ErrorDetail(code, message)
                        )
                ),
                message,
                "Please try again later."
        );
    }
}