package com.littlewhale.scene;

import com.littlewhale.net.GameClient;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class JoinScene extends Scene {

    private JTextField ipField;
    private JLabel statusLabel;

    public JoinScene(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.name = "join_game";

        setLayout(new BorderLayout());

        // 顶部输入面板
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(new JLabel("输入房主IP：Host IP:"));
        ipField = new JTextField("127.0.0.1", 15);
        panel.add(ipField);

        // 连接按钮
        JButton connectButton = new JButton("连接 Connect");
        panel.add(connectButton);

        // 状态标签
        statusLabel = new JLabel(" ", JLabel.CENTER);

        add(panel, BorderLayout.NORTH);
        add(statusLabel, BorderLayout.CENTER);

        // 按钮点击事件
        connectButton.addActionListener(e -> {
            String ip = ipField.getText().trim();
            new Thread(() -> {
                try {
                    // 创建 GameClient 并连接
                    GameClient client = new GameClient();
                    client.connect(ip, 7777); // 连接房主

                    statusLabel.setText("连接成功！Connected!");

                    // 延迟后进入游戏场景
                    Thread.sleep(1000);

                    // 传入 GameClient 实例并跳转
                    sceneManager.registerScene("online_game", new OnlineGameScene(sceneManager, client));
                    sceneManager.switchTo("online_game");

                } catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                    statusLabel.setText("连接失败！Connection failed.");
                }
            }).start();
        });
    }

    @Override
    public void onEnter() {}

    @Override
    public void onExit() {
        bgm.stop();  // 退出时停止背景音乐
    }

    @Override
    public void update() {}
}

