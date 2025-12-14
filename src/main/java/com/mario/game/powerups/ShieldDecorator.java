package com.mario.game.powerups;

import com.mario.game.player.PlayerComponent;

/**
 * DECORATOR PATTERN - Concrete Decorator
 * 
 * Shield power-up: Absorbs one hit of damage.
 * Permanent until activated (absorbs damage).
 */
public class ShieldDecorator extends PowerUpDecorator {

    private boolean shieldActive = true;

    public ShieldDecorator(PlayerComponent component) {
        super(component, -1); // Permanent until used
    }

    @Override
    public String getDecoratorName() {
        return "Shield";
    }

    @Override
    public boolean hasShield() {
        return shieldActive;
    }

    /**
     * Called when shield absorbs damage
     */
    public void absorbDamage() {
        if (shieldActive) {
            shieldActive = false;
            isExpired = true;
        }
    }

    @Override
    public String getDescription() {
        if (shieldActive) {
            return "Shielded " + wrappedComponent.getDescription();
        }
        return wrappedComponent.getDescription();
    }
}
