package com.miniblockdash;

import java.awt.*;

public class Spike extends Obstacle {
    private double pulsePhase = Math.random() * Math.PI * 2;

    public Spike(int x) {
        this.x = x;
        this.y = 300;
        this.width = 30;
        this.height = 30;
    }

    @Override
    public void update(int speed) {
        x -= speed;
        pulsePhase += 0.1;
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int[] xs = {x, x + width / 2, x + width};
        int[] ys = {y + height, y, y + height};

        // Glow effect
        double pulse = 0.6 + 0.4 * Math.sin(pulsePhase);
        g2.setColor(NeonColors.glow(NeonColors.WHITE, (int)(60 * pulse)));
        int[] glowXs = {x - 4, x + width / 2, x + width + 4};
        int[] glowYs = {y + height + 4, y - 6, y + height + 4};
        g2.fillPolygon(glowXs, glowYs, 3);

        // Main spike
        g2.setColor(NeonColors.pulse(NeonColors.WHITE, pulse));
        g2.fillPolygon(xs, ys, 3);

        // Inner darker triangle
        g2.setColor(new Color(100, 100, 120));
        int[] innerXs = {x + 6, x + width / 2, x + width - 6};
        int[] innerYs = {y + height - 2, y + 10, y + height - 2};
        g2.fillPolygon(innerXs, innerYs, 3);

        // Glowing outline
        g2.setColor(NeonColors.CYAN);
        g2.setStroke(new BasicStroke(2));
        g2.drawPolygon(xs, ys, 3);
    }

    @Override
    public Rectangle getBounds() {
        // Slightly smaller hitbox for fairness
        return new Rectangle(x + 5, y + 10, width - 10, height - 10);
    }
}
