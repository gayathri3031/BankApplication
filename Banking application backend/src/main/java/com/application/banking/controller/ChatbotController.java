package com.application.banking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.application.banking.model.ChatMessage;
import com.application.banking.model.ChatResponse;
import com.application.banking.service.ChatbotService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatbotController {
    
    @Autowired
    private ChatbotService chatbotService;
    
    @PostMapping("/message")
    public ResponseEntity<ChatResponse> sendMessage(@Valid @RequestBody ChatMessage message) {
        try {
            ChatResponse response = chatbotService.processMessage(message);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ChatResponse errorResponse = new ChatResponse("Sorry, I encountered an error processing your message. Please try again.");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Chatbot is running!");
    }
    
    @GetMapping("/")
    public ResponseEntity<String> welcome() {
        return ResponseEntity.ok("Welcome to the Chatbot API! Send a POST request to /api/chat/message with a JSON body containing 'message' and optional 'sender' fields.");
    }
} 