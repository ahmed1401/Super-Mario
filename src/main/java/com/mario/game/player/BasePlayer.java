package com.mario.game.player;

/**
 * DECORATOR PATTERN - Concrete Component
 * 
 * Represents the base player with standard abilities.
 * Power-up decorators wrap this component to enhance its functionality.
 */

public class BasePlayer implements PlayerComponent {

    private int currentHealth;
    private static final int DEFAULT_MAX_HEALTH = 1;
    private static final double DEFAULT_SPEED = 220.0;
    private static final double DEFAULT_JUMP_FORCE = 520.0;

    public BasePlayer() {
        this.currentHealth = DEFAULT_MAX_HEALTH;
    }

    @Override
    public double getSpeed() {
        return DEFAULT_SPEED;
    }

    @Override
    public double getJumpForce() {
        return DEFAULT_JUMP_FORCE;
    }

    @Override
    public int getMaxHealth() {
        return DEFAULT_MAX_HEALTH;
    }

    @Override
    public int getCurrentHealth() {
        return currentHealth;
    }

    @Override
    public boolean canShoot() {
        return false;
    }

    @Override
    public boolean isInvincible() {
        return false;
    }

    @Override
    public boolean hasShield() {
        return false;
    }

    @Override
    public double getSizeMultiplier() {
        return 1.0;
    }

    @Override
    public String getDescription() {
        return "Small Mario";
    }

    @Override
    public void update(double deltaTime) {
        // Base player has no time-based effects
    }

    public void setCurrentHealth(int health) {
        this.currentHealth = Math.max(0, Math.min(health, getMaxHealth()));
    }

    public void takeDamage(int damage) {
        this.currentHealth = Math.max(0, currentHealth - damage);
    }

    public void heal(int amount) {
        this.currentHealth = Math.min(getMaxHealth(), currentHealth + amount);
    }
}
