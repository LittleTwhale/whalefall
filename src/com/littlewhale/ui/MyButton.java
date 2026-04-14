package com.littlewhale.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// 高级自定义按钮类
public class MyButton extends JButton {

    private Color normalColor = new Color(100, 149, 237);   // 默认背景色
    private Color hoverColor = new Color(70, 130, 180);     // 悬停背景色
    private Color textColor = Color.WHITE;                          // 字体颜色
    private boolean hovered = false;

    private Image backgroundImage = null;                   // 可选背景图

    // 构造函数：默认样式
    public MyButton(String text) {
        this(text, null, null, null);
    }

    // 构造函数：支持设置颜色
    public MyButton(String text, Color normalColor, Color hoverColor, Color textColor) {
        super(text);
        if (normalColor != null) this.normalColor = normalColor;
        if (hoverColor != null) this.hoverColor = hoverColor;
        if (textColor != null) this.textColor = textColor;

        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false); // 自绘背景
        setFont(new Font("微软雅黑", Font.BOLD, 20));
        setForeground(this.textColor);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(200, 50));

        // 添加悬停监听
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                repaint();
            }
        });
    }

    // 设置背景图像
    public void setBackgroundImage(Image image) {
        this.backgroundImage = image;
        repaint();
    }

    // 自定义绘图
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        // 抗锯齿
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 绘制背景
        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g2.setColor(hovered ? hoverColor : normalColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
        }

        // 绘制文字
        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(getText())) / 2;
        int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
        g2.setColor(textColor);
        g2.drawString(getText(), x, y);

        g2.dispose();
    }
}
