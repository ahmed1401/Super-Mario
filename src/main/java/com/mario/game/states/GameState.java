package com.mario.game.states;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;

/**
 * STATE PATTERN - Abstract State
 * 
 * Defines the interface for all game states.
 * Each concrete state handles its own behavior for entering, exiting,
 * updating, rendering, and input handling.
 */
public interface GameState {

    /**
     * Called when entering this state
     */
    void enter();

    /**
     * Called when exiting this state
     */
    void exit();

    /**
     * Update game logic for this state
     * 
     * @param deltaTime Time elapsed since last update in seconds
     */
    void update(double deltaTime);

    /**
     * Render this state
     * 
     * @param gc GraphicsContext to draw on
     */
    void render(GraphicsContext gc);

    /**
     * Handle key press events
     * 
     * @param event The key event
     */
    void handleKeyPressed(KeyEvent event);

    /**
     * Handle key release events
     * 
     * @param event The key event
     */
    void handleKeyReleased(KeyEvent event);

    /**
     * Get the name of this state (for logging)
     * 
     * @return State name
     */
    String getStateName();
}
