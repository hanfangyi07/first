// ChatMessage.java
package model;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    private String sender;
    private String content;
    private LocalDateTime timestamp;
    
    public ChatMessage(String sender, String content) {
        this.sender = sender;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }
    
    // 格式化显示消息
    public String getFormattedMessage() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return String.format("[%s] %s: %s", 
                           timestamp.format(formatter), sender, content);
    }
    
    // Getter方法
    public String getSender() { return sender; }
    public String getContent() { return content; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
