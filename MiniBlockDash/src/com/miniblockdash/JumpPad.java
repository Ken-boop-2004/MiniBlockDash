package com.miniblockdash;

import java.awt.*;

public class JumpPad extends Obstacle {
    private double pulsePhase = 0;
    private final double boostStrength;

    public JumpPad(int x) {
        this(x, 25); // Default boost
    }

    public JumpPad(int x, double boostStrength) {
        this.x = x;
        this.y = 310;
        this.width = 40;
        this.height = 20;
        this.boostStrength = boostStrength;
    }

    public double getBoostStrength() {
        return boostStrength;
    }

    @Override
    public void update(int speed) {
        x -= speed;
        pulsePhase += 0.2;
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double pulse = 0.7 + 0.3 * Math.sin(pulsePhase);
        Color padColor = NeonColors.pulse(NeonColors.YELLOW, pulse);

        // Glow effect
        g2.setColor(NeonColors.glow(NeonColors.YELLOW, 60));
        g2.fillRect(x - 4, y - 4, width + 8, height + 8);

        // Main pad
        g2.setColor(padColor);
        int[] xs = {x, x + width / 2, x + width, x + width, x};
        int[] ys = {y + height, y, y + height, y + height, y + height};
        g2.fillPolygon(xs, ys, 5);

        // Arrow indicator
        g2.setColor(NeonColors.ORANGE);
        int cx = x + width / 2;
        int[] arrowX = {cx - 8, cx, cx + 8};
        int[] arrowY = {y + 12, y + 4, y + 12};
        g2.fillPolygon(arrowX, arrowY, 3);

        // Outline
        g2.setColor(NeonColors.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.drawPolygon(xs, ys, 5);
    }
}
