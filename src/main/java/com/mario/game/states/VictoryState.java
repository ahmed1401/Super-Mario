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
 * Victory state - player has completed the level.
 * Now properly advances to next level and saves scores.
 */
public class VictoryState implements GameState {

    private final GameStateManager stateManager;
    private int selectedOption = 0;
    private final String[] options = {
            "Next Level",
            "Replay Level",
            "Main Menu"
    };
    private int score = 0;
    private int coinsCollected = 0;
    private int currentLevel = 1;
    private double animationTime = 0;
    private boolean isLastLevel = false;

    public VictoryState(GameStateManager stateManager) {
        this.stateManager = stateManager;
    }

    public void setStats(int score, int coins, int level, boolean lastLevel) {
        this.score = score;
        this.coinsCollected = coins;
        this.currentLevel = level;
        this.isLastLevel = lastLevel;
    }

    @Override
    public void enter() {
        selectedOption = 0;
        animationTime = 0;

        // Save score to leaderboard
        AuthenticationService auth = AuthenticationService.getInstance();
        if (auth.isLoggedIn()) {
            String playerName = auth.getCurrentUser().getUsername();
            LeaderboardService.getInstance().addScore(playerName, score, currentLevel);
            auth.updateHighScore(score);
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

        // Gradient background
        LinearGradient gradient = new LinearGradient(0, 0, 0, height, false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(50, 150, 50)),
                new Stop(1, Color.rgb(30, 100, 30)));
        gc.setFill(gradient);
        gc.fillRect(0, 0, width, height);

        double centerX = width / 2;
        double centerY = height / 2;

        // Animated stars
        gc.setFill(Color.YELLOW);
        for (int i = 0; i < 5; i++) {
            double starX = centerX + Math.cos(animationTime * 2 + i * 1.2) * 200;
            double starY = 80 + Math.sin(animationTime * 3 + i) * 20;
            drawStar(gc, starX, starY, 15);
        }

        // Victory text
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 56));
        gc.setFill(Color.rgb(255, 215, 0)); // Gold
        gc.setTextAlign(TextAlignment.CENTER);
        double bounce = Math.sin(animationTime * 5) * 5;

        String title = isLastLevel ? "🎉 GAME COMPLETE! 🎉" : "LEVEL COMPLETE!";
        gc.fillText(title, centerX, centerY - 140 + bounce);

        // Level info
        gc.setFont(Font.font("Arial", 20));
        gc.setFill(Color.LIGHTGREEN);
        gc.fillText("World 1-" + currentLevel, centerX, centerY - 90);

        // Stats box
        gc.setFill(Color.rgb(0, 0, 0, 0.4));
        gc.fillRoundRect(centerX - 120, centerY - 75, 240, 80, 10, 10);

        gc.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gc.setFill(Color.WHITE);
        gc.fillText("Score: " + score, centerX, centerY - 45);
        gc.fillText("Coins: " + coinsCollected, centerX, centerY - 15);

        // Options
        String[] displayOptions = isLastLevel ? new String[] { "Play Again", "Main Menu" } : options;

        gc.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        for (int i = 0; i < displayOptions.length; i++) {
            double y = centerY + 40 + i * 50;

            if (i == selectedOption) {
                gc.setFill(Color.YELLOW);
                gc.fillText("► " + displayOptions[i] + " ◄", centerX, y);
            } else {
                gc.setFill(Color.WHITE);
                gc.fillText(displayOptions[i], centerX, y);
            }
        }

        // Leaderboard entry notification
        gc.setFont(Font.font("Arial", 14));
        gc.setFill(Color.LIGHTGRAY);
        gc.fillText("Score saved to leaderboard!", centerX, height - 30);
    }

    private void drawStar(GraphicsContext gc, double x, double y, double size) {
        double[] xPoints = new double[10];
        double[] yPoints = new double[10];
        for (int i = 0; i < 10; i++) {
            double radius = (i % 2 == 0) ? size : size / 2;
            double angle = Math.PI / 2 + i * Math.PI / 5;
            xPoints[i] = x + Math.cos(angle) * radius;
            yPoints[i] = y - Math.sin(angle) * radius;
        }
        gc.fillPolygon(xPoints, yPoints, 10);
    }

    @Override
    public void handleKeyPressed(KeyEvent event) {
        int maxOptions = isLastLevel ? 2 : options.length;

        switch (event.getCode()) {
            case UP:
            case W:
                selectedOption = (selectedOption - 1 + maxOptions) % maxOptions;
                break;
            case DOWN:
            case S:
                selectedOption = (selectedOption + 1) % maxOptions;
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
        if (isLastLevel) {
            // Game complete options
            switch (selectedOption) {
                case 0: // Play Again
                    stateManager.getPlayingState().getGameEngine().reset();
                    stateManager.goToPlaying();
                    break;
                case 1: // Main Menu
                    stateManager.goToMenu();
                    break;
            }
        } else {
            switch (selectedOption) {
                case 0: // Next Level
                    boolean hasNext = stateManager.getPlayingState().getGameEngine().nextLevel();
                    if (hasNext) {
                        stateManager.goToPlaying();
                    } else {
                        // No more levels - shouldn't happen if isLastLevel is set correctly
                        stateManager.goToMenu();
                    }
                    break;
                case 1: // Replay Level
                    stateManager.getPlayingState().getGameEngine().startLevel();
                    stateManager.goToPlaying();
                    break;
                case 2: // Main Menu
                    stateManager.goToMenu();
                    break;
            }
        }
    }

    @Override
    public void handleKeyReleased(KeyEvent event) {
        // Not needed
    }

    @Override
    public String getStateName() {
        return "VICTORY";
    }

    public void handleMouseClicked(MouseEvent event) {
        selectOption();
    }
}
