// 在ChessBoardView.java中添加setController方法
package view;

import javax.swing.*;

import controller.GameController;
import model.BoardModel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ChessBoardView extends JPanel {
    private static final int MARGIN = 30;
    private static final int GRID_SPACING = 40;
    
    private int boardSize;
    private GameController controller;
    private StoneProvider stoneProvider; // 用于复盘模式
    
    // 棋子提供者接口（用于复盘）
    public interface StoneProvider {
        int getStone(int x, int y);
    }
    
    public ChessBoardView(int size, GameController controller) {
        this.boardSize = size;
        this.controller = controller;
        setPreferredSize(new Dimension(
            MARGIN * 2 + GRID_SPACING * (size - 1),
            MARGIN * 2 + GRID_SPACING * (size - 1)
        ));
        
        // 只有在游戏模式下才添加鼠标监听
        if (controller != null) {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    handleMouseClick(e.getX(), e.getY());
                }
            });
        }
    }
    
    // 设置控制器的方法
    public void setController(GameController controller) {
        this.controller = controller;
        // 移除所有监听器再重新添加
        removeAllMouseListeners();
        if (controller != null) {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    handleMouseClick(e.getX(), e.getY());
                }
            });
        }
    }
 // 设置棋子提供者（用于复盘模式）
    public void setStoneProvider(StoneProvider provider) {
        this.stoneProvider = provider;
        // 复盘模式下移除鼠标监听
        removeAllMouseListeners();
    }
    private void removeAllMouseListeners() {
        for (java.awt.event.MouseListener listener : getMouseListeners()) {
            removeMouseListener(listener);
        }
    }
 

    private void handleMouseClick(int mouseX, int mouseY) {
        if (controller == null) return;
        
        // 转换为棋盘坐标
        int x = Math.round((float)(mouseX - MARGIN) / GRID_SPACING);
        int y = Math.round((float)(mouseY - MARGIN) / GRID_SPACING);
        
        if (x >= 0 && x < boardSize && y >= 0 && y < boardSize) {
            controller.handleMove(x, y);
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        drawStones(g);
    }
    
    private void drawBoard(Graphics g) {
        g.setColor(new Color(210, 180, 140)); // 棋盘背景色
        g.fillRect(0, 0, getWidth(), getHeight());
        
        g.setColor(Color.BLACK);
        // 画网格线
        for (int i = 0; i < boardSize; i++) {
            // 横线
            g.drawLine(MARGIN, MARGIN + i * GRID_SPACING, 
                      MARGIN + (boardSize - 1) * GRID_SPACING, MARGIN + i * GRID_SPACING);
            // 竖线  
            g.drawLine(MARGIN + i * GRID_SPACING, MARGIN,
                      MARGIN + i * GRID_SPACING, MARGIN + (boardSize - 1) * GRID_SPACING);
        }
    }
    
    private void drawStones(Graphics g) {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                int stone = getStoneAt(i, j);
                if (stone != BoardModel.EMPTY) {
                    drawStone(g, i, j, stone == BoardModel.BLACK);
                }
            }
        }
    }
    
    private int getStoneAt(int x, int y) {
        if (stoneProvider != null) {
            // 复盘模式
            return stoneProvider.getStone(x, y);
        } else if (controller != null) {
            // 游戏模式
            return controller.getStone(x, y);
        }
        return BoardModel.EMPTY;
    }
    
    private void drawStone(Graphics g, int x, int y, boolean isBlack) {
        g.setColor(isBlack ? Color.BLACK : Color.WHITE);
        int centerX = MARGIN + x * GRID_SPACING;
        int centerY = MARGIN + y * GRID_SPACING;
        
        g.fillOval(centerX - 18, centerY - 18, 36, 36);
        
        if (!isBlack) {
            g.setColor(Color.BLACK);
            g.drawOval(centerX - 18, centerY - 18, 36, 36);
        }
    }
}