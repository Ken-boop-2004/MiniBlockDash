package com.miniblockdash;

import java.util.ArrayList;
import java.util.List;

/**
 * Extended level design - 3-4 minutes long, score 10000 = 100%
 * More green platforms, gravity sections, and fair but challenging obstacles
 */
public class LevelData {

    // Gaps scale with difficulty - always fair reaction time
    private static final int EASY_GAP = 280;
    private static final int MEDIUM_GAP = 320;
    private static final int HARD_GAP = 360;
    private static final int EXPERT_GAP = 400;

    public static List<Obstacle> createLevel() {
        List<Obstacle> level = new ArrayList<>();
        int x = 800;

        // ==================== PHASE 1: CYAN THEME (0-20%) ====================
        // Intro - Learn the basics
        x += 200;
        x = addBlock(level, x); x += EASY_GAP;
        x = addBlock(level, x); x += EASY_GAP;
        x = addSpike(level, x); x += EASY_GAP;
        x = addBlock(level, x); x += EASY_GAP;
        x = addSpike(level, x); x += EASY_GAP;
        x = addBlock(level, x); x += 180;
        x = addBlock(level, x); x += EASY_GAP;
        x = addSpike(level, x); x += EASY_GAP;
        
        // First green platform
        x = addPlatformWithSpike(level, x, 2); x += EASY_GAP + 100;
        x = addBlock(level, x); x += EASY_GAP;
        x = addSpike(level, x); x += 180;
        x = addSpike(level, x); x += EASY_GAP;
        
        // Jump pad intro
        x = addJumpPad(level, x); x += 400;
        x = addSpike(level, x); x += EASY_GAP;
        x = addBlock(level, x); x += EASY_GAP;
        x = addPlatformWithSpike(level, x, 3); x += EASY_GAP + 120;
        x = addBlock(level, x); x += EASY_GAP;
        
        // Double jumps
        x = addBlock(level, x); x += 180;
        x = addBlock(level, x); x += EASY_GAP;
        x = addSpike(level, x); x += 180;
        x = addSpike(level, x); x += EASY_GAP;
        x = addJumpPad(level, x); x += 420;
        x = addBlock(level, x); x += EASY_GAP;

        // ==================== PHASE 2: GREEN THEME (20-40%) ====================
        // More platforms and tall blocks
        x = addTallBlock(level, x); x += MEDIUM_GAP;
        x = addSpike(level, x); x += MEDIUM_GAP;
        x = addPlatformWithSpike(level, x, 3); x += MEDIUM_GAP + 100;
        x = addTallBlock(level, x); x += MEDIUM_GAP;
        x = addBlock(level, x); x += 180;
        x = addSpike(level, x); x += MEDIUM_GAP;
        
        // First gravity section!
        x = addGravityEnter(level, x); x += MEDIUM_GAP;
        x = addCeilingBlock(level, x); x += MEDIUM_GAP;
        x = addCeilingSpike(level, x); x += MEDIUM_GAP;
        x = addCeilingBlock(level, x); x += MEDIUM_GAP;
        x = addGravityExit(level, x); x += MEDIUM_GAP;
        
        x = addBlock(level, x); x += MEDIUM_GAP;
        x = addPlatformWithSpike(level, x, 2); x += MEDIUM_GAP + 100;
        x = addSpike(level, x); x += MEDIUM_GAP;
        x = addTallBlock(level, x); x += MEDIUM_GAP;
        
        // Saw intro
        x = addSaw(level, x, 280); x += MEDIUM_GAP;
        x = addBlock(level, x); x += MEDIUM_GAP;
        x = addPlatformWithSaw(level, x, 3); x += MEDIUM_GAP + 120;
        x = addSpike(level, x); x += MEDIUM_GAP;
        x = addJumpPad(level, x); x += 450;
        x = addSaw(level, x, 275); x += MEDIUM_GAP;
        x = addBlock(level, x); x += MEDIUM_GAP;

        // ==================== PHASE 3: MAGENTA THEME (40-55%) ====================
        // More intense combinations
        x = addDoubleSpike(level, x); x += MEDIUM_GAP;
        x = addTallBlock(level, x); x += MEDIUM_GAP;
        x = addPlatformWithSpike(level, x, 4); x += MEDIUM_GAP + 150;
        x = addSaw(level, x, 280); x += MEDIUM_GAP;
        x = addBlock(level, x); x += 180;
        x = addSpike(level, x); x += MEDIUM_GAP;
        
        // Second gravity section - longer
        x = addGravityEnter(level, x); x += HARD_GAP;
        x = addCeilingBlock(level, x); x += HARD_GAP;
        x = addCeilingSpike(level, x); x += HARD_GAP;
        x = addCeilingBlock(level, x); x += 200;
        x = addCeilingBlock(level, x); x += HARD_GAP;
        x = addCeilingSpike(level, x); x += HARD_GAP;
        x = addCeilingBlock(level, x); x += HARD_GAP;
        x = addGravityExit(level, x); x += HARD_GAP;
        
        x = addJumpPad(level, x); x += 480;
        x = addPlatformWithSpike(level, x, 3); x += HARD_GAP + 100;
        x = addTallBlock(level, x); x += HARD_GAP;
        x = addSaw(level, x, 270); x += HARD_GAP;
        x = addSpike(level, x); x += 180;
        x = addSpike(level, x); x += HARD_GAP;
        x = addBlock(level, x); x += HARD_GAP;

        // ==================== PHASE 4: ORANGE THEME (55-70%) ====================
        // Platform heavy section
        x = addPlatformWithSaw(level, x, 4); x += HARD_GAP + 150;
        x = addTallBlock(level, x); x += HARD_GAP;
        x = addPlatformWithSpike(level, x, 3); x += HARD_GAP + 100;
        x = addDoubleSpike(level, x); x += HARD_GAP;
        x = addJumpPad(level, x); x += 500;
        x = addSaw(level, x, 265); x += HARD_GAP;
        
        // Third gravity section with platforms
        x = addGravityEnter(level, x); x += HARD_GAP;
        x = addCeilingBlock(level, x); x += HARD_GAP;
        x = addCeilingSpike(level, x); x += 200;
        x = addCeilingSpike(level, x); x += HARD_GAP;
        x = addCeilingBlock(level, x); x += HARD_GAP;
        x = addCeilingSpike(level, x); x += HARD_GAP;
        x = addCeilingBlock(level, x); x += 200;
        x = addCeilingBlock(level, x); x += HARD_GAP;
        x = addGravityExit(level, x); x += HARD_GAP;
        
        x = addPlatformWithSpike(level, x, 3); x += HARD_GAP + 100;
        x = addBlock(level, x); x += HARD_GAP;
        x = addSaw(level, x, 280); x += HARD_GAP;
        x = addTallBlock(level, x); x += HARD_GAP;
        x = addSpike(level, x); x += 180;
        x = addDoubleSpike(level, x); x += HARD_GAP;

        // ==================== PHASE 5: PURPLE THEME (70-85%) ====================
        // Expert combinations
        x = addJumpPad(level, x); x += 520;
        x = addPlatformWithSaw(level, x, 3); x += EXPERT_GAP + 100;
        x = addTallBlock(level, x); x += EXPERT_GAP;
        x = addSpike(level, x); x += 180;
        x = addSpike(level, x); x += 180;
        x = addSpike(level, x); x += EXPERT_GAP;
        x = addSaw(level, x, 275); x += EXPERT_GAP;
        x = addPlatformWithSpike(level, x, 4); x += EXPERT_GAP + 150;
        
        // Fourth gravity - intense
        x = addGravityEnter(level, x); x += EXPERT_GAP;
        x = addCeilingSpike(level, x); x += EXPERT_GAP;
        x = addCeilingBlock(level, x); x += 200;
        x = addCeilingSpike(level, x); x += EXPERT_GAP;
        x = addCeilingBlock(level, x); x += EXPERT_GAP;
        x = addCeilingSpike(level, x); x += 200;
        x = addCeilingSpike(level, x); x += EXPERT_GAP;
        x = addCeilingBlock(level, x); x += EXPERT_GAP;
        x = addGravityExit(level, x); x += EXPERT_GAP;
        
        x = addDoubleSpike(level, x); x += EXPERT_GAP;
        x = addJumpPad(level, x); x += 550;
        x = addSaw(level, x, 270); x += EXPERT_GAP;
        x = addPlatformWithSpike(level, x, 3); x += EXPERT_GAP + 100;
        x = addTallBlock(level, x); x += EXPERT_GAP;

        // ==================== PHASE 6: RED/GOLD FINALE (85-100%) ====================
        // Final challenge - everything combined
        x = addSpike(level, x); x += 180;
        x = addSpike(level, x); x += 180;
        x = addDoubleSpike(level, x); x += EXPERT_GAP;
        x = addPlatformWithSaw(level, x, 4); x += EXPERT_GAP + 150;
        x = addTallBlock(level, x); x += EXPERT_GAP;
        x = addSaw(level, x, 275); x += EXPERT_GAP;
        
        // Final gravity gauntlet
        x = addGravityEnter(level, x); x += EXPERT_GAP;
        x = addCeilingBlock(level, x); x += 200;
        x = addCeilingSpike(level, x); x += EXPERT_GAP;
        x = addCeilingBlock(level, x); x += EXPERT_GAP;
        x = addCeilingSpike(level, x); x += 200;
        x = addCeilingBlock(level, x); x += EXPERT_GAP;
        x = addGravityExit(level, x); x += EXPERT_GAP;
        
        x = addJumpPad(level, x); x += 550;
        x = addPlatformWithSpike(level, x, 3); x += EXPERT_GAP + 100;
        x = addDoubleSpike(level, x); x += EXPERT_GAP;
        x = addTallBlock(level, x); x += EXPERT_GAP;
        x = addSaw(level, x, 280); x += EXPERT_GAP;
        x = addSpike(level, x); x += 180;
        x = addSpike(level, x); x += EXPERT_GAP;
        x = addBlock(level, x); x += EXPERT_GAP;
        
        // Victory run
        x += 500;
        level.add(new EndMarker(x));

        return level;
    }

    // Helper methods
    private static int addBlock(List<Obstacle> level, int x) {
        level.add(new Block(x));
        return x;
    }

    private static int addSpike(List<Obstacle> level, int x) {
        level.add(new Spike(x));
        return x;
    }

    private static int addTallBlock(List<Obstacle> level, int x) {
        level.add(new TallBlock(x));
        return x;
    }

    private static int addDoubleSpike(List<Obstacle> level, int x) {
        level.add(new DoubleSpike(x));
        return x;
    }

    private static int addJumpPad(List<Obstacle> level, int x) {
        level.add(new JumpPad(x));
        return x;
    }

    private static int addSaw(List<Obstacle> level, int x, int y) {
        level.add(new RotatingSaw(x, y));
        return x;
    }

    private static int addCeilingBlock(List<Obstacle> level, int x) {
        level.add(new CeilingBlock(x));
        return x;
    }

    private static int addCeilingSpike(List<Obstacle> level, int x) {
        level.add(new CeilingSpike(x));
        return x;
    }

    private static int addGravityEnter(List<Obstacle> level, int x) {
        level.add(new GravityPortal(x, false));
        return x;
    }

    private static int addGravityExit(List<Obstacle> level, int x) {
        level.add(new GravityPortal(x, true));
        return x;
    }

    private static int addPlatformWithSpike(List<Obstacle> level, int x, int blocks) {
        level.add(new SafePlatform(x, 260, blocks));
        for (int i = 0; i < blocks - 1; i++) {
            level.add(new Spike(x + 15 + i * 30));
        }
        return x + blocks * 30;
    }

    private static int addPlatformWithSaw(List<Obstacle> level, int x, int blocks) {
        level.add(new SafePlatform(x, 250, blocks));
        level.add(new RotatingSaw(x + (blocks * 15), 290));
        return x + blocks * 30;
    }
}
