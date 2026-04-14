package com.littlewhale.entity;

import com.littlewhale.net.WhaleState;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Whale {
    private BufferedImage[] frames; // 存储每帧图像
    private int currentFrame = 0;   // 当前帧索引
    private int frameCount = 10;     // 总帧数
    private final int frameWidth = 64;
    private final int frameHeight = 64;

    private int x = 100, y = 300;   // 鲸鱼在屏幕上的位置

    public boolean facingRight = true;  // 角色朝向

    private int hp = 100;           // 血量
    private int score = 0;          // 分数

    public Whale(String image_name) {
        try {
            BufferedImage spriteSheet = ImageIO.read(new File("assets/images/"+image_name));
            frames = new BufferedImage[frameCount];

            for (int i = 0; i < frameCount; i++) {
                frames[i] = spriteSheet.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Whale(String image_name,int frameCount) {
        this.frameCount = frameCount;
        try {
            BufferedImage spriteSheet = ImageIO.read(new File("assets/images/"+image_name));
            frames = new BufferedImage[frameCount];

            for (int i = 0; i < frameCount; i++) {
                frames[i] = spriteSheet.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 切换到下一帧
    public void updateFrame() {
        currentFrame = (currentFrame + 1) % frameCount;
    }

    // 绘制当前帧
    public void draw(Graphics g) {
        BufferedImage frame = frames[currentFrame];

        if (facingRight) {
            // 正常绘制（朝右）
            g.drawImage(frame, x, y, null);
        } else {
            // 镜像翻转绘制（朝左）
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(frame, x + frame.getWidth(), y, -frame.getWidth(), frame.getHeight(), null);
        }
    }

    // 设置位置（可用于键盘控制）
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // 获取位置
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }

    // 设置朝向
    public void setFacingRight(boolean facingRight) {
        this.facingRight = facingRight;
    }

    // 获取血量
    public int getHp() {
        return hp;
    }
    // 设置血量
    public void setHp(int hp) {
        this.hp = Math.max(0, Math.min(hp, 100));
    }

    // 获取分数
    public int getScore() {
        return score;
    }
    // 设置分数
    public void setScore(int score) {
        this.score = score;
    }

    // 获取碰撞矩形边界
    public Rectangle getBounds() {
        return new Rectangle(x, y+10, frameWidth, frameHeight-10);
    }

    public void setState(WhaleState whale2) {
        this.x = whale2.x;
        this.y = whale2.y;
    }
}
