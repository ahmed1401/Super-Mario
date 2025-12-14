package com.mario.game.states;

import com.mario.auth.AuthenticationService;
import com.mario.game.GameEngine;
import javafx.application.Platform;
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
import javafx.stage.Stage;

/**
 * STATE PATTERN - Concrete State
 * 
 * Enhanced Main menu state with mouse support, Exit button, and fullscreen
 * toggle.
 */
public class MenuState implements GameState {

    private final GameStateManager stateManager;
    private int selectedOption = 0;
    private int hoveredOption = -1;
    private double animationTime = 0;
    // stage removed

    private final String[] menuOptions = {
            "Start Game",
            "Continue Game",
            "Select Level",
            "Leaderboard",
            "Settings",
            "Logout",
            "Exit"
    };

    private double[][] buttonBounds;
    private static final double BUTTON_WIDTH = 250;
    private static final double BUTTON_HEIGHT = 45;

    public MenuState(GameStateManager stateManager) {
        this.stateManager = stateManager;
        this.buttonBounds = new double[menuOptions.length][4];
    }

    @Override
    public void enter() {
        selectedOption = 0;
        hoveredOption = -1;
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

        // Gradient sky background
        LinearGradient skyGradient = new LinearGradient(0, 0, 0, height, false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(135, 206, 235)),
                new Stop(0.6, Color.rgb(92, 148, 252)),
                new Stop(1, Color.rgb(60, 100, 200)));
        gc.setFill(skyGradient);
        gc.fillRect(0, 0, width, height);

        // Animated clouds
        gc.setFill(Color.WHITE);
        for (int i = 0; i < 6; i++) {
            double cloudX = ((i * 150 + animationTime * 20) % (width + 100)) - 50;
            double cloudY = 50 + (i % 3) * 40;
            drawCloud(gc, cloudX, cloudY, 40 + (i % 2) * 20);
        }

        // Hills in background
        gc.setFill(Color.rgb(100, 180, 100));
        for (int i = 0; i < 5; i++) {
            gc.fillOval(i * 200 - 50, height - 180, 250, 150);
        }

        // Ground
        gc.setFill(Color.rgb(139, 90, 43));
        gc.fillRect(0, height - 80, width, 80);
        gc.setFill(Color.rgb(34, 139, 34));
        gc.fillRect(0, height - 80, width, 20);

        // Title with shadow and animation
        double titleBounce = Math.sin(animationTime * 3) * 5;
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 56));
        gc.setTextAlign(TextAlignment.CENTER);

        // Shadow
        gc.setFill(Color.rgb(0, 0, 0, 0.3));
        gc.fillText("SUPER MARIO", centerX + 4, 74 + titleBounce);

        // Main title
        LinearGradient titleGradient = new LinearGradient(0, 40, 0, 90, false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(255, 220, 50)),
                new Stop(1, Color.rgb(255, 150, 0)));
        gc.setFill(titleGradient);
        gc.fillText("SUPER MARIO", centerX, 70 + titleBounce);

        // Outline
        gc.setStroke(Color.rgb(150, 50, 0));
        gc.setLineWidth(2);
        gc.strokeText("SUPER MARIO", centerX, 70 + titleBounce);

        // User info
        AuthenticationService auth = AuthenticationService.getInstance();
        if (auth.isLoggedIn()) {
            gc.setFont(Font.font("Arial", 14));
            gc.setFill(Color.WHITE);
            gc.setTextAlign(TextAlignment.RIGHT);
            String userText = "Welcome, " + auth.getCurrentUser().getUsername();
            if (!auth.getCurrentUser().isGuest()) {
                userText += " | High Score: " + auth.getCurrentUser().getHighScore();
            }
            gc.fillText(userText, width - 15, 25);
        }

        // Menu container
        double menuY = 90;
        gc.setFill(Color.rgb(0, 0, 0, 0.4));
        gc.fillRoundRect(centerX - 150, menuY, 300, menuOptions.length * 48 + 20, 15, 15);

        // Menu options
        for (int i = 0; i < menuOptions.length; i++) {
            double y = menuY + 18 + i * 48;
            buttonBounds[i] = new double[] { centerX - BUTTON_WIDTH / 2, y, BUTTON_WIDTH, BUTTON_HEIGHT };

            boolean isSelected = (i == selectedOption);
            boolean isHovered = (i == hoveredOption);

            renderMenuButton(gc, buttonBounds[i][0], buttonBounds[i][1],
                    BUTTON_WIDTH, BUTTON_HEIGHT, menuOptions[i],
                    isSelected, isHovered, i);
        }

        // Instructions
        gc.setFont(Font.font("Arial", 13));
        gc.setFill(Color.WHITE);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("↑↓ Navigate | ENTER Select | Mouse Click", centerX, height - 15);
    }

    private void renderMenuButton(GraphicsContext gc, double x, double y, double w, double h,
            String text, boolean selected, boolean hovered, int index) {
        Color baseColor = getButtonColor(index);

        if (selected || hovered) {
            // Highlighted button
            LinearGradient btnGradient = new LinearGradient(0, y, 0, y + h, false, CycleMethod.NO_CYCLE,
                    new Stop(0, baseColor.brighter().brighter()),
                    new Stop(1, baseColor));
            gc.setFill(btnGradient);
            gc.fillRoundRect(x - 5, y - 2, w + 10, h + 4, 10, 10);

            // Glow effect
            gc.setStroke(Color.YELLOW);
            gc.setLineWidth(2);
            gc.strokeRoundRect(x - 5, y - 2, w + 10, h + 4, 10, 10);
        } else {
            // Normal button
            gc.setFill(baseColor);
            gc.fillRoundRect(x, y, w, h, 8, 8);
        }

        // Button text
        gc.setFont(Font.font("Arial", FontWeight.BOLD, selected ? 18 : 16));
        gc.setFill(Color.WHITE);
        gc.setTextAlign(TextAlignment.CENTER);

        String displayText = selected ? "► " + text + " ◄" : text;
        gc.fillText(displayText, x + w / 2, y + h / 2 + 6);
    }

    private Color getButtonColor(int index) {
        switch (index) {
            case 0:
                return Color.rgb(50, 150, 50); // Start - Green
            case 1:
                return Color.rgb(80, 120, 160); // Continue - Blue
            case 2:
                return Color.rgb(150, 100, 50); // Level Select - Brown
            case 3:
                return Color.rgb(180, 150, 30); // Leaderboard - Gold
            case 4:
                return Color.rgb(100, 100, 120); // Settings - Gray
            case 5:
                return Color.rgb(150, 80, 50); // Logout - Orange
            case 6:
                return Color.rgb(150, 50, 50); // Exit - Red
            default:
                return Color.rgb(100, 100, 100);
        }
    }

    private void drawCloud(GraphicsContext gc, double x, double y, double size) {
        gc.fillOval(x, y, size, size * 0.6);
        gc.fillOval(x + size * 0.3, y - size * 0.2, size * 0.8, size * 0.6);
        gc.fillOval(x + size * 0.6, y, size * 0.7, size * 0.5);
    }

    @Override
    public void handleKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case UP:
            case W:
                selectedOption = (selectedOption - 1 + menuOptions.length) % menuOptions.length;
                break;
            case DOWN:
            case S:
                selectedOption = (selectedOption + 1) % menuOptions.length;
                break;
            case ENTER:
            case SPACE:
                selectOption(selectedOption);
                break;
            case ESCAPE:
                stateManager.goToLogin();
                break;
            default:
                break;
        }
    }

    public void handleMouseMoved(MouseEvent event) {
        double mx = event.getX();
        double my = event.getY();

        hoveredOption = -1;
        for (int i = 0; i < buttonBounds.length; i++) {
            if (isInBounds(mx, my, buttonBounds[i])) {
                hoveredOption = i;
                break;
            }
        }
    }

    public void handleMouseClicked(MouseEvent event) {
        double mx = event.getX();
        double my = event.getY();

        for (int i = 0; i < buttonBounds.length; i++) {
            if (isInBounds(mx, my, buttonBounds[i])) {
                selectOption(i);
                break;
            }
        }
    }

    private boolean isInBounds(double mx, double my, double[] bounds) {
        if (bounds == null || bounds.length < 4)
            return false;
        return mx >= bounds[0] && mx <= bounds[0] + bounds[2] &&
                my >= bounds[1] && my <= bounds[1] + bounds[3];
    }

    private void selectOption(int option) {
        GameEngine engine = stateManager.getPlayingState().getGameEngine();

        switch (option) {
            case 0: // Start Game
                if (engine != null)
                    engine.reset();
                stateManager.goToPlaying();
                break;
            case 1: // Continue Game
                stateManager.goToPlaying();
                break;
            case 2: // Select Level
                stateManager.goToLevelSelect();
                break;
            case 3: // Leaderboard
                stateManager.goToLeaderboard();
                break;
            case 4: // Settings
                stateManager.goToSettings();
                break;
            case 5: // Logout
                AuthenticationService.getInstance().logout();
                stateManager.goToLogin();
                break;
            case 6: // Exit
                Platform.exit();
                System.exit(0);
                break;
        }
    }

    @Override
    public void handleKeyReleased(KeyEvent event) {
        // Not needed
    }

    @Override
    public String getStateName() {
        return "MENU";
    }
}
