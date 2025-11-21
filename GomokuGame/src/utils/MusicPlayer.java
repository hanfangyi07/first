// MusicPlayer.java
package utils;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MusicPlayer {
    private Clip clip;
    private boolean isPlaying = false;
    
    public void playBackgroundMusic(String filePath) {
        try {
            // 首先尝试从文件系统加载
            File musicFile = new File(filePath);
            if (!musicFile.exists()) {
                // 如果文件不存在，尝试从类路径加载
                System.out.println("文件不存在: " + filePath);
                return;
            }
            
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicFile);
            clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.loop(Clip.LOOP_CONTINUOUSLY); // 循环播放
            clip.start();
            isPlaying = true;
            System.out.println("背景音乐开始播放");
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("音乐播放失败: " + e.getMessage());
        }
    }
    
    public void stopMusic() {
        if (clip != null && isPlaying) {
            clip.stop();
            isPlaying = false;
            System.out.println("背景音乐已停止");
        }
    }
    
    public void toggleMusic() {
        if (isPlaying) {
            stopMusic();
        } else if (clip != null) {
            clip.start();
            isPlaying = true;
            System.out.println("背景音乐开始播放");
        }
    }
    
    public boolean isPlaying() {
        return isPlaying;
    }
}