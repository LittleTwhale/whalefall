package com.littlewhale.scene;

import com.littlewhale.resource.MusicPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GameOverScene extends Scene {

    public GameOverScene(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        setLayout(new BorderLayout());

        JLabel label = new JLabel("You Die!" , SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setForeground(Color.WHITE);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // 背景透明
        JButton menuBtn = new JButton("返回主菜单");

        menuBtn.addActionListener(e -> sceneManager.switchTo("menu"));
        buttonPanel.add(menuBtn);

        add(label, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setBackground(Color.BLACK); // 背景黑色
    }
    // 重写Scene的方法
    @Override
    public void onEnter() {
        bgm = new MusicPlayer();
        bgm.load("assets/audio/over.wav");
        bgm.play();
    }
    @Override
    public void onExit() {
    }
    @Override
    public void update() {
    }
}