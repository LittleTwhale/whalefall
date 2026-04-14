package com.littlewhale.ui;

import javax.swing.*;
import java.awt.*;

// 自定义艺术标题标签（支持大字体、阴影、渐变等）
public class TitleLabel extends JComponent {
    private String title;

    public TitleLabel(String title) {
        this.title = title;
        setPreferredSize(new Dimension(800, 120));
        setOpaque(false); // 背景透明
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        // 开启抗锯齿
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // 设置字体
        g2.setFont(new Font("Serif", Font.BOLD, 48));

        // 获取字符串尺寸
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(title);
        int x = (getWidth() - textWidth) / 2;
        int y = getHeight() / 2 + fm.getAscent() / 2 - 10;

        // 添加阴影效果
        g2.setColor(Color.DARK_GRAY);
        g2.drawString(title, x + 4, y + 4);

        // 主标题颜色（渐变）
        GradientPaint gradient = new GradientPaint(0, 0,
                new Color(135, 206, 250), getWidth(), 0,
                new Color(65, 105, 225));
        g2.setPaint(gradient);
        g2.drawString(title, x, y);

        g2.dispose();
    }
}
