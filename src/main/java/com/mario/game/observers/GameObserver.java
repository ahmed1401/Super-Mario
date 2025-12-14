package com.mario.game.observers;

/**
 * OBSERVER PATTERN - Observer Interface
 * 
 * Defines the interface for objects that should be notified of game events.
 */
public interface GameObserver {

    /**
     * Called when score changes
     * 
     * @param newScore New score value
     */
    void onScoreChange(int newScore);

    /**
     * Called when lives count changes
     * 
     * @param newLives New lives count
     */
    void onLivesChange(int newLives);

    /**
     * Called when a power-up is applied or removed
     * 
     * @param powerUpName Name of the power-up
     * @param applied     true if applied, false if removed
     */
    void onPowerUpChange(String powerUpName, boolean applied);

    /**
     * Called when coins are collected
     * 
     * @param totalCoins Total coins collected
     */
    void onCoinsChange(int totalCoins);

    /**
     * Called when player health changes
     * 
     * @param health    Current health
     * @param maxHealth Maximum health
     */
    void onHealthChange(int health, int maxHealth);

    /**
     * Called for achievement or event notifications
     * 
     * @param message Notification message
     */
    void onNotification(String message);
}
