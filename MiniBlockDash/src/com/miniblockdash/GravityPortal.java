package com.miniblockdash;

import java.awt.*;

public class GravityPortal extends Obstacle {
    private final boolean exit;
    private double pulsePhase = 0;
    private double rotationAngle = 0;

    public GravityPortal(int x, boolean exit) {
        this.x = x;
        this.exit = exit;
        this.y = 40; // Start near ceiling
        this.width = 30;
        this.height = 290; // Span from ceiling (40) to ground (330) so player can reach from either position
    }

    public boolean isExit() {
        return exit;
    }

    @Override
    public void update(int speed) {
        x -= speed;
        pulsePhase += 0.15;
        rotationAngle += 0.1;
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color portalColor = exit ? NeonColors.CYAN : NeonColors.PURPLE;
        double pulse = 0.5 + 0.5 * Math.sin(pulsePhase);

        int cx = x + width / 2;
        int cy = y + height / 2;

        // Outer glow - vertical beam
        for (int i = 3; i >= 0; i--) {
            int alpha = (int)(40 * pulse * (4 - i) / 4);
            g2.setColor(NeonColors.glow(portalColor, alpha));
            g2.fillRoundRect(x - i * 3, y - i * 2, width + i * 6, height + i * 4, 15, 15);
        }

        // Main portal beam
        g2.setColor(portalColor);
        g2.fillRoundRect(x, y, width, height, 10, 10);

        // Inner darker area
        g2.setColor(NeonColors.dark(portalColor));
        g2.fillRoundRect(x + 5, y + 10, width - 10, height - 20, 8, 8);

        // Rotating energy particles along the beam
        g2.setColor(NeonColors.WHITE);
        for (int i = 0; i < 5; i++) {
            double angle = rotationAngle + i * Math.PI * 2 / 5;
            int py = y + 30 + (i * (height - 60) / 4);
            int px = (int)(cx + 5 * Math.cos(angle + py * 0.02));
            g2.fillOval(px - 3, py - 3, 6, 6);
        }

        // Arrow indicators (top and bottom)
        g2.setColor(NeonColors.WHITE);
        int[] arrowX = {cx - 10, cx, cx + 10};
        
        // Top arrow
        int topArrowY = y + 25;
        int[] topArrowYs;
        if (exit) {
            topArrowYs = new int[]{topArrowY - 8, topArrowY + 8, topArrowY - 8}; // Down arrow
        } else {
            topArrowYs = new int[]{topArrowY + 8, topArrowY - 8, topArrowY + 8}; // Up arrow
        }
        g2.fillPolygon(arrowX, topArrowYs, 3);
        
        // Bottom arrow
        int bottomArrowY = y + height - 25;
        int[] bottomArrowYs;
        if (exit) {
            bottomArrowYs = new int[]{bottomArrowY - 8, bottomArrowY + 8, bottomArrowY - 8}; // Down arrow
        } else {
            bottomArrowYs = new int[]{bottomArrowY + 8, bottomArrowY - 8, bottomArrowY + 8}; // Up arrow
        }
        g2.fillPolygon(arrowX, bottomArrowYs, 3);

        // Glowing outline
        g2.setColor(NeonColors.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x, y, width, height, 10, 10);
    }
}
