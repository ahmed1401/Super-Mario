package com.mario.auth;

/**
 * User entity for authentication system.
 */
public class User {

    private String username;
    private String passwordHash;
    private int highScore;
    private int lastLevel;
    private int savedLives;
    private int savedScore;
    private int totalCoinsCollected;
    private boolean isGuest;

    public User() {
        // Default constructor for JSON deserialization
    }

    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.highScore = 0;
        this.lastLevel = 1;
        this.savedLives = 3;
        this.savedScore = 0;
        this.totalCoinsCollected = 0;
        this.isGuest = false;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public int getLastLevel() {
        return lastLevel;
    }

    public void setLastLevel(int lastLevel) {
        this.lastLevel = lastLevel;
    }

    public int getSavedLives() {
        return savedLives;
    }

    public void setSavedLives(int savedLives) {
        this.savedLives = savedLives;
    }

    public int getSavedScore() {
        return savedScore;
    }

    public void setSavedScore(int savedScore) {
        this.savedScore = savedScore;
    }

    public int getTotalCoinsCollected() {
        return totalCoinsCollected;
    }

    public void setTotalCoinsCollected(int totalCoinsCollected) {
        this.totalCoinsCollected = totalCoinsCollected;
    }

    public boolean isGuest() {
        return isGuest;
    }

    public void setGuest(boolean guest) {
        isGuest = guest;
    }
}
