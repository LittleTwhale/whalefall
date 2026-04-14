package com.littlewhale.net;

import com.littlewhale.entity.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameState implements Serializable {
    public WhaleState whale1;
    public WhaleState whale2;
    public List<FoodState> foods;

    public int sceneWidth = 1280;
    public int sceneHeight = 720;

    private int foodCount = 10;


    public GameState() {
        foods = new ArrayList<>();

        // 初始生成10个食物
        for (int i = 0; i < foodCount; i++) {
            foods.add(generateRandomFood());
        }
    }
    // 设置场景大小
    public void setScene(int sceneWidth, int sceneHeight){
        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;
    }

    // 食物被吃掉后重新生成一个
    public void spawnFood() {
        foods.add(generateRandomFood());
    }

    // 创建一个随机位置的食物
    private FoodState generateRandomFood() {
        int x = (int) (Math.random() * (sceneWidth - 100));
        int y = (int) (Math.random() * (sceneHeight - 100));
        return new FoodState(x, y);
    }

}
