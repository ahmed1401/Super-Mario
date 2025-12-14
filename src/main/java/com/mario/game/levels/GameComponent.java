package com.mario.game.levels;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

import java.util.List;

/**
 * COMPOSITE PATTERN - Component Interface
 * 
 * Defines the interface for all game components in the level hierarchy.
 * Both leaf nodes (platforms, coins) and composite nodes (levels, worlds)
 * implement this interface.
 */
public interface GameComponent {

    /**
     * Update the component
     * 
     * @param deltaTime Time elapsed since last update
     */
    void update(double deltaTime);

    /**
     * Render the component
     * 
     * @param gc      Graphics context
     * @param cameraX Camera X offset for scrolling
     * @param cameraY Camera Y offset
     */
    void render(GraphicsContext gc, double cameraX, double cameraY);

    /**
     * Get the bounding box for collision detection
     * 
     * @return Rectangle2D representing bounds
     */
    Rectangle2D getBounds();

    /**
     * Get child components (empty list for leaf nodes)
     * 
     * @return List of child components
     */
    List<GameComponent> getChildren();

    /**
     * Check if this is a composite (has children)
     * 
     * @return true if composite
     */
    boolean isComposite();

    /**
     * Get the type of this component
     * 
     * @return Component type string
     */
    String getType();
}
