package com.miniblockdash;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.*;

public class AudioManager {

    private Clip backgroundMusic;

    public void loadBackgroundMusic(String resourcePath) {
        try {
            URL url = getClass().getResource(resourcePath);
            if (url == null) {
                System.out.println("Music file not found: " + resourcePath);
                return;
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioIn);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Error loading music: " + e.getMessage());
        }
    }

    public void playBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.setFramePosition(0);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }
    }

    public void restartBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.setFramePosition(0);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void setVolume(float volume) {
        if (backgroundMusic != null) {
            FloatControl gainControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
            // Convert 0-1 range to decibels (-80 to 6)
            float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
            gainControl.setValue(Math.max(dB, gainControl.getMinimum()));
        }
    }
}
