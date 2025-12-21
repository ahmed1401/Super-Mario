package com.mario.game.levels;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Collections;
import java.util.List;

/**
 * COMPOSITE PATTERN - Leaf Component
 * 
 * Platform that player can stand on.
 */
public class Platform implements GameComponent {

    private double x, y, width, height;
    private final Color color;
    private final boolean isSolid = true;
    private final String platformType; // "ground", "brick", "question", "pipe"

    public Platform(double x, double y, double width, double height, String type) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.platformType = type;

        // Set color based on type
        switch (type) {
            case "ground":
                this.color = Color.rgb(139, 69, 19);
                break;
            case "brick":
                this.color = Color.rgb(200, 100, 50);
                break;
            case "question":
                this.color = Color.GOLD;
                break;
            case "pipe":
                this.color = Color.rgb(0, 180, 0);
                break;
            default:
                this.color = Color.BROWN;
        }
    }

    @Override
    public void update(double deltaTime) {
        // Static platform - no update needed
    }

    @Override
    public void render(GraphicsContext gc, double cameraX, double cameraY) {
        double screenX = x - cameraX;
        double screenY = y - cameraY;

        // Draw platform
        gc.setFill(color);
        gc.fillRect(screenX, screenY, width, height);

        // Add details based on type
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.strokeRect(screenX, screenY, width, height);

        if (platformType.equals("brick")) {
            // Draw brick pattern
            gc.setStroke(Color.rgb(100, 50, 25));
            for (double bx = screenX; bx < screenX + width; bx += 32) {
                gc.strokeLine(bx, screenY, bx, screenY + height);
            }
            gc.strokeLine(screenX, screenY + height / 2, screenX + width, screenY + height / 2);
        } else if (platformType.equals("question")) {
            // Draw question mark
            gc.setFill(Color.WHITE);
            gc.fillText("?", screenX + width / 2 - 5, screenY + height / 2 + 5);
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
        return "platform";
    }

    // Getters
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public boolean isSolid() {
        return isSolid;
    }

    public String getPlatformType() {
        return platformType;
    }
}
