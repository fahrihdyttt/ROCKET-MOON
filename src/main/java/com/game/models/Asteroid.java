package com.game.models;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Asteroid {
    private final double x;
    private double y;
    private final double speed;
    private final double size;
    private final Image image;

    public Asteroid(double x, double y, double speed, double size, Image image) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.size = size;
        this.image = image;
    }

    public void update() {
        y += speed;
    }

    public void render(GraphicsContext gc) {
        if (image != null && !image.isError()) {
            gc.drawImage(image, x, y, size, size);
        }
    }

    public boolean collidesWith(Rocket rocket) {
        return rocket.getX() < x + size &&
               rocket.getX() + rocket.getWidth() > x &&
               rocket.getY() < y + size &&
               rocket.getY() + rocket.getHeight() > y;
    }

    public double getY() { return y; }
}
