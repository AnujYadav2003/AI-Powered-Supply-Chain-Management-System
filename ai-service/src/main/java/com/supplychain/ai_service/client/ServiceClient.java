package com.supplychain.ai_service.client;

import com.supplychain.ai_service.exception.ToolExecutionException;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ServiceClient {

    public String execute(RestClient.ResponseSpec responseSpec) {

        try {
            return responseSpec
                    .onStatus(
                            HttpStatusCode::isError,
                            (req, res) -> {
                                throw new ToolExecutionException(
                                        "SERVICE_ERROR",
                                        "Unable to retrieve data from the service."
                                );
                            }
                    )
                    .body(String.class);

        } catch (ToolExecutionException ex) {
            throw ex;

        } catch (Exception ex) {
            throw new ToolExecutionException(
                    "SERVICE_ERROR",
                    "Unable to communicate with the service."
            );
        }
    }
}