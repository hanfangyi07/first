// GameRecordManager.java
package utils;
//修改
import model.GameRecord;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GameRecordManager {
   private static final String SAVE_DIR = System.getProperty("user.dir") + "/saves/";

    public GameRecordManager() {
        // 创建保存目录
        File dir = new File(SAVE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    
    // 保存游戏记录
    public boolean saveGameRecord(GameRecord record, String filename) {
        try {
            if (!filename.endsWith(".gomoku")) {
                filename += ".gomoku";
            }
            
            File file = new File(SAVE_DIR + filename);
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(record);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // 加载游戏记录
   public GameRecord loadGameRecord(String filename) {
    try {
        if (!filename.endsWith(".gomoku")) {
            filename += ".gomoku";
        }
        
        File file = new File(SAVE_DIR + filename);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(null, "文件不存在: " + filename, "错误", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (GameRecord) ois.readObject();
        }
    } catch (IOException | ClassNotFoundException e) {
        String errorMsg = "加载游戏记录失败: " + e.getMessage();
        JOptionPane.showMessageDialog(null, errorMsg, "错误", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
        return null;
    }
}
    
    // 获取所有保存的游戏记录
    public List<String> getSavedGames() {
        List<String> savedGames = new ArrayList<>();
        File dir = new File(SAVE_DIR);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".gomoku"));
        
        if (files != null) {
            for (File file : files) {
                savedGames.add(file.getName().replace(".gomoku", ""));
            }
        }
        
        return savedGames;
    }
    
    // 删除游戏记录
    public boolean deleteGameRecord(String filename) {
        if (!filename.endsWith(".gomoku")) {
            filename += ".gomoku";
        }
        
        File file = new File(SAVE_DIR + filename);
        return file.delete();
    }

}
