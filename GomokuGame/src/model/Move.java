// Move.java
package model;

import java.io.Serializable;

public class Move implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int x;
    private int y;
    private int player; // BoardModel.BLACK 或 BoardModel.WHITE
    private int step;   // 第几步
    
    public Move(int x, int y, int player, int step) {
        this.x = x;
        this.y = y;
        this.player = player;
        this.step = step;
    }
    
    // Getter方法
    public int getX() { return x; }
    public int getY() { return y; }
    public int getPlayer() { return player; }
    public int getStep() { return step; }
    
    @Override
    public String toString() {
        String playerStr = (player == BoardModel.BLACK) ? "黑" : "白";
        return String.format("第%d步: %s棋 (%d, %d)", step, playerStr, x, y);
    }
}