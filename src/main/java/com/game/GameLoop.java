package com.game;

import com.game.fx.ExplosionManager;
import com.game.models.Asteroid;
import com.game.models.Rocket;
import com.game.sound.SoundManager;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameLoop extends AnimationTimer {

    private final GraphicsContext gc;
    private final double width;
    private final double height;

    private final Rocket rocket;
    private final List<Asteroid> asteroids = new ArrayList<>();
    private final Random random = new Random();
    private final ExplosionManager explosionManager = new ExplosionManager();
    private final SoundManager soundManager = new SoundManager();

    private final Image asteroidImg;
    private final Image moonImg;
    private final Image spaceBgImg;
    private final Image flagImg;

    private GameState state = GameState.MENU;
    private boolean moveLeft = false;
    private boolean moveRight = false;
    private int score = 0;

    private double landingY;
    private double landingTargetY;
    private double landingSpeed;

    public GameLoop(GraphicsContext gc, double width, double height, Rocket rocket,
                    Image asteroidImg, Image moonImg, Image spaceBgImg, Image flagImg) {
        this.gc = gc;
        this.width = width;
        this.height = height;
        this.rocket = rocket;
        this.asteroidImg = asteroidImg;
        this.moonImg = moonImg;
        this.spaceBgImg = spaceBgImg;
        this.flagImg = flagImg;
    }

    @Override
    public void handle(long now) {
        if (state == GameState.PLAYING) {
            update();
        } else if (state == GameState.LANDING) {
            updateLandingAnimation();
        } else if (state == GameState.GAME_OVER) {
            explosionManager.update();
        }
        render();
    }

    private void update() {
        if (moveLeft) rocket.moveLeft();
        if (moveRight) rocket.moveRight();

        rocket.updateParticles(true);

        score += 5;

        if (score >= 5000) {
            initLandingAnimation();
            soundManager.playVictory();
            return;
        }

        if (random.nextInt(100) < 5) {
            double size = 40 + random.nextInt(30);
            double xPos = random.nextInt((int) (width - size));
            double speed = 3 + random.nextInt(4);
            asteroids.add(new Asteroid(xPos, -size, speed, size, asteroidImg));
        }

        for (int i = 0; i < asteroids.size(); i++) {
            Asteroid asteroid = asteroids.get(i);
            asteroid.update();

            if (asteroid.collidesWith(rocket)) {
                double collisionX = rocket.getX() + rocket.getWidth() / 2.0;
                double collisionY = rocket.getY() + rocket.getHeight() / 2.0;

                explosionManager.spawnExplosion(collisionX, collisionY);
                rocket.clearParticles();

                soundManager.playExplosion();
                state = GameState.GAME_OVER;
                break;
            }

            if (asteroid.getY() > height) {
                asteroids.remove(i);
                i--;
            }
        }
    }

    private void initLandingAnimation() {
        state = GameState.LANDING;
        asteroids.clear();

        double targetX = width / 2.0 - rocket.getWidth() / 2.0;
        landingY = -rocket.getHeight();
        landingTargetY = height - 260;
        landingSpeed = 6.0;

        rocket.setX(targetX);
        rocket.setY(landingY);
    }

    private void updateLandingAnimation() {
        double distanceLeft = landingTargetY - landingY;

        if (distanceLeft > 0) {
            landingSpeed = Math.max(0.5, distanceLeft * 0.03);
            landingY += landingSpeed;
            rocket.setY(landingY);
            rocket.updateParticles(distanceLeft > 30);
        } else {
            rocket.setY(landingTargetY);
            rocket.clearParticles();
            state = GameState.VICTORY;
        }
    }

    private void render() {
        if (spaceBgImg != null && !spaceBgImg.isError()) {
            gc.drawImage(spaceBgImg, 0, 0, width, height);
        } else {
            gc.setFill(Color.web("#0b0e14"));
            gc.fillRect(0, 0, width, height);
        }

        switch (state) {
            case MENU:
                renderMenuScreen();
                break;
            case PLAYING:
                renderGameplay();
                break;
            case LANDING:
                renderLandingAnimation();
                break;
            case GAME_OVER:
                renderGameOverScreen();
                break;
            case VICTORY:
                renderVictoryScreen();
                break;
        }
    }

    private void renderMenuScreen() {
        gc.setFill(Color.GOLD);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        gc.fillText("SPACE MISSION: MOON LANDING", width / 2.0 - 220, height / 2.0 - 80);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        gc.fillText("Hindari asteroid & capai skor 5000 untuk mendarat!", width / 2.0 - 210, height / 2.0 - 30);

        gc.setFill(Color.LIGHTGREEN);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        gc.fillText("Tekan ENTER untuk Memulai Game", width / 2.0 - 180, height / 2.0 + 50);
    }

    private void renderGameplay() {
        rocket.render(gc);
        for (Asteroid asteroid : asteroids) {
            asteroid.render(gc);
        }

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gc.fillText("Skor: " + score + " / 5000", 20, 40);
    }

    private void renderLandingAnimation() {
        if (moonImg != null && !moonImg.isError()) {
            gc.drawImage(moonImg, 0, height - 250, width, 250);
        }

        rocket.render(gc);

        gc.setFill(Color.CYAN);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        gc.fillText("MENDARAT DI BULAN...", width / 2.0 - 110, 100);
    }

    private void renderVictoryScreen() {
        if (moonImg != null && !moonImg.isError()) {
            gc.drawImage(moonImg, 0, height - 250, width, 250);
        }

        rocket.render(gc);

        if (flagImg != null && !flagImg.isError()) {
            gc.drawImage(flagImg, width / 2.0 + 40, height - 230, 50, 60);
        }

        gc.setFill(Color.GOLD);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        gc.fillText("MISI SUKSES!", width / 2.0 - 90, 120);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        gc.fillText("Pilot Berhasil Mendarat & Menancapkan Bendera!", width / 2.0 - 200, 160);

        gc.setFill(Color.LIGHTGREEN);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gc.fillText("Tekan 'R' untuk Main Lagi", width / 2.0 - 120, 210);
    }

    private void renderGameOverScreen() {
        for (Asteroid asteroid : asteroids) {
            asteroid.render(gc);
        }

        explosionManager.render(gc);

        gc.setFill(Color.RED);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        gc.fillText("GAME OVER", width / 2.0 - 110, height / 2.0 - 30);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        gc.fillText("Skor Akhir: " + score, width / 2.0 - 65, height / 2.0 + 10);

        gc.setFill(Color.YELLOW);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gc.fillText("Tekan 'R' untuk Restart Misi", width / 2.0 - 130, height / 2.0 + 60);
    }

    public void restartGame() {
        score = 0;
        asteroids.clear();
        explosionManager.clear();
        rocket.resetPosition(width / 2.0 - 30, height - 120);
        state = GameState.PLAYING;
    }

    public void startGame() {
        if (state == GameState.MENU) {
            soundManager.playBGM();
            restartGame();
        }
    }

    public GameState getState() { return state; }
    public void setMoveLeft(boolean moveLeft) { this.moveLeft = moveLeft; }
    public void setMoveRight(boolean moveRight) { this.moveRight = moveRight; }
}
