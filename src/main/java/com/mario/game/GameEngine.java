package com.mario.game;

import com.mario.game.enemies.Enemy;
import com.mario.game.factories.LevelFactory;
import com.mario.game.factories.PowerUpFactory;
import com.mario.game.levels.*;
import com.mario.game.observers.GameEventManager;
import com.mario.game.observers.HUDObserver;
import com.mario.game.player.Player;
import com.mario.game.powerups.PowerUpDecorator;
import com.mario.utils.GameLogger;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.util.Iterator;
import java.util.Set;

/**
 * Main game engine handling game logic, rendering, and state.
 */
public class GameEngine {

    private World world;
    private Level currentLevel;
    private Player player;

    private GameEventManager eventManager;
    private HUDObserver hudObserver;

    private double cameraX = 0;
    private double cameraY = 0;
    private double screenWidth;
    private double screenHeight;

    private boolean isGameOver = false;
    private boolean isLevelComplete = false;
    private boolean isPaused = false;

    public GameEngine(double screenWidth, double screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        // Initialize event system (OBSERVER PATTERN)
        this.eventManager = new GameEventManager();
        this.hudObserver = new HUDObserver();
        eventManager.addObserver(hudObserver);

        // Create game world (FACTORY & COMPOSITE PATTERN)
        this.world = LevelFactory.createWorld1();

        GameLogger.info("GameEngine initialized");
    }

    /**
     * Start/restart the current level
     */
    public void startLevel() {
        currentLevel = world.getCurrentLevel();
        if (currentLevel == null) {
            GameLogger.logError("No level found!", null);
            return;
        }

        // Create player at level start position
        player = new Player(currentLevel.getPlayerStartX(), currentLevel.getPlayerStartY());

        // Reset camera
        cameraX = 0;
        cameraY = 0;

        // Reset game state
        isGameOver = false;
        isLevelComplete = false;

        // Update HUD with level info
        hudObserver.setLevelInfo(currentLevel.getName(), currentLevel.getLevelNumber());
        hudObserver.clearPowerUps();

        GameLogger.logLevel("Started", currentLevel.getName());
        eventManager.sendNotification("Level " + currentLevel.getLevelNumber() + " Start!");
        com.mario.utils.SoundManager.getInstance().playMenuSelect(); // Play start sound
    }

    /**
     * Update game state
     */
    public void update(double deltaTime, Set<KeyCode> pressedKeys) {
        if (isPaused || isGameOver || isLevelComplete)
            return;

        // Update player
        player.update(deltaTime, pressedKeys, currentLevel.getPlatforms());

        // Update level (enemies, items, etc.)
        currentLevel.update(deltaTime);

        // Update camera to follow player
        updateCamera();

        // Handle collisions
        handleCoinCollisions();
        handlePowerUpCollisions();
        handleEnemyCollisions();
        handleCheckpointCollisions();

        // Check win condition
        if (player.getX() >= currentLevel.getGoalX()) {
            isLevelComplete = true;
            eventManager.sendNotification("Level Complete!");
            GameLogger.logLevel("Completed", currentLevel.getName());
        }

        // Check death
        if (player.isDead()) {
            eventManager.loseLife();
            if (eventManager.getLives() <= 0) {
                isGameOver = true;
                GameLogger.logPlayer("Game Over", "No lives remaining");
            } else {
                // Respawn
                player.reset(currentLevel.getPlayerStartX(), currentLevel.getPlayerStartY());
                cameraX = 0;
                eventManager.sendNotification("Lives: " + eventManager.getLives());
            }
        }

        // Update HUD
        hudObserver.update(deltaTime);
    }

    private void updateCamera() {
        // Camera follows player horizontally
        double targetX = player.getX() - screenWidth / 3;
        cameraX += (targetX - cameraX) * 0.1; // Smooth camera

        // Clamp camera to level bounds
        cameraX = Math.max(0, Math.min(cameraX, currentLevel.getWidth() - screenWidth));
        cameraY = 0; // No vertical scrolling for now
    }

    private void handleCoinCollisions() {
        for (Coin coin : currentLevel.getCoins()) {
            if (!coin.isCollected() && player.getBounds().intersects(coin.getBounds())) {
                coin.collect();
                eventManager.collectCoin(coin.getValue());
                com.mario.utils.SoundManager.getInstance().playCoin();
            }
        }
    }

    private void handlePowerUpCollisions() {
        for (PowerUpItem item : currentLevel.getPowerUpItems()) {
            if (!item.isCollected() && player.getBounds().intersects(item.getBounds())) {
                item.collect();

                // Apply power-up using FACTORY and DECORATOR patterns
                PowerUpFactory.PowerUpType type = convertPowerUpType(item.getPowerUpType());
                PowerUpDecorator decorator = PowerUpFactory.createPowerUp(type, player.getComponent());
                player.applyPowerUp(decorator);

                eventManager.notifyPowerUp(type.name(), true);
                eventManager.addScore(200);
                com.mario.utils.SoundManager.getInstance().playPowerUp();
                com.mario.utils.SoundManager.getInstance().playSFX("powerup");
            }
        }
    }

    private PowerUpFactory.PowerUpType convertPowerUpType(PowerUpItem.PowerUpType itemType) {
        switch (itemType) {
            case MUSHROOM:
                return PowerUpFactory.PowerUpType.MUSHROOM;
            case FIRE_FLOWER:
                return PowerUpFactory.PowerUpType.FIRE_FLOWER;
            case STAR:
                return PowerUpFactory.PowerUpType.STAR;
            case SPEED_BOOST:
                return PowerUpFactory.PowerUpType.SPEED_BOOST;
            case SHIELD:
                return PowerUpFactory.PowerUpType.SHIELD;
            default:
                return PowerUpFactory.PowerUpType.MUSHROOM;
        }
    }

    private void handleEnemyCollisions() {
        Iterator<Enemy> enemyIt = currentLevel.getEnemies().iterator();
        while (enemyIt.hasNext()) {
            Enemy enemy = enemyIt.next();
            if (enemy.isDead())
                continue;

            Rectangle2D playerBounds = player.getBounds();
            Rectangle2D enemyBounds = enemy.getBounds();

            if (playerBounds.intersects(enemyBounds)) {
                // Check if player is stomping (coming from above)
                boolean stomping = player.getVelocityY() > 0 &&
                        playerBounds.getMaxY() < enemyBounds.getMinY() + enemyBounds.getHeight() / 2;

                if (stomping || player.getComponent().isInvincible()) {
                    // Kill enemy
                    enemy.stomp();
                    if (enemy.isDead()) {
                        eventManager.addScore(enemy.getScoreValue());
                        GameLogger.logPlayer("Enemy defeated", enemy.getType());
                    }
                    // Bounce player up after stomp
                    if (stomping) {
                        // Small bounce
                    }
                } else {
                    // Player takes damage
                    player.takeDamage();
                }
            }
        }
    }

    private void handleCheckpointCollisions() {
        for (Checkpoint checkpoint : currentLevel.getCheckpoints()) {
            if (!checkpoint.isActivated() && player.getBounds().intersects(checkpoint.getBounds())) {
                checkpoint.activate();
                currentLevel.setPlayerStart(checkpoint.getX(), checkpoint.getY() - player.getHeight());
                eventManager.sendNotification("Checkpoint!");
                GameLogger.logLevel("Checkpoint activated", "");
            }
        }
    }

    /**
     * Render the game
     */
    public void render(GraphicsContext gc) {
        // Clear screen
        gc.setFill(Color.rgb(92, 148, 252));
        gc.fillRect(0, 0, screenWidth, screenHeight);

        if (currentLevel != null) {
            // Render level
            currentLevel.render(gc, cameraX, cameraY);

            // Render player
            if (player != null) {
                player.render(gc, cameraX, cameraY);
            }
        }

        // Render HUD (always on top)
        hudObserver.render(gc, screenWidth, screenHeight);
    }

    // Getters
    public boolean isGameOver() {
        return isGameOver;
    }

    public boolean isLevelComplete() {
        return isLevelComplete;
    }

    public GameEventManager getEventManager() {
        return eventManager;
    }

    public int getCurrentScore() {
        return eventManager.getScore();
    }

    public int getCurrentLives() {
        return eventManager.getLives();
    }

    /**
     * Advance to next level
     */
    public boolean nextLevel() {
        if (world.nextLevel()) {
            startLevel();
            return true;
        }
        return false; // All levels complete
    }

    /**
     * Reset the game
     */
    public void reset() {
        world.setCurrentLevel(0);
        eventManager.reset();
        // Recreate world to reset all level state
        this.world = LevelFactory.createWorld1();
        startLevel();
    }

    /**
     * Set specific level to play
     */
    public void setLevel(int levelIndex) {
        world.setCurrentLevel(levelIndex);
        eventManager.reset();
        startLevel();
    }

    /**
     * Get the world
     */
    public World getWorld() {
        return world;
    }

    /**
     * Get current level number (1-based)
     */
    public int getCurrentLevelNumber() {
        return world.getCurrentLevelIndex() + 1;
    }

    /**
     * Check if current level is the last one
     */
    public boolean isLastLevel() {
        return world.getCurrentLevelIndex() >= world.getTotalLevels() - 1;
    }

    /**
     * Get coins collected
     */
    public int getCoinsCollected() {
        return eventManager.getCoins();
    }
}
