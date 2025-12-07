package com.comp2042;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundManager {

    private static SoundManager instance;

    private AudioClip clearSound;
    private AudioClip gameOverSound;
    private MediaPlayer bgmPlayer;

    private boolean soundEnabled = true;

    private SoundManager() {
        try {
            //Load Sound Effects
            clearSound = new AudioClip(getClass().getResource("/clear.mp3").toExternalForm());
            gameOverSound = new AudioClip(getClass().getResource("/gameover.mp3").toExternalForm());

            //Load Background Music
            Media bgm = new Media(getClass().getResource("/bgm.mp3").toExternalForm());
            bgmPlayer = new MediaPlayer(bgm);
            bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE); //Infinite Loop
            bgmPlayer.setVolume(0.5); //Volume50%
        } catch (Exception e) {
            System.out.println("Warning: Could not load sound files: " + e.getMessage());
        }
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    public void playClearSound() {
        if (soundEnabled && clearSound != null) {
            clearSound.play();
        }
    }

    public void playGameOverSound() {
        if (soundEnabled && gameOverSound != null) {
            stopBgm(); // Game Over 时停止背景音乐
            gameOverSound.play();
        }
    }

    public void playBgm() {
        if (soundEnabled && bgmPlayer != null) {
            bgmPlayer.play();
        }
    }

    public void stopBgm() {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
        }
    }

    public void pauseBgm() {
        if (bgmPlayer != null) {
            bgmPlayer.pause();
        }
    }

    public void resumeBgm() {
        if (soundEnabled && bgmPlayer != null) {
            bgmPlayer.play();
        }
    }

}
