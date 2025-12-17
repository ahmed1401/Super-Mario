package com.mario.game.player;

    /**
    * DECORATOR PATTERN - Component Interface
    *  
    * Specifies the interface for all player behaviors.
    * Implemented by both the concrete component (BasePlayer) and power-up decorators.
    */

public interface PlayerComponent {

    /**
    * Retrieves the player's movement speed.
    * 
    * @return Speed multiplier (1.0 represents normal speed)
    */

    double getSpeed();

    /**
     * Get jump force
     * 
     * @return Jump force multiplier
     */
    double getJumpForce();

    /**
    * Retrieves the player's maximum health.
    * 
    * @return Maximum health points
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
    * Retrieves the player's size multiplier.
    * 
    * @return Size factor (1.0 represents normal size)
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
