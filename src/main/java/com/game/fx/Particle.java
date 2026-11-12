package com.game.fx;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Particle {
    private double x;
    private double y;
    private double vx;
    private double vy;
    private double size;
    private double alpha;
    private final Color color;
    private final double decayRate;

    public Particle(double x, double y, double vx, double vy, double size, Color color, double decayRate) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.size = size;
        this.alpha = 1.0;
        this.color = color;
        this.decayRate = decayRate;
    }

    public Particle(double x, double y, double vx, double vy, double size, Color color) {
        this(x, y, vx, vy, size, color, 0.04);
    }

    public void update() {
        x += vx;
        y += vy;
        vx *= 0.96;
        vy *= 0.96;
        size = Math.max(0, size - 0.15);
        alpha = Math.max(0, alpha - decayRate);
    }

    public void render(GraphicsContext gc) {
        if (alpha <= 0 || size <= 0) return;

        gc.save();
        gc.setGlobalAlpha(alpha);
        gc.setFill(color);
        gc.fillOval(x - size / 2.0, y - size / 2.0, size, size);
        gc.restore();
    }

    public boolean isDead() {
        return alpha <= 0 || size <= 0;
    }
}
