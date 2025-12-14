package com.mario.game.levels;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Collections;
import java.util.List;

/**
 * COMPOSITE PATTERN - Leaf Component
 * 
 * Collectible coin.
 */
public class Coin implements GameComponent {

    private double x, y;
    private double radius = 12;
    private boolean collected = false;
    private int value = 100;
    private double animationTime = 0;

    public Coin(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Coin(double x, double y, int value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    @Override
    public void update(double deltaTime) {
        if (!collected) {
            animationTime += deltaTime;
        }
    }

    @Override
    public void render(GraphicsContext gc, double cameraX, double cameraY) {
        if (collected)
            return;

        double screenX = x - cameraX;
        double screenY = y - cameraY;

        // Spinning animation
        double scaleX = Math.abs(Math.cos(animationTime * 5));
        double displayWidth = radius * 2 * scaleX;

        // Bobbing animation
        double bobOffset = Math.sin(animationTime * 3) * 5;

        // Draw coin
        gc.setFill(Color.GOLD);
        gc.fillOval(screenX - displayWidth / 2, screenY - radius + bobOffset, displayWidth, radius * 2);

        // Shine effect
        gc.setFill(Color.YELLOW);
        gc.fillOval(screenX - displayWidth / 4, screenY - radius / 2 + bobOffset, displayWidth / 3, radius);
    }

    @Override
    public Rectangle2D getBounds() {
        if (collected) {
            return new Rectangle2D(0, 0, 0, 0);
        }
        return new Rectangle2D(x - radius, y - radius, radius * 2, radius * 2);
    }

    @Override
    public List<GameComponent> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public boolean isComposite() {
        return false;
    }

    @Override
    public String getType() {
        return "coin";
    }

    public void collect() {
        this.collected = true;
    }

    public boolean isCollected() {
        return collected;
    }

    public int getValue() {
        return value;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
