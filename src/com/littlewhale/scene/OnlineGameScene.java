package com.littlewhale.scene;

import com.littlewhale.entity.*;
import com.littlewhale.net.*;
import com.littlewhale.resource.MusicPlayer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class OnlineGameScene extends Scene {
    private GameServer server;
    private GameClient client;
    private boolean isHost;

    private BufferedImage backgroundImage;


    private GameState gameState;

    // 键盘控制变量
    private boolean WPressed = false;
    private boolean APressed = false;
    private boolean SPressed = false;
    private boolean DPressed = false;

    private Timer gameTimer;

    private int elapsedTime = 0;  // 计时器计数单位：100ms
    private final int maxTime = 1200; // 120秒 = 1200 * 100ms

    public OnlineGameScene(SceneManager sceneManager,GameServer server) {
        this.sceneManager = sceneManager;
        this.name = "online_game";
        this.server = server;
        this.isHost = true;
        init();
    }

    public OnlineGameScene(SceneManager sceneManager,GameClient client) {
        this.sceneManager = sceneManager;
        this.name = "online_game";
        this.client = client;
        this.isHost = false;
        init();
    }

    private void init() {
        // 加载背景图
        try {
            backgroundImage = ImageIO.read(new File("assets/images/game_background.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 初始化游戏状态
        gameState = new GameState();
        // 初始化鲸鱼位置和状态，防止空指针
        gameState.whale1 = new WhaleState(100, 100, 0, true);
        gameState.whale2 = new WhaleState(300, 100, 0, true);

        setupKeyBindings();


        gameTimer = new Timer(100, e -> {
            elapsedTime++;

            if (elapsedTime >= maxTime) {
                gameTimer.stop();
                showGameOverDialog();
                return;
            }

            gameState.whale1.whale1.updateFrame();
            gameState.whale2.whale2.updateFrame();

            updateGame();

            repaint();
        });
    }

    // 显示游戏结束弹窗，比较分数判胜负
    private void showGameOverDialog() {
        String winner;
        if (gameState.whale1.score > gameState.whale2.score) {
            winner = "房主 Whale1 获胜！";
        } else if (gameState.whale1.score < gameState.whale2.score) {
            winner = "加入者 Whale2 获胜！";
        } else {
            winner = "平局！";
        }
        JOptionPane.showMessageDialog(this, "游戏结束！\n" + winner);
        System.exit(0); // 结束游戏，您也可以改成切换场景
    }

    // 游戏逻辑更新
    private void updateGame() {
        int step = 20; // 移动步长

        // 依据身份控制对应鲸鱼
        if (isHost) {
            moveWhale(gameState.whale1, step);
        } else {
            moveWhale(gameState.whale2, step);
        }

        // 检查食物碰撞
        checkFoodCollision();

        // 网络同步状态
        syncGameState();
    }

    private void moveWhale(WhaleState whale, int step) {
        int x = whale.x;
        int y = whale.y;

        if (WPressed) y -= step;
        if (SPressed) y += step;
        if (APressed) {
            x -= step;
            whale.facingRight = false;
        }
        if (DPressed) {
            x += step;
            whale.facingRight = true;
        }

        // 限制在屏幕范围内
        x = Math.max(0, Math.min(x, gameState.sceneWidth - 64));
        y = Math.max(0, Math.min(y, gameState.sceneHeight - 50));

        whale.x = x;
        whale.y = y;
    }

    private void checkFoodCollision() {
        Rectangle rect1 = new Rectangle(gameState.whale1.x, gameState.whale1.y, 64, 50);
        Rectangle rect2 = new Rectangle(gameState.whale2.x, gameState.whale2.y, 64, 50);

        for (int i = gameState.foods.size() - 1; i >= 0; i--) {
            FoodState food = gameState.foods.get(i);
            Rectangle foodRect = food.getBounds();

            if (rect1.intersects(foodRect)) {
                gameState.whale1.score += 10;
                gameState.foods.remove(i);
                if(isHost)
                    gameState.spawnFood();
                continue;
            }
            if (rect2.intersects(foodRect)) {
                gameState.whale2.score += 10;
                gameState.foods.remove(i);
                if(isHost)
                    gameState.spawnFood();
            }
        }
    }

    private void syncGameState() {
        try {
            if (isHost) {
                // 主机接收客户端的 whale2 状态
                WhaleState clientWhale = server.receiveState();
                if (clientWhale != null && clientWhale.whale2 != null) {
                    gameState.whale2 = clientWhale;
                }
                // 主机发送最新状态给客户端
                server.sendFullState(gameState);
            } else {
                // 客户端发送自己的 whale2 状态
                client.sendState(gameState.whale2);
                // 客户端接收主机最新状态
                GameState hostState = client.receiveFullState();
                if (hostState != null) {
                    this.gameState = hostState;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupKeyBindings() {
        InputMap im = this.getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = this.getActionMap();

        im.put(KeyStroke.getKeyStroke("pressed W"), "pressW");
        im.put(KeyStroke.getKeyStroke("released W"), "releaseW");
        im.put(KeyStroke.getKeyStroke("pressed A"), "pressA");
        im.put(KeyStroke.getKeyStroke("released A"), "releaseA");
        im.put(KeyStroke.getKeyStroke("pressed S"), "pressS");
        im.put(KeyStroke.getKeyStroke("released S"), "releaseS");
        im.put(KeyStroke.getKeyStroke("pressed D"), "pressD");
        im.put(KeyStroke.getKeyStroke("released D"), "releaseD");

        am.put("pressW", new AbstractAction() { public void actionPerformed(ActionEvent e) { WPressed = true; }});
        am.put("releaseW", new AbstractAction() { public void actionPerformed(ActionEvent e) { WPressed = false; }});
        am.put("pressA", new AbstractAction() { public void actionPerformed(ActionEvent e) { APressed = true; }});
        am.put("releaseA", new AbstractAction() { public void actionPerformed(ActionEvent e) { APressed = false; }});
        am.put("pressS", new AbstractAction() { public void actionPerformed(ActionEvent e) { SPressed = true; }});
        am.put("releaseS", new AbstractAction() { public void actionPerformed(ActionEvent e) { SPressed = false; }});
        am.put("pressD", new AbstractAction() { public void actionPerformed(ActionEvent e) { DPressed = true; }});
        am.put("releaseD", new AbstractAction() { public void actionPerformed(ActionEvent e) { DPressed = false; }});
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 绘制背景
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        }

        // 绘制食物
        for (FoodState food : gameState.foods) {
            food.draw(g);
        }
        gameState.whale1.draw1(g);
        gameState.whale2.draw2(g);

        // 绘制分数和倒计时
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Whale1 Score: " + gameState.whale1.score, 20, 30);
        g.drawString("Whale2 Score: " + gameState.whale2.score, 20, 60);

        int timeLeft = (maxTime - elapsedTime) / 10;  // 秒
        g.drawString("Time: " + timeLeft + " s", getWidth() - 180, 30);
    }

    @Override
    public void onEnter() {
        bgm = new MusicPlayer();
        bgm.load("assets/audio/hero.wav");
        bgm.loop();
        setFocusable(true);
        requestFocusInWindow();
        gameTimer.start();
    }

    @Override
    public void onExit() {

    }

    @Override
    public void update() {

    }
}
