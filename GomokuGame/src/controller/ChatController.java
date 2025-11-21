// ChatController.java
package controller;

import model.ChatMessage;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ChatController {
    private List<ChatMessage> messageHistory;
    private JTextArea chatDisplay;
    private String currentUser;
    
    public ChatController(JTextArea chatDisplay, String username) {
        this.messageHistory = new ArrayList<>();
        this.chatDisplay = chatDisplay;
        this.currentUser = username;
    }
    
    // 发送消息
    public void sendMessage(String content) {
        if (content == null || content.trim().isEmpty()) {
            return;
        }
        
        ChatMessage message = new ChatMessage(currentUser, content.trim());
        messageHistory.add(message);
        
        // 更新聊天显示
        updateChatDisplay();
        
        // 如果是网络对战，这里会通过网络发送消息
        // 目前先实现本地显示
    }
    
    // 接收消息（网络对战用）
    public void receiveMessage(String sender, String content) {
        ChatMessage message = new ChatMessage(sender, content);
        messageHistory.add(message);
        updateChatDisplay();
    }
    
    // 更新聊天显示区域
    private void updateChatDisplay() {
        StringBuilder displayText = new StringBuilder();
        for (ChatMessage message : messageHistory) {
            displayText.append(message.getFormattedMessage()).append("\n");
        }
        
        SwingUtilities.invokeLater(() -> {
            chatDisplay.setText(displayText.toString());
            // 自动滚动到底部
            chatDisplay.setCaretPosition(chatDisplay.getDocument().getLength());
        });
    }
    
    // 清空聊天记录
    public void clearChat() {
        messageHistory.clear();
        chatDisplay.setText("");
    }
    
    // 获取聊天历史（用于保存）
    public List<ChatMessage> getMessageHistory() {
        return new ArrayList<>(messageHistory);
    }
}
