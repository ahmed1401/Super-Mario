package com.mario.game.levels;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Collections;
import java.util.List;

/**
 * COMPOSITE PATTERN - Leaf Component
 * 
 * Checkpoint for saving player progress in a level.
 */
public class Checkpoint implements GameComponent {

    private double x, y;
    private double width = 32;
    private double height = 64;
    private boolean activated = false;
    private double animationTime = 0;

    public Checkpoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void update(double deltaTime) {
        if (activated) {
            animationTime += deltaTime;
        }
    }

    @Override
    public void render(GraphicsContext gc, double cameraX, double cameraY) {
        double screenX = x - cameraX;
        double screenY = y - cameraY;

        // Draw flag pole
        gc.setFill(Color.GRAY);
        gc.fillRect(screenX + width / 2 - 3, screenY, 6, height);

        // Draw flag
        if (activated) {
            // Green flag waving
            gc.setFill(Color.GREEN);
            double wave = Math.sin(animationTime * 5) * 5;
            gc.fillPolygon(
                    new double[] { screenX + width / 2, screenX + width + wave, screenX + width / 2 },
                    new double[] { screenY, screenY + 15, screenY + 30 },
                    3);
        } else {
            // Gray flag (not activated)
            gc.setFill(Color.DARKGRAY);
            gc.fillPolygon(
                    new double[] { screenX + width / 2, screenX + width, screenX + width / 2 },
                    new double[] { screenY, screenY + 15, screenY + 30 },
                    3);
        }
    }

    @Override
    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
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
        return "checkpoint";
    }

    public void activate() {
        this.activated = true;
    }

    public boolean isActivated() {
        return activated;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
