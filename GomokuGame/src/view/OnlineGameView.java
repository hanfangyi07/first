// OnlineGameView.java
package view;

import controller.GameController;
import javax.swing.*;
import java.awt.*;

public class OnlineGameView extends JFrame {
    private ChessBoardView chessBoard;
    private GameController controller;
    private JLabel statusLabel;
    private JLabel opponentInfo;
    private JButton undoBtn;
    private JButton restartBtn;
    private JButton musicBtn;
    private JButton surrenderBtn;
    private ChatPanel chatPanel;
    private String playerName;
    private String opponentName;
    
    public OnlineGameView(String playerName, String opponentName, boolean isHost) {
        this.playerName = playerName;
        this.opponentName = opponentName;
        
        setTitle("五子棋 - 网络对战 (" + (isHost ? "房主" : "玩家") + ")");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new BorderLayout());
        
        // 初始化棋盘
        int boardSize = 15;
        chessBoard = new ChessBoardView(boardSize, null);
        controller = new GameController(boardSize, chessBoard, null);
        chessBoard.setController(controller);
        
        // 顶部信息面板
        JPanel topPanel = createTopPanel(isHost);
        add(topPanel, BorderLayout.NORTH);
        
        // 主内容面板
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(chessBoard, BorderLayout.CENTER);
        
        // 右侧面板
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        
        // 功能按钮面板
        JPanel functionPanel = createFunctionPanel();
        rightPanel.add(functionPanel);
        
        // 聊天面板
        chatPanel = new ChatPanel(playerName);
        rightPanel.add(chatPanel);
        
        mainPanel.add(rightPanel, BorderLayout.EAST);
        add(mainPanel, BorderLayout.CENTER);
        
        // 更新控制器
        controller.setStatusLabel(statusLabel);
        
        setVisible(true);
        
        // 模拟接收欢迎消息
        SwingUtilities.invokeLater(() -> {
            chatPanel.receiveMessage("系统", "欢迎来到五子棋网络对战！");
            chatPanel.receiveMessage("系统", "你的对手: " + opponentName);
        });
    }
    
    private JPanel createTopPanel(boolean isHost) {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // 状态标签
        statusLabel = new JLabel("当前回合: " + (isHost ? "黑棋(你)" : "等待对手"), JLabel.CENTER);
        statusLabel.setFont(new Font("宋体", Font.BOLD, 18));
        
        // 对手信息
        String hostInfo = isHost ? " (房主)" : "";
        opponentInfo = new JLabel("你: " + playerName + hostInfo + " | 对手: " + opponentName, JLabel.CENTER);
        opponentInfo.setFont(new Font("宋体", Font.PLAIN, 14));
        
        panel.add(statusLabel);
        panel.add(opponentInfo);
        
        return panel;
    }
    
    private JPanel createFunctionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("游戏控制"));
        
        undoBtn = new JButton("悔棋");
        restartBtn = new JButton("重新开始");
        musicBtn = new JButton("播放音乐");
        surrenderBtn = new JButton("认输");
        
        Dimension btnSize = new Dimension(120, 40);
        undoBtn.setPreferredSize(btnSize);
        restartBtn.setPreferredSize(btnSize);
        musicBtn.setPreferredSize(btnSize);
        surrenderBtn.setPreferredSize(btnSize);
        
        // 网络对战暂时禁用悔棋和重新开始
        undoBtn.setEnabled(false);
        restartBtn.setEnabled(false);
        
        undoBtn.addActionListener(e -> controller.undoMove());
        restartBtn.addActionListener(e -> controller.resetGame());
        musicBtn.addActionListener(e -> toggleMusic());
        surrenderBtn.addActionListener(e -> surrender());
        
        panel.add(Box.createVerticalStrut(10));
        panel.add(undoBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(restartBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(musicBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(surrenderBtn);
        
        return panel;
    }
    
    private void toggleMusic() {
        controller.toggleMusic();
        musicBtn.setText(controller.isMusicPlaying() ? "停止音乐" : "播放音乐");
    }
    
    private void surrender() {
        int result = JOptionPane.showConfirmDialog(this, 
            "确定要认输吗？", "认输确认", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "你已认输，游戏结束！");
            // 这里可以添加网络通知对手的逻辑
        }
    }
    
    // 获取聊天面板（用于网络通信）
    public ChatPanel getChatPanel() {
        return chatPanel;
    }
}