package com.mario.game.powerups;

import com.mario.game.player.PlayerComponent;

/**
 * DECORATOR PATTERN - Concrete Decorator
 * 
 * Mushroom power-up: Enlarges the player and grants additional health.
 * Effect persists until the player takes damage.
 */

public class MushroomDecorator extends PowerUpDecorator {

    private static final double SIZE_INCREASE = 1.5;
    private static final int HEALTH_INCREASE = 1;

    public MushroomDecorator(PlayerComponent component) {
        super(component, -1); // Lasts until the player takes damage

    }

    @Override
    public String getDecoratorName() {
        return "Mushroom";
    }

    @Override
    public int getMaxHealth() {
        return wrappedComponent.getMaxHealth() + HEALTH_INCREASE;
    }

    @Override
    public double getSizeMultiplier() {
        return wrappedComponent.getSizeMultiplier() * SIZE_INCREASE;
    }

    @Override
    public String getDescription() {
        return "Super " + wrappedComponent.getDescription();
    }
}
