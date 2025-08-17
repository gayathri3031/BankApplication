package com.application.banking.service;

import org.springframework.stereotype.Service;

import com.application.banking.model.ChatMessage;
import com.application.banking.model.ChatResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class ChatbotService {
    
    private final Map<String, String> responses;
    private final String[] defaultResponses;
    private final Random random;
    
    public ChatbotService() {
        this.random = new Random();
        this.responses = new HashMap<>();
        this.defaultResponses = new String[]{
            "That's interesting! Tell me more.",
            "I understand. How does that make you feel?",
            "Could you elaborate on that?",
            "That's a great point!",
            "I see what you mean.",
            "Thanks for sharing that with me.",
            "How can I help you with that?",
            "That sounds important to you."
        };
        
        initializeResponses();
    }
    
    private void initializeResponses() {
        // Greetings
        responses.put("hello", "Hello! How can I help you today?");
        responses.put("hi", "Hi there! What's on your mind?");
        responses.put("hey", "Hey! How are you doing?");
        responses.put("good morning", "Good morning! Hope you're having a great day!");
        responses.put("good evening", "Good evening! How has your day been?");
        
        // Common questions
        responses.put("how are you", "I'm doing well, thank you for asking! How are you?");
        responses.put("what is your name", "I'm ChatBot, your friendly AI assistant!");
        responses.put("who are you", "I'm ChatBot, here to help and chat with you!");
        responses.put("what can you do", "I can chat with you, answer questions, and provide assistance. What would you like to talk about?");
        
        // Farewells
        responses.put("bye", "Goodbye! It was nice chatting with you!");
        responses.put("goodbye", "Goodbye! Take care and have a wonderful day!");
        responses.put("see you later", "See you later! Looking forward to our next conversation!");
        
        // Help
        responses.put("help", "I'm here to help! You can ask me questions, have a conversation, or just say hello. What would you like to do?");
        
        // Weather (simple responses)
        responses.put("weather", "I wish I could check the weather for you! You might want to check a weather app or website for accurate information.");
        responses.put("how is the weather", "I don't have access to current weather data, but I hope it's nice where you are!");
        
        // Time
        responses.put("what time", "I don't have access to the current time, but you can check your device's clock!");
        responses.put("time", "Time flies when you're having a good conversation, doesn't it?");
    }
    
    public ChatResponse processMessage(ChatMessage message) {
        String userMessage = message.getMessage().toLowerCase().trim();
        String response = findResponse(userMessage);
        return new ChatResponse(response);
    }
    
    private String findResponse(String message) {
        // Check for exact matches first
        if (responses.containsKey(message)) {
            return responses.get(message);
        }
        
        // Check for partial matches
        for (Map.Entry<String, String> entry : responses.entrySet()) {
            if (message.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        
        // Return a random default response
        return defaultResponses[random.nextInt(defaultResponses.length)];
    }
} 