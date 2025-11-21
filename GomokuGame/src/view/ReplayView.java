// ReplayView.java
package view;

import controller.ReplayController;
import model.GameRecord;
import utils.GameRecordManager;
import javax.swing.*;
import java.awt.*;
import java.awt.Color;

public class ReplayView extends JFrame {
    private ChessBoardView chessBoard;
    private ReplayController controller;
    private JLabel statusLabel;
    private JButton firstBtn;
    private JButton prevBtn;
    private JButton nextBtn;
    private JButton lastBtn;
    private JButton loadBtn;
    private JButton backBtn;
    private GameRecordManager recordManager;
    
    public ReplayView() {
        setTitle("五子棋 - 复盘模式");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        recordManager = new GameRecordManager();
        
        // 使用 BorderLayout 确保稳定布局
        setLayout(new BorderLayout(5, 5));
        
        // 状态栏
        statusLabel = new JLabel("请加载游戏记录", JLabel.CENTER);
        statusLabel.setFont(new Font("宋体", Font.BOLD, 16));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(statusLabel, BorderLayout.NORTH);
        
        // 创建默认棋盘
        chessBoard = new ChessBoardView(15, null);
        
        // 主内容面板 - 使用 BorderLayout 确保稳定
        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        mainPanel.add(chessBoard, BorderLayout.CENTER);
        
        // 右侧控制面板
        JPanel controlPanel = createControlPanel();
        mainPanel.add(controlPanel, BorderLayout.EAST);
        
        add(mainPanel, BorderLayout.CENTER);
        
        setVisible(true);
    }
    
    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 0, 10)); // 改为 GridLayout，确保每个按钮都有固定位置
        panel.setBorder(BorderFactory.createTitledBorder("复盘控制"));
        panel.setPreferredSize(new Dimension(200, 600));
        panel.setBackground(Color.WHITE);

        // 控制按钮
        firstBtn = createStyledButton("第一步", new Color(220, 220, 220));
        prevBtn = createStyledButton("上一步", new Color(220, 220, 220));
        nextBtn = createStyledButton("下一步", new Color(220, 220, 220));
        lastBtn = createStyledButton("最后一步", new Color(220, 220, 220));
        loadBtn = createStyledButton("加载记录", new Color(200, 230, 255));
        backBtn = createStyledButton("返回", new Color(255, 200, 200));

        // 设置按钮大小
        Dimension btnSize = new Dimension(150, 45);
        firstBtn.setPreferredSize(btnSize);
        prevBtn.setPreferredSize(btnSize);
        nextBtn.setPreferredSize(btnSize);
        lastBtn.setPreferredSize(btnSize);
        loadBtn.setPreferredSize(btnSize);
        backBtn.setPreferredSize(btnSize);

        // 初始禁用步进按钮
        setStepButtonsEnabled(false);

        // 添加事件监听（保持不变）
        firstBtn.addActionListener(e -> {
            if (controller != null) {
                controller.firstMove();
                updateStepButtons();
            }
        });
        
        prevBtn.addActionListener(e -> {
            if (controller != null) {
                controller.previousMove();
                updateStepButtons();
            }
        });
        
        nextBtn.addActionListener(e -> {
            if (controller != null) {
                controller.nextMove();
                updateStepButtons();
            }
        });
        
        lastBtn.addActionListener(e -> {
            if (controller != null) {
                controller.lastMove();
                updateStepButtons();
            }
        });
        
        loadBtn.addActionListener(e -> loadGameRecord());
        backBtn.addActionListener(e -> {
            new StartView();
            dispose();
        });

        // 使用固定顺序添加所有按钮
        panel.add(firstBtn);
        panel.add(prevBtn);
        panel.add(nextBtn);
        panel.add(lastBtn);
        panel.add(createSeparator());
        panel.add(loadBtn);
        panel.add(backBtn);

        return panel;
    }
 // 添加辅助方法创建统一样式的按钮
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.DARK_GRAY, 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        button.setFocusPainted(false);
        return button;
    }

    // 添加分隔符
    private JComponent createSeparator() {
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setForeground(Color.GRAY);
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.add(separator);
        return panel;
    }
 private void loadGameRecord() {
        // 获取保存的游戏记录列表
        java.util.List<String> savedGames = recordManager.getSavedGames();
        
        if (savedGames.isEmpty()) {
            JOptionPane.showMessageDialog(this, "没有找到保存的游戏记录！", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // 创建选择对话框
        String[] options = savedGames.toArray(new String[0]);
        String selected = (String) JOptionPane.showInputDialog(this,
            "选择要复盘的记录:", "加载游戏记录",
            JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        
        if (selected != null) {
            GameRecord record = recordManager.loadGameRecord(selected);
            if (record != null) {
                setupReplay(record);
            } else {
                JOptionPane.showMessageDialog(this, "加载游戏记录失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
 private void setupReplay(GameRecord record) {
	    // 移除旧棋盘（如果存在）
	    Component[] components = getContentPane().getComponents();
	    for (Component component : components) {
	        if (component instanceof JPanel) {
	            JPanel panel = (JPanel) component;
	            // 查找包含棋盘的主面板
	            for (Component comp : panel.getComponents()) {
	                if (comp instanceof ChessBoardView) {
	                    panel.remove(comp);
	                    break;
	                }
	            }
	        }
	    }
	    
	    // 创建新棋盘
	    chessBoard = new ChessBoardView(record.getBoardSize(), null);
	    
	    // 创建控制器
	    controller = new ReplayController(record, chessBoard, statusLabel);
	    
	    // 设置棋子提供者
	    chessBoard.setStoneProvider((x, y) -> controller.getStone(x, y));
	    
	    // 将新棋盘添加到主内容面板
	    JPanel mainPanel = (JPanel) getContentPane().getComponent(1); // 获取主内容面板
	    mainPanel.add(chessBoard, BorderLayout.CENTER);
	    
	    setStepButtonsEnabled(true);
	    updateStepButtons();
	    
	    // 强制刷新整个界面
	    revalidate();
	    repaint();
	    
	    // 显示对局信息
	    String gameInfo = String.format("对局: %s vs %s | 棋盘: %dx%d | 总步数: %d", 
	        record.getPlayer1(), record.getPlayer2(), 
	        record.getBoardSize(), record.getBoardSize(), record.getTotalMoves());
	    statusLabel.setText(gameInfo);
	}
    
    private void setStepButtonsEnabled(boolean enabled) {
        firstBtn.setEnabled(enabled);
        prevBtn.setEnabled(enabled);
        nextBtn.setEnabled(enabled);
        lastBtn.setEnabled(enabled);
        // 更新按钮外观
        updateButtonAppearance(firstBtn, enabled);
        updateButtonAppearance(prevBtn, enabled);
        updateButtonAppearance(nextBtn, enabled);
        updateButtonAppearance(lastBtn, enabled);
    }
 // 更新按钮外观
    private void updateButtonAppearance(JButton button, boolean enabled) {
        if (enabled) {
            button.setBackground(new Color(220, 220, 220));
            button.setForeground(Color.BLACK);
        } else {
            button.setBackground(new Color(240, 240, 240));
            button.setForeground(Color.GRAY);
        }
        button.repaint();
    }
    private void updateStepButtons() {
        if (controller == null) return;
        
        int current = controller.getCurrentMoveIndex();
        int total = controller.getTotalMoves();
        
        boolean hasPrevious = current > 0;
        boolean hasNext = current < total - 1;
        
        firstBtn.setEnabled(hasPrevious);
        prevBtn.setEnabled(hasPrevious);
        nextBtn.setEnabled(hasNext);
        lastBtn.setEnabled(hasNext);
        
        // 更新按钮文本显示当前进度
        if (total > 0) {
            firstBtn.setText("第一步(" + (hasPrevious ? "✓" : " ") + ")");
            prevBtn.setText("上一步(" + (hasPrevious ? "✓" : " ") + ")");
            nextBtn.setText("下一步(" + (hasNext ? "✓" : " ") + ")");
            lastBtn.setText("最后一步(" + (hasNext ? "✓" : " ") + ")");
        }
        
        // 强制重绘所有按钮
        firstBtn.repaint();
        prevBtn.repaint();
        nextBtn.repaint();
        lastBtn.repaint();
        loadBtn.repaint();
        backBtn.repaint();
    }
}
