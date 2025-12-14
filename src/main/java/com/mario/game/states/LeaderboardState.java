package com.mario.game.states;

import com.mario.game.LeaderboardService;
import com.mario.utils.SoundManager;
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

import java.util.List;

/**
 * STATE PATTERN - Concrete State
 * 
 * Leaderboard display screen.
 */
public class LeaderboardState implements GameState {

    private final GameStateManager stateManager;
    private double animationTime = 0;

    // Scrolling support
    private int scrollOffset = 0;
    private static final int VISIBLE_ROWS = 8;
    private List<LeaderboardService.LeaderboardEntry> allEntries;

    // UI Bounds
    private double backButtonX, backButtonY, backButtonW, backButtonH;

    public LeaderboardState(GameStateManager stateManager) {
        this.stateManager = stateManager;
    }

    @Override
    public void enter() {
        // Refresh leaderboard
        allEntries = LeaderboardService.getInstance().getTopEntries(100);
        scrollOffset = 0;
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
                new Stop(0, Color.rgb(50, 30, 80)),
                new Stop(1, Color.rgb(20, 10, 40)));
        gc.setFill(gradient);
        gc.fillRect(0, 0, width, height);

        // Title
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFill(Color.GOLD);
        gc.fillText("🏆 LEADERBOARD 🏆", centerX, 60);

        // Leaderboard table
        double tableX = centerX - 300;
        double tableY = 100;
        double tableW = 600;
        double rowH = 45;
        double totalTableHeight = VISIBLE_ROWS * rowH;

        // Header
        gc.setFill(Color.rgb(80, 60, 120));
        gc.fillRoundRect(tableX, tableY, tableW, rowH, 10, 10);

        gc.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        gc.setFill(Color.WHITE);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText("RANK", tableX + 20, tableY + 30);
        gc.fillText("PLAYER", tableX + 100, tableY + 30);
        gc.fillText("SCORE", tableX + 350, tableY + 30);
        gc.fillText("LEVEL", tableX + 500, tableY + 30);

        // Entries
        if (allEntries == null || allEntries.isEmpty()) {
            // No entries
            gc.setFont(Font.font("Arial", 20));
            gc.setFill(Color.LIGHTGRAY);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText("No scores yet! Be the first to play!", centerX, tableY + 150);
        } else {
            // Render visible rows
            int endIndex = Math.min(scrollOffset + VISIBLE_ROWS, allEntries.size());

            for (int i = scrollOffset; i < endIndex; i++) {
                LeaderboardService.LeaderboardEntry entry = allEntries.get(i);
                int visualIndex = i - scrollOffset;
                double rowY = tableY + rowH + visualIndex * rowH;

                // Row background
                Color rowColor = (i % 2 == 0) ? Color.rgb(40, 30, 60, 0.8) : Color.rgb(50, 40, 70, 0.8);
                if (i < 3) {
                    // Top 3 highlighted
                    rowColor = Color.rgb(80 + (2 - i) * 30, 60, 40, 0.9);
                }
                gc.setFill(rowColor);
                gc.fillRect(tableX, rowY, tableW, rowH);

                // Rank
                gc.setFont(Font.font("Arial", FontWeight.BOLD, 18));
                gc.setFill(i < 3 ? Color.GOLD : Color.WHITE);
                gc.setTextAlign(TextAlignment.LEFT);
                String rankText = (i == 0) ? "🥇" : (i == 1) ? "🥈" : (i == 2) ? "🥉" : String.valueOf(i + 1);
                gc.fillText(rankText, tableX + 30, rowY + 30);

                // Player name
                gc.setFont(Font.font("Arial", 16));
                gc.setFill(Color.WHITE);
                gc.fillText(entry.getPlayerName(), tableX + 100, rowY + 30);

                // Score
                gc.setFill(Color.YELLOW);
                gc.fillText(String.format("%,d", entry.getScore()), tableX + 350, rowY + 30);

                // Level
                gc.setFill(Color.LIGHTGRAY);
                gc.fillText("1-" + entry.getLevel(), tableX + 500, rowY + 30);
            }

            // Scrollbar
            if (allEntries.size() > VISIBLE_ROWS) {
                double scrollBarX = tableX + tableW + 10;
                double scrollBarY = tableY + rowH;
                double scrollBarH = totalTableHeight;

                // Track
                gc.setFill(Color.rgb(100, 100, 100, 0.5));
                gc.fillRoundRect(scrollBarX, scrollBarY, 10, scrollBarH, 5, 5);

                // Thumb
                double ratio = (double) VISIBLE_ROWS / allEntries.size();
                double thumbH = Math.max(30, scrollBarH * ratio);
                double thumbY = scrollBarY
                        + (scrollOffset / (double) (allEntries.size() - VISIBLE_ROWS)) * (scrollBarH - thumbH);

                gc.setFill(Color.rgb(200, 200, 200, 0.8));
                gc.fillRoundRect(scrollBarX, thumbY, 10, thumbH, 5, 5);
            }
        }

        // Back Button
        backButtonW = 200;
        backButtonH = 50;
        backButtonX = centerX - backButtonW / 2;
        backButtonY = height - 80;

        // Button hover animation handled by external styles or simple color change?
        // We'll do simple rect for now
        gc.setFill(Color.rgb(150, 50, 50));
        gc.fillRoundRect(backButtonX, backButtonY, backButtonW, backButtonH, 15, 15);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeRoundRect(backButtonX, backButtonY, backButtonW, backButtonH, 15, 15);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("BACK TO MENU", centerX, backButtonY + 32);

        // Instructions
        gc.setFont(Font.font("Arial", 14));
        gc.setFill(Color.GRAY);
        gc.fillText("Use UP/DOWN arrows or Mouse Wheel to scroll", centerX, height - 20);
    }

    @Override
    public void handleKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case UP:
                if (scrollOffset > 0) {
                    scrollOffset--;
                    SoundManager.getInstance().playSFX(SoundManager.SFX_MENU_MOVE);
                }
                break;
            case DOWN:
                if (allEntries != null && scrollOffset < allEntries.size() - VISIBLE_ROWS) {
                    scrollOffset++;
                    SoundManager.getInstance().playSFX(SoundManager.SFX_MENU_MOVE);
                }
                break;
            case ESCAPE:
            case ENTER:
                SoundManager.getInstance().playSFX(SoundManager.SFX_MENU_SELECT);
                stateManager.goToMenu();
                break;
            default:
                break;
        }
    }

    public void handleMouseClicked(MouseEvent event) {
        double mx = event.getX();
        double my = event.getY();

        // Check Back Button
        if (mx >= backButtonX && mx <= backButtonX + backButtonW &&
                my >= backButtonY && my <= backButtonY + backButtonH) {
            SoundManager.getInstance().playSFX(SoundManager.SFX_MENU_SELECT);
            stateManager.goToMenu();
        }
    }

    // Add Scroll Event handling if passed from Main/GameStateManager
    public void handleScroll(double deltaY) {
        if (allEntries == null)
            return;

        if (deltaY > 0) {
            if (scrollOffset > 0)
                scrollOffset--;
        } else {
            if (scrollOffset < allEntries.size() - VISIBLE_ROWS)
                scrollOffset++;
        }
    }

    @Override
    public void handleKeyReleased(KeyEvent event) {
        // Not needed
    }

    @Override
    public String getStateName() {
        return "LEADERBOARD";
    }
}
