package com.mario.game.powerups;

import com.mario.game.player.PlayerComponent;
import com.mario.utils.GameLogger;

/**
 * DECORATOR PATTERN - Abstract Decorator
 * 
 * Serves as the base class for all power-up decorators.
 * Encapsulates a PlayerComponent to extend its behavior.
 */

public abstract class PowerUpDecorator implements PlayerComponent {

    protected PlayerComponent wrappedComponent;
    protected double duration; // Duration in seconds (-1 indicates a permanent effect)

    protected double remainingTime;
    protected boolean isExpired = false;

    public PowerUpDecorator(PlayerComponent component, double duration) {
        this.wrappedComponent = component;
        this.duration = duration;
        this.remainingTime = duration;

        // Record which decorator is being applied

        GameLogger.logDecoratorApplied(getDecoratorName(), "Player");
    }

  /**
 * Retrieves the decorator's name for logging purposes
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
        // First, update the wrapped component

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
 * Determines whether this power-up has expired
 */

    public boolean isExpired() {
        return isExpired;
    }

   /**
 * Returns the remaining duration of this power-up
 */

    public double getRemainingTime() {
        return remainingTime;
    }

   /**
 * Returns the wrapped component
 */

    public PlayerComponent getWrappedComponent() {
        return wrappedComponent;
    }
}
