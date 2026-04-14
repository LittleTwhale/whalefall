package com.littlewhale.entity;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class LittleWhale {
    private final BufferedImage[] frames;
    private int currentFrame = 0;
    private final int frameCount = 14;

    private final int frameWidth = 64;
    private final int frameHeight = 64;

    private int x = 300, y = 400; // 出现的位置

    public LittleWhale() {
        frames = new BufferedImage[frameCount];
        try {
            BufferedImage spriteSheet = ImageIO.read(new File("assets/images/blue-littlewhale.png"));
            for (int i = 0; i < frameCount; i++) {
                frames[i] = spriteSheet.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateFrame() {
        currentFrame = (currentFrame + 1) % frameCount;
    }

    public void draw(Graphics g) {
        g.drawImage(frames[currentFrame], x, y, null);
    }

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
}
