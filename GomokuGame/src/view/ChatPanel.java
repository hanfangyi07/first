// ChatPanel.java
package view;

import controller.ChatController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ChatPanel extends JPanel {
    private JTextArea chatDisplay;
    private JTextField messageInput;
    private JButton sendButton;
    private ChatController chatController;
    private String username;
    
    public ChatPanel(String username) {
        this.username = username;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(300, 400));
        setBorder(BorderFactory.createTitledBorder("聊天室"));
        
        initializeComponents();
        setupLayout();
        setupEventListeners();
    }
    
    private void initializeComponents() {
        // 聊天显示区域
        chatDisplay = new JTextArea();
        chatDisplay.setEditable(false);
        chatDisplay.setLineWrap(true);
        chatDisplay.setWrapStyleWord(true);
        chatDisplay.setBackground(new Color(240, 240, 240));
        
        // 添加滚动条
        JScrollPane scrollPane = new JScrollPane(chatDisplay);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        // 消息输入区域
        messageInput = new JTextField();
        messageInput.setToolTipText("输入消息...");
        
        // 发送按钮
        sendButton = new JButton("发送");
        
        // 创建聊天控制器
        chatController = new ChatController(chatDisplay, username);
    }
    
    private void setupLayout() {
        // 聊天显示区域
        JScrollPane scrollPane = new JScrollPane(chatDisplay);
        scrollPane.setPreferredSize(new Dimension(280, 300));
        add(scrollPane, BorderLayout.CENTER);
        
        // 输入面板
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(messageInput, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        
        add(inputPanel, BorderLayout.SOUTH);
    }
    
    private void setupEventListeners() {
        // 发送按钮点击事件
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        
        // 输入框回车事件
        messageInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });
    }
    
    private void sendMessage() {
        String message = messageInput.getText().trim();
        if (!message.isEmpty()) {
            chatController.sendMessage(message);
            messageInput.setText("");
        }
        messageInput.requestFocus();
    }
    
    // 接收消息的方法（用于网络对战）
    public void receiveMessage(String sender, String content) {
        chatController.receiveMessage(sender, content);
    }
    
    // 清空聊天
    public void clearChat() {
        chatController.clearChat();
    }
    
    // Getter方法
    public ChatController getChatController() {
        return chatController;
    }
}