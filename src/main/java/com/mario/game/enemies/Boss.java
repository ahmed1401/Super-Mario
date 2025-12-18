package com.mario.game.enemies;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Boss enemy - large, powerful enemy.
 * FACTORY PATTERN - Concrete product created by EnemyFactory.
 */
public class Boss extends Enemy {

    private static final int DEFAULT_MAX_HEALTH = 8;
    private int maxHealth = DEFAULT_MAX_HEALTH;

    private double attackTimer = 0;
    private double attackInterval = 2.0;
    private boolean isAttacking = false;
    private double attackDuration = 0.5;
    private double attackTime = 0;

    public Boss(double x, double y) {
        super(x, y, DEFAULT_MAX_HEALTH, 3, 1500);
        this.maxHealth = DEFAULT_MAX_HEALTH;
        this.width = 80;
        this.height = 96;
        this.speed = 22;
        this.velocityX = -speed;
    }

    @Override
    public void update(double deltaTime) {
        if (isDead)
            return;

        animationTime += deltaTime;
        attackTimer += deltaTime;

        // Attack pattern
        if (attackTimer >= attackInterval && !isAttacking) {
            isAttacking = true;
            attackTime = 0;
        }

        if (isAttacking) {
            attackTime += deltaTime;
            if (attackTime >= attackDuration) {
                isAttacking = false;
                attackTimer = 0;
            }
        } else {
            // Movement
            x += velocityX * deltaTime;
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
    public void render(GraphicsContext gc, double cameraX, double cameraY) {
        if (isDead)
            return;

        double screenX = x - cameraX;
        double screenY = y - cameraY;

        double shake = isAttacking ? Math.sin(attackTime * 50) * 3 : 0;

        // Body (large shell)
        gc.setFill(Color.rgb(0, 100, 0));
        gc.fillOval(screenX + shake, screenY + 25, width, height - 25);

        // Shell spikes
        gc.setFill(Color.rgb(200, 200, 200));
        for (int i = 0; i < 5; i++) {
            double spikeX = screenX + 10 + i * 15 + shake;
            gc.fillPolygon(
                    new double[] { spikeX, spikeX + 7, spikeX + 14 },
                    new double[] { screenY + 30, screenY + 10, screenY + 30 },
                    3);
        }

        // Head
        gc.setFill(Color.rgb(255, 220, 180));
        gc.fillOval(screenX - 10 + shake, screenY, 45, 45);

        // Angry eyes
        gc.setFill(Color.WHITE);
        gc.fillOval(screenX + shake, screenY + 10, 16, 16);
        gc.fillOval(screenX + 20 + shake, screenY + 10, 16, 16);

        gc.setFill(Color.RED);
        gc.fillOval(screenX + 4 + shake, screenY + 14, 8, 8);
        gc.fillOval(screenX + 24 + shake, screenY + 14, 8, 8);

        // Eyebrows
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);
        gc.strokeLine(screenX - 2 + shake, screenY + 6, screenX + 14 + shake, screenY + 12);
        gc.strokeLine(screenX + 22 + shake, screenY + 12, screenX + 38 + shake, screenY + 6);

        // Mouth
        gc.setFill(Color.rgb(50, 50, 50));
        gc.fillRect(screenX + 5 + shake, screenY + 32, 25, 8);

        // Teeth
        gc.setFill(Color.WHITE);
        for (int i = 0; i < 4; i++) {
            gc.fillRect(screenX + 7 + i * 6 + shake, screenY + 32, 4, 6);
        }

        // Health bar
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(screenX, screenY - 15, width, 8);
        double healthPercent = (double) health / (double) maxHealth;
        gc.setFill(healthPercent > 0.5 ? Color.GREEN : healthPercent > 0.25 ? Color.YELLOW : Color.RED);
        gc.fillRect(screenX, screenY - 15, width * healthPercent, 8);
    }

    @Override
    public String getType() {
        return "Boss";
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    /**
     * Allow factory overrides of max health for data-driven configs.
     */
    public void setMaxHealth(int maxHealth) {
        this.maxHealth = Math.max(1, maxHealth);
        this.health = Math.min(this.health, this.maxHealth);
    }
}
