package com.mario.game.powerups;

import com.mario.game.player.PlayerComponent;

/**
 * DECORATOR PATTERN - Concrete Decorator
 * 
 * Speed Boost power-up: Temporarily increases movement speed.
 * Time-based power-up (default 8 seconds).
 */
public class SpeedBoostDecorator extends PowerUpDecorator {

    private static final double DEFAULT_DURATION = 8.0;
    private static final double SPEED_MULTIPLIER = 1.5;

    public SpeedBoostDecorator(PlayerComponent component) {
        super(component, DEFAULT_DURATION);
    }

    public SpeedBoostDecorator(PlayerComponent component, double duration) {
        super(component, duration);
    }

    @Override
    public String getDecoratorName() {
        return "SpeedBoost";
    }

    @Override
    public double getSpeed() {
        if (!isExpired) {
            return wrappedComponent.getSpeed() * SPEED_MULTIPLIER;
        }
        return wrappedComponent.getSpeed();
    }

    @Override
    public String getDescription() {
        if (!isExpired) {
            return "Fast " + wrappedComponent.getDescription();
        }
        return wrappedComponent.getDescription();
    }
}
