package com.littlewhale.scene;

import javax.swing.*;
import java.awt.*;

public class EndScene extends Scene {
    public EndScene(SceneManager sceneManager) {
        super();
        this.sceneManager = sceneManager;
        this.name = "end";
        setLayout(new BorderLayout());
        setBackground(new Color(0, 30, 60));
        JLabel label = new JLabel("Game Over", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 32));
        label.setForeground(Color.WHITE);
        add(label, BorderLayout.CENTER);

        JButton backBtn = new JButton("返回菜单 Return to Menu");
        backBtn.addActionListener(e ->sceneManager.switchTo("menu"));
        add(backBtn, BorderLayout.SOUTH);
    }

    @Override
    public void onEnter() {

    }

    @Override
    public void onExit() {

    }

    @Override
    public void update() {

    }
}