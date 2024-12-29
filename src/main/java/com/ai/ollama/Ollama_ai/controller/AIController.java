package com.ai.ollama.Ollama_ai.controller;

import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    private final OllamaChatModel model;

    // Constructor-based dependency injection
    @Autowired
    public AIController(OllamaChatModel model) {
        this.model = model;
    }

    // Improved endpoint that allows dynamic prompts
    @GetMapping("/prompt/{prompt}")
    public ResponseEntity<Flux<String>> promptResponse(@PathVariable String prompt) {
        try {
            // Assuming model.stream(prompt) returns a Flux<String> (stream of responses)
            Flux<String> response = model.stream(prompt)
                    .onErrorReturn("Error processing the request");

            // Return a successful response with the model's output as a stream
            return ResponseEntity.ok()
                    .header("Content-Type", "text/event-stream") // Indicating it's a stream
                    .body(response);
        } catch (Exception e) {
            // Return a failure response with error message in case of exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Flux.just("Error processing the request: " + e.getMessage()));
        }
    }
}
