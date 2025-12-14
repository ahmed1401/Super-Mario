package com.mario.game.levels;

import com.mario.game.enemies.Enemy;
import com.mario.utils.GameLogger;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * COMPOSITE PATTERN - Composite Component
 * 
 * Represents a game level containing platforms, enemies, coins, and other
 * components.
 * This is the main composite that groups all level elements together.
 */
public class Level implements GameComponent {

    private String name;
    private int levelNumber;
    private double width;
    private double height;

    private List<GameComponent> children;
    private List<Platform> platforms;
    private List<Coin> coins;
    private List<Enemy> enemies;
    private List<Checkpoint> checkpoints;
    private List<PowerUpItem> powerUpItems;

    private double playerStartX;
    private double playerStartY;
    private double goalX; // End goal position

    public Level(String name, int levelNumber, double width, double height) {
        this.name = name;
        this.levelNumber = levelNumber;
        this.width = width;
        this.height = height;

        this.children = new ArrayList<>();
        this.platforms = new ArrayList<>();
        this.coins = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.checkpoints = new ArrayList<>();
        this.powerUpItems = new ArrayList<>();

        this.playerStartX = 50;
        this.playerStartY = 400;
        this.goalX = width - 100;

        GameLogger.logLevel("Created", name + " (Level " + levelNumber + ")");
    }

    // Component management
    public void addPlatform(Platform platform) {
        platforms.add(platform);
        children.add(platform);
    }

    public void addCoin(Coin coin) {
        coins.add(coin);
        children.add(coin);
    }

    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }

    public void addCheckpoint(Checkpoint checkpoint) {
        checkpoints.add(checkpoint);
        children.add(checkpoint);
    }

    public void addPowerUpItem(PowerUpItem item) {
        powerUpItems.add(item);
        children.add(item);
    }

    @Override
    public void update(double deltaTime) {
        // Update all children
        for (GameComponent child : children) {
            child.update(deltaTime);
        }

        // Update enemies separately
        for (Enemy enemy : enemies) {
            enemy.update(deltaTime);
        }
    }

    @Override
    public void render(GraphicsContext gc, double cameraX, double cameraY) {
        // Draw sky background
        gc.setFill(Color.rgb(92, 148, 252));
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        // Draw background hills
        drawBackground(gc, cameraX);

        // Render all children
        for (GameComponent child : children) {
            child.render(gc, cameraX, cameraY);
        }

        // Render enemies
        for (Enemy enemy : enemies) {
            enemy.render(gc, cameraX, cameraY);
        }

        // Draw goal flag
        drawGoalFlag(gc, cameraX, cameraY);
    }

    private void drawBackground(GraphicsContext gc, double cameraX) {
        // Hills in background (parallax scrolling)
        gc.setFill(Color.rgb(100, 200, 100));
        double parallaxOffset = cameraX * 0.3;
        for (int i = 0; i < 10; i++) {
            double hillX = i * 300 - parallaxOffset % 300;
            gc.fillOval(hillX - 50, 400, 200, 150);
        }
    }

    private void drawGoalFlag(GraphicsContext gc, double cameraX, double cameraY) {
        double screenX = goalX - cameraX;
        double screenY = 300 - cameraY;

        // Flag pole
        gc.setFill(Color.GRAY);
        gc.fillRect(screenX, screenY, 8, 200);

        // Flag
        gc.setFill(Color.RED);
        gc.fillPolygon(
                new double[] { screenX + 8, screenX + 60, screenX + 8 },
                new double[] { screenY, screenY + 25, screenY + 50 },
                3);
    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(0, 0, width, height);
    }

    @Override
    public List<GameComponent> getChildren() {
        return new ArrayList<>(children);
    }

    @Override
    public boolean isComposite() {
        return true;
    }

    @Override
    public String getType() {
        return "level";
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getPlayerStartX() {
        return playerStartX;
    }

    public double getPlayerStartY() {
        return playerStartY;
    }

    public double getGoalX() {
        return goalX;
    }

    public List<Platform> getPlatforms() {
        return platforms;
    }

    public List<Coin> getCoins() {
        return coins;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public List<PowerUpItem> getPowerUpItems() {
        return powerUpItems;
    }

    // Setters
    public void setPlayerStart(double x, double y) {
        this.playerStartX = x;
        this.playerStartY = y;
    }

    public void setGoalX(double goalX) {
        this.goalX = goalX;
    }
}
