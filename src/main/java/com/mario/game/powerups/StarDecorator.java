package com.mario.game.powerups;

import com.mario.game.player.PlayerComponent;

/**
 * DECORATOR PATTERN - Concrete Decorator
 * 
 * Star power-up: Makes player temporarily invincible.
 * Time-based power-up (default 10 seconds).
 */
public class StarDecorator extends PowerUpDecorator {

    private static final double DEFAULT_DURATION = 10.0; // 10 seconds
    private static final double SPEED_BOOST = 1.3;

    public StarDecorator(PlayerComponent component) {
        super(component, DEFAULT_DURATION);
    }

    public StarDecorator(PlayerComponent component, double duration) {
        super(component, duration);
    }

    @Override
    public String getDecoratorName() {
        return "Star";
    }

    @Override
    public boolean isInvincible() {
        return !isExpired; // Invincible until star effect ends
    }

    @Override
    public double getSpeed() {
        // Star also gives a speed boost
        if (!isExpired) {
            return wrappedComponent.getSpeed() * SPEED_BOOST;
        }
        return wrappedComponent.getSpeed();
    }

    @Override
    public String getDescription() {
        if (!isExpired) {
            return "★ Invincible " + wrappedComponent.getDescription() + " ★";
        }
        return wrappedComponent.getDescription();
    }
}
