package com.game.fx;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExplosionManager {
    private final List<Particle> explosionParticles = new ArrayList<>();
    private final Random random = new Random();

    public void spawnExplosion(double originX, double originY) {
        int particleCount = 50;

        for (int i = 0; i < particleCount; i++) {
            double angle = random.nextDouble() * 2 * Math.PI;
            double speed = 2 + random.nextDouble() * 7;

            double vx = Math.cos(angle) * speed;
            double vy = Math.sin(angle) * speed;

            double size = 6 + random.nextDouble() * 10;
            double decayRate = 0.015 + random.nextDouble() * 0.025;

            Color color;
            double randColor = random.nextDouble();
            if (randColor < 0.4) {
                color = Color.RED;
            } else if (randColor < 0.7) {
                color = Color.ORANGE;
            } else if (randColor < 0.9) {
                color = Color.YELLOW;
            } else {
                color = Color.LIGHTGRAY;
            }

            explosionParticles.add(new Particle(originX, originY, vx, vy, size, color, decayRate));
        }
    }

    public void update() {
        for (int i = 0; i < explosionParticles.size(); i++) {
            Particle p = explosionParticles.get(i);
            p.update();
            if (p.isDead()) {
                explosionParticles.remove(i);
                i--;
            }
        }
    }

    public void render(GraphicsContext gc) {
        for (Particle p : explosionParticles) {
            p.render(gc);
        }
    }

    public void clear() {
        explosionParticles.clear();
    }
}
