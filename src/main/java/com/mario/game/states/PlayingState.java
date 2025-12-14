package com.mario.game.states;

import com.mario.game.GameEngine;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashSet;
import java.util.Set;

/**
 * STATE PATTERN - Concrete State
 * 
 * Playing state - the main gameplay state.
 * Handles player input, updates game logic, and renders the game world.
 */
public class PlayingState implements GameState {

    private final GameStateManager stateManager;
    private GameEngine gameEngine;
    private Set<KeyCode> pressedKeys;

    public PlayingState(GameStateManager stateManager) {
        this.stateManager = stateManager;
        this.pressedKeys = new HashSet<>();
    }

    public void setGameEngine(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

    @Override
    public void enter() {
        pressedKeys.clear();
        if (gameEngine != null) {
            gameEngine.startLevel();
        }
    }

    @Override
    public void exit() {
        pressedKeys.clear();
    }

    @Override
    public void update(double deltaTime) {
        if (gameEngine != null) {
            gameEngine.update(deltaTime, pressedKeys);

            // Check game over conditions
            if (gameEngine.isGameOver()) {
                stateManager.getGameOverState().setFinalScore(
                        gameEngine.getCurrentScore(),
                        gameEngine.getCurrentLevelNumber());
                stateManager.goToGameOver();
            } else if (gameEngine.isLevelComplete()) {
                stateManager.getVictoryState().setStats(
                        gameEngine.getCurrentScore(),
                        gameEngine.getCoinsCollected(),
                        gameEngine.getCurrentLevelNumber(),
                        gameEngine.isLastLevel());
                stateManager.goToVictory();
            }
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (gameEngine != null) {
            gameEngine.render(gc);
        }
    }

    @Override
    public void handleKeyPressed(KeyEvent event) {
        pressedKeys.add(event.getCode());

        // Handle pause
        if (event.getCode() == KeyCode.ESCAPE || event.getCode() == KeyCode.P) {
            stateManager.goToPaused();
        }
    }

    @Override
    public void handleKeyReleased(KeyEvent event) {
        pressedKeys.remove(event.getCode());
    }

    public Set<KeyCode> getPressedKeys() {
        return pressedKeys;
    }

    @Override
    public String getStateName() {
        return "PLAYING";
    }

    /**
     * Get the game engine
     */
    public GameEngine getGameEngine() {
        return gameEngine;
    }
}
