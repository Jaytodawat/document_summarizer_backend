package com.jay.paper_summarizer.services;

import lombok.AllArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;


@Service
@AllArgsConstructor
public class PromptService {
    private final ChatModel chatModel;
    public Flux<String> callPromptService(String prompt) {
        return chatModel.stream(prompt);
    }

}
