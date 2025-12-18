package com.miniblockdash;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class ObstacleManager {

    private List<Obstacle> obstacles = new ArrayList<>();
    private boolean levelLoaded = false;

    public void update(int speed, int score) {
        // Load the fixed level once at start
        if (!levelLoaded) {
            obstacles = LevelData.createLevel();
            levelLoaded = true;
        }

        // Update all obstacles
        for (Obstacle o : obstacles) {
            o.update(speed);
        }

        // Remove obstacles that have passed
        obstacles.removeIf(o -> o.x < -100);
    }

    public boolean checkCollision(Player p, GamePanel panel) {
        for (Obstacle o : obstacles) {
            // End marker - level complete!
            if (o instanceof EndMarker) {
                if (p.getBounds(panel.invertedGravity)
                        .intersects(o.getBounds(panel.invertedGravity))) {
                    panel.triggerLevelComplete();
                    o.x = -1000; // Remove it
                }
                continue;
            }

            // Gravity portal - flip gravity
            if (o instanceof GravityPortal) {
                if (p.getBounds(panel.invertedGravity)
                        .intersects(o.getBounds(panel.invertedGravity))) {
                    panel.flipGravity();
                    o.x = -100;
                }
                continue;
            }

            // Jump pad - super jump
            if (o instanceof JumpPad pad) {
                if (p.getBounds(panel.invertedGravity)
                        .intersects(pad.getBounds(panel.invertedGravity))) {
                    p.superJump(pad.getBoostStrength());
                    panel.triggerJumpPadEffect();
                    o.x = -100;
                }
                continue;
            }

            // Safe platform - player can land on it
            if (o instanceof SafePlatform platform) {
                if (p.getBounds(panel.invertedGravity)
                        .intersects(platform.getBounds(panel.invertedGravity))) {
                    p.landOnPlatform(platform.y, platform.height, panel.invertedGravity);
                }
                continue;
            }

            // Collision with deadly obstacles
            if (p.getBounds(panel.invertedGravity)
                    .intersects(o.getBounds(panel.invertedGravity))) {
                return true;
            }
        }
        return false;
    }

    public void draw(Graphics g, boolean inverted) {
        for (Obstacle o : obstacles) {
            o.draw(g, inverted);
        }
    }

    // Store initial end marker position for accurate progress
    private int initialEndMarkerX = -1;

    // Get progress based on end marker position
    public int getProgress() {
        if (!levelLoaded) return 0;
        
        for (Obstacle o : obstacles) {
            if (o instanceof EndMarker) {
                // Store initial position on first check
                if (initialEndMarkerX < 0) {
                    initialEndMarkerX = o.x;
                }
                
                // Progress based on how far end marker has moved toward player
                int distanceTraveled = initialEndMarkerX - o.x;
                int totalDistance = initialEndMarkerX - 100; // Distance from start to player position
                
                if (o.x <= 100) return 100;
                
                int progress = (distanceTraveled * 100) / totalDistance;
                return Math.max(0, Math.min(99, progress));
            }
        }
        
        // If end marker passed, we're at 100%
        return 100;
    }

    public void reset() {
        obstacles.clear();
        levelLoaded = false;
        initialEndMarkerX = -1;
    }
}
