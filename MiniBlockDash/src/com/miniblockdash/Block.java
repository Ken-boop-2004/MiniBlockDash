package com.miniblockdash;

import java.awt.*;

public class Block extends Obstacle {
    private double pulsePhase = Math.random() * Math.PI * 2;

    public Block(int x) {
        this.x = x;
        this.y = 300;
        this.width = 30;
        this.height = 30;
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
        g2.setColor(NeonColors.glow(NeonColors.MAGENTA, (int)(50 * pulse)));
        g2.fillRect(x - 5, y - 5, width + 10, height + 10);

        // Main block
        g2.setColor(NeonColors.pulse(NeonColors.MAGENTA, pulse));
        g2.fillRect(x, y, width, height);

        // Inner pattern (grid lines)
        g2.setColor(NeonColors.dark(NeonColors.MAGENTA));
        g2.fillRect(x + 2, y + 2, width - 4, height - 4);

        // Cross pattern
        g2.setColor(NeonColors.MAGENTA);
        g2.fillRect(x + width/2 - 2, y + 4, 4, height - 8);
        g2.fillRect(x + 4, y + height/2 - 2, width - 8, 4);

        // Glowing outline
        g2.setColor(NeonColors.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(x, y, width, height);
    }
}
