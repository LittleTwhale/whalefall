package com.littlewhale.net;

import com.littlewhale.entity.Whale;

import java.awt.*;
import java.io.Serializable;

public class WhaleState implements Serializable {
    public Whale whale1 = new Whale("green-whale.png",10);
    public Whale whale2 = new Whale("blue-littlewhale.png",14);

    public int x, y;
    public int score;
    public boolean facingRight;

    public WhaleState(int x, int y, int score, boolean facingRight) {
        this.x = x;
        this.y = y;
        this.score = score;
        this.facingRight = facingRight;
    }
    public void draw1(Graphics g){
        whale1.setPosition(x,y);
        whale1.setFacingRight(facingRight);
        whale1.draw(g);
    }
    public void draw2(Graphics g){
        whale2.setPosition(x,y);
        whale2.setFacingRight(facingRight);
        whale2.draw(g);
    }
}
