// OnlineGameView.java
package view;

import controller.NetworkGameController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OnlineGameView extends JFrame {
    private ChessBoardView chessBoard;
    private NetworkGameController controller;
    private JLabel statusLabel;
    private JLabel opponentInfo;
    private JButton undoBtn;
    private JButton restartBtn;
    private JButton musicBtn;
    private JButton surrenderBtn;
    private JButton disconnectBtn;
    private ChatPanel chatPanel;
    private String playerName;
    private boolean isHost;
    private String host; // 添加host字段
    private int port;    // 添加port字段
    
    public OnlineGameView(String playerName, String host, int port, boolean isHost) {
        this.playerName = playerName;
        this.host = host;      // 保存host参数
        this.port = port;      // 保存port参数  
        this.isHost = isHost;
        
        setTitle("五子棋 - 网络对战 (" + (isHost ? "房主" : "玩家") + ")");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new BorderLayout());
        
        // 初始化棋盘
        int boardSize = 15;
        chessBoard = new ChessBoardView(boardSize, null);
        
        // 初始化聊天面板
        chatPanel = new ChatPanel(playerName);
        
        // 创建状态标签
        statusLabel = new JLabel("正在连接...", JLabel.CENTER);
        statusLabel.setFont(new Font("宋体", Font.BOLD, 18));
        
        // 创建控制器
        controller = new NetworkGameController(boardSize, chessBoard, chatPanel, statusLabel, playerName, isHost);
        chessBoard.setController(controller);
        
        // 设置聊天面板的发送消息回调
        chatPanel.setSendMessageCallback(new ChatPanel.SendMessageCallback() {
            @Override
            public void onSendMessage(String message) {
                controller.sendChatMessage(message);
            }
        });
        
        // 顶部信息面板
        JPanel topPanel = createTopPanel();
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
        rightPanel.add(chatPanel);
        
        mainPanel.add(rightPanel, BorderLayout.EAST);
        add(mainPanel, BorderLayout.CENTER);
        
        setVisible(true);
        
        // 连接网络 - 现在使用保存的host和port
        connectToNetwork();
        
        // 窗口关闭事件
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                controller.disconnect();
            }
        });
    }
    
    // 修改connectToNetwork方法，不使用参数
    private void connectToNetwork() {
        new Thread(() -> {
            boolean success;
            if (isHost) {
                success = controller.createRoom(port); // 使用保存的port
            } else {
                success = controller.joinRoom(host, port); // 使用保存的host和port
            }
            
            if (!success) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "连接失败，请检查网络设置", "错误", JOptionPane.ERROR_MESSAGE);
                    new NetworkSetupView();
                    dispose();
                });
            }
        }).start();
    }
    
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // 状态标签
        statusLabel.setFont(new Font("宋体", Font.BOLD, 18));
        
        // 玩家信息
        opponentInfo = new JLabel("你: " + playerName + (isHost ? " (房主)" : ""), JLabel.CENTER);
        opponentInfo.setFont(new Font("宋体", Font.PLAIN, 14));
        
        panel.add(statusLabel);
        panel.add(opponentInfo);
        
        return panel;
    }
    
    private JPanel createFunctionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("游戏控制"));
        
        undoBtn = new JButton("请求悔棋");
        restartBtn = new JButton("重新开始");
        musicBtn = new JButton("播放音乐");
        surrenderBtn = new JButton("认输");
        disconnectBtn = new JButton("断开连接");
        
        Dimension btnSize = new Dimension(120, 40);
        undoBtn.setPreferredSize(btnSize);
        restartBtn.setPreferredSize(btnSize);
        musicBtn.setPreferredSize(btnSize);
        surrenderBtn.setPreferredSize(btnSize);
        disconnectBtn.setPreferredSize(btnSize);
        
        undoBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.requestUndo();
            }
        });
        
        restartBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.restartGame();
            }
        });
        
        musicBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleMusic();
            }
        });
        
        surrenderBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.surrender();
            }
        });
        
        disconnectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disconnect();
            }
        });
        
        panel.add(Box.createVerticalStrut(10));
        panel.add(undoBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(restartBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(musicBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(surrenderBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(disconnectBtn);
        
        return panel;
    }
    
    private void toggleMusic() {
        JOptionPane.showMessageDialog(this, "音乐功能开发中...");
    }
    
    private void disconnect() {
        controller.disconnect();
        new NetworkSetupView();
        dispose();
    }
}
