package com.littlewhale.scene;

import com.littlewhale.resource.MusicPlayer;

import javax.swing.*;
import java.util.HashMap;

public class SceneManager {
    private final JFrame frame; // 主窗口
    private final HashMap<String, JPanel> scenes = new HashMap<>(); // 场景映射
    private Scene current = null;

    public SceneManager(JFrame frame) {
        this.frame = frame;
    }


    // 注册一个场景
    public void registerScene(String name, JPanel panel) {
        scenes.put(name, panel);
    }

    // 切换到指定场景
    public void switchTo(String name) {
        MusicPlayer music = null;
        if(current != null){
            current.onExit();
            music = current.bgm;
        }

        Scene panel = (Scene)(scenes.get(name));
        if (panel != null) {
            current = panel;
            current.sceneManager = this;
            if(music != null)
                current.bgm = music;
            current.onEnter();

            frame.setContentPane(panel); // 设置面板
            frame.revalidate();          // 刷新布局
            frame.repaint();             // 重绘窗口
        } else {
            System.err.println("场景不存在：" + name);
        }
    }

    public void updateScene() {
        if (current != null) {
            current.update();
        }
    }
}
