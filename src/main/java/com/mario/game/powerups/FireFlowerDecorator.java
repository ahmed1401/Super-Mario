package com.mario.game.powerups;

import com.mario.game.player.PlayerComponent;

/**
 * DECORATOR PATTERN - Concrete Decorator
 * 
 * Fire Flower power-up: Allows player to shoot fireballs.
 * Permanent power-up (until player takes damage).
 */
public class FireFlowerDecorator extends PowerUpDecorator {

    public FireFlowerDecorator(PlayerComponent component) {
        super(component, -1); // Permanent until damage
    }

    @Override
    public String getDecoratorName() {
        return "FireFlower";
    }

    @Override
    public boolean canShoot() {
        return true; // Enable shooting
    }

    @Override
    public String getDescription() {
        return "Fire " + wrappedComponent.getDescription();
    }
}
