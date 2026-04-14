package com.littlewhale.scene;

import com.littlewhale.entity.*;
import com.littlewhale.logic.*;
import com.littlewhale.resource.MusicPlayer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

public class SingleGameScene extends Scene {

    private Whale whale;
    private BufferedImage backgroundImage;
    private final Timer gameTimer;
    private boolean isPaused = false;

    // 键盘状态变量
    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    private GameObjectManager objectManager;    // 游戏对象管理

    private MusicPlayer eat;
    private MusicPlayer explode;

    public SingleGameScene(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.name = "single_game";

        // 原子表示宽度和高度
        AtomicInteger width = new AtomicInteger(1280);
        AtomicInteger height = new AtomicInteger(720);

        whale = new Whale("green-whale.png");

        // 初始固定尺寸，后续会动态更新
        objectManager = new GameObjectManager(width.get(), height.get());

        // 监听窗口尺寸变化
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                objectManager.setScene(getWidth(), getHeight());
            }
        });

        // 加载背景图
        try {
            backgroundImage = ImageIO.read(new File("assets/images/game_background.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 初始化游戏对象
        objectManager = new GameObjectManager(width.get(), height.get());

        // 键盘控制
        setupKeyBindings();

        // 动画更新定时器
        gameTimer = new Timer(100, e -> {
            if (!isPaused) {
                whale.updateFrame();

                int x = whale.getX();
                int y = whale.getY();
                int step = 20;               // 移动步长
                if (upPressed) y -= step;
                if (downPressed) y += step;
                if (leftPressed) x -= step;
                if (rightPressed) x += step;

                if(getWidth() > 0 && getHeight() > 0){
                    width.set(getWidth());
                    height.set(getHeight());
                }
                whale.setPosition(Math.max(0, Math.min(x, width.get() - 64)), Math.max(-14, Math.min(y, height.get() - 50)));

                objectManager.setScene(width.get(), height.get());    // 更新游戏窗口大小
                checkCollisions();  //进行碰撞检测
                repaint();
            }

            objectManager.update();
        });

    }

    // 重写Scene的方法
    @Override
    public void onEnter() {
        bgm.stop();
        bgm = new MusicPlayer();
        bgm.load("assets/audio/hero.wav");
        bgm.loop();

        eat = new MusicPlayer();
        eat.load("assets/audio/eat.wav");
        explode = new MusicPlayer();
        explode.load("assets/audio/explode.wav");

        // 启动计时器
        gameTimer.start();

        // 焦点获取
        setFocusable(true);
        requestFocusInWindow();
    }
    @Override
    public void onExit() {
        gameTimer.stop();
        bgm.stop();
    }
    @Override
    public void update() {
        if(whale.getScore()==3000){
            gameWin();
        }
        if(whale.getScore()%100==0)
            whale.setHp(whale.getHp()+1);
        if(whale.getScore()%500==0)
            objectManager.levelChange();
    }

    private void setupKeyBindings() {
        int condition = WHEN_IN_FOCUSED_WINDOW;
        InputMap inputMap = getInputMap(condition);
        ActionMap actionMap = getActionMap();

        // 按下 W
        inputMap.put(KeyStroke.getKeyStroke("pressed W"), "pressUp");
        actionMap.put("pressUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                upPressed = true;
            }
        });

        // 松开 W
        inputMap.put(KeyStroke.getKeyStroke("released W"), "releaseUp");
        actionMap.put("releaseUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                upPressed = false;
            }
        });

        // 按下 S
        inputMap.put(KeyStroke.getKeyStroke("pressed S"), "pressDown");
        actionMap.put("pressDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                downPressed = true;
            }
        });

        // 松开 S
        inputMap.put(KeyStroke.getKeyStroke("released S"), "releaseDown");
        actionMap.put("releaseDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                downPressed = false;
            }
        });

        // 按下 A
        inputMap.put(KeyStroke.getKeyStroke("pressed A"), "pressLeft");
        actionMap.put("pressLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                leftPressed = true;
                whale.setFacingRight(false);  // 朝左
            }
        });

        // 松开 A
        inputMap.put(KeyStroke.getKeyStroke("released A"), "releaseLeft");
        actionMap.put("releaseLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                leftPressed = false;
            }
        });

        // 按下 D
        inputMap.put(KeyStroke.getKeyStroke("pressed D"), "pressRight");
        actionMap.put("pressRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rightPressed = true;
                whale.setFacingRight(true);  // 朝右
            }
        });

        // 松开 D
        inputMap.put(KeyStroke.getKeyStroke("released D"), "releaseRight");
        actionMap.put("releaseRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rightPressed = false;
            }
        });
    }

    // 检查碰撞
    private void checkCollisions() {
        Rectangle whaleRect = whale.getBounds();

        // 食物碰撞检测 Food Collision
        for (int i = 0; i < objectManager.getFoodList().size(); i++) {
            Food food = objectManager.getFoodList().get(i);
            if (CollisionChecker.isColliding(whaleRect, food.getBounds())) {
                eat.play();
                objectManager.getFoodList().remove(i);
                whale.setScore(whale.getScore()+10); // 加分
                update();
                objectManager.spawnFood(); // 再生成一个食物
                break; // 退出循环避免并发修改异常
            }
        }

        // 炸弹碰撞检测 Bomb Collision
        for (int i = 0; i < objectManager.getBombList().size(); i++) {
            Bomb bomb = objectManager.getBombList().get(i);
            if (CollisionChecker.isColliding(whaleRect, bomb.getBounds())) {
                explode.play();
                objectManager.getBombList().remove(i);
                whale.setHp(whale.getHp()-10); // 扣血 reduce health
                if (whale.getHp() <= 0) {
                    gameOver(); // 游戏结束
                }
                break;
            }
        }
    }
    private void gameOver() {
        gameTimer.stop();
        clear();
        // 跳转 GameOverScene
        sceneManager.switchTo("game_over");
    }
    private void gameWin(){
        gameTimer.stop();
        clear();
        sceneManager.switchTo("end");
    }

    public void clear(){
        whale = new Whale("green-whale.png");
        objectManager.clear();
        isPaused = false;
        upPressed = false;
        downPressed = false;
        leftPressed = false;
        rightPressed = false;
    }

    // 绘制
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        }

        for (Food food : objectManager.getFoodList()) {
            food.draw(g);
        }
        for (Bomb bomb : objectManager.getBombList()) {
            bomb.draw(g);
        }
        whale.draw(g);

        // 绘制分数
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + whale.getScore(), 20, 30);
        // 绘制血量条
        drawHpBar(g, 20, 50, whale.getHp(), 100);

    }
    private void drawHpBar(Graphics g, int x, int y, int hp, int maxHp) {
        int barWidth = 150; // 血条总宽度
        int barHeight = 20; // 血条高度

        // 边框 Border
        g.setColor(Color.WHITE);
        g.drawRect(x, y, barWidth, barHeight);

        // 血量比例 Filled red part
        g.setColor(Color.RED);
        int filledWidth = (int) ((double) hp / maxHp * barWidth);
        g.fillRect(x, y, filledWidth, barHeight);

        // 显示文字文字 Show HP text
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 14));
        g.drawString("HP: " + hp + " / " + maxHp, x + 5, y + barHeight - 5);
    }
}

