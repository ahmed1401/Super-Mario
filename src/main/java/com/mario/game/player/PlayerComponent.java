package com.mario.game.player;

/**
 * DECORATOR PATTERN - Component Interface
 * 
 * Defines the interface for all player capabilities.
 * Both the concrete component (BasePlayer) and decorators (power-ups)
 * implement this interface.
 */
public interface PlayerComponent {

    /**
     * Get movement speed
     * 
     * @return Speed multiplier (1.0 is normal)
     */
    double getSpeed();

    /**
     * Get jump force
     * 
     * @return Jump force multiplier
     */
    double getJumpForce();

    /**
     * Get maximum health
     * 
     * @return Max health points
     */
    int getMaxHealth();

    /**
     * Get current health
     * 
     * @return Current health points
     */
    int getCurrentHealth();

    /**
     * Check if player can shoot fireballs
     * 
     * @return true if can shoot
     */
    boolean canShoot();

    /**
     * Check if player is invincible
     * 
     * @return true if invincible
     */
    boolean isInvincible();

    /**
     * Check if player has shield
     * 
     * @return true if has shield
     */
    boolean hasShield();

    /**
     * Get player size multiplier
     * 
     * @return Size multiplier (1.0 is normal)
     */
    double getSizeMultiplier();

    /**
     * Get the description of current abilities
     * 
     * @return Description string
     */
    String getDescription();

    /**
     * Update time-based effects
     * 
     * @param deltaTime Time elapsed
     */
    void update(double deltaTime);
}
