// ReplayController.java
package controller;

import model.BoardModel;
import model.GameRecord;
import view.ChessBoardView;
import javax.swing.*;

public class ReplayController {
    private BoardModel model;
    private ChessBoardView view;
    private GameRecord record;
    private int currentMoveIndex;
    private JLabel statusLabel;
    
    public ReplayController(GameRecord record, ChessBoardView view, JLabel statusLabel) {
        this.record = record;
        this.view = view;
        this.statusLabel = statusLabel;
        this.model = new BoardModel(record.getBoardSize());
        this.currentMoveIndex = -1; // -1 表示还没有开始
        
        updateStatus();
    }
    
    // 下一步
    public boolean nextMove() {
        if (record == null || record.getMoves() == null || record.getMoves().isEmpty()) {
            return false;
        }
        
        if (currentMoveIndex < record.getMoves().size() - 1) {
            currentMoveIndex++;
            GameRecord.MoveRecord move = record.getMoves().get(currentMoveIndex);
            
            // 确保设置正确的当前玩家再落子
            model.setCurrentPlayer(move.getPlayer());
            
            if (model.placeStone(move.getX(), move.getY())) {
                view.repaint();
                updateStatus();
                return true;
            }
        }
        return false;
    }
    
    // 上一步
    public boolean previousMove() {
        if (currentMoveIndex >= 0) {
            GameRecord.MoveRecord move = record.getMoves().get(currentMoveIndex);
            model.undoMove(move.getX(), move.getY());
            
            // 手动控制玩家状态，避免undoMove中的自动切换导致的问题
            currentMoveIndex--;
            if (currentMoveIndex >= 0) {
                // 如果还有上一步，设置为上一步的玩家
                model.setCurrentPlayer(record.getMoves().get(currentMoveIndex).getPlayer());
            } else {
                // 如果回到开始，重置为黑棋先行
                model.setCurrentPlayer(BoardModel.BLACK);
            }
            
            view.repaint();
            updateStatus();
            return true;
        }
        return false;
    }
    
    // 第一步
    public void firstMove() {
        resetToStart();
        nextMove();
    }
    
    // 最后一步
    public void lastMove() {
        resetToStart();
        while (nextMove()) {
            // 一直执行到最后一步
        }
    }
    
    // 重置到开始
    public void resetToStart() {
        model.resetGame();
        currentMoveIndex = -1;
        view.repaint();
        updateStatus();
    }
    
    // 更新状态显示
   // 修改ReplayController.java中的updateStatus方法
private void updateStatus() {
    if (statusLabel != null) {
        String status;
        if (currentMoveIndex < 0) {
            status = "准备开始复盘";
        } else if (currentMoveIndex >= record.getMoves().size() - 1) {
            String winnerText = (record.getWinner() != null) ? record.getWinner() : "未决";
            status = String.format("复盘结束 - 获胜方: %s", winnerText);
        } else {
            String currentPlayer = (record.getMoves().get(currentMoveIndex).getPlayer() == 1) ? 
                                  record.getPlayer1() : record.getPlayer2();
            status = String.format("第 %d/%d 步 - %s", 
                currentMoveIndex + 1, record.getTotalMoves(), currentPlayer);
        }
        statusLabel.setText(status);
    }
}
    
    // 获取当前步数
    public int getCurrentMoveIndex() {
        return currentMoveIndex;
    }
    
    // 获取总步数
    public int getTotalMoves() {
        return record.getTotalMoves();
    }
    
    // 获取棋子状态（用于棋盘绘制）
    public int getStone(int x, int y) {
        return model.getStone(x, y);
    }
}
