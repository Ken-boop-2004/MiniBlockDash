package com.miniblockdash;

import java.awt.*;

public class SpeedPortal extends Obstacle {
    private final int speedBoost;
    private double pulsePhase = 0;
    private double waveOffset = 0;

    public SpeedPortal(int x, int speedBoost) {
        this.x = x;
        this.y = 260;
        this.width = 30;
        this.height = 80;
        this.speedBoost = speedBoost;
    }

    @Override
    public void update(int speed) {
        x -= speed;
        pulsePhase += 0.2;
        waveOffset += 0.3;
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color portalColor = speedBoost > 0 ? NeonColors.GREEN : NeonColors.BLUE;
        double pulse = 0.6 + 0.4 * Math.sin(pulsePhase);

        // Outer glow
        for (int i = 2; i >= 0; i--) {
            int alpha = (int)(40 * pulse * (3 - i) / 3);
            g2.setColor(NeonColors.glow(portalColor, alpha));
            g2.fillRect(x - i * 3, y - i * 3, width + i * 6, height + i * 6);
        }

        // Main portal
        g2.setColor(NeonColors.pulse(portalColor, pulse));
        g2.fillRect(x, y, width, height);

        // Inner darker area
        g2.setColor(NeonColors.dark(portalColor));
        g2.fillRect(x + 4, y + 4, width - 8, height - 8);

        // Speed lines (animated)
        g2.setColor(portalColor);
        g2.setStroke(new BasicStroke(2));
        for (int i = 0; i < 4; i++) {
            int lineY = y + 15 + i * 15;
            int offset = (int)(5 * Math.sin(waveOffset + i));
            g2.drawLine(x + 6 + offset, lineY, x + width - 6 + offset, lineY);
        }

        // Arrow indicators
        g2.setColor(NeonColors.WHITE);
        int cx = x + width / 2;
        if (speedBoost > 0) {
            // Fast arrows (>>)
            for (int i = 0; i < 2; i++) {
                int ax = cx - 5 + i * 8;
                int[] arrowX = {ax - 5, ax + 5, ax - 5};
                int[] arrowY = {y + height/2 - 8, y + height/2, y + height/2 + 8};
                g2.fillPolygon(arrowX, arrowY, 3);
            }
        } else {
            // Slow arrows (<<)
            for (int i = 0; i < 2; i++) {
                int ax = cx + 5 - i * 8;
                int[] arrowX = {ax + 5, ax - 5, ax + 5};
                int[] arrowY = {y + height/2 - 8, y + height/2, y + height/2 + 8};
                g2.fillPolygon(arrowX, arrowY, 3);
            }
        }

        // Glowing outline
        g2.setColor(NeonColors.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(x, y, width, height);
    }

    public int getSpeedBoost() {
        return speedBoost;
    }
}
