package com.game;

import com.game.models.Rocket;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class RocketGame extends Application {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 800;

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Image rocketImg = loadImage("/assets/rocket.png");
        Image asteroidImg = loadImage("/assets/asteroid.png");
        Image moonImg = loadImage("/assets/moon.png");
        Image spaceBgImg = loadImage("/assets/space_bg.jpg");
        Image flagImg = loadImage("/assets/flag.png");

        Rocket rocket = new Rocket(WIDTH / 2.0 - 30, HEIGHT - 120, 60, 90, WIDTH, rocketImg);
        GameLoop gameLoop = new GameLoop(gc, WIDTH, HEIGHT, rocket, asteroidImg, moonImg, spaceBgImg, flagImg);

        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.A) gameLoop.setMoveLeft(true);
            if (e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.D) gameLoop.setMoveRight(true);

            if (e.getCode() == KeyCode.ENTER) {
                gameLoop.startGame();
            }
            if (e.getCode() == KeyCode.R) {
                if (gameLoop.getState() == GameState.GAME_OVER || gameLoop.getState() == GameState.VICTORY) {
                    gameLoop.restartGame();
                }
            }
        });

        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.A) gameLoop.setMoveLeft(false);
            if (e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.D) gameLoop.setMoveRight(false);
        });

        gameLoop.start();

        primaryStage.setTitle("Space Mission: Moon Landing");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private Image loadImage(String path) {
        try {
            return new Image(getClass().getResourceAsStream(path));
        } catch (Exception e) {
            System.err.println("Gagal memuat gambar: " + path);
            return null;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
