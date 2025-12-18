package com.miniblockdash;

import java.util.List;

public class ObstaclePattern {

    // Easy rhythm jump
    public static List<Obstacle> singleJump(int x) {
        return List.of(new Block(x));
    }

    // Two quick jumps
    public static List<Obstacle> doubleJump(int x) {
        return List.of(
                new Block(x),
                new Block(x + 260)
        );
    }

    // Spike timing jump
    public static List<Obstacle> spikeJump(int x) {
        return List.of(new Spike(x));
    }

    // Spike -> block rhythm
    public static List<Obstacle> comboJump(int x) {
        return List.of(
                new Spike(x),
                new Block(x + 280)
        );
    }

    // Tall block forces late jump
    public static List<Obstacle> tallJump(int x) {
        return List.of(new TallBlock(x));
    }

    // Spike -> tall block
    public static List<Obstacle> spikeTallCombo(int x) {
        return List.of(
                new Spike(x),
                new TallBlock(x + 300)
        );
    }

    // Triple rhythm pattern
    public static List<Obstacle> tripleRhythm(int x) {
        return List.of(
                new Block(x),
                new Spike(x + 220),
                new Block(x + 440)
        );
    }

    // High skill late-game pattern
    public static List<Obstacle> lateGameCombo(int x) {
        return List.of(
                new DoubleSpike(x),
                new Block(x + 260),
                new TallBlock(x + 520)
        );
    }

    public static List<Obstacle> gravityFlip(int x) {
        return List.of(
                new GravityPortal(x, false),
                new Block(x + 300)
        );
    }

    public static List<Obstacle> gravityCombo(int x) {
        return List.of(
                new GravityPortal(x, true),
                new Spike(x + 220),
                new Block(x + 440)
        );
    }

    public static List<Obstacle> gravitySection(int x) {
        return List.of(
                new GravityPortal(x, false),
                new CeilingBlock(x + 250),
                new CeilingSpike(x + 420),
                new CeilingBlock(x + 580),
                new GravityPortal(x + 760, true)
        );
    }

    public static List<Obstacle> gravityComboSection(int x) {
        return List.of(
                new GravityPortal(x, false),
                new CeilingSpike(x + 240),
                new CeilingBlock(x + 420),
                new CeilingSpike(x + 600),
                new GravityPortal(x + 820, true)
        );
    }

    // NEW: Jump pad pattern - launches player high
    public static List<Obstacle> jumpPadLaunch(int x) {
        return List.of(
                new JumpPad(x),
                new Spike(x + 350)
        );
    }

    // NEW: Saw blade pattern
    public static List<Obstacle> sawBlade(int x) {
        return List.of(new RotatingSaw(x, 280));
    }

    // NEW: Double saw challenge
    public static List<Obstacle> doubleSaw(int x) {
        return List.of(
                new RotatingSaw(x, 280),
                new RotatingSaw(x + 200, 300)
        );
    }

    // NEW: Jump pad over saw
    public static List<Obstacle> jumpPadOverSaw(int x) {
        return List.of(
                new JumpPad(x, 28),
                new RotatingSaw(x + 150, 260)
        );
    }

    // NEW: Intense combo
    public static List<Obstacle> intenseCombo(int x) {
        return List.of(
                new JumpPad(x),
                new RotatingSaw(x + 200, 270),
                new Spike(x + 400),
                new TallBlock(x + 600)
        );
    }

    // Safe platform over danger - jump on green platform to avoid spikes below
    public static List<Obstacle> platformOverSpikes(int x) {
        return List.of(
                new SafePlatform(x, 260, 3),  // Green platform (3 blocks wide)
                new Spike(x + 10),             // Spike below
                new Spike(x + 40),
                new Spike(x + 70)
        );
    }

    // Safe platform over saw blade
    public static List<Obstacle> platformOverSaw(int x) {
        return List.of(
                new SafePlatform(x, 250, 3),
                new RotatingSaw(x + 20, 290)
        );
    }

    // Platform hop - multiple platforms with danger below
    public static List<Obstacle> platformHop(int x) {
        return List.of(
                new SafePlatform(x, 265, 2),
                new Spike(x + 20),
                new SafePlatform(x + 150, 265, 2),
                new Spike(x + 170)
        );
    }

    // Long platform over multiple dangers
    public static List<Obstacle> longPlatformRun(int x) {
        return List.of(
                new SafePlatform(x, 255, 5),   // 5 blocks wide
                new Spike(x + 20),
                new Spike(x + 50),
                new RotatingSaw(x + 80, 290),
                new Spike(x + 120)
        );
    }
}
