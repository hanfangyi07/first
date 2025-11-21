// BoardModel.java
package model;

public class BoardModel {
    public static final int EMPTY = 0;
    public static final int BLACK = 1;
    public static final int WHITE = 2;
    
    private int[][] board;  // 棋盘数据
    private int currentPlayer; // 当前玩家
    private int boardSize;  // 棋盘大小
    // 设置当前玩家（复盘时需要）
    public void setCurrentPlayer(int player) {
        if (player == BLACK || player == WHITE) {
            this.currentPlayer = player;
        }
    }
    
 // 悔棋：移除指定位置的棋子
    public void undoMove(int x, int y) {
        if (x >= 0 && x < boardSize && y >= 0 && y < boardSize) {
            board[x][y] = EMPTY;
        }
        // 移除自动切换玩家的逻辑，让控制器来管理玩家状态
        // 这样可以确保复盘时的玩家状态与记录完全一致
    }
    
    // 重置游戏
    public void resetGame() {
        initializeBoard();
        currentPlayer = BLACK;
    }
    public BoardModel(int size) {
        this.boardSize = size;
        this.board = new int[size][size];
        this.currentPlayer = BLACK; // 黑棋先行
        initializeBoard();
    }
    
    private void initializeBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = EMPTY;
            }
        }
    }
    
    // 落子
    public boolean placeStone(int x, int y) {
        if (x < 0 || x >= boardSize || y < 0 || y >= boardSize) {
            return false;
        }
        if (board[x][y] != EMPTY) {
            return false;
        }
        
        board[x][y] = currentPlayer;
        return true;
    }
    
    // 切换玩家
    public void switchPlayer() {
        currentPlayer = (currentPlayer == BLACK) ? WHITE : BLACK;
    }
    
    // 检查胜负
    public int checkWinner(int x, int y) {
        int[][] directions = {
            {1, 0}, {0, 1}, {1, 1}, {1, -1}  // 横、竖、右下斜、右上斜
        };
        
        for (int[] dir : directions) {
            int count = 1;
            
            // 正向检查
            for (int i = 1; i <= 4; i++) {
                int newX = x + dir[0] * i;
                int newY = y + dir[1] * i;
                if (isValidPosition(newX, newY) && board[newX][newY] == currentPlayer) {
                    count++;
                } else {
                    break;
                }
            }
            
            // 反向检查
            for (int i = 1; i <= 4; i++) {
                int newX = x - dir[0] * i;
                int newY = y - dir[1] * i;
                if (isValidPosition(newX, newY) && board[newX][newY] == currentPlayer) {
                    count++;
                } else {
                    break;
                }
            }
            
            if (count >= 5) {
                return currentPlayer;
            }
        }
        
        return EMPTY;
    }
    
    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < boardSize && y >= 0 && y < boardSize;
    }
    
    // Getter方法
    public int getCurrentPlayer() { return currentPlayer; }
    public int getBoardSize() { return boardSize; }
    public int getStone(int x, int y) {
        if (x >= 0 && x < boardSize && y >= 0 && y < boardSize) {
            return board[x][y];
        }
        return EMPTY;
    }
}
