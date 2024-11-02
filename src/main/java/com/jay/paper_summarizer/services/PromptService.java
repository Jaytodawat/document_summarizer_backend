package com.jay.paper_summarizer.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.node.ObjectNode;


@Service
@AllArgsConstructor
public class PromptService {
    private final ChatModel chatModel;
    public String callPromptService(String prompt) {
        return chatModel.call(prompt);
    }

}
