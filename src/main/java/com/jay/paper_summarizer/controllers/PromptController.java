package com.jay.paper_summarizer.controllers;

import com.jay.paper_summarizer.dto.PromptDTO;
import com.jay.paper_summarizer.services.PromptService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@AllArgsConstructor
@RestController
@RequestMapping("/api/paper_summarizer")
public class PromptController {

    final private PromptService promptService;

    @PostMapping(value = "/prompt", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> getPrompt(@RequestBody PromptDTO promptDTO) {
        Flux<String> response = promptService.callPromptService(promptDTO.prompt());
        return response.map(message -> ServerSentEvent.builder(message).build());
    }
}
