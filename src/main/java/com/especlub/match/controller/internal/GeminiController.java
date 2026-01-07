package com.especlub.match.controller.internal;

import com.especlub.match.dto.response.JsonDtoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/gemini")
@RequiredArgsConstructor
public class GeminiController {

    private final ChatModel chatModel;

    @GetMapping("/chat")
    public ResponseEntity<JsonDtoResponse<String>> chat(@RequestParam String prompt) {
        String respuestaIA = chatModel.call(prompt);

        return JsonDtoResponse.ok("Respuesta generada por LLM", respuestaIA).toResponseEntity();
    }
}