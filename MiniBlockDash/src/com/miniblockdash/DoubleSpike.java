package com.miniblockdash;

import java.awt.*;

public class DoubleSpike extends Obstacle {
    private double pulsePhase = Math.random() * Math.PI * 2;

    public DoubleSpike(int x) {
        this.x = x;
        this.y = 300;
        this.width = 50;
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

        double pulse = 0.6 + 0.4 * Math.sin(pulsePhase);

        // First spike
        drawSpike(g2, x, pulse);
        // Second spike (offset)
        drawSpike(g2, x + 20, pulse);
    }

    private void drawSpike(Graphics2D g2, int sx, double pulse) {
        int[] xs = {sx, sx + 15, sx + 30};
        int[] ys = {y + height, y, y + height};

        // Glow
        g2.setColor(NeonColors.glow(NeonColors.WHITE, (int)(50 * pulse)));
        int[] glowXs = {sx - 3, sx + 15, sx + 33};
        int[] glowYs = {y + height + 3, y - 5, y + height + 3};
        g2.fillPolygon(glowXs, glowYs, 3);

        // Main spike
        g2.setColor(NeonColors.pulse(NeonColors.WHITE, pulse));
        g2.fillPolygon(xs, ys, 3);

        // Inner
        g2.setColor(new Color(100, 100, 120));
        int[] innerXs = {sx + 5, sx + 15, sx + 25};
        int[] innerYs = {y + height - 2, y + 8, y + height - 2};
        g2.fillPolygon(innerXs, innerYs, 3);

        // Outline
        g2.setColor(NeonColors.CYAN);
        g2.setStroke(new BasicStroke(2));
        g2.drawPolygon(xs, ys, 3);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x + 5, y + 10, width - 10, height - 10);
    }
}
