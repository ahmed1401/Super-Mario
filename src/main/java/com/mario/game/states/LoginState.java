package com.mario.game.states;

import com.mario.auth.AuthenticationService;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
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
 * Enhanced Login state with real text input fields.
 * Supports keyboard input for username/password and mouse clicks for buttons.
 */
public class LoginState implements GameState {

    private final GameStateManager stateManager;
    private final AuthenticationService authService;
    private StringBuilder username;
    private StringBuilder password;
    private String message = "";
    private Color messageColor = Color.WHITE;
    private boolean isRegistering = false;
    private int focusedField = 0; // 0 = username, 1 = password
    private double animationTime = 0;

    // Button bounds for mouse clicks
    private double[] loginBtnBounds = new double[4]; // x, y, w, h
    private double[] registerBtnBounds = new double[4];
    private double[] guestBtnBounds = new double[4];
    private double[] toggleBtnBounds = new double[4];

    // UI Constants
    private static final double FIELD_WIDTH = 280;
    private static final double FIELD_HEIGHT = 40;
    private static final double BUTTON_WIDTH = 180;
    private static final double BUTTON_HEIGHT = 45;

    public LoginState(GameStateManager stateManager) {
        this.stateManager = stateManager;
        this.authService = AuthenticationService.getInstance();
        this.username = new StringBuilder();
        this.password = new StringBuilder();
    }

    @Override
    public void enter() {
        username = new StringBuilder();
        password = new StringBuilder();
        message = "Enter your credentials or play as Guest";
        messageColor = Color.LIGHTGRAY;
        isRegistering = false;
        focusedField = 0;
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

        // Gradient background
        LinearGradient gradient = new LinearGradient(0, 0, 0, height, false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(30, 50, 100)),
                new Stop(1, Color.rgb(10, 20, 50)));
        gc.setFill(gradient);
        gc.fillRect(0, 0, width, height);

        // Animated stars in background
        gc.setFill(Color.WHITE);
        for (int i = 0; i < 50; i++) {
            double starX = (i * 17 + animationTime * 10) % width;
            double starY = (i * 13) % height;
            double size = 1 + (i % 3);
            gc.fillOval(starX, starY, size, size);
        }

        // Title with glow effect
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 52));
        gc.setTextAlign(TextAlignment.CENTER);

        // Glow
        gc.setFill(Color.rgb(255, 200, 0, 0.3));
        gc.fillText("SUPER MARIO", centerX + 2, 82);
        gc.setFill(Color.rgb(255, 215, 0));
        gc.fillText("SUPER MARIO", centerX, 80);

        // Subtitle
        gc.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
        gc.setFill(Color.WHITE);
        gc.fillText(isRegistering ? "Create Account" : "Login", centerX, 115);

        // Form container
        double formY = 150;
        gc.setFill(Color.rgb(0, 0, 0, 0.5));
        gc.fillRoundRect(centerX - 170, formY, 340, 280, 15, 15);
        gc.setStroke(Color.rgb(100, 150, 255, 0.5));
        gc.setLineWidth(2);
        gc.strokeRoundRect(centerX - 170, formY, 340, 280, 15, 15);

        // Username field
        gc.setFont(Font.font("Arial", 14));
        gc.setFill(Color.LIGHTGRAY);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText("Username:", centerX - 140, formY + 35);

        renderTextField(gc, centerX - 140, formY + 45, FIELD_WIDTH, FIELD_HEIGHT,
                username.toString(), focusedField == 0, false);

        // Password field
        gc.setFill(Color.LIGHTGRAY);
        gc.fillText("Password:", centerX - 140, formY + 110);

        renderTextField(gc, centerX - 140, formY + 120, FIELD_WIDTH, FIELD_HEIGHT,
                password.toString(), focusedField == 1, true);

        // Message
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(Font.font("Arial", 13));
        gc.setFill(messageColor);
        gc.fillText(message, centerX, formY + 190);

        // Buttons
        double btnY = formY + 210;

        // Login/Register button
        loginBtnBounds = new double[] { centerX - 90, btnY, BUTTON_WIDTH, BUTTON_HEIGHT };
        renderButton(gc, loginBtnBounds[0], loginBtnBounds[1], loginBtnBounds[2], loginBtnBounds[3],
                isRegistering ? "Create Account" : "Login", Color.rgb(50, 150, 50));

        // Guest button
        guestBtnBounds = new double[] { centerX - 90, btnY + 55, BUTTON_WIDTH, BUTTON_HEIGHT };
        renderButton(gc, guestBtnBounds[0], guestBtnBounds[1], guestBtnBounds[2], guestBtnBounds[3],
                "Play as Guest", Color.rgb(100, 100, 150));

        // Toggle mode link
        gc.setFont(Font.font("Arial", 13));
        gc.setFill(Color.rgb(150, 200, 255));
        String toggleText = isRegistering ? "Already have an account? Login" : "New player? Create account";
        gc.fillText(toggleText, centerX, height - 80);
        toggleBtnBounds = new double[] { centerX - 120, height - 95, 240, 20 };

        // Controls hint
        gc.setFont(Font.font("Arial", 12));
        gc.setFill(Color.GRAY);
        gc.fillText("TAB to switch fields | ENTER to submit | Click buttons", centerX, height - 30);
    }

    private void renderTextField(GraphicsContext gc, double x, double y, double w, double h,
            String text, boolean focused, boolean isPassword) {
        // Background
        gc.setFill(focused ? Color.rgb(60, 60, 80) : Color.rgb(40, 40, 60));
        gc.fillRoundRect(x, y, w, h, 8, 8);

        // Border
        gc.setStroke(focused ? Color.rgb(100, 180, 255) : Color.rgb(80, 80, 100));
        gc.setLineWidth(focused ? 2 : 1);
        gc.strokeRoundRect(x, y, w, h, 8, 8);

        // Text
        gc.setFont(Font.font("Consolas", 16));
        gc.setFill(Color.WHITE);
        gc.setTextAlign(TextAlignment.LEFT);
        String displayText = isPassword ? "●".repeat(text.length()) : text;
        gc.fillText(displayText, x + 10, y + h / 2 + 6);

        // Cursor blink
        if (focused && ((int) (animationTime * 2) % 2 == 0)) {
            double cursorX = x + 10 + displayText.length() * 9.5;
            gc.setFill(Color.WHITE);
            gc.fillRect(cursorX, y + 10, 2, h - 20);
        }
    }

    private void renderButton(GraphicsContext gc, double x, double y, double w, double h,
            String text, Color baseColor) {
        // Gradient button
        LinearGradient btnGradient = new LinearGradient(0, y, 0, y + h, false, CycleMethod.NO_CYCLE,
                new Stop(0, baseColor.brighter()),
                new Stop(1, baseColor.darker()));
        gc.setFill(btnGradient);
        gc.fillRoundRect(x, y, w, h, 10, 10);

        // Border
        gc.setStroke(baseColor.brighter().brighter());
        gc.setLineWidth(1);
        gc.strokeRoundRect(x, y, w, h, 10, 10);

        // Text
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        gc.setFill(Color.WHITE);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(text, x + w / 2, y + h / 2 + 6);
    }

    @Override
    public void handleKeyPressed(KeyEvent event) {
        KeyCode code = event.getCode();

        switch (code) {
            case TAB:
                // Switch between fields
                focusedField = (focusedField + 1) % 2;
                break;
            case ENTER:
                // Submit form
                submitForm();
                break;
            case BACK_SPACE:
                // Delete character
                if (focusedField == 0 && username.length() > 0) {
                    username.deleteCharAt(username.length() - 1);
                } else if (focusedField == 1 && password.length() > 0) {
                    password.deleteCharAt(password.length() - 1);
                }
                break;
            case ESCAPE:
                // Play as guest
                playAsGuest();
                break;
            default:
                // Type character
                String text = event.getText();
                if (text != null && text.length() == 1 && Character.isLetterOrDigit(text.charAt(0))) {
                    if (focusedField == 0 && username.length() < 20) {
                        username.append(text);
                    } else if (focusedField == 1 && password.length() < 20) {
                        password.append(text);
                    }
                }
        }
    }

    /**
     * Handle mouse clicks for buttons
     */
    public void handleMouseClicked(MouseEvent event) {
        double mx = event.getX();
        double my = event.getY();

        // Check login button
        if (isInBounds(mx, my, loginBtnBounds)) {
            submitForm();
        }
        // Check guest button
        else if (isInBounds(mx, my, guestBtnBounds)) {
            playAsGuest();
        }
        // Check toggle button
        else if (isInBounds(mx, my, toggleBtnBounds)) {
            isRegistering = !isRegistering;
            message = isRegistering ? "Create a new account" : "Enter your credentials";
            messageColor = Color.LIGHTGRAY;
        }
        // Check field focus
        else {
            double formY = 150;
            if (my >= formY + 45 && my <= formY + 85) {
                focusedField = 0;
            } else if (my >= formY + 120 && my <= formY + 160) {
                focusedField = 1;
            }
        }
    }

    private boolean isInBounds(double mx, double my, double[] bounds) {
        return mx >= bounds[0] && mx <= bounds[0] + bounds[2] &&
                my >= bounds[1] && my <= bounds[1] + bounds[3];
    }

    private void submitForm() {
        String user = username.toString().trim();
        String pass = password.toString();

        if (user.isEmpty()) {
            message = "Please enter a username";
            messageColor = Color.ORANGE;
            return;
        }
        if (pass.isEmpty()) {
            message = "Please enter a password";
            messageColor = Color.ORANGE;
            return;
        }

        AuthenticationService auth = AuthenticationService.getInstance();

        if (isRegistering) {
            if (auth.register(user, pass)) {
                message = "Account created! Logging in...";
                messageColor = Color.LIGHTGREEN;
                auth.login(user, pass);
                stateManager.goToMenu();
            } else {
                message = "Username already exists!";
                messageColor = Color.rgb(255, 100, 100);
            }
        } else {
            if (auth.login(user, pass)) {
                message = "Login successful!";
                messageColor = Color.LIGHTGREEN;
                stateManager.goToMenu();
            } else {
                message = "Invalid username or password";
                messageColor = Color.rgb(255, 100, 100);
            }
        }
    }

    private void playAsGuest() {
        AuthenticationService.getInstance().loginAsGuest();
        stateManager.goToMenu();
    }

    @Override
    public void handleKeyReleased(KeyEvent event) {
        // Not needed
    }

    @Override
    public String getStateName() {
        return "LOGIN";
    }
}
