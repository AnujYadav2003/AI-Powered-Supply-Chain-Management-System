package com.supplychain.ai_service.controller;

import com.supplychain.ai_service.service.AiAgentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ai")
public class AiController {

    private final AiAgentService aiAgentService;

    public AiController(AiAgentService aiAgentService) {
        this.aiAgentService = aiAgentService;
    }

    @PostMapping("/chat")
    public AiChatResponse chat(
            @RequestBody AiChatRequest request
    ) {
        return aiAgentService.ask(request.message());
    }
}