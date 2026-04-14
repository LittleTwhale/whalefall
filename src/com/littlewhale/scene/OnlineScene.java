package com.littlewhale.scene;

import com.littlewhale.ui.MyButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class OnlineScene extends Scene {

    private Image backgroundImage;

    public OnlineScene(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.name = "online";

        setLayout(new GridBagLayout()); // 使用网格布局便于垂直居中
        loadBackground(); // 加载背景图片

        // 创建按钮
        MyButton createRoomButton = new MyButton("创建房间");
        createRoomButton.addActionListener(e -> sceneManager.switchTo("host"));

        MyButton joinRoomButton = new MyButton("加入房间");
        joinRoomButton.addActionListener(e -> sceneManager.switchTo("join"));

        // 创建按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 200, 0));
        buttonPanel.setOpaque(false); // 背景透明
        buttonPanel.add(createRoomButton);
        buttonPanel.add(joinRoomButton);

        // 添加到主面板（让按钮居中显示）
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(100, 0, 0, 0); // 上方留空
        add(buttonPanel, gbc);
    }

    private void loadBackground() {
        try {
            backgroundImage = ImageIO.read(new File("assets/images/online_background.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        }
    }

    @Override
    public void onEnter() {
        setFocusable(true);
        requestFocusInWindow();
    }

    @Override
    public void onExit() {;
    }

    @Override
    public void update() {}
}