package com.mario.game.levels;

import com.mario.utils.GameLogger;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

/**
 * COMPOSITE PATTERN - Top-level Composite
 * 
 * Represents the game world containing multiple levels.
 * This is the root of the composite hierarchy.
 */
public class World implements GameComponent {

    // World identity and contained levels (immutable references)
    private final String name;
    private final List<Level> levels;
    private int currentLevelIndex = 0;

    public World(String name) {
        this.name = name;
        this.levels = new ArrayList<>();
        GameLogger.logLevel("World created", name);
    }

    public void addLevel(Level level) {
        levels.add(level);
        GameLogger.logLevel("Level added to world", level.getName());
    }

    public Level getCurrentLevel() {
        if (currentLevelIndex >= 0 && currentLevelIndex < levels.size()) {
            return levels.get(currentLevelIndex);
        }
        return null;
    }

    public boolean nextLevel() {
        if (currentLevelIndex < levels.size() - 1) {
            currentLevelIndex++;
            GameLogger.logLevel("Advanced to", getCurrentLevel().getName());
            return true;
        }
        return false; // No more levels
    }

    public void setCurrentLevel(int index) {
        if (index >= 0 && index < levels.size()) {
            currentLevelIndex = index;
            GameLogger.logLevel("Jumped to", getCurrentLevel().getName());
        }
    }

    @Override
    public void update(double deltaTime) {
        Level current = getCurrentLevel();
        if (current != null) {
            current.update(deltaTime);
        }
    }

    @Override
    public void render(GraphicsContext gc, double cameraX, double cameraY) {
        Level current = getCurrentLevel();
        if (current != null) {
            current.render(gc, cameraX, cameraY);
        }
    }

    @Override
    public Rectangle2D getBounds() {
        Level current = getCurrentLevel();
        if (current != null) {
            return current.getBounds();
        }
        // Safe default bounds when no level is active
        return new Rectangle2D(0, 0, 800, 600);
    }

    @Override
    public List<GameComponent> getChildren() {
        return new ArrayList<>(levels);
    }

    @Override
    public boolean isComposite() {
        return true;
    }

    @Override
    public String getType() {
        return "world";
    }

    // Getters
    public String getName() {
        return name;
    }

    public List<Level> getLevels() {
        return levels;
    }

    public int getCurrentLevelIndex() {
        return currentLevelIndex;
    }

    public int getTotalLevels() {
        return levels.size();
    }
}
