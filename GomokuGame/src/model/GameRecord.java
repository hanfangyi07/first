// GameRecord.java
package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String player1;
    private String player2;
    private String winner;
    private long startTime;
    private long endTime;
    private List<MoveRecord> moves;
    private int boardSize;
    
    public GameRecord(String player1, String player2, int boardSize) {
        this.player1 = player1;
        this.player2 = player2;
        this.boardSize = boardSize;
        this.moves = new ArrayList<>();
        this.startTime = System.currentTimeMillis();
    }
    
    // 添加一步棋
    public void addMove(int x, int y, int player) {
        moves.add(new MoveRecord(x, y, player, System.currentTimeMillis()));
    }
    
    // 结束游戏
    public void endGame(String winner) {
        this.winner = winner;
        this.endTime = System.currentTimeMillis();
    }
    
    // 获取对局时长（分钟）
    public int getGameDuration() {
        long duration = (endTime - startTime) / 1000 / 60;
        return (int) Math.max(1, duration);
    }
    
    // Getter方法
    public String getPlayer1() { return player1; }
    public String getPlayer2() { return player2; }
    public String getWinner() { return winner; }
    public List<MoveRecord> getMoves() { return moves; }
    public int getBoardSize() { return boardSize; }
    public int getTotalMoves() { return moves.size(); }
    
    // 内部类：每一步的记录
    public static class MoveRecord implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private int x;
        private int y;
        private int player; // 1:黑棋, 2:白棋
        private long timestamp;
        
        public MoveRecord(int x, int y, int player, long timestamp) {
            this.x = x;
            this.y = y;
            this.player = player;
            this.timestamp = timestamp;
        }
        
        // Getter方法
        public int getX() { return x; }
        public int getY() { return y; }
        public int getPlayer() { return player; }
        public long getTimestamp() { return timestamp; }
    }
}
