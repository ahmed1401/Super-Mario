package com.mario.game.observers;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * OBSERVER PATTERN - Concrete Observer
 * 
 * Enhanced HUD Observer with dynamic level display, FPS counter, and improved
 * visuals.
 */
public class HUDObserver implements GameObserver {

    private int score = 0;
    private int lives = 3;
    private int coins = 0;
    private int health = 1;
    private int maxHealth = 1;
    private String currentLevelName = "WORLD 1-1";
    private int currentLevelNumber = 1;
    private List<PowerUpDisplay> activePowerUps;
    private List<NotificationDisplay> notifications;

    // FPS tracking
    private boolean showFPS = false;
    private double fpsTimer = 0;
    private int frameCount = 0;
    private int currentFPS = 0;

    // Animation
    private double animationTime = 0;

    private static class PowerUpDisplay {
        String name;
        Color color;
        double timeRemaining;

        PowerUpDisplay(String name) {
            this.name = name;
            this.timeRemaining = 10.0;
            // Set color based on power-up
            switch (name.toLowerCase()) {
                case "mushroom":
                    color = Color.rgb(200, 50, 50);
                    break;
                case "fireflower":
                    color = Color.rgb(255, 150, 50);
                    break;
                case "star":
                    color = Color.rgb(255, 215, 0);
                    break;
                case "speedboost":
                    color = Color.rgb(50, 150, 255);
                    break;
                case "shield":
                    color = Color.rgb(100, 255, 150);
                    break;
                default:
                    color = Color.rgb(200, 200, 200);
            }
        }
    }

    private static class NotificationDisplay {
        String message;
        double timeRemaining;
        double startY;

        NotificationDisplay(String message) {
            this.message = message;
            this.timeRemaining = 2.5;
            this.startY = 0;
        }
    }

    public HUDObserver() {
        this.activePowerUps = new ArrayList<>();
        this.notifications = new ArrayList<>();
    }

    @Override
    public void onScoreChange(int newScore) {
        this.score = newScore;
    }

    @Override
    public void onLivesChange(int newLives) {
        this.lives = newLives;
    }

    @Override
    public void onPowerUpChange(String powerUpName, boolean applied) {
        if (applied) {
            activePowerUps.add(new PowerUpDisplay(powerUpName));
        } else {
            activePowerUps.removeIf(p -> p.name.equals(powerUpName));
        }
    }

    @Override
    public void onCoinsChange(int totalCoins) {
        this.coins = totalCoins;
    }

    @Override
    public void onHealthChange(int health, int maxHealth) {
        this.health = health;
        this.maxHealth = maxHealth;
    }

    @Override
    public void onNotification(String message) {
        notifications.add(new NotificationDisplay(message));
    }

    /**
     * Clear all active power-ups (for level reset)
     */
    public void clearPowerUps() {
        activePowerUps.clear();
    }

    /**
     * Set current level info for display
     */
    public void setLevelInfo(String levelName, int levelNumber) {
        this.currentLevelName = levelName;
        this.currentLevelNumber = levelNumber;
    }

    /**
     * Enable/disable FPS display
     */
    public void setShowFPS(boolean show) {
        this.showFPS = show;
    }

    /**
     * Update HUD animations
     */
    public void update(double deltaTime) {
        animationTime += deltaTime;

        // FPS calculation
        fpsTimer += deltaTime;
        frameCount++;
        if (fpsTimer >= 1.0) {
            currentFPS = frameCount;
            frameCount = 0;
            fpsTimer = 0;
        }

        // Update power-up display timers
        Iterator<PowerUpDisplay> powerUpIt = activePowerUps.iterator();
        while (powerUpIt.hasNext()) {
            PowerUpDisplay p = powerUpIt.next();
            p.timeRemaining -= deltaTime;
        }

        // Update notification timers
        Iterator<NotificationDisplay> notifIt = notifications.iterator();
        while (notifIt.hasNext()) {
            NotificationDisplay n = notifIt.next();
            n.timeRemaining -= deltaTime;
            if (n.timeRemaining <= 0) {
                notifIt.remove();
            }
        }
    }

    /**
     * Render the enhanced HUD
     */
    public void render(GraphicsContext gc, double width, double height) {
        // Top HUD background with gradient
        gc.setFill(Color.rgb(0, 0, 0, 0.6));
        gc.fillRect(0, 0, width, 55);

        // Bottom border accent
        gc.setFill(Color.rgb(255, 215, 0, 0.8));
        gc.fillRect(0, 53, width, 2);

        // Score with icon
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setFill(Color.rgb(200, 200, 200));
        gc.fillText("SCORE", 20, 18);

        gc.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        gc.setFill(Color.WHITE);
        gc.fillText(String.format("%07d", score), 20, 42);

        // Coins with animated icon
        double coinBounce = Math.sin(animationTime * 8) * 2;
        gc.setFill(Color.GOLD);
        gc.setFont(Font.font("Arial", 22));
        gc.fillText("●", 160, 32 + coinBounce);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        gc.setFill(Color.WHITE);
        gc.fillText("× " + coins, 180, 32);

        // Lives with heart icons
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        gc.setFill(Color.rgb(200, 200, 200));
        gc.fillText("LIVES", 280, 18);

        gc.setFont(Font.font("Arial", 20));
        for (int i = 0; i < Math.min(lives, 5); i++) {
            gc.setFill(Color.RED);
            gc.fillText("♥", 280 + i * 22, 42);
        }
        if (lives > 5) {
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            gc.setFill(Color.WHITE);
            gc.fillText("+" + (lives - 5), 280 + 5 * 22, 42);
        }

        // Level name (centered) with dynamic display
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        gc.setFill(Color.rgb(200, 200, 200));
        gc.fillText("WORLD", width / 2, 18);

        gc.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gc.setFill(Color.rgb(135, 206, 250)); // Sky blue
        gc.fillText("1-" + currentLevelNumber, width / 2, 42);

        // Timer/Time display (right side)
        gc.setTextAlign(TextAlignment.RIGHT);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        gc.setFill(Color.rgb(200, 200, 200));
        gc.fillText("TIME", width - 20, 18);

        gc.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        gc.setFill(Color.WHITE);
        int displayTime = 300 - (int) (animationTime % 300);
        gc.fillText(String.format("%03d", displayTime), width - 20, 42);

        // Health bar (if applicable)
        if (maxHealth > 1) {
            double barX = width - 150;
            double barY = 48;
            double barWidth = 100;
            double barHeight = 6;

            gc.setFill(Color.DARKGRAY);
            gc.fillRoundRect(barX, barY, barWidth, barHeight, 3, 3);

            double healthPercent = (double) health / maxHealth;
            Color healthColor = healthPercent > 0.5 ? Color.LIME : healthPercent > 0.25 ? Color.YELLOW : Color.RED;
            gc.setFill(healthColor);
            gc.fillRoundRect(barX, barY, barWidth * healthPercent, barHeight, 3, 3);
        }

        // FPS counter (top right corner)
        if (showFPS) {
            gc.setTextAlign(TextAlignment.RIGHT);
            gc.setFont(Font.font("Consolas", FontWeight.BOLD, 12));
            gc.setFill(currentFPS >= 55 ? Color.LIME : currentFPS >= 30 ? Color.YELLOW : Color.RED);
            gc.fillText("FPS: " + currentFPS, width - 10, 70);
        }

        // Active power-ups (left side, below HUD)
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        int powerUpY = 70;
        for (PowerUpDisplay p : activePowerUps) {
            // Badge background
            gc.setFill(p.color.deriveColor(0, 1, 0.8, 0.9));
            gc.fillRoundRect(10, powerUpY - 14, 90, 20, 8, 8);

            // Border
            gc.setStroke(p.color.brighter());
            gc.setLineWidth(1);
            gc.strokeRoundRect(10, powerUpY - 14, 90, 20, 8, 8);

            // Text
            gc.setFill(Color.WHITE);
            gc.fillText(p.name.toUpperCase(), 15, powerUpY);
            powerUpY += 25;
        }

        // Notifications (center of screen, animated)
        gc.setTextAlign(TextAlignment.CENTER);
        int notifY = (int) (height / 3);
        for (NotificationDisplay n : notifications) {
            double alpha = Math.min(1.0, n.timeRemaining);
            double scale = 1.0 + (1.0 - Math.min(1.0, n.timeRemaining / 0.3)) * 0.3;
            double floatUp = (2.5 - n.timeRemaining) * 20;

            gc.setFont(Font.font("Arial", FontWeight.BOLD, 28 * scale));

            // Shadow
            gc.setFill(Color.rgb(0, 0, 0, alpha * 0.5));
            gc.fillText(n.message, width / 2 + 2, notifY - floatUp + 2);

            // Main text
            gc.setFill(Color.rgb(255, 255, 255, alpha));
            gc.fillText(n.message, width / 2, notifY - floatUp);

            notifY += 40;
        }
    }
}
