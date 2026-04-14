package com.littlewhale.scene;

import com.littlewhale.resource.MusicPlayer;

import javax.swing.*;
import java.awt.event.ActionListener;

// 抽象场景基类
public abstract class Scene extends JPanel {
    public MusicPlayer bgm;
    protected String name;
    public SceneManager sceneManager;

    // 当场景被切换到时调用（初始化、重置等）
    public abstract void onEnter();

    // 当场景被切换走时调用（释放资源等）
    public abstract void onExit();

    // 每帧或定时更新调用
    public abstract void update();

    // 统一接口：供子类绑定退出动作等
    protected ActionListener onBackToMenu;

    public Scene() {
        super();
    }

    public void setOnBackToMenu(ActionListener onBackToMenu) {
        this.onBackToMenu = onBackToMenu;
    }
}
