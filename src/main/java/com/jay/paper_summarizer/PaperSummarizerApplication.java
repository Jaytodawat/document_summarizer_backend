package com.jay.paper_summarizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PaperSummarizerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaperSummarizerApplication.class, args);
    }

}
