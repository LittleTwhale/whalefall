package com.littlewhale.scene;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import com.littlewhale.resource.MusicPlayer;
import com.littlewhale.ui.*;

public class MenuScene extends Scene {

    //导入背景图片
    private Image backgroundImage;


    public MenuScene(SceneManager sceneManager) {
        super();
        this.name = "menu";
        this.sceneManager = sceneManager;

        // 加载背景图片
        backgroundImage = new ImageIcon("assets/images/menu_background.png").getImage();

        // 设置透明布局，使按钮浮在背景图上
        setLayout(new GridBagLayout());
        setOpaque(false); // 背景使用 paintComponent 绘制

        // 创建标题
        TitleLabel title = new TitleLabel("🐋 鲸落之旅 Whale's Journey");

        // 创建按钮
        MyButton singleButton = new MyButton("单人模式");
        singleButton.addActionListener(e -> sceneManager.switchTo("single_game"));

        MyButton dualButton = new MyButton("双人模式");
        dualButton.addActionListener(e -> sceneManager.switchTo("dual_game"));

        MyButton onlineButton = new MyButton("双人联机");
        onlineButton.addActionListener(e -> sceneManager.switchTo("online"));

        MyButton exitButton = new MyButton("退出游戏 Exit");
        exitButton.addActionListener(e -> System.exit(0));

        // 创建按钮容器
        // 将四个按钮放在一行：使用 FlowLayout 的子面板
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 80, 0)); // 横向间距 40
        buttonRow.setOpaque(false);
        buttonRow.add(singleButton);
        buttonRow.add(dualButton);
        buttonRow.add(onlineButton);
        buttonRow.add(exitButton);

        // 使用 GridBagLayout 定位（整体下移）
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;

        // 添加标题
        gbc.gridy = 0;
        gbc.insets = new Insets(-75, 0, 0, 0); // 标题下方留空
        add(title, gbc);

        // 添加按钮
        gbc.gridy = 1;
        gbc.insets = new Insets(50, 0, 0, 0);
        add(buttonRow, gbc);

        bgm = new MusicPlayer();
    }

    // 重写Scene的方法
    @Override
    public void onEnter() {
        bgm.load("assets/audio/mine.wav");
        bgm.loop();
    }
    @Override
    public void onExit() {
        //bgm.stop();
    }
    @Override
    public void update() {
    }

    // 重写绘图方法绘制背景图
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
