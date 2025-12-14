package com.mario;

import com.mario.game.GameEngine;
import com.mario.game.states.GameStateManager;
import com.mario.game.states.PlayingState;
import com.mario.utils.GameLogger;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Main application entry point for Super Mario Game.
 * 
 * This game demonstrates the following Design Patterns:
 * 1. STATE PATTERN - Game states and player states
 * 2. DECORATOR PATTERN - Power-up system
 * 3. COMPOSITE PATTERN - Level structure
 * 4. FACTORY PATTERN - Entity creation
 * 5. OBSERVER PATTERN - HUD and event system
 */
public class Main extends Application {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final String TITLE = "Super Mario - Design Patterns Demo";

    private Canvas canvas;
    private GraphicsContext gc;
    private GameStateManager stateManager;
    private GameEngine gameEngine;
    private Stage primaryStage;

    private long lastTime = 0;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        GameLogger.info("Starting Super Mario Game...");

        // Create canvas
        canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();

        // Initialize game systems
        stateManager = new GameStateManager();
        gameEngine = new GameEngine(WIDTH, HEIGHT);

        // Connect game engine to playing state
        PlayingState playingState = stateManager.getPlayingState();
        playingState.setGameEngine(gameEngine);

        // Pass stage to menu state for fullscreen toggle
        // stateManager.getMenuState().setStage(primaryStage); // Removed

        // Create scene
        StackPane root = new StackPane(canvas);
        root.setStyle("-fx-background-color: black;");
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        // Handle keyboard input
        scene.setOnKeyPressed(this::handleKeyPressed);
        scene.setOnKeyReleased(this::handleKeyReleased);

        // Handle mouse input
        scene.setOnMouseClicked(this::handleMouseClicked);
        scene.setOnMouseMoved(this::handleMouseMoved);
        scene.setCursor(Cursor.HAND);

        // Setup stage
        primaryStage.setTitle(TITLE);
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.setMinWidth(WIDTH);
        primaryStage.setMinHeight(HEIGHT);
        primaryStage.show();

        // Focus for keyboard input
        canvas.requestFocus();
        canvas.setFocusTraversable(true);

        // Bind canvas size to window size for fullscreen support
        canvas.widthProperty().bind(root.widthProperty());
        canvas.heightProperty().bind(root.heightProperty());

        // Start with entering the login state
        stateManager.goToLogin();

        // Game loop
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }

                double deltaTime = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;

                // Cap delta time to prevent huge jumps
                deltaTime = Math.min(deltaTime, 0.05);

                update(deltaTime);
                render();
            }
        };
        gameLoop.start();

        GameLogger.info("Game loop started");
    }

    private void update(double deltaTime) {
        stateManager.update(deltaTime);
    }

    private void render() {
        // Clear screen
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Render current state
        stateManager.render(gc);
    }

    private void handleKeyPressed(KeyEvent event) {
        // Global fullscreen toggle
        if (event.getCode() == javafx.scene.input.KeyCode.F11) {
            primaryStage.setFullScreen(!primaryStage.isFullScreen());
            return;
        }
        stateManager.getCurrentState().handleKeyPressed(event);
    }

    private void handleKeyReleased(KeyEvent event) {
        stateManager.getCurrentState().handleKeyReleased(event);
    }

    private void handleMouseClicked(MouseEvent event) {
        stateManager.handleMouseClicked(event);
    }

    private void handleMouseMoved(MouseEvent event) {
        stateManager.handleMouseMoved(event);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
