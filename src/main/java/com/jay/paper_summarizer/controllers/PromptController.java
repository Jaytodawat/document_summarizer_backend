package com.jay.paper_summarizer.controllers;

import com.jay.paper_summarizer.dto.PromptDTO;
import com.jay.paper_summarizer.services.PromptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/paper_summarizer")
public class PromptController {

    @Autowired
    private PromptService promptService;

    @PostMapping("/prompt")
    public String getPrompt(@RequestBody PromptDTO promptDTO) {
        return promptService.callPromptService(promptDTO.prompt());
    }
}
