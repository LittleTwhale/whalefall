package com.littlewhale.logic;

import com.littlewhale.entity.Bomb;
import com.littlewhale.entity.Food;

import java.util.*;

// 游戏对象管理器
public class GameObjectManager {
    private List<Food> foodList;
    private List<Bomb> bombList;
    private int sceneWidth;
    private int sceneHeight;

    private int bombTimer = 0;
    private int bombInterval = 6;  // 每6帧投放一个炸弹

    private int foodCount = 5;
    private int randomVelocity = 15;

    private boolean restart = false;

    public GameObjectManager(int sceneWidth, int sceneHeight) {
        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;
        this.foodList = new ArrayList<>();
        this.bombList = new ArrayList<>();

        // 初始生成5个食物
        for (int i = 0; i < foodCount; i++) {
            foodList.add(generateRandomFood());
        }
    }

    // 设置场景大小
    public void setScene(int sceneWidth, int sceneHeight){
        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;
    }
    // 每帧调用一次进行更新
    public void update() {
        if(restart){
            for (int i = 0; i < foodCount; i++) {
                foodList.add(generateRandomFood());
            }
            restart = false;
        }
        // 炸弹计时生成
        bombTimer++;
        if (bombTimer >= bombInterval) {
            spawnRandomBomb();
            bombTimer = 0;
        }

        // 更新所有炸弹的位置
        for (Bomb bomb : bombList) {
            bomb.update();
        }

        // 清理超出屏幕的炸弹
        bombList.removeIf(bomb -> bomb.getY() > sceneHeight + 100);
    }

    public void levelChange(){
        for (int i = 0; i < foodCount; i++) {
            foodList.add(generateRandomFood());
        }
        bombInterval--;
        randomVelocity+=10;
    }

    // 食物被吃掉后重新生成一个
    public void spawnFood() {
        foodList.add(generateRandomFood());
    }

    // 创建一个随机位置的食物
    private Food generateRandomFood() {
        int x = (int) (Math.random() * (sceneWidth - 100));
        int y = (int) (Math.random() * (sceneHeight - 100));
        return new Food(x, y);
    }

    // 创建一个随机初速度方向的炸弹
    private void spawnRandomBomb() {
        double x = Math.random() * (sceneWidth - 100)+100;
        double vx = Math.random() * 2 * randomVelocity - randomVelocity;  // -randomV~randomV随机速度
        bombList.add(new Bomb(x, 0, vx));
    }

    // 提供访问接口
    public List<Food> getFoodList() {
        return foodList;
    }

    public List<Bomb> getBombList() {
        return bombList;
    }

    public void dualMode(){
        bombInterval = 12;
        foodCount = 3;
    }
    public void clear(){
        bombList.clear();
        foodList.clear();
        restart = true;
        foodCount = 5;
        randomVelocity = 15;
        bombInterval = 6;
    }
}
