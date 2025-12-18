package com.mario.game.enemies;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Goomba enemy - basic walking enemy.
 * FACTORY PATTERN - Concrete product created by EnemyFactory.
 */
public class Goomba extends Enemy {

    public Goomba(double x, double y) {
        super(x, y, 1, 1, 75);
        this.width = 32;
        this.height = 32;
        this.speed = 35;
        this.velocityX = -speed; // Walk left initially
    }

    @Override
    public void update(double deltaTime) {
        if (isDead)
            return;

        animationTime += deltaTime;

        // Simple movement
        x += velocityX * deltaTime;

        // Gravity (basic)
        velocityY += 400 * deltaTime; // Gravity
        y += velocityY * deltaTime;

        // Ground check (simple)
        if (y > 500 - height) {
            y = 500 - height;
            velocityY = 0;
        }
    }

    @Override
    public void render(GraphicsContext gc, double cameraX, double cameraY) {
        if (isDead)
            return;

        double screenX = x - cameraX;
        double screenY = y - cameraY;

        // Walking animation
        double squish = 1 + Math.sin(animationTime * 10) * 0.05;

        // Body (brown mushroom shape)
        gc.setFill(Color.rgb(165, 42, 42));
        gc.fillOval(screenX, screenY + height * 0.3, width, height * 0.7 * squish);

        // Head
        gc.setFill(Color.rgb(139, 69, 19));
        gc.fillOval(screenX - 2, screenY, width + 4, height * 0.5);

        // Eyes
        gc.setFill(Color.WHITE);
        gc.fillOval(screenX + 6, screenY + 10, 8, 8);
        gc.fillOval(screenX + 18, screenY + 10, 8, 8);

        // Pupils
        gc.setFill(Color.BLACK);
        double pupilOffset = facingRight ? 2 : -2;
        gc.fillOval(screenX + 8 + pupilOffset, screenY + 12, 4, 4);
        gc.fillOval(screenX + 20 + pupilOffset, screenY + 12, 4, 4);

        // Eyebrows (angry)
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeLine(screenX + 6, screenY + 8, screenX + 14, screenY + 6);
        gc.strokeLine(screenX + 18, screenY + 6, screenX + 26, screenY + 8);

        // Feet
        gc.setFill(Color.rgb(139, 69, 19));
        double footOffset = Math.sin(animationTime * 10) * 3;
        gc.fillOval(screenX + 2, screenY + height - 8 + footOffset, 10, 8);
        gc.fillOval(screenX + 20, screenY + height - 8 - footOffset, 10, 8);
    }

    @Override
    public String getType() {
        return "Goomba";
    }
}
