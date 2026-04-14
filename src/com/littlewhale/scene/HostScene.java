package com.littlewhale.scene;

import com.littlewhale.net.GameServer;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class HostScene extends Scene {

    private GameServer server;

    private JLabel statusLabel;

    public HostScene(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.name = "host";

        setLayout(new BorderLayout());

        // 显示连接状态
        statusLabel = new JLabel("等待玩家连接中... Waiting for player...", JLabel.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(statusLabel, BorderLayout.CENTER);

        // 启动监听线程
        new Thread(() -> {
            try {
                server = new GameServer();
                server.start(7777);     // 启动7777服务器
                statusLabel.setText("玩家已连接！Player connected!");

                // 成功连接后切换至联机对战场景
                Thread.sleep(1000);
                sceneManager.registerScene("online_game", new OnlineGameScene(sceneManager, server));
                sceneManager.switchTo("online_game");

            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
                statusLabel.setText("连接失败 Connection failed.");
            }
        }).start();
    }

    @Override
    public void onEnter() {

    }

    @Override
    public void onExit() {
        try {
            if(server != null)
                server.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
        bgm.stop();
    }

    @Override
    public void update() {

    }
}
