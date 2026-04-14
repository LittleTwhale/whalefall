package com.littlewhale.resource;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MusicPlayer {
    private Clip clip;

    // 加载音频文件 Load audio file
    public void load(String filePath) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(filePath));
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // 播放 Play once
    public void play() {
        if (clip != null) {
            clip.setFramePosition(0); // 从头播放
            clip.start();
        }
    }

    // 循环播放 Loop
    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY); // 无限循环
        }
    }

    // 停止播放 Stop
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    // 暂停播放 Pause (模拟)
    public void pause() {
        if (clip != null && clip.isRunning()) {
            clip.stop(); // 停止播放，但保留位置
        }
    }

    // 恢复播放 Resume
    public void resume() {
        if (clip != null && !clip.isRunning()) {
            clip.start(); // 从当前位置恢复
        }
    }
}