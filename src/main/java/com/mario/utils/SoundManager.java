package com.mario.utils;

import com.mario.game.states.SettingsState;
import javafx.scene.media.AudioClip;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Sound Manager for game audio.
 * Handles sound effects and background music.
 * 
 * Uses JavaFX Media API for audio playback.
 */
public class SoundManager {

    private static SoundManager instance;

    // Sound effect clips (short sounds)
    private Map<String, AudioClip> soundEffects;

    // Music player (background music)
    private MediaPlayer musicPlayer;

    // Volume settings (0.0 to 1.0)
    private double sfxVolume = 0.8;
    private double musicVolume = 0.7;
    private boolean soundEnabled = true;
    private boolean musicEnabled = true;

    // Sound effect names
    public static final String SFX_JUMP = "jump";
    public static final String SFX_COIN = "coin";
    public static final String SFX_POWERUP = "powerup";
    public static final String SFX_STOMP = "stomp";
    public static final String SFX_DEATH = "death";
    public static final String SFX_LEVEL_COMPLETE = "level_complete";
    public static final String SFX_GAME_OVER = "game_over";
    public static final String SFX_MENU_SELECT = "menu_select";
    public static final String SFX_MENU_MOVE = "menu_move";
    public static final String SFX_CHECKPOINT = "checkpoint";

    private SoundManager() {
        soundEffects = new HashMap<>();
        loadSoundEffects();
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    /**
     * Load all sound effects
     * Note: In production, these would be actual audio files.
     * For this demo, we'll use placeholder behavior.
     */
    /**
     * Load all sound effects
     */
    private void loadSoundEffects() {
        String[] soundNames = {
                SFX_JUMP, SFX_COIN, SFX_POWERUP, SFX_STOMP,
                SFX_DEATH, SFX_LEVEL_COMPLETE, SFX_GAME_OVER,
                SFX_MENU_SELECT, SFX_MENU_MOVE, SFX_CHECKPOINT
        };

        for (String name : soundNames) {
            try {
                // Try to load from resources
                URL resource = getClass().getResource("/sounds/" + name + ".wav");
                if (resource != null) {
                    soundEffects.put(name, new AudioClip(resource.toExternalForm()));
                } else {
                    GameLogger.logError("Sound file not found: " + name, null);
                }
            } catch (Exception e) {
                GameLogger.logError("Failed to load sound: " + name, e);
            }
        }

        GameLogger.info("SoundManager initialized");
    }

    /**
     * Play a sound effect
     */
    public void playSFX(String name) {
        if (!soundEnabled)
            return;

        AudioClip clip = soundEffects.get(name);
        if (clip != null) {
            clip.play(sfxVolume);
        } else {
            // Log simulated sound for demo
            GameLogger.logSound("SFX", name);
        }
    }

    /**
     * Play jump sound
     */
    public void playJump() {
        playSFX(SFX_JUMP);
    }

    /**
     * Play coin collect sound
     */
    public void playCoin() {
        playSFX(SFX_COIN);
    }

    /**
     * Play power-up sound
     */
    public void playPowerUp() {
        playSFX(SFX_POWERUP);
    }

    /**
     * Play enemy stomp sound
     */
    public void playStomp() {
        playSFX(SFX_STOMP);
    }

    /**
     * Play death sound
     */
    public void playDeath() {
        playSFX(SFX_DEATH);
    }

    /**
     * Play level complete sound
     */
    public void playLevelComplete() {
        playSFX(SFX_LEVEL_COMPLETE);
    }

    /**
     * Play game over sound
     */
    public void playGameOver() {
        playSFX(SFX_GAME_OVER);
    }

    /**
     * Play menu select sound
     */
    public void playMenuSelect() {
        playSFX(SFX_MENU_SELECT);
    }

    /**
     * Play menu move sound
     */
    public void playMenuMove() {
        playSFX(SFX_MENU_MOVE);
    }

    /**
     * Play checkpoint sound
     */
    public void playCheckpoint() {
        playSFX(SFX_CHECKPOINT);
    }

    /**
     * Start background music
     */
    public void playMusic(String musicName) {
        if (!musicEnabled)
            return;

        stopMusic();

        // In production, load and play actual music file
        GameLogger.logSound("MUSIC", "Started: " + musicName);
    }

    /**
     * Stop background music
     */
    public void stopMusic() {
        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer = null;
        }
    }

    /**
     * Pause background music
     */
    public void pauseMusic() {
        if (musicPlayer != null) {
            musicPlayer.pause();
        }
    }

    /**
     * Resume background music
     */
    public void resumeMusic() {
        if (musicPlayer != null && musicEnabled) {
            musicPlayer.play();
        }
    }

    // Settings
    public void setSfxVolume(double volume) {
        this.sfxVolume = Math.max(0, Math.min(1, volume));
    }

    public void setMusicVolume(double volume) {
        this.musicVolume = Math.max(0, Math.min(1, volume));
        if (musicPlayer != null) {
            musicPlayer.setVolume(musicVolume);
        }
    }

    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
    }

    public void setMusicEnabled(boolean enabled) {
        this.musicEnabled = enabled;
        if (!enabled) {
            stopMusic();
        }
    }

    public double getSfxVolume() {
        return sfxVolume;
    }

    public double getMusicVolume() {
        return musicVolume;
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public boolean isMusicEnabled() {
        return musicEnabled;
    }

    /**
     * Apply settings from SettingsState
     */
    public void applySettings(SettingsState settings) {
        setSfxVolume(settings.getSoundVolume() / 100.0);
        setMusicVolume(settings.getMusicVolume() / 100.0);
    }
}
