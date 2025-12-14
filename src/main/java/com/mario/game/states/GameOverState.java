package com.mario.game.states;

import com.mario.auth.AuthenticationService;
import com.mario.game.LeaderboardService;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * STATE PATTERN - Concrete State
 * 
 * Game Over state - player has lost all lives.
 * Now saves score to leaderboard.
 */
public class GameOverState implements GameState {

    private final GameStateManager stateManager;
    private int selectedOption = 0;
    private final String[] options = {
            "Try Again",
            "Main Menu"
    };
    private int finalScore = 0;
    private int currentLevel = 1;
    private double animationTime = 0;

    public GameOverState(GameStateManager stateManager) {
        this.stateManager = stateManager;
    }

    public void setFinalScore(int score, int level) {
        this.finalScore = score;
        this.currentLevel = level;
    }

    @Override
    public void enter() {
        selectedOption = 0;
        animationTime = 0;

        // Save score to leaderboard
        AuthenticationService auth = AuthenticationService.getInstance();
        if (auth.isLoggedIn() && finalScore > 0) {
            String playerName = auth.getCurrentUser().getUsername();
            LeaderboardService.getInstance().addScore(playerName, finalScore, currentLevel);
            auth.updateHighScore(finalScore);
        }
    }

    @Override
    public void exit() {
        // Clean up
    }

    @Override
    public void update(double deltaTime) {
        animationTime += deltaTime;
    }

    @Override
    public void render(GraphicsContext gc) {
        double width = gc.getCanvas().getWidth();
        double height = gc.getCanvas().getHeight();

        // Dark gradient background
        LinearGradient gradient = new LinearGradient(0, 0, 0, height, false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(40, 20, 20)),
                new Stop(1, Color.rgb(20, 10, 10)));
        gc.setFill(gradient);
        gc.fillRect(0, 0, width, height);

        double centerX = width / 2;
        double centerY = height / 2;

        // Game Over text with shake effect
        double shake = Math.sin(animationTime * 20) * 2;
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 72));
        gc.setFill(Color.rgb(150, 0, 0));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("GAME OVER", centerX + 4 + shake, centerY - 100 + 4);
        gc.setFill(Color.RED);
        gc.fillText("GAME OVER", centerX + shake, centerY - 100);

        // Stats box
        gc.setFill(Color.rgb(0, 0, 0, 0.5));
        gc.fillRoundRect(centerX - 100, centerY - 60, 200, 70, 10, 10);

        gc.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gc.setFill(Color.WHITE);
        gc.fillText("Final Score: " + finalScore, centerX, centerY - 30);
        gc.setFont(Font.font("Arial", 18));
        gc.setFill(Color.LIGHTGRAY);
        gc.fillText("Level: 1-" + currentLevel, centerX, centerY);

        // Options with hover effect
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        for (int i = 0; i < options.length; i++) {
            double y = centerY + 60 + i * 55;

            if (i == selectedOption) {
                // Highlight box
                gc.setFill(Color.rgb(100, 50, 50, 0.5));
                gc.fillRoundRect(centerX - 100, y - 25, 200, 40, 10, 10);
                gc.setFill(Color.YELLOW);
                gc.fillText("► " + options[i] + " ◄", centerX, y);
            } else {
                gc.setFill(Color.WHITE);
                gc.fillText(options[i], centerX, y);
            }
        }

        // Leaderboard message
        if (finalScore > 0) {
            gc.setFont(Font.font("Arial", 14));
            gc.setFill(Color.LIGHTGRAY);
            gc.fillText("Score saved to leaderboard", centerX, height - 30);
        }
    }

    @Override
    public void handleKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case UP:
            case W:
                selectedOption = (selectedOption - 1 + options.length) % options.length;
                break;
            case DOWN:
            case S:
                selectedOption = (selectedOption + 1) % options.length;
                break;
            case ENTER:
            case SPACE:
                selectOption();
                break;
            default:
                break;
        }
    }

    private void selectOption() {
        switch (selectedOption) {
            case 0: // Try Again
                stateManager.getPlayingState().getGameEngine().reset();
                stateManager.goToPlaying();
                break;
            case 1: // Main Menu
                stateManager.goToMenu();
                break;
        }
    }

    @Override
    public void handleKeyReleased(KeyEvent event) {
        // Not needed
    }

    @Override
    public String getStateName() {
        return "GAME_OVER";
    }

    public void handleMouseClicked(MouseEvent event) {
        selectOption();
    }
}
