package com.mario.game.powerups;

import com.mario.game.player.PlayerComponent;
import com.mario.utils.GameLogger;

/**
 * DECORATOR PATTERN - Abstract Decorator
 * 
 * Base class for all power-up decorators.
 * Wraps a PlayerComponent and adds additional behavior.
 */
public abstract class PowerUpDecorator implements PlayerComponent {

    protected PlayerComponent wrappedComponent;
    protected double duration; // Duration in seconds, -1 for permanent
    protected double remainingTime;
    protected boolean isExpired = false;

    public PowerUpDecorator(PlayerComponent component, double duration) {
        this.wrappedComponent = component;
        this.duration = duration;
        this.remainingTime = duration;

        // Log the decorator being applied
        GameLogger.logDecoratorApplied(getDecoratorName(), "Player");
    }

    /**
     * Get the name of this decorator (for logging)
     */
    public abstract String getDecoratorName();

    @Override
    public double getSpeed() {
        return wrappedComponent.getSpeed();
    }

    @Override
    public double getJumpForce() {
        return wrappedComponent.getJumpForce();
    }

    @Override
    public int getMaxHealth() {
        return wrappedComponent.getMaxHealth();
    }

    @Override
    public int getCurrentHealth() {
        return wrappedComponent.getCurrentHealth();
    }

    @Override
    public boolean canShoot() {
        return wrappedComponent.canShoot();
    }

    @Override
    public boolean isInvincible() {
        return wrappedComponent.isInvincible();
    }

    @Override
    public boolean hasShield() {
        return wrappedComponent.hasShield();
    }

    @Override
    public double getSizeMultiplier() {
        return wrappedComponent.getSizeMultiplier();
    }

    @Override
    public void update(double deltaTime) {
        // Update wrapped component first
        wrappedComponent.update(deltaTime);

        // Update duration timer
        if (duration > 0) {
            remainingTime -= deltaTime;
            if (remainingTime <= 0 && !isExpired) {
                isExpired = true;
                GameLogger.logDecoratorRemoved(getDecoratorName(), "Player");
            }
        }
    }

    /**
     * Check if this power-up has expired
     */
    public boolean isExpired() {
        return isExpired;
    }

    /**
     * Get remaining time for this power-up
     */
    public double getRemainingTime() {
        return remainingTime;
    }

    /**
     * Get the wrapped component
     */
    public PlayerComponent getWrappedComponent() {
        return wrappedComponent;
    }
}
