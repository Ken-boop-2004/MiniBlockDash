package com.miniblockdash;

import java.awt.*;

public class CeilingSpike extends Obstacle {
    private double pulsePhase = Math.random() * Math.PI * 2;

    public CeilingSpike(int x) {
        this.x = x;
        this.y = 30; // At ceiling - player hits this when inverted (walking on ceiling)
        this.width = 30;
        this.height = 30;
        this.isCeilingObstacle = true;
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

        // Draw spike pointing DOWN (hanging from ceiling)
        int[] xs = {x, x + width / 2, x + width};
        int[] ys = {y, y + height, y};

        double pulse = 0.6 + 0.4 * Math.sin(pulsePhase);

        // Glow effect
        g2.setColor(NeonColors.glow(NeonColors.WHITE, (int)(60 * pulse)));
        int[] glowXs = {x - 4, x + width / 2, x + width + 4};
        int[] glowYs = {y - 4, y + height + 6, y - 4};
        g2.fillPolygon(glowXs, glowYs, 3);

        // Main spike
        g2.setColor(NeonColors.pulse(NeonColors.WHITE, pulse));
        g2.fillPolygon(xs, ys, 3);

        // Inner darker triangle
        g2.setColor(new Color(100, 100, 120));
        int[] innerXs = {x + 6, x + width / 2, x + width - 6};
        int[] innerYs = {y + 2, y + height - 10, y + 2};
        g2.fillPolygon(innerXs, innerYs, 3);

        // Glowing outline - use PINK for ceiling spikes to differentiate
        g2.setColor(NeonColors.PINK);
        g2.setStroke(new BasicStroke(2));
        g2.drawPolygon(xs, ys, 3);
    }

    @Override
    public Rectangle getBounds() {
        // Slightly smaller hitbox for fairness - spike points down
        return new Rectangle(x + 5, y, width - 10, height - 10);
    }
}
