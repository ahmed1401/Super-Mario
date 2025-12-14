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
 * Settings state with game options.
 */
public class SettingsState implements GameState {

    private final GameStateManager stateManager;
    private int selectedOption = 0;
    private double animationTime = 0;

    // Settings values
    private int difficulty = 1; // 0=Easy, 1=Normal, 2=Hard
    private int soundVolume = 80;
    private int musicVolume = 70;
    private boolean showFPS = false;

    private final String[] options = {
            "Difficulty",
            "Sound Effects",
            "Music Volume",
            "Show FPS",
            "Back to Menu"
    };

    private static final String[] DIFFICULTY_NAMES = { "Easy", "Normal", "Hard" };

    public SettingsState(GameStateManager stateManager) {
        this.stateManager = stateManager;
    }

    @Override
    public void enter() {
        selectedOption = 0;
    }

    @Override
    public void exit() {
        // Save settings here if needed
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

        // Background gradient
        LinearGradient gradient = new LinearGradient(0, 0, 0, height, false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(40, 40, 60)),
                new Stop(1, Color.rgb(20, 20, 40)));
        gc.setFill(gradient);
        gc.fillRect(0, 0, width, height);

        // Title
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFill(Color.WHITE);
        gc.fillText("SETTINGS", centerX, 70);

        // Settings panel
        double panelX = centerX - 250;
        double panelY = 100;
        double panelW = 500;
        double panelH = 380;

        gc.setFill(Color.rgb(0, 0, 0, 0.5));
        gc.fillRoundRect(panelX, panelY, panelW, panelH, 15, 15);
        gc.setStroke(Color.rgb(100, 100, 150));
        gc.setLineWidth(2);
        gc.strokeRoundRect(panelX, panelY, panelW, panelH, 15, 15);

        // Render each setting
        for (int i = 0; i < options.length; i++) {
            double y = panelY + 50 + i * 65;
            boolean selected = (i == selectedOption);

            renderSettingRow(gc, panelX + 30, y, panelW - 60, options[i], i, selected);
        }

        // Instructions
        gc.setFont(Font.font("Arial", 14));
        gc.setFill(Color.LIGHTGRAY);
        gc.fillText("↑↓ Navigate | ←→ Change Value | ENTER/ESC Back", centerX, height - 30);
    }

    private void renderSettingRow(GraphicsContext gc, double x, double y, double w, String label, int index,
            boolean selected) {
        // Background
        if (selected) {
            gc.setFill(Color.rgb(80, 80, 120, 0.5));
            gc.fillRoundRect(x - 10, y - 25, w + 20, 55, 10, 10);
            gc.setStroke(Color.rgb(150, 150, 255));
            gc.setLineWidth(2);
            gc.strokeRoundRect(x - 10, y - 25, w + 20, 55, 10, 10);
        }

        // Label
        gc.setFont(Font.font("Arial", FontWeight.BOLD, selected ? 22 : 20));
        gc.setFill(selected ? Color.YELLOW : Color.WHITE);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText(label, x, y + 5);

        // Value
        gc.setTextAlign(TextAlignment.RIGHT);
        String valueText = getValueText(index);
        gc.fillText(valueText, x + w, y + 5);

        // Slider for volume settings
        if (index == 1 || index == 2) {
            int volume = (index == 1) ? soundVolume : musicVolume;
            renderSlider(gc, x + w - 200, y - 10, 150, 20, volume);
        }
    }

    private void renderSlider(GraphicsContext gc, double x, double y, double w, double h, int value) {
        // Track
        gc.setFill(Color.rgb(60, 60, 80));
        gc.fillRoundRect(x, y, w, h, 5, 5);

        // Fill
        double fillW = (value / 100.0) * w;
        gc.setFill(Color.rgb(100, 180, 255));
        gc.fillRoundRect(x, y, fillW, h, 5, 5);

        // Handle
        gc.setFill(Color.WHITE);
        gc.fillOval(x + fillW - 8, y - 2, h + 4, h + 4);
    }

    private String getValueText(int index) {
        switch (index) {
            case 0:
                return "< " + DIFFICULTY_NAMES[difficulty] + " >";
            case 1:
                return soundVolume + "%";
            case 2:
                return musicVolume + "%";
            case 3:
                return showFPS ? "ON" : "OFF";
            case 4:
                return "";
            default:
                return "";
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
            case LEFT:
            case A:
                adjustValue(-1);
                break;
            case RIGHT:
            case D:
                adjustValue(1);
                break;
            case ENTER:
            case ESCAPE:
                if (selectedOption == options.length - 1 || event.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
                    stateManager.goToMenu();
                }
                break;
            default:
                break;
        }
    }

    private void adjustValue(int delta) {
        switch (selectedOption) {
            case 0: // Difficulty
                difficulty = Math.max(0, Math.min(2, difficulty + delta));
                break;
            case 1: // Sound
                soundVolume = Math.max(0, Math.min(100, soundVolume + delta * 10));
                break;
            case 2: // Music
                musicVolume = Math.max(0, Math.min(100, musicVolume + delta * 10));
                break;
            case 3: // FPS
                showFPS = !showFPS;
                break;
        }
    }

    public void handleMouseClicked(MouseEvent event) {
        // TODO: Add mouse clicking for settings
    }

    @Override
    public void handleKeyReleased(KeyEvent event) {
        // Not needed
    }

    @Override
    public String getStateName() {
        return "SETTINGS";
    }

    // Getters for game to use
    public int getDifficulty() {
        return difficulty;
    }

    public int getSoundVolume() {
        return soundVolume;
    }

    public int getMusicVolume() {
        return musicVolume;
    }

    public boolean isShowFPS() {
        return showFPS;
    }
}
