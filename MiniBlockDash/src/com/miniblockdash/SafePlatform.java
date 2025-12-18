package com.miniblockdash;

import java.awt.*;

public class SafePlatform extends Obstacle {
    private double pulsePhase = Math.random() * Math.PI * 2;
    private int blockCount;

    public SafePlatform(int x, int y, int blockCount) {
        this.x = x;
        this.y = y;
        this.blockCount = blockCount;
        this.width = 30 * blockCount;
        this.height = 20;
    }

    public SafePlatform(int x, int y) {
        this(x, y, 3); // Default 3 blocks wide
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

        double pulse = 0.7 + 0.3 * Math.sin(pulsePhase);

        // Outer glow
        g2.setColor(NeonColors.glow(NeonColors.GREEN, (int)(60 * pulse)));
        g2.fillRoundRect(x - 4, y - 4, width + 8, height + 8, 8, 8);

        // Main platform
        g2.setColor(NeonColors.pulse(NeonColors.GREEN, pulse));
        g2.fillRoundRect(x, y, width, height, 6, 6);

        // Inner darker area
        g2.setColor(NeonColors.dark(NeonColors.GREEN));
        g2.fillRoundRect(x + 3, y + 3, width - 6, height - 6, 4, 4);

        // Grid pattern (block divisions)
        g2.setColor(NeonColors.GREEN);
        for (int i = 1; i < blockCount; i++) {
            int lineX = x + i * 30;
            g2.drawLine(lineX, y + 4, lineX, y + height - 4);
        }

        // Top highlight
        g2.setColor(new Color(150, 255, 150, 100));
        g2.fillRoundRect(x + 4, y + 2, width - 8, 4, 2, 2);

        // Glowing outline
        g2.setColor(NeonColors.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x, y, width, height, 6, 6);
    }

    // Safe platforms don't kill - they're handled specially in collision
    public boolean isSafe() {
        return true;
    }
}
