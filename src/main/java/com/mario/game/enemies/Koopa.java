package com.mario.game.enemies;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Koopa enemy - turtle that can be kicked when stomped.
 * FACTORY PATTERN - Concrete product created by EnemyFactory.
 */
public class Koopa extends Enemy {

    private boolean inShell = false;
    private double shellSpeedMultiplier = 5;

    public Koopa(double x, double y) {
        super(x, y, 3, 1, 150);
        this.width = 32;
        this.height = 48;
        this.speed = 28;
        this.velocityX = -speed;
    }

    @Override
    public void update(double deltaTime) {
        if (isDead)
            return;

        animationTime += deltaTime;

        double currentSpeed = inShell ? speed * shellSpeedMultiplier : speed;

        if (inShell && velocityX == 0) {
            // Shell is stationary
        } else {
            x += (velocityX > 0 ? currentSpeed : -currentSpeed) * deltaTime;
        }

        // Gravity
        velocityY += 400 * deltaTime;
        y += velocityY * deltaTime;

        // Ground check
        if (y > 500 - height) {
            y = 500 - height;
            velocityY = 0;
        }
    }

    @Override
    public void stomp() {
        if (!inShell) {
            inShell = true;
            velocityX = 0; // Shell stops initially
            height = 28; // Shorter in shell
        } else {
            // Kick the shell
            velocityX = -speed * shellSpeedMultiplier;
        }
    }

    @Override
    public void render(GraphicsContext gc, double cameraX, double cameraY) {
        if (isDead)
            return;

        double screenX = x - cameraX;
        double screenY = y - cameraY;

        if (inShell) {
            // Draw shell
            gc.setFill(Color.GREEN);
            gc.fillOval(screenX, screenY, width, height);
            gc.setFill(Color.rgb(0, 100, 0));
            gc.fillOval(screenX + 4, screenY + 4, width - 8, height - 8);

            // Shell pattern
            gc.setStroke(Color.rgb(0, 150, 0));
            gc.setLineWidth(2);
            gc.strokeLine(screenX + width / 2, screenY + 2, screenX + width / 2, screenY + height - 2);
            gc.strokeLine(screenX + 2, screenY + height / 2, screenX + width - 2, screenY + height / 2);
        } else {
            // Shell (back)
            gc.setFill(Color.GREEN);
            gc.fillOval(screenX, screenY + 15, width, height - 15);

            // Head
            gc.setFill(Color.rgb(255, 220, 180));
            gc.fillOval(screenX + (facingRight ? 15 : -5), screenY, 22, 25);

            // Eyes
            gc.setFill(Color.WHITE);
            gc.fillOval(screenX + (facingRight ? 22 : 2), screenY + 8, 10, 10);
            gc.setFill(Color.BLACK);
            gc.fillOval(screenX + (facingRight ? 25 : 5), screenY + 11, 4, 4);

            // Legs animation
            double legOffset = Math.sin(animationTime * 8) * 5;
            gc.setFill(Color.rgb(255, 200, 150));
            gc.fillOval(screenX + 4, screenY + height - 12 + legOffset, 10, 12);
            gc.fillOval(screenX + 18, screenY + height - 12 - legOffset, 10, 12);
        }
    }

    @Override
    public String getType() {
        return "Koopa";
    }

    public boolean isInShell() {
        return inShell;
    }
}
