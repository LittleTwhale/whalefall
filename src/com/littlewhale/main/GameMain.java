package com.littlewhale.main;
import com.littlewhale.ui.GameWindow;

public class GameMain {
    // 主方法：游戏启动入口
    public static void main(String[] args) {
        // 创建游戏窗口
        GameWindow window = new GameWindow();
        window.setVisible(true); // 显示窗口
        System.out.println("游戏已启动！");
    }
}
