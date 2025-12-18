package com.miniblockdash;

import java.awt.*;

public class NeonColors {
    // Primary neon colors
    public static final Color CYAN = new Color(0, 255, 255);
    public static final Color MAGENTA = new Color(255, 0, 255);
    public static final Color YELLOW = new Color(255, 255, 0);
    public static final Color GREEN = new Color(0, 255, 128);
    public static final Color ORANGE = new Color(255, 128, 0);
    public static final Color PINK = new Color(255, 100, 200);
    public static final Color PURPLE = new Color(180, 0, 255);
    public static final Color BLUE = new Color(0, 128, 255);
    public static final Color RED = new Color(255, 50, 50);
    public static final Color WHITE = new Color(255, 255, 255);

    // Glow versions (slightly transparent for layering)
    public static Color glow(Color c, int alpha) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
    }

    // Darker versions for backgrounds
    public static Color dark(Color c) {
        return new Color(c.getRed() / 4, c.getGreen() / 4, c.getBlue() / 4);
    }

    // Pulse color based on beat
    public static Color pulse(Color c, double intensity) {
        int r = Math.min(255, (int)(c.getRed() * (0.6 + 0.4 * intensity)));
        int g = Math.min(255, (int)(c.getGreen() * (0.6 + 0.4 * intensity)));
        int b = Math.min(255, (int)(c.getBlue() * (0.6 + 0.4 * intensity)));
        return new Color(r, g, b);
    }
}
