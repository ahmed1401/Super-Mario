package com.mario.game.states;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * STATE PATTERN - Concrete State
 * 
 * Paused state - game is temporarily stopped.
 */
public class PausedState implements GameState {

    private final GameStateManager stateManager;
    private int selectedOption = 0;
    private final String[] pauseOptions = {
            "Resume",
            "Restart Level",
            "Settings",
            "Main Menu"
    };

    public PausedState(GameStateManager stateManager) {
        this.stateManager = stateManager;
    }

    @Override
    public void enter() {
        selectedOption = 0;
    }

    @Override
    public void exit() {
        // Clean up pause state
    }

    @Override
    public void update(double deltaTime) {
        // Paused state doesn't update game logic
    }

    @Override
    public void render(GraphicsContext gc) {
        double width = gc.getCanvas().getWidth();
        double height = gc.getCanvas().getHeight();

        // Semi-transparent overlay
        gc.setFill(Color.rgb(0, 0, 0, 0.7));
        gc.fillRect(0, 0, width, height);

        double centerX = width / 2;
        double centerY = height / 2;

        // Pause panel background
        gc.setFill(Color.rgb(50, 50, 50, 0.9));
        gc.fillRoundRect(centerX - 150, centerY - 150, 300, 300, 20, 20);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(3);
        gc.strokeRoundRect(centerX - 150, centerY - 150, 300, 300, 20, 20);

        // Title
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 36));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("PAUSED", centerX, centerY - 100);

        // Menu options
        gc.setFont(Font.font("Arial", 20));
        for (int i = 0; i < pauseOptions.length; i++) {
            double y = centerY - 30 + i * 40;

            if (i == selectedOption) {
                gc.setFill(Color.YELLOW);
                gc.fillText("► " + pauseOptions[i], centerX, y);
            } else {
                gc.setFill(Color.WHITE);
                gc.fillText(pauseOptions[i], centerX, y);
            }
        }
    }

    @Override
    public void handleKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case UP:
            case W:
                selectedOption = (selectedOption - 1 + pauseOptions.length) % pauseOptions.length;
                break;
            case DOWN:
            case S:
                selectedOption = (selectedOption + 1) % pauseOptions.length;
                break;
            case ENTER:
            case SPACE:
                selectOption();
                break;
            case ESCAPE:
            case P:
                stateManager.goToPlaying(); // Resume
                break;
            default:
                break;
        }
    }

    private void selectOption() {
        switch (selectedOption) {
            case 0: // Resume
                stateManager.goToPlaying();
                break;
            case 1: // Restart Level
                stateManager.goToPlaying(); // TODO: Reset level
                break;
            case 2: // Settings
                // TODO: Settings screen
                break;
            case 3: // Main Menu
                stateManager.goToMenu();
                break;
        }
    }

    @Override
    public void handleKeyReleased(KeyEvent event) {
        // Not needed for pause
    }

    @Override
    public String getStateName() {
        return "PAUSED";
    }
}
