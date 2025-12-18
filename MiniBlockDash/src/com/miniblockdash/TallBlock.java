package com.miniblockdash;

import java.awt.*;

public class TallBlock extends Obstacle {
    private double pulsePhase = Math.random() * Math.PI * 2;

    public TallBlock(int x) {
        this.x = x;
        this.y = 260;
        this.width = 30;
        this.height = 70;
    }

    @Override
    public void update(int speed) {
        x -= speed;
        pulsePhase += 0.08;
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double pulse = 0.7 + 0.3 * Math.sin(pulsePhase);

        // Outer glow
        g2.setColor(NeonColors.glow(NeonColors.ORANGE, (int)(50 * pulse)));
        g2.fillRect(x - 5, y - 5, width + 10, height + 10);

        // Main block
        g2.setColor(NeonColors.pulse(NeonColors.ORANGE, pulse));
        g2.fillRect(x, y, width, height);

        // Inner darker area
        g2.setColor(NeonColors.dark(NeonColors.ORANGE));
        g2.fillRect(x + 3, y + 3, width - 6, height - 6);

        // Horizontal stripes
        g2.setColor(NeonColors.ORANGE);
        for (int i = 0; i < 3; i++) {
            int stripeY = y + 12 + i * 20;
            g2.fillRect(x + 5, stripeY, width - 10, 4);
        }

        // Glowing outline
        g2.setColor(NeonColors.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(x, y, width, height);
    }
}
