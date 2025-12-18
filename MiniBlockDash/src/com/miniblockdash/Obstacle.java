package com.miniblockdash;


import java.awt.*;


public abstract class Obstacle {
    protected int x, y, width, height;
    
    // Whether this obstacle is designed for ceiling (inverted gravity sections)
    protected boolean isCeilingObstacle = false;


    public abstract void update(int speed);
    public abstract void draw(Graphics g);


    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public Rectangle getBounds(boolean inverted) {
        // No transformation - obstacles are placed at their actual positions
        // Ceiling obstacles at y=20 (top), ground obstacles at y=300 (bottom)
        return getBounds();
    }

    public void draw(Graphics g, boolean inverted) {
        // No flipping - just draw obstacles at their actual positions
        draw(g);
    }


}