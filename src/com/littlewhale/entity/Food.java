package com.littlewhale.entity;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Food {
    private int x, y;
    private BufferedImage image;
    private boolean eaten = false;

    public Food(int x, int y) {
        this.x = x;
        this.y = y;
        try {
            image = ImageIO.read(new File("assets/images/food.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics g) {
        if (!eaten && image != null) {
            g.drawImage(image, x, y, null);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y+15, image.getWidth(), image.getHeight()-15);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setEaten(boolean eaten) {
        this.eaten = eaten;
    }

    public boolean isEaten() {
        return eaten;
    }

    public void resetPosition(int width, int height) {
        this.x = (int)(Math.random() * (width - 100));
        this.y = (int)(Math.random() * (height - 100));
        this.eaten = false;
    }
}
