package com.game.sound;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class SoundManager {

    private MediaPlayer bgmPlayer;
    private AudioClip explosionSound;
    private AudioClip victorySound;

    public SoundManager() {
        loadSounds();
    }

    private void loadSounds() {
        try {
            URL bgmUrl = getClass().getResource("/sounds/bgm_space.mp3");
            if (bgmUrl != null) {
                Media bgmMedia = new Media(bgmUrl.toExternalForm());
                bgmPlayer = new MediaPlayer(bgmMedia);
                bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                bgmPlayer.setVolume(0.4);
            }
        } catch (Exception e) {
            System.err.println("Info Sound: file bgm_space.mp3 belum ditambahkan ke resources/sounds.");
        }

        try {
            URL explosionUrl = getClass().getResource("/sounds/explosion.wav");
            if (explosionUrl != null) {
                explosionSound = new AudioClip(explosionUrl.toExternalForm());
                explosionSound.setVolume(0.8);
            }

            URL victoryUrl = getClass().getResource("/sounds/victory.wav");
            if (victoryUrl != null) {
                victorySound = new AudioClip(victoryUrl.toExternalForm());
                victorySound.setVolume(0.8);
            }
        } catch (Exception e) {
            System.err.println("Info Sound: file efek suara belum ditambahkan ke resources/sounds.");
        }
    }

    public void playBGM() {
        if (bgmPlayer != null) {
            bgmPlayer.play();
        }
    }

    public void stopBGM() {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
        }
    }

    public void playExplosion() {
        if (explosionSound != null) {
            explosionSound.play();
        }
    }

    public void playVictory() {
        if (victorySound != null) {
            victorySound.play();
        }
    }
}
