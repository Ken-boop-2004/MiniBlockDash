package com.miniblockdash;

import java.awt.*;

public class CeilingBlock extends Obstacle {
    private double pulsePhase = Math.random() * Math.PI * 2;

    public CeilingBlock(int x) {
        this.x = x;
        this.y = 30; // At ceiling - player hits this when inverted (walking on ceiling)
        this.width = 30;
        this.height = 40;
        this.isCeilingObstacle = true;
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
        g2.setColor(NeonColors.glow(NeonColors.BLUE, (int)(50 * pulse)));
        g2.fillRect(x - 5, y - 5, width + 10, height + 10);

        // Main block
        g2.setColor(NeonColors.pulse(NeonColors.BLUE, pulse));
        g2.fillRect(x, y, width, height);

        // Inner darker area
        g2.setColor(NeonColors.dark(NeonColors.BLUE));
        g2.fillRect(x + 3, y + 3, width - 6, height - 6);

        // Horizontal lines
        g2.setColor(NeonColors.BLUE);
        g2.fillRect(x + 5, y + height/3, width - 10, 3);
        g2.fillRect(x + 5, y + height*2/3, width - 10, 3);

        // Glowing outline
        g2.setColor(NeonColors.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(x, y, width, height);
    }
}
