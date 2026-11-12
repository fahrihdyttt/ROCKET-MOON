package com.game.models;

import com.game.fx.Particle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Rocket {
    private double x;
    private double y;
    private final double width;
    private final double height;
    private final double screenWidth;
    private final Image image;

    private final List<Particle> particles = new ArrayList<>();
    private final Random random = new Random();

    public Rocket(double startX, double startY, double width, double height, double screenWidth, Image image) {
        this.x = startX;
        this.y = startY;
        this.width = width;
        this.height = height;
        this.screenWidth = screenWidth;
        this.image = image;
    }

    public void moveLeft() {
        if (x > 0) x -= 7;
    }

    public void moveRight() {
        if (x < screenWidth - width) x += 7;
    }

    public void updateParticles(boolean activeThruster) {
        if (activeThruster) {
            double exhaustX = x + width / 2.0;
            double exhaustY = y + height - 5;

            for (int i = 0; i < 4; i++) {
                double pX = exhaustX + (random.nextDouble() * 12 - 6);
                double pY = exhaustY;
                double vx = (random.nextDouble() * 2 - 1);
                double vy = 3 + random.nextDouble() * 4;
                double size = 8 + random.nextDouble() * 6;

                Color pColor = random.nextBoolean() ? Color.ORANGE : (random.nextBoolean() ? Color.YELLOW : Color.RED);
                particles.add(new Particle(pX, pY, vx, vy, size, pColor));
            }
        }

        for (int i = 0; i < particles.size(); i++) {
            Particle p = particles.get(i);
            p.update();
            if (p.isDead()) {
                particles.remove(i);
                i--;
            }
        }
    }

    public void render(GraphicsContext gc) {
        for (Particle p : particles) {
            p.render(gc);
        }

        if (image != null && !image.isError()) {
            gc.drawImage(image, x, y, width, height);
        } else {
            gc.setFill(Color.CYAN);
            gc.fillRect(x, y, width, height);
        }
    }

    public void clearParticles() {
        particles.clear();
    }

    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }

    public void resetPosition(double startX, double startY) {
        this.x = startX;
        this.y = startY;
        clearParticles();
    }
}
