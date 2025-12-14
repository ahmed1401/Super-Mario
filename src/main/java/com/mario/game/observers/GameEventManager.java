package com.mario.game.observers;

import com.mario.utils.GameLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * OBSERVER PATTERN - Subject (Observable)
 * 
 * Manages game event observers and notifies them of changes.
 * Central hub for all game events.
 */
public class GameEventManager {

    private List<GameObserver> observers;
    private int score;
    private int lives;
    private int coins;
    private int health;
    private int maxHealth;

    public GameEventManager() {
        this.observers = new ArrayList<>();
        this.score = 0;
        this.lives = 3;
        this.coins = 0;
        this.health = 1;
        this.maxHealth = 1;
    }

    /**
     * Add an observer
     */
    public void addObserver(GameObserver observer) {
        observers.add(observer);
        GameLogger.logObserverNotification("Observer added", observer.getClass().getSimpleName());
    }

    /**
     * Remove an observer
     */
    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    /**
     * Update score and notify observers
     */
    public void addScore(int points) {
        this.score += points;
        GameLogger.logObserverNotification("Score", score);
        for (GameObserver observer : observers) {
            observer.onScoreChange(score);
        }
    }

    /**
     * Set lives and notify observers
     */
    public void setLives(int lives) {
        this.lives = lives;
        GameLogger.logObserverNotification("Lives", lives);
        for (GameObserver observer : observers) {
            observer.onLivesChange(lives);
        }
    }

    /**
     * Remove a life and notify observers
     */
    public void loseLife() {
        this.lives--;
        GameLogger.logPlayer("Lost life", "Lives remaining: " + lives);
        for (GameObserver observer : observers) {
            observer.onLivesChange(lives);
        }
    }

    /**
     * Add a life and notify observers
     */
    public void gainLife() {
        this.lives++;
        GameLogger.logPlayer("Gained life", "Lives: " + lives);
        for (GameObserver observer : observers) {
            observer.onLivesChange(lives);
        }
    }

    /**
     * Notify power-up change
     */
    public void notifyPowerUp(String powerUpName, boolean applied) {
        for (GameObserver observer : observers) {
            observer.onPowerUpChange(powerUpName, applied);
        }
    }

    /**
     * Collect coins and notify observers
     */
    public void collectCoin(int value) {
        this.coins++;
        this.score += value;
        GameLogger.logObserverNotification("Coin collected", coins);
        for (GameObserver observer : observers) {
            observer.onCoinsChange(coins);
            observer.onScoreChange(score);
        }

        // Extra life every 100 coins
        if (coins % 100 == 0) {
            gainLife();
            sendNotification("100 Coins! +1 Life!");
        }
    }

    /**
     * Update health and notify observers
     */
    public void setHealth(int health, int maxHealth) {
        this.health = health;
        this.maxHealth = maxHealth;
        for (GameObserver observer : observers) {
            observer.onHealthChange(health, maxHealth);
        }
    }

    /**
     * Send a notification to observers
     */
    public void sendNotification(String message) {
        GameLogger.logObserverNotification("Notification", message);
        for (GameObserver observer : observers) {
            observer.onNotification(message);
        }
    }

    // Getters
    public int getScore() {
        return score;
    }

    public int getLives() {
        return lives;
    }

    public int getCoins() {
        return coins;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Reset stats for new game
     */
    public void reset() {
        this.score = 0;
        this.lives = 3;
        this.coins = 0;
        this.health = 1;
        this.maxHealth = 1;

        for (GameObserver observer : observers) {
            observer.onScoreChange(score);
            observer.onLivesChange(lives);
            observer.onCoinsChange(coins);
        }
    }
}
