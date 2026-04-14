package com.littlewhale.entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Bomb {
    private double x, y;
    private double vx; // 横向速度
    private double vy; // 纵向速度
    private double gravity = 0.8; // 重力加速度
    private BufferedImage image;
    private boolean active = true;

    public Bomb(double x, double y, double vx) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = 0;

        try {
            image = ImageIO.read(new File("assets/images/bomb.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if (active) {
            x += vx;
            vy += gravity;
            y += vy;
        }
    }

    public void draw(Graphics g) {
        if (active && image != null) {
            g.drawImage(image, (int) x, (int) y, null);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, image.getWidth(), image.getHeight());
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        this.active = false;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getVx() {
        return vx;
    }
}