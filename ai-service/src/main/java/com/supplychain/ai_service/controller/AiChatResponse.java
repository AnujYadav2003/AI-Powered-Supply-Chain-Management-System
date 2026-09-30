package com.supplychain.ai_service.controller;

import java.util.List;
import java.util.Map;

public record AiChatResponse(
        String status,
        String query,
        String summary,
        Map<String, Object> searchCriteria,
        Investigation investigation,
        String conclusion,
        String nextAction
) {

    public record Investigation(
            boolean searchPerformed,
            int resultsFound,
            List<ErrorDetail> errors
    ) {}

    public record ErrorDetail(
            String code,
            String message
    ) {}
}