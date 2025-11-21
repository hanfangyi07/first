// 在GameController.java中添加以下方法
package controller;

import model.BoardModel;
import model.GameRecord;
import view.ChessBoardView;
import utils.GameRecordManager;
import utils.MusicPlayer;
import javax.swing.*;
import java.util.Stack;
public class GameController {
    private BoardModel model;
    private ChessBoardView view;
    private JLabel statusLabel;
    private MusicPlayer musicPlayer;
    private Stack<int[]> moveHistory; // 悔棋用的历史记录
    private boolean gameActive;
    private GameRecord currentRecord;
    private GameRecordManager recordManager;
    private String player1Name;
    private String player2Name;
    public GameController(int boardSize, ChessBoardView view, JLabel statusLabel) {
        this.model = new BoardModel(boardSize);
        this.view = view;
        this.statusLabel = statusLabel;
        this.moveHistory = new Stack<>();
        this.gameActive = true;
        this.musicPlayer = new MusicPlayer();
        this.recordManager = new GameRecordManager();
     // 默认玩家名称
        this.player1Name = "玩家1";
        this.player2Name = "玩家2";
        
        // 创建新的游戏记录
        this.currentRecord = new GameRecord(player1Name, player2Name, boardSize);
        
        // 尝试加载背景音乐（需要你有音乐文件）
        try {
            musicPlayer.playBackgroundMusic("resources/music/background.wav");
        } catch (Exception e) {
            System.out.println("背景音乐加载失败: " + e.getMessage());
        }
    }
    // 开始记录游戏
    public void startRecording(String blackPlayer, String whitePlayer) {
        this.currentRecord = new GameRecord(blackPlayer, whitePlayer, model.getBoardSize());
      
    }
    // 设置玩家名称
    public void setPlayerNames(String player1, String player2) {
        this.player1Name = player1;
        this.player2Name = player2;
        this.currentRecord = new GameRecord(player1, player2, model.getBoardSize());
    }
    // 设置状态标签（在LocalGameView构造后调用）
    public void setStatusLabel(JLabel statusLabel) {
        this.statusLabel = statusLabel;
        updateStatus();
    }
    
    public void handleMove(int x, int y) {
        if (!gameActive) return;
        
        if (model.placeStone(x, y)) {
            // 记录这一步
            moveHistory.push(new int[]{x, y, model.getCurrentPlayer()});
            
            // 记录到游戏记录中
            currentRecord.addMove(x, y, model.getCurrentPlayer());
            
            view.repaint();
            
            // 检查胜负
            int winner = model.checkWinner(x, y);
            if (winner != BoardModel.EMPTY) {
                gameActive = false;
                String winnerText = (winner == BoardModel.BLACK) ? player1Name : player2Name;
                currentRecord.endGame(winnerText);
                
                JOptionPane.showMessageDialog(view, winnerText + "获胜！", "游戏结束", JOptionPane.INFORMATION_MESSAGE);
            } else {
                model.switchPlayer();
                updateStatus();
            }
        }
    }
    
 // 保存游戏记录
    public boolean saveGameRecord(String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            filename = generateDefaultFilename();
        }
        return recordManager.saveGameRecord(currentRecord, filename);
    }
    
    // 生成默认文件名
    private String generateDefaultFilename() {
        return String.format("game_%tY%tm%td_%tH%tM", 
                           System.currentTimeMillis(), System.currentTimeMillis(),
                           System.currentTimeMillis(), System.currentTimeMillis(),
                           System.currentTimeMillis());
    }
    // 悔棋功能
    public void undoMove() {
        if (moveHistory.size() < 2 || !gameActive) {
            JOptionPane.showMessageDialog(view, "无法悔棋！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 悔两步（对方和本方各一步）
        int[] lastMove1 = moveHistory.pop();
        int[] lastMove2 = moveHistory.pop();
        
        model.undoMove(lastMove1[0], lastMove1[1]);
        model.undoMove(lastMove2[0], lastMove2[1]);
        
        view.repaint();
        updateStatus();
    }
    
    public void resetGame() {
    	 model.resetGame();
         moveHistory.clear();
         gameActive = true;
         // 创建新的游戏记录
         currentRecord = new GameRecord(player1Name, player2Name, model.getBoardSize());
         view.repaint();
         updateStatus();
    }
    
    private void updateStatus() {
        if (statusLabel != null) {
            String playerText = (model.getCurrentPlayer() == BoardModel.BLACK) ? player1Name : player2Name;
            statusLabel.setText("当前回合: " + playerText);
        }
    }
    
    // 音乐控制
    public void toggleMusic() {
        musicPlayer.toggleMusic();
    }
    
    public boolean isMusicPlaying() {
        return musicPlayer.isPlaying();
    }
    
    public int getStone(int x, int y) {
        return model.getStone(x, y);
    }
    // 获取游戏记录管理器
    public GameRecordManager getRecordManager() {
        return recordManager;
    }
}
