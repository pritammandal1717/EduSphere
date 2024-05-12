package org.education.controller;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/bot")
public class OllamaController {

    private OllamaChatClient client;
    public OllamaController(OllamaChatClient client) {
        this.client = client;
    }
    @GetMapping("/prompt")
    public Flux<String> promptResponse(
            @RequestParam("prompt") String prompt
    ) {
        return client.stream(prompt);
    }
}
