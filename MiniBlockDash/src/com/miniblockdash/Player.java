package com.miniblockdash;

import java.awt.*;

public class Player {
    int x = 100;
    int y = 300;
    int size = 30;

    double velocityY = -15;
    double gravity = 1.2;
    boolean onGround = true;
    double rotation = 0;
    boolean inverted = false;
    double jumpStrength = 18;

    // Visual effects
    private double glowPulse = 0;
    private Color playerColor = NeonColors.CYAN;
    private boolean justJumped = false;

    public void update(boolean invertedGravity) {
        // Apply gravity based on direction
        if (invertedGravity) {
            velocityY -= gravity; // Fall upward
        } else {
            velocityY += gravity; // Fall downward
        }
        
        y += velocityY;
        glowPulse += 0.15;
        justJumped = false;

        if (!onGround) {
            rotation += invertedGravity ? -8 : 8;
        }

        // Ground/ceiling collision
        if (invertedGravity) {
            // Ceiling at y=40 (player walks on ceiling)
            if (y <= 40) {
                y = 40;
                velocityY = 0;
                onGround = true;
                rotation = Math.round(rotation / 90.0) * 90;
            }
            // Don't fall below ground
            if (y >= 300) {
                y = 300;
                velocityY = 0;
            }
        } else {
            // Ground at y=300
            if (y >= 300) {
                y = 300;
                velocityY = 0;
                onGround = true;
                rotation = Math.round(rotation / 90.0) * 90;
            }
            // Don't go above ceiling
            if (y <= 40) {
                y = 40;
                velocityY = 0;
            }
        }
    }

    public void jump(boolean invertedGravity) {
        if (onGround) {
            if (invertedGravity) {
                velocityY = jumpStrength; // Jump downward when inverted
            } else {
                velocityY = -jumpStrength; // Jump upward normally
            }
            onGround = false;
            justJumped = true;
        }
    }

    public void superJump(double strength) {
        // Super jump always goes in the "up" direction relative to current gravity
        velocityY = inverted ? strength : -strength;
        onGround = false;
        justJumped = true;
    }

    public void landOnPlatform(int platformY, int platformHeight, boolean invertedGravity) {
        if (!invertedGravity) {
            // Normal gravity - land on top of platform when falling down
            if (velocityY > 0) {
                int playerBottom = y + size;
                if (playerBottom >= platformY && playerBottom <= platformY + 15) {
                    y = platformY - size;
                    velocityY = 0;
                    onGround = true;
                    rotation = Math.round(rotation / 90.0) * 90;
                }
            }
        } else {
            // Inverted gravity - land on bottom of platform when "falling" up
            // Player is at ceiling, falling upward (velocityY < 0)
            if (velocityY < 0) {
                int playerTop = y;
                int platformBottom = platformY + platformHeight;
                if (playerTop <= platformBottom && playerTop >= platformBottom - 15) {
                    y = platformBottom;
                    velocityY = 0;
                    onGround = true;
                    rotation = Math.round(rotation / 90.0) * 90;
                }
            }
        }
    }

    public boolean didJustJump() {
        return justJumped;
    }

    public void flipGravity() {
        inverted = !inverted;
        onGround = false; // Player is now in the air, will fall to new ground/ceiling
        velocityY = 0;
    }

    public void reset() {
        y = 300;
        velocityY = 0;
        onGround = true;
        rotation = 0;
    }

    public void setColor(Color c) {
        this.playerColor = c;
    }

    public Color getColor() {
        return playerColor;
    }

    public void draw(Graphics g, boolean invertedGravity) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Player y is now the actual screen position
        int drawY = y;
        int cx = x + size / 2;
        int cy = drawY + size / 2;

        // Save transform
        var oldTransform = g2.getTransform();

        // Rotate around center
        g2.rotate(Math.toRadians(rotation), cx, cy);

        // Outer glow (pulsing)
        double pulse = 0.5 + 0.5 * Math.sin(glowPulse);
        int glowSize = (int)(8 + 4 * pulse);
        g2.setColor(NeonColors.glow(playerColor, (int)(80 * pulse)));
        g2.fillRect(x - glowSize, drawY - glowSize, size + glowSize * 2, size + glowSize * 2);

        // Inner glow
        g2.setColor(NeonColors.glow(playerColor, 120));
        g2.fillRect(x - 3, drawY - 3, size + 6, size + 6);

        // Main cube
        g2.setColor(playerColor);
        g2.fillRect(x, drawY, size, size);

        // Highlight (top-left corner shine)
        g2.setColor(new Color(255, 255, 255, 150));
        g2.fillRect(x + 3, drawY + 3, 8, 8);

        // Glowing outline
        g2.setColor(NeonColors.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(x, drawY, size, size);

        // Restore transform
        g2.setTransform(oldTransform);
    }

    public Rectangle getBounds(boolean invertedGravity) {
        // Player y is now the actual screen position - no transformation needed
        return new Rectangle(x, y, size, size);
    }
}
