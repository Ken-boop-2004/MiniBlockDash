package com.miniblockdash;

import java.awt.*;

public class RotatingSaw extends Obstacle {
    private double rotation = 0;
    private final int radius;
    private final int teeth;

    public RotatingSaw(int x, int y) {
        this(x, y, 25, 8);
    }

    public RotatingSaw(int x, int y, int radius, int teeth) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.teeth = teeth;
        this.width = radius * 2;
        this.height = radius * 2;
    }

    @Override
    public void update(int speed) {
        x -= speed;
        rotation += 0.15;
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int cx = x + radius;
        int cy = y + radius;

        // Outer glow
        g2.setColor(NeonColors.glow(NeonColors.RED, 40));
        g2.fillOval(cx - radius - 8, cy - radius - 8, (radius + 8) * 2, (radius + 8) * 2);

        // Draw saw teeth
        g2.setColor(NeonColors.RED);
        int[] xPoints = new int[teeth * 2];
        int[] yPoints = new int[teeth * 2];

        for (int i = 0; i < teeth * 2; i++) {
            double angle = rotation + (i * Math.PI / teeth);
            int r = (i % 2 == 0) ? radius : radius - 10;
            xPoints[i] = (int) (cx + r * Math.cos(angle));
            yPoints[i] = (int) (cy + r * Math.sin(angle));
        }
        g2.fillPolygon(xPoints, yPoints, teeth * 2);

        // Center circle
        g2.setColor(NeonColors.dark(NeonColors.RED));
        g2.fillOval(cx - 8, cy - 8, 16, 16);

        // Glowing outline
        g2.setColor(NeonColors.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.drawPolygon(xPoints, yPoints, teeth * 2);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x + 5, y + 5, width - 10, height - 10);
    }
}
