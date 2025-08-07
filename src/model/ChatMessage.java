package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatMessage {
    public enum MessageType {
        USER, ASSISTANT, SYSTEM
    }
    
    private String content;
    private MessageType type;
    private LocalDateTime timestamp;
    
    public ChatMessage(String content, MessageType type) {
        this.content = content;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters and setters
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public MessageType getType() {
        return type;
    }
    
    public void setType(MessageType type) {
        this.type = type;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getFormattedTimestamp() {
        return timestamp.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s: %s", 
            getFormattedTimestamp(), 
            type.toString(), 
            content);
    }
}
