package com.application.banking.model;

import jakarta.validation.constraints.NotBlank;

public class ChatMessage {
    
    @NotBlank(message = "Message cannot be empty")
    private String message;
    
    private String sender;
    
    public ChatMessage() {}
    
    public ChatMessage(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getSender() {
        return sender;
    }
    
    public void setSender(String sender) {
        this.sender = sender;
    }
    
    @Override
    public String toString() {
        return "ChatMessage{" +
                "message='" + message + '\'' +
                ", sender='" + sender + '\'' +
                '}';
    }
} 