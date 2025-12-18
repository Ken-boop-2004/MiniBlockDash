package com.miniblockdash;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class ParticleSystem {
    private final List<Particle> particles = new ArrayList<>();
    private final Random rand = new Random();

    // Trail particles behind player
    public void emitTrail(int x, int y, Color color) {
        for (int i = 0; i < 2; i++) {
            double vx = -2 - rand.nextDouble() * 2;
            double vy = (rand.nextDouble() - 0.5) * 2;
            int life = 15 + rand.nextInt(10);
            int size = 3 + rand.nextInt(4);
            particles.add(new Particle(x, y + rand.nextInt(20), vx, vy, life, color, size));
        }
    }

    // Burst on death
    public void emitBurst(int x, int y, Color color, int count) {
        for (int i = 0; i < count; i++) {
            double angle = rand.nextDouble() * Math.PI * 2;
            double speed = 3 + rand.nextDouble() * 5;
            double vx = Math.cos(angle) * speed;
            double vy = Math.sin(angle) * speed;
            int life = 20 + rand.nextInt(20);
            int size = 4 + rand.nextInt(6);
            particles.add(new Particle(x, y, vx, vy, life, color, size));
        }
    }

    // Jump burst
    public void emitJump(int x, int y, Color color) {
        for (int i = 0; i < 8; i++) {
            double vx = (rand.nextDouble() - 0.5) * 4;
            double vy = 2 + rand.nextDouble() * 3;
            int life = 10 + rand.nextInt(10);
            particles.add(new Particle(x + rand.nextInt(30), y + 30, vx, vy, life, color, 4));
        }
    }

    public void update() {
        Iterator<Particle> it = particles.iterator();
        while (it.hasNext()) {
            Particle p = it.next();
            p.update();
            if (p.isDead()) it.remove();
        }
    }

    public void draw(Graphics2D g2) {
        for (Particle p : particles) {
            p.draw(g2);
        }
    }

    public void clear() {
        particles.clear();
    }
}
