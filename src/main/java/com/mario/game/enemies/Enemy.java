package com.mario.game.enemies;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

/**
 * Base class for all enemies.
 * Part of the FACTORY PATTERN - products created by EnemyFactory.
 */
public abstract class Enemy {

    protected double x, y;
    protected double width, height;
    protected double velocityX, velocityY;
    protected double speed;
    protected int health;
    protected int damage;
    protected int scoreValue;
    protected boolean isDead = false;
    protected boolean facingRight = false;
    protected double animationTime = 0;

    public Enemy(double x, double y, int health, int damage, int scoreValue) {
        this.x = x;
        this.y = y;
        this.health = health;
        this.damage = damage;
        this.scoreValue = scoreValue;
        this.speed = 50;
    }

    /**
     * Update enemy logic
     */
    public abstract void update(double deltaTime);

    /**
     * Render the enemy
     */
    public abstract void render(GraphicsContext gc, double cameraX, double cameraY);

    /**
     * Get enemy type name
     */
    public abstract String getType();

    /**
     * Handle being stomped by player
     */
    public void stomp() {
        health--;
        if (health <= 0) {
            isDead = true;
        }
    }

    /**
     * Handle collision with platform edge
     */
    public void reverseDirection() {
        velocityX = -velocityX;
        facingRight = !facingRight;
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, width, height);
    }

    // Getters and setters
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

    public int getDamage() {
        return damage;
    }

    public int getScoreValue() {
        return scoreValue;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
