package com.miniblockdash;

import java.awt.*;

public class Particle {
    double x, y;
    double vx, vy;
    int life;
    int maxLife;
    Color color;
    int size;

    public Particle(double x, double y, double vx, double vy, int life, Color color, int size) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.life = life;
        this.maxLife = life;
        this.color = color;
        this.size = size;
    }

    public void update() {
        x += vx;
        y += vy;
        life--;
    }

    public boolean isDead() {
        return life <= 0;
    }

    public void draw(Graphics2D g2) {
        float alpha = (float) life / maxLife;
        g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(alpha * 255)));
        g2.fillRect((int)x, (int)y, size, size);
    }
}
