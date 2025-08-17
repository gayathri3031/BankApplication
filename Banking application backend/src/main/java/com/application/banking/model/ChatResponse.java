package com.application.banking.model;

public class ChatResponse {
    
    private String response;
    private String botName;
    private long timestamp;
    
    public ChatResponse() {
        this.timestamp = System.currentTimeMillis();
        this.botName = "ChatBot";
    }
    
    public ChatResponse(String response) {
        this();
        this.response = response;
    }
    
    public String getResponse() {
        return response;
    }
    
    public void setResponse(String response) {
        this.response = response;
    }
    
    public String getBotName() {
        return botName;
    }
    
    public void setBotName(String botName) {
        this.botName = botName;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    @Override
    public String toString() {
        return "ChatResponse{" +
                "response='" + response + '\'' +
                ", botName='" + botName + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
} 