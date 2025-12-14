package com.mario.game.states;

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
 * Level selection screen.
 */
public class LevelSelectState implements GameState {

    private final GameStateManager stateManager;
    private int selectedLevel = 0;
    private double animationTime = 0;

    private final String[] levelNames = {
            "World 1-1: Grassland",
            "World 1-2: Underground",
            "World 1-3: Castle (Boss)"
    };

    private final String[] levelDescriptions = {
            "A sunny beginning with easy enemies",
            "Dark tunnels and tricky platforms",
            "Face the final boss!"
    };

    private final Color[] levelColors = {
            Color.rgb(100, 180, 100),
            Color.rgb(80, 80, 100),
            Color.rgb(150, 80, 80)
    };

    public LevelSelectState(GameStateManager stateManager) {
        this.stateManager = stateManager;
    }

    @Override
    public void enter() {
        selectedLevel = 0;
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
        double centerX = width / 2;

        // Background
        LinearGradient gradient = new LinearGradient(0, 0, 0, height, false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(40, 60, 100)),
                new Stop(1, Color.rgb(20, 30, 50)));
        gc.setFill(gradient);
        gc.fillRect(0, 0, width, height);

        // Title
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 42));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFill(Color.WHITE);
        gc.fillText("SELECT LEVEL", centerX, 60);

        // Level cards
        double cardW = 220;
        double cardH = 280;
        double startX = centerX - (levelNames.length * cardW + (levelNames.length - 1) * 20) / 2;

        for (int i = 0; i < levelNames.length; i++) {
            double x = startX + i * (cardW + 20);
            double y = 100;
            boolean selected = (i == selectedLevel);

            renderLevelCard(gc, x, y, cardW, cardH, i, selected);
        }

        // Description
        gc.setFont(Font.font("Arial", 18));
        gc.setFill(Color.LIGHTGRAY);
        gc.fillText(levelDescriptions[selectedLevel], centerX, height - 100);

        // Instructions
        gc.setFont(Font.font("Arial", 14));
        gc.setFill(Color.GRAY);
        gc.fillText("←→ Select | ENTER Play | ESC Back", centerX, height - 30);
    }

    private void renderLevelCard(GraphicsContext gc, double x, double y, double w, double h, int level,
            boolean selected) {
        // Card background
        Color baseColor = levelColors[level];

        if (selected) {
            // Glow effect
            gc.setFill(Color.rgb(255, 255, 200, 0.2));
            gc.fillRoundRect(x - 10, y - 10, w + 20, h + 20, 20, 20);
        }

        LinearGradient cardGradient = new LinearGradient(0, y, 0, y + h, false, CycleMethod.NO_CYCLE,
                new Stop(0, baseColor.brighter()),
                new Stop(1, baseColor.darker()));
        gc.setFill(cardGradient);
        gc.fillRoundRect(x, y, w, h, 15, 15);

        // Border
        gc.setStroke(selected ? Color.YELLOW : Color.rgb(100, 100, 100));
        gc.setLineWidth(selected ? 4 : 2);
        gc.strokeRoundRect(x, y, w, h, 15, 15);

        // Level number
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 72));
        gc.setFill(Color.rgb(255, 255, 255, 0.3));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(String.valueOf(level + 1), x + w / 2, y + 100);

        // Level name
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        gc.setFill(Color.WHITE);
        gc.fillText("WORLD 1-" + (level + 1), x + w / 2, y + h - 60);

        // Stars (for completion)
        gc.setFill(Color.rgb(180, 180, 180));
        gc.setFont(Font.font("Arial", 24));
        gc.fillText("☆ ☆ ☆", x + w / 2, y + h - 25);

        // Lock icon for locked levels (example: all unlocked)
        // If locked: draw lock icon
    }

    @Override
    public void handleKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT:
            case A:
                selectedLevel = (selectedLevel - 1 + levelNames.length) % levelNames.length;
                break;
            case RIGHT:
            case D:
                selectedLevel = (selectedLevel + 1) % levelNames.length;
                break;
            case ENTER:
            case SPACE:
                startLevel();
                break;
            case ESCAPE:
                stateManager.goToMenu();
                break;
            default:
                break;
        }
    }

    private void startLevel() {
        // Set the selected level and start
        stateManager.getPlayingState().getGameEngine().setLevel(selectedLevel);
        stateManager.goToPlaying();
    }

    public void handleMouseClicked(MouseEvent event) {
        // TODO: Implement mouse click on cards
    }

    @Override
    public void handleKeyReleased(KeyEvent event) {
        // Not needed
    }

    @Override
    public String getStateName() {
        return "LEVEL_SELECT";
    }
}
