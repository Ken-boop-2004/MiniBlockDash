package com.miniblockdash;

import java.awt.*;

public class GameSettings {
    // Audio
    public float musicVolume = 0.8f;  // 0.0 to 1.0
    public boolean musicEnabled = true;

    // Visual
    public boolean particlesEnabled = true;
    public boolean showFPS = false;
    public int playerColorIndex = 0;

    // Player color options
    public static final Color[] PLAYER_COLORS = {
        NeonColors.CYAN,
        NeonColors.MAGENTA,
        NeonColors.GREEN,
        NeonColors.YELLOW,
        NeonColors.ORANGE,
        NeonColors.PINK,
        NeonColors.PURPLE,
        NeonColors.RED
    };

    public static final String[] COLOR_NAMES = {
        "Cyan", "Magenta", "Green", "Yellow", "Orange", "Pink", "Purple", "Red"
    };

    public Color getPlayerColor() {
        return PLAYER_COLORS[playerColorIndex];
    }

    public String getPlayerColorName() {
        return COLOR_NAMES[playerColorIndex];
    }

    public void nextPlayerColor() {
        playerColorIndex = (playerColorIndex + 1) % PLAYER_COLORS.length;
    }

    public void prevPlayerColor() {
        playerColorIndex = (playerColorIndex - 1 + PLAYER_COLORS.length) % PLAYER_COLORS.length;
    }
}
