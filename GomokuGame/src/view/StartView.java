// StartView.java
package view;

import javax.swing.*;
import java.awt.*;

public class StartView extends JFrame {
    public StartView() {
        setTitle("五子棋游戏");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // 使用BorderLayout布局
        setLayout(new BorderLayout());
        
        // 标题
        JLabel titleLabel = new JLabel("五子棋", JLabel.CENTER);
        titleLabel.setFont(new Font("宋体", Font.BOLD, 36));
        add(titleLabel, BorderLayout.NORTH);
        
        // 按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 10));
        
        JButton localBtn = new JButton("单机对战");
        JButton onlineBtn = new JButton("网络对战"); 
        JButton replayBtn = new JButton("对局复盘"); 
        JButton helpBtn = new JButton("游戏说明");
        
        buttonPanel.add(localBtn);
        buttonPanel.add(onlineBtn);
        buttonPanel.add(replayBtn); 
        buttonPanel.add(helpBtn);
        
        add(buttonPanel, BorderLayout.CENTER);
        
     // 在StartView.java中更新按钮事件
        replayBtn.addActionListener(e -> {
            new ReplayView();
            dispose();
        });
     localBtn.addActionListener(e -> {
         new LocalGameView();
         dispose();
     });

     onlineBtn.addActionListener(e -> {
         new NetworkSetupView();
         dispose();
     });
        
        helpBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                "游戏规则：\n1. 黑白双方轮流落子\n2. 先将五子连成一线者获胜\n3. 横、竖、斜线均可",
                "游戏说明", JOptionPane.INFORMATION_MESSAGE);
        });
        
        setVisible(true);
    }
}
