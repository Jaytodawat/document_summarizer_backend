package com.jay.paper_summarizer.services;

import lombok.AllArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;


@Service
@AllArgsConstructor
public class PromptService {
    private final OllamaChatModel chatModel;
    public Flux<ChatResponse> callPromptService(String msg) {


        Prompt prompt = new Prompt(msg);


        return chatModel.stream(prompt);
    }

}
