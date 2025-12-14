package com.mario.game.levels;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Collections;
import java.util.List;

/**
 * COMPOSITE PATTERN - Leaf Component
 * 
 * Power-up item that can be collected by the player.
 */
public class PowerUpItem implements GameComponent {

    public enum PowerUpType {
        MUSHROOM, FIRE_FLOWER, STAR, SPEED_BOOST, SHIELD
    }

    private double x, y;
    private double width = 28;
    private double height = 28;
    private PowerUpType type;
    private boolean collected = false;
    private double animationTime = 0;

    public PowerUpItem(double x, double y, PowerUpType type) {
        this.x = x;
        this.y = y;
        this.type = type;
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
        double screenY = y - cameraY + Math.sin(animationTime * 3) * 3; // Bobbing animation

        switch (type) {
            case MUSHROOM:
                // Red mushroom cap
                gc.setFill(Color.RED);
                gc.fillOval(screenX, screenY, width, height * 0.7);
                // White spots
                gc.setFill(Color.WHITE);
                gc.fillOval(screenX + 5, screenY + 5, 6, 6);
                gc.fillOval(screenX + 17, screenY + 8, 5, 5);
                // Stem
                gc.setFill(Color.BEIGE);
                gc.fillRect(screenX + 8, screenY + height * 0.5, 12, height * 0.5);
                break;

            case FIRE_FLOWER:
                // Orange flower
                gc.setFill(Color.ORANGE);
                // Petals
                for (int i = 0; i < 5; i++) {
                    double angle = i * Math.PI * 2 / 5 + animationTime;
                    double petalX = screenX + width / 2 + Math.cos(angle) * 8;
                    double petalY = screenY + height / 3 + Math.sin(angle) * 8;
                    gc.fillOval(petalX - 5, petalY - 5, 10, 10);
                }
                // Center
                gc.setFill(Color.YELLOW);
                gc.fillOval(screenX + width / 2 - 5, screenY + height / 3 - 5, 10, 10);
                // Stem
                gc.setFill(Color.GREEN);
                gc.fillRect(screenX + width / 2 - 3, screenY + height / 2, 6, height / 2);
                break;

            case STAR:
                // Yellow star with glow
                gc.setFill(Color.YELLOW);
                drawStar(gc, screenX + width / 2, screenY + height / 2, width / 2);
                // Eyes
                gc.setFill(Color.BLACK);
                gc.fillOval(screenX + 8, screenY + 10, 4, 4);
                gc.fillOval(screenX + 16, screenY + 10, 4, 4);
                break;

            case SPEED_BOOST:
                // Blue lightning bolt
                gc.setFill(Color.CYAN);
                gc.fillPolygon(
                        new double[] { screenX + 18, screenX + 8, screenX + 14, screenX + 10, screenX + 20,
                                screenX + 14 },
                        new double[] { screenY, screenY + 12, screenY + 12, screenY + 28, screenY + 16, screenY + 16 },
                        6);
                break;

            case SHIELD:
                // Blue shield
                gc.setFill(Color.rgb(100, 150, 255));
                gc.fillOval(screenX, screenY, width, height);
                gc.setStroke(Color.BLUE);
                gc.setLineWidth(2);
                gc.strokeOval(screenX, screenY, width, height);
                // Cross pattern
                gc.setFill(Color.WHITE);
                gc.fillRect(screenX + width / 2 - 2, screenY + 5, 4, height - 10);
                gc.fillRect(screenX + 5, screenY + height / 2 - 2, width - 10, 4);
                break;
        }
    }

    private void drawStar(GraphicsContext gc, double cx, double cy, double size) {
        double[] xPoints = new double[10];
        double[] yPoints = new double[10];
        for (int i = 0; i < 10; i++) {
            double radius = (i % 2 == 0) ? size : size / 2;
            double angle = Math.PI / 2 + i * Math.PI / 5;
            xPoints[i] = cx + Math.cos(angle) * radius;
            yPoints[i] = cy - Math.sin(angle) * radius;
        }
        gc.fillPolygon(xPoints, yPoints, 10);
    }

    @Override
    public Rectangle2D getBounds() {
        if (collected) {
            return new Rectangle2D(0, 0, 0, 0);
        }
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
        return "powerup";
    }

    public void collect() {
        this.collected = true;
    }

    public boolean isCollected() {
        return collected;
    }

    public PowerUpType getPowerUpType() {
        return type;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
