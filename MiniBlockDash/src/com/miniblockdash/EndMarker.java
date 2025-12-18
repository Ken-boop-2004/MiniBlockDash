package com.miniblockdash;

import java.awt.*;
import javax.swing.ImageIcon;

/**
 * End of level marker - triggers level complete when player reaches it.
 * Displays the Victory image at the finish line.
 */
public class EndMarker extends Obstacle {
    private double pulsePhase = 0;
    private Image victoryImage;
    private boolean imageLoaded = false;

    public EndMarker(int x) {
        this.x = x;
        this.y = 80; // Position so it's visible and player can reach it at ground level
        this.width = 180;
        this.height = 250;
        loadImage();
    }

    private void loadImage() {
        try {
            java.net.URL imgUrl = getClass().getResource("/com/miniblockdash/resources/VictoryIMG.jpg");
            if (imgUrl != null) {
                victoryImage = new ImageIcon(imgUrl).getImage();
                imageLoaded = true;
            }
        } catch (Exception e) {
            System.out.println("Could not load victory image: " + e.getMessage());
        }
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

        double pulse = 0.5 + 0.5 * Math.sin(pulsePhase);

        // Outer glow effect
        for (int i = 4; i >= 0; i--) {
            int alpha = (int)(40 * pulse * (5 - i) / 5);
            g2.setColor(new Color(255, 215, 0, alpha)); // Gold glow
            g2.fillRect(x - i * 8, y - i * 8, width + i * 16, height + i * 16);
        }

        if (imageLoaded && victoryImage != null) {
            // Draw the victory image
            g2.drawImage(victoryImage, x, y, width, height, null);

            // Glowing border around image
            g2.setColor(NeonColors.pulse(NeonColors.YELLOW, pulse));
            g2.setStroke(new BasicStroke(4));
            g2.drawRect(x, y, width, height);

            // Inner white border
            g2.setColor(NeonColors.WHITE);
            g2.setStroke(new BasicStroke(2));
            g2.drawRect(x + 3, y + 3, width - 6, height - 6);
        } else {
            // Fallback if image not loaded - draw a finish portal
            g2.setColor(NeonColors.GREEN);
            g2.fillRect(x, y, width, height);

            g2.setColor(new Color(0, 50, 30));
            g2.fillRect(x + 10, y + 10, width - 20, height - 20);

            g2.setFont(new Font("Arial", Font.BOLD, 16));
            g2.setColor(NeonColors.WHITE);
            g2.drawString("FINISH", x + 45, y + height / 2);
        }

        // "FINISH" banner above
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRoundRect(x + 20, y - 35, 110, 30, 10, 10);
        g2.setColor(NeonColors.pulse(NeonColors.YELLOW, pulse));
        g2.drawString("FINISH!", x + 40, y - 13);

        // Sparkle effects around the finish
        g2.setColor(new Color(255, 255, 100, (int)(150 * pulse)));
        for (int i = 0; i < 6; i++) {
            double angle = pulsePhase + i * Math.PI / 3;
            int sparkleX = x + width / 2 + (int)(80 * Math.cos(angle));
            int sparkleY = y + height / 2 + (int)(120 * Math.sin(angle));
            drawSparkle(g2, sparkleX, sparkleY, (int)(8 * pulse));
        }
    }

    private void drawSparkle(Graphics2D g2, int cx, int cy, int size) {
        // Draw a 4-pointed star sparkle
        int[] xPoints = {cx, cx + size, cx, cx - size};
        int[] yPoints = {cy - size * 2, cy, cy + size * 2, cy};
        g2.fillPolygon(xPoints, yPoints, 4);

        int[] xPoints2 = {cx - size * 2, cx, cx + size * 2, cx};
        int[] yPoints2 = {cy, cy - size, cy, cy + size};
        g2.fillPolygon(xPoints2, yPoints2, 4);
    }

    @Override
    public Rectangle getBounds() {
        // Hitbox spans full height so player can reach it from ground (y=300) or ceiling (y=40)
        return new Rectangle(x + width / 2 - 15, 40, 30, 290);
    }
}
