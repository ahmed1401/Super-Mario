package com.mario.game.states;

import com.mario.game.GameEngine;
import com.mario.utils.GameLogger;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

/**
 * STATE PATTERN - Context
 * 
 * Manages game state transitions with support for new states.
 */
public class GameStateManager {

    private GameState currentState;
    private GameState previousState;

    // All available game states
    private LoginState loginState;
    private MenuState menuState;
    private PlayingState playingState;
    private PausedState pausedState;
    private GameOverState gameOverState;
    private VictoryState victoryState;
    private SettingsState settingsState;
    private LevelSelectState levelSelectState;
    private LeaderboardState leaderboardState;

    public GameStateManager() {
        // Initialize all states with reference to this manager
        this.loginState = new LoginState(this);
        this.menuState = new MenuState(this);
        this.playingState = new PlayingState(this);
        this.pausedState = new PausedState(this);
        this.gameOverState = new GameOverState(this);
        this.victoryState = new VictoryState(this);
        this.settingsState = new SettingsState(this);
        this.levelSelectState = new LevelSelectState(this);
        this.leaderboardState = new LeaderboardState(this);

        // Start with login state
        this.currentState = loginState;
        this.previousState = null;
    }

    /**
     * Transition to a new state
     */
    public void setState(GameState newState) {
        if (currentState != null) {
            String fromState = currentState.getStateName();
            currentState.exit();
            previousState = currentState;
            currentState = newState;
            currentState.enter();

            GameLogger.logStateChange("Game", fromState, newState.getStateName());
        } else {
            currentState = newState;
            currentState.enter();
            GameLogger.logStateChange("Game", "NULL", newState.getStateName());
        }
    }

    /**
     * Update the current state
     */
    public void update(double deltaTime) {
        if (currentState != null) {
            currentState.update(deltaTime);
        }
    }

    /**
     * Render the current state
     */
    public void render(GraphicsContext gc) {
        if (currentState != null) {
            currentState.render(gc);
        }
    }

    /**
     * Handle mouse clicks - delegate to current state
     */
    public void handleMouseClicked(MouseEvent event) {
        if (currentState instanceof LoginState) {
            ((LoginState) currentState).handleMouseClicked(event);
        } else if (currentState instanceof MenuState) {
            ((MenuState) currentState).handleMouseClicked(event);
        } else if (currentState instanceof SettingsState) {
            ((SettingsState) currentState).handleMouseClicked(event);
        } else if (currentState instanceof LevelSelectState) {
            ((LevelSelectState) currentState).handleMouseClicked(event);
        } else if (currentState instanceof LeaderboardState) {
            ((LeaderboardState) currentState).handleMouseClicked(event);
        } else if (currentState instanceof GameOverState) {
            ((GameOverState) currentState).handleMouseClicked(event);
        } else if (currentState instanceof VictoryState) {
            ((VictoryState) currentState).handleMouseClicked(event);
        }
    }

    /**
     * Handle mouse movement - delegate to current state
     */
    public void handleMouseMoved(MouseEvent event) {
        if (currentState instanceof MenuState) {
            ((MenuState) currentState).handleMouseMoved(event);
        }
    }

    /**
     * Return to the previous state
     */
    public void returnToPreviousState() {
        if (previousState != null) {
            setState(previousState);
        }
    }

    // State getters
    public GameState getCurrentState() {
        return currentState;
    }

    public LoginState getLoginState() {
        return loginState;
    }

    public MenuState getMenuState() {
        return menuState;
    }

    public PlayingState getPlayingState() {
        return playingState;
    }

    public PausedState getPausedState() {
        return pausedState;
    }

    public GameOverState getGameOverState() {
        return gameOverState;
    }

    public VictoryState getVictoryState() {
        return victoryState;
    }

    public SettingsState getSettingsState() {
        return settingsState;
    }

    public LevelSelectState getLevelSelectState() {
        return levelSelectState;
    }

    public LeaderboardState getLeaderboardState() {
        return leaderboardState;
    }

    // Navigation methods
    public void goToLogin() {
        setState(loginState);
    }

    public void goToMenu() {
        setState(menuState);
    }

    public void goToPlaying() {
        setState(playingState);
    }

    public void goToPaused() {
        setState(pausedState);
    }

    public void goToGameOver() {
        setState(gameOverState);
    }

    public void goToVictory() {
        setState(victoryState);
    }

    public void goToSettings() {
        setState(settingsState);
    }

    public void goToLevelSelect() {
        setState(levelSelectState);
    }

    public void goToLeaderboard() {
        setState(leaderboardState);
    }
}
