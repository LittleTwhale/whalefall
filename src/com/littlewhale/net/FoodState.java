package com.littlewhale.net;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.Serializable;

public class FoodState implements Serializable {
    public int x, y;
    private BufferedImage image;


    public FoodState(int x, int y) {
        this.x = x;
        this.y = y;
        try {
            image = ImageIO.read(new File("assets/images/food.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y + 15, image.getWidth(), image.getHeight() - 15);
    }

    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, null);
        }
    }
}
