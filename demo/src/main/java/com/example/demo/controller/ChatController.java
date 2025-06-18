package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;

import java.util.*;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Value("${gemini.api.key}")
    private String apiKey;

    private final String GEMINI_ENDPOINT = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=";

    @PostMapping
    public Map<String, Object> chat(@RequestBody Map<String, String> body) {
        String prompt = body.get("message");

        RestTemplate restTemplate = new RestTemplate();

        // Construimos el payload según la API de Gemini
        Map<String, Object> requestPayload = new HashMap<>();
        Map<String, String> part = new HashMap<>();
        part.put("text", prompt);
        Map<String, Object> content = new HashMap<>();
        content.put("parts", List.of(part));
        requestPayload.put("contents", List.of(content));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestPayload, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
            GEMINI_ENDPOINT + apiKey,
            HttpMethod.POST,
            requestEntity,
            new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        return response.getBody();
    }
}

