// NetworkSetupView.java
package view;

import javax.swing.*;
import java.awt.*;

public class NetworkSetupView extends JFrame {
    private JTextField ipField;
    private JTextField portField;
    private JTextField usernameField;
    private JButton hostBtn;
    private JButton joinBtn;
    private JButton backBtn;
    
    public NetworkSetupView() {
        setTitle("网络对战设置");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new BorderLayout());
        
        // 标题
        JLabel titleLabel = new JLabel("网络对战设置", JLabel.CENTER);
        titleLabel.setFont(new Font("宋体", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);
        
        // 中央面板
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(3, 2, 10, 10));
        
        centerPanel.add(new JLabel("服务器IP:"));
        ipField = new JTextField("127.0.0.1");
        centerPanel.add(ipField);
        
        centerPanel.add(new JLabel("端口号:"));
        portField = new JTextField("8080");
        centerPanel.add(portField);
        
        centerPanel.add(new JLabel()); // 空标签占位
        centerPanel.add(new JLabel()); // 空标签占位
        
        add(centerPanel, BorderLayout.CENTER);
        
        // 按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        
        hostBtn = new JButton("创建房间");
        joinBtn = new JButton("加入房间");
        backBtn = new JButton("返回");
        
        buttonPanel.add(hostBtn);
        buttonPanel.add(joinBtn);
        buttonPanel.add(backBtn);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
     // 按钮事件 - 现在打开测试用的在线游戏界面
        hostBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            if (username.isEmpty()) {
                username = "房主";
            }
            new OnlineGameView(username, "等待对手...", true);
            dispose();
        });
        
        joinBtn.addActionListener(e -> {
            String username = usernameField.getText().trim();
            if (username.isEmpty()) {
                username = "玩家";
            }
            new OnlineGameView(username, "远程玩家", false);
            dispose();
        });
        
        backBtn.addActionListener(e -> {
            new StartView();
            dispose();
        });
        
        setVisible(true);
    }
}