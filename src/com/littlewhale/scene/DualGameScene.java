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

public class DualGameScene extends Scene {

    private Whale whale1;
    private Whale whale2;
    private BufferedImage backgroundImage;
    private final Timer gameTimer;
    private boolean isPaused = false;
    private boolean isWhale1Dead = false;
    private boolean isWhale2Dead = false;

    // 键盘状态变量
    private boolean WPressed = false;
    private boolean SPressed = false;
    private boolean APressed = false;
    private boolean DPressed = false;
    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    private GameObjectManager objectManager;    // 游戏对象管理

    private MusicPlayer eat;
    private MusicPlayer explode;

    public DualGameScene(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.name = "single_game";

        // 原子表示宽度和高度
        AtomicInteger width = new AtomicInteger(1280);
        AtomicInteger height = new AtomicInteger(720);

        whale1 = new Whale("green-whale.png");
        whale2 = new Whale("blue-littlewhale.png",14);
        whale2.setPosition(1000,500);

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
                whale1.updateFrame();
                whale2.updateFrame();

                int x1 = whale1.getX();
                int y1 = whale1.getY();
                int step = 20;               // 移动步长
                if (WPressed) y1 -= step;
                if (SPressed) y1 += step;
                if (APressed) x1 -= step;
                if (DPressed) x1 += step;

                if(getWidth() > 0 && getHeight() > 0){
                    width.set(getWidth());
                    height.set(getHeight());
                }
                whale1.setPosition(Math.max(0, Math.min(x1, width.get() - 64)), Math.max(-14, Math.min(y1, height.get() - 50)));

                int x2 = whale2.getX();
                int y2 = whale2.getY();
                if (upPressed) y2 -= step;
                if (downPressed) y2 += step;
                if (leftPressed) x2 -= step;
                if (rightPressed) x2 += step;

                if(getWidth() > 0 && getHeight() > 0){
                    width.set(getWidth());
                    height.set(getHeight());
                }
                whale2.setPosition(Math.max(0, Math.min(x2, width.get() - 64)), Math.max(-14, Math.min(y2, height.get() - 50)));

                objectManager.setScene(width.get(), height.get());    // 更新游戏窗口大小

                checkCollisions();  //进行碰撞检测
                repaint();
            }
            update();
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
        objectManager.dualMode();
    }
    @Override
    public void update() {
        if(whale1.getScore()+whale2.getScore()>6000)
            gameWin();
    }

    private void setupKeyBindings() {
        int condition = WHEN_IN_FOCUSED_WINDOW;
        InputMap inputMap = getInputMap(condition);
        ActionMap actionMap = getActionMap();

        // 按下 W
        inputMap.put(KeyStroke.getKeyStroke("pressed W"), "pressW");
        actionMap.put("pressW", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WPressed = true;
            }
        });

        // 松开 W
        inputMap.put(KeyStroke.getKeyStroke("released W"), "releaseW");
        actionMap.put("releaseW", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WPressed = false;
            }
        });

        // 按下 S
        inputMap.put(KeyStroke.getKeyStroke("pressed S"), "pressS");
        actionMap.put("pressS", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SPressed = true;
            }
        });

        // 松开 S
        inputMap.put(KeyStroke.getKeyStroke("released S"), "releaseS");
        actionMap.put("releaseS", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SPressed = false;
            }
        });

        // 按下 A
        inputMap.put(KeyStroke.getKeyStroke("pressed A"), "pressA");
        actionMap.put("pressA", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                APressed = true;
                whale1.setFacingRight(false);  // 朝左
            }
        });

        // 松开 A
        inputMap.put(KeyStroke.getKeyStroke("released A"), "releaseA");
        actionMap.put("releaseA", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                APressed = false;
            }
        });

        // 按下 D
        inputMap.put(KeyStroke.getKeyStroke("pressed D"), "pressD");
        actionMap.put("pressD", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DPressed = true;
                whale1.setFacingRight(true);  // 朝右
            }
        });

        // 松开 D
        inputMap.put(KeyStroke.getKeyStroke("released D"), "releaseD");
        actionMap.put("releaseD", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DPressed = false;
            }
        });

        // 按下 Up
        inputMap.put(KeyStroke.getKeyStroke("pressed UP"), "pressUp");
        actionMap.put("pressUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                upPressed = true;
            }
        });

        // 松开 UP
        inputMap.put(KeyStroke.getKeyStroke("released UP"), "releaseUp");
        actionMap.put("releaseUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                upPressed = false;
            }
        });

        // 按下 DOWN
        inputMap.put(KeyStroke.getKeyStroke("pressed DOWN"), "pressDown");
        actionMap.put("pressDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                downPressed = true;
            }
        });

        // 松开 DOWN
        inputMap.put(KeyStroke.getKeyStroke("released DOWN"), "releaseDown");
        actionMap.put("releaseDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                downPressed = false;
            }
        });

        // 按下 LEFT
        inputMap.put(KeyStroke.getKeyStroke("pressed LEFT"), "pressLeft");
        actionMap.put("pressLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                leftPressed = true;
                whale2.setFacingRight(false);  // 朝左
            }
        });

        // 松开 LEFT
        inputMap.put(KeyStroke.getKeyStroke("released LEFT"), "releaseLeft");
        actionMap.put("releaseLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                leftPressed = false;
            }
        });

        // 按下 RIGHT
        inputMap.put(KeyStroke.getKeyStroke("pressed RIGHT"), "pressRight");
        actionMap.put("pressRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rightPressed = true;
                whale2.setFacingRight(true);  // 朝右
            }
        });

        // 松开 D
        inputMap.put(KeyStroke.getKeyStroke("released RIGHT"), "releaseRight");
        actionMap.put("releaseRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rightPressed = false;
            }
        });
    }

    // 检查碰撞
    private void checkCollisions() {
        Rectangle whale1Rect = whale1.getBounds();
        Rectangle whale2Rect = whale2.getBounds();

        // 食物碰撞检测 Food Collision
        if(!isWhale1Dead) {
            for (int i = 0; i < objectManager.getFoodList().size(); i++) {
                Food food = objectManager.getFoodList().get(i);
                if (CollisionChecker.isColliding(whale1Rect, food.getBounds())) {
                    eat.play();
                    objectManager.getFoodList().remove(i);
                    whale1.setScore(whale1.getScore() + 10); // 加分
                    if (whale1.getScore() % 100 == 0)
                        whale1.setHp(whale1.getHp() + 1);
                    if (whale1.getScore() % 500 == 0)
                        objectManager.levelChange();
                    objectManager.spawnFood(); // 再生成一个食物
                    break; // 退出循环避免并发修改异常
                }
            }
        }
        if(!isWhale2Dead) {
            for (int i = 0; i < objectManager.getFoodList().size(); i++) {
                Food food = objectManager.getFoodList().get(i);
                if (CollisionChecker.isColliding(whale2Rect, food.getBounds())) {
                    eat.play();
                    objectManager.getFoodList().remove(i);
                    whale2.setScore(whale2.getScore() + 10); // 加分
                    if (whale2.getScore() % 100 == 0)
                        whale2.setHp(whale2.getHp() + 1);
                    if (whale2.getScore() % 500 == 0)
                        objectManager.levelChange();
                    objectManager.spawnFood(); // 再生成一个食物
                    break; // 退出循环避免并发修改异常
                }
            }
        }

        // 炸弹碰撞检测 Bomb Collision
        if(!isWhale1Dead) {
            for (int i = 0; i < objectManager.getBombList().size(); i++) {
                Bomb bomb = objectManager.getBombList().get(i);
                if (CollisionChecker.isColliding(whale1Rect, bomb.getBounds())) {
                    explode.play();
                    objectManager.getBombList().remove(i);
                    whale1.setHp(whale1.getHp() - 10); // 扣血 reduce health
                    if (whale1.getHp() <= 0) {
                        isWhale1Dead = true;
                        if (isWhale2Dead)
                            gameOver(); // 游戏结束
                    }
                    break;
                }
            }
        }
        if(!isWhale1Dead) {
            for (int i = 0; i < objectManager.getBombList().size(); i++) {
                Bomb bomb = objectManager.getBombList().get(i);
                if (CollisionChecker.isColliding(whale2Rect, bomb.getBounds())) {
                    explode.play();
                    objectManager.getBombList().remove(i);
                    whale2.setHp(whale2.getHp() - 10); // 扣血 reduce health
                    if (whale2.getHp() <= 0) {
                        isWhale2Dead = true;
                        if (isWhale1Dead)
                            gameOver(); // 游戏结束
                    }
                    break;
                }
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
        whale1 = new Whale("green-whale.png");
        whale2 = new Whale("blue-littlewhale.png",14);
        whale2.setPosition(1000, 500);
        isWhale1Dead = false;
        isWhale2Dead = false;
        objectManager.clear();
        isPaused = false;
        upPressed = false;
        downPressed = false;
        leftPressed = false;
        rightPressed = false;
        APressed = false;
        DPressed = false;
        WPressed = false;
        SPressed = false;
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
        if(!isWhale1Dead)
            whale1.draw(g);
        if(!isWhale2Dead)
            whale2.draw(g);

        // 绘制分数
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Whale1Score: " + whale1.getScore(), 20, 30);
        // 绘制血量条
        drawHpBar(g, 20, 50, whale1.getHp(), 100);

        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Whale2Score: " + whale2.getScore(), getWidth() - 175, 30);
        // 绘制血量条
        drawHpBar(g, getWidth() - 175, 50, whale2.getHp(), 100);

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