package com.littlewhale.ui;

import javax.swing.*;
import com.littlewhale.scene.*;

public class GameWindow extends JFrame {

    private final SceneManager sceneManager;

    public GameWindow() {
        setTitle("鲸落之旅 Whale's Journey");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        sceneManager = new SceneManager(this);

        // 注册所有场景
        sceneManager.registerScene("menu", new MenuScene(sceneManager));
        sceneManager.registerScene("single_game", new SingleGameScene(sceneManager));
        sceneManager.registerScene("dual_game", new DualGameScene(sceneManager));
        sceneManager.registerScene("game_over", new GameOverScene(sceneManager));
        sceneManager.registerScene("end", new EndScene(sceneManager));
        sceneManager.registerScene("online", new OnlineScene(sceneManager));
        sceneManager.registerScene("host", new HostScene(sceneManager));
        sceneManager.registerScene("join", new JoinScene(sceneManager));

        // 进入主菜单
        sceneManager.switchTo("menu");
    }
}
