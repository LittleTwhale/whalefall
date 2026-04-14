package com.littlewhale.logic;

import java.awt.Rectangle;

// 碰撞检测
public class CollisionChecker {

    // 判断两个矩形是否碰撞
    public static boolean isColliding(Rectangle r1, Rectangle r2) {
        return r1.intersects(r2);
    }


     // 判断目标是否在范围内（支持中心点检测）
    public static boolean isInside(Rectangle area, int x, int y) {
        return area.contains(x, y);
    }
}
