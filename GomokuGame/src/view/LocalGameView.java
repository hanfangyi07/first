// LocalGameView.java
package view;

import javax.swing.*;
import java.awt.*;
import controller.GameController;

public class LocalGameView extends JFrame {
    private ChessBoardView chessBoard;
    private GameController controller;
    private JLabel statusLabel;
    private JButton undoBtn;
    private JButton restartBtn;
    private JButton musicBtn;
    private ChatPanel chatPanel;
    public LocalGameView() {
        setTitle("五子棋 - 单机对战");
        setSize(1000, 700); // 增加窗口宽度以容纳聊天面板
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // 设置布局
        setLayout(new BorderLayout());
        
        // 初始化棋盘
        int boardSize = 15;
        chessBoard = new ChessBoardView(boardSize, null);
        controller = new GameController(boardSize, chessBoard, null);
        controller.startRecording("玩家1(黑)", "玩家2(白)");
        chessBoard.setController(controller);
        
        // 创建状态栏
        statusLabel = new JLabel("当前回合: 黑棋", JLabel.CENTER);
        statusLabel.setFont(new Font("宋体", Font.BOLD, 16));
        add(statusLabel, BorderLayout.NORTH);
        
        // 主内容面板
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // 添加棋盘
        mainPanel.add(chessBoard, BorderLayout.CENTER);
        
        // 创建右侧面板（功能按钮 + 聊天）
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        
        // 功能按钮面板
        JPanel functionPanel = createSidePanel();
        rightPanel.add(functionPanel);
        
        // 聊天面板
        chatPanel = new ChatPanel("玩家"); // 默认用户名
        rightPanel.add(chatPanel);
        
        mainPanel.add(rightPanel, BorderLayout.EAST);
        add(mainPanel, BorderLayout.CENTER);
        
        // 更新控制器的状态标签引用
        controller.setStatusLabel(statusLabel);
        
        setVisible(true);
    }
    private JPanel createSidePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // 功能按钮
        undoBtn = new JButton("悔棋");
        restartBtn = new JButton("重新开始");
        musicBtn = new JButton("播放音乐");
        JButton saveBtn = new JButton("保存对局"); // 新增保存按钮
        
        
        // 设置按钮大小
        Dimension btnSize = new Dimension(120, 40);
        undoBtn.setPreferredSize(btnSize);
        restartBtn.setPreferredSize(btnSize);
        musicBtn.setPreferredSize(btnSize);
        saveBtn.setPreferredSize(btnSize);
        
        // 添加按钮事件
        undoBtn.addActionListener(e -> controller.undoMove());
        restartBtn.addActionListener(e -> controller.resetGame());
        musicBtn.addActionListener(e -> toggleMusic());
        saveBtn.addActionListener(e -> saveGame()); 
        
        panel.add(Box.createVerticalStrut(20));
        panel.add(undoBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(restartBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(musicBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(saveBtn);
        
       
        
        return panel;
    }
 // 添加保存游戏的方法
    private void saveGame() {
        String filename = JOptionPane.showInputDialog(this, 
            "输入保存的文件名（可选）:", "保存对局", JOptionPane.QUESTION_MESSAGE);
        
        boolean success = controller.saveGameRecord(filename);
        if (success) {
            JOptionPane.showMessageDialog(this, "对局保存成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "对局保存失败！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void toggleMusic() {
        // 音乐控制逻辑
        controller.toggleMusic();
        musicBtn.setText(controller.isMusicPlaying() ? "停止音乐" : "播放音乐");
    }
}
