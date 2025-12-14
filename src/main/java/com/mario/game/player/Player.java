package com.mario.game.player;

import com.mario.game.levels.Platform;
import com.mario.game.powerups.PowerUpDecorator;
import com.mario.utils.GameLogger;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Main Player class that uses the Decorator pattern for power-ups.
 * Handles physics, movement, and rendering.
 */
public class Player {

    // Position and size
    private double x, y;
    private double baseWidth = 32;
    private double baseHeight = 48;

    // Physics
    private double velocityX = 0;
    private double velocityY = 0;
    private static final double GRAVITY = 900;
    private static final double MAX_FALL_SPEED = 600;
    private static final double FRICTION = 0.88;

    // State
    private boolean onGround = false;
    private boolean facingRight = true;
    private boolean isDead = false;
    private double animationTime = 0;

    // Double-jump support
    private boolean canDoubleJump = false;
    private boolean jumpKeyHeld = false;

    // Power-ups using DECORATOR PATTERN
    private PlayerComponent component;
    private List<PowerUpDecorator> activeDecorators;

    public Player(double x, double y) {
        this.x = x;
        this.y = y;
        this.component = new BasePlayer();
        this.activeDecorators = new ArrayList<>();

        GameLogger.logPlayer("Created", "at (" + x + ", " + y + ")");
    }

    /**
     * Apply a power-up decorator
     */
    public void applyPowerUp(PowerUpDecorator decorator) {
        activeDecorators.add(decorator);
        component = decorator;
        GameLogger.logDecoratorApplied(decorator.getDecoratorName(), "Player");
    }

    /**
     * Get current width (affected by decorators)
     */
    public double getWidth() {
        return baseWidth * component.getSizeMultiplier();
    }

    /**
     * Get current height (affected by decorators)
     */
    public double getHeight() {
        return baseHeight * component.getSizeMultiplier();
    }

    /**
     * Update player based on input
     */
    public void update(double deltaTime, Set<KeyCode> pressedKeys, List<Platform> platforms) {
        if (isDead)
            return;

        animationTime += deltaTime;

        // Update decorators and remove expired ones
        component.update(deltaTime);
        cleanupExpiredDecorators();

        // Handle input
        handleInput(pressedKeys);

        // Apply physics
        applyPhysics(deltaTime);

        // Handle collisions
        handleCollisions(platforms);

        // Death check
        if (y > 700) {
            isDead = true;
            GameLogger.logPlayer("Death", "Fell off map");
        }
    }

    private void handleInput(Set<KeyCode> pressedKeys) {
        double speed = component.getSpeed();
        double acceleration = 20;

        // Horizontal movement
        if (pressedKeys.contains(KeyCode.LEFT) || pressedKeys.contains(KeyCode.A)) {
            velocityX -= acceleration;
            facingRight = false;
        }
        if (pressedKeys.contains(KeyCode.RIGHT) || pressedKeys.contains(KeyCode.D)) {
            velocityX += acceleration;
            facingRight = true;
        }

        // Clamp horizontal speed
        double maxSpeed = speed;
        velocityX = Math.max(-maxSpeed, Math.min(maxSpeed, velocityX));

        // Jump (with double-jump support)
        boolean jumpPressed = pressedKeys.contains(KeyCode.SPACE) ||
                pressedKeys.contains(KeyCode.UP) ||
                pressedKeys.contains(KeyCode.W);

        if (jumpPressed && !jumpKeyHeld) {
            if (onGround) {
                velocityY = -component.getJumpForce();
                onGround = false;
                canDoubleJump = true;
                com.mario.utils.SoundManager.getInstance().playJump();
            } else if (canDoubleJump) {
                velocityY = -component.getJumpForce() * 0.85; // Slightly weaker
                canDoubleJump = false;
                com.mario.utils.SoundManager.getInstance().playJump(); // Use same sound for now
            }
        }
        jumpKeyHeld = jumpPressed;
    }

    private void applyPhysics(double deltaTime) {
        // Apply gravity
        velocityY += GRAVITY * deltaTime;
        velocityY = Math.min(velocityY, MAX_FALL_SPEED);

        // Apply friction when on ground
        if (onGround) {
            velocityX *= FRICTION;
        }

        // Update position
        x += velocityX * deltaTime;
        y += velocityY * deltaTime;
    }

    private void handleCollisions(List<Platform> platforms) {
        Rectangle2D playerBounds = getBounds();
        onGround = false;

        for (Platform platform : platforms) {
            if (!platform.isSolid())
                continue;

            Rectangle2D platBounds = platform.getBounds();

            if (playerBounds.intersects(platBounds)) {
                // Calculate overlap
                double overlapLeft = (playerBounds.getMaxX()) - platBounds.getMinX();
                double overlapRight = platBounds.getMaxX() - playerBounds.getMinX();
                double overlapTop = (playerBounds.getMaxY()) - platBounds.getMinY();
                double overlapBottom = platBounds.getMaxY() - playerBounds.getMinY();

                // Find minimum overlap
                double minOverlapX = Math.min(overlapLeft, overlapRight);
                double minOverlapY = Math.min(overlapTop, overlapBottom);

                if (minOverlapY < minOverlapX) {
                    // Vertical collision
                    if (overlapTop < overlapBottom) {
                        // Landing on platform
                        if (velocityY > 0) {
                            y = platBounds.getMinY() - getHeight();
                            velocityY = 0;
                            onGround = true;
                        }
                    } else {
                        // Hitting from below
                        if (velocityY < 0) {
                            y = platBounds.getMaxY();
                            velocityY = 0;
                        }
                    }
                } else {
                    // Horizontal collision
                    if (overlapLeft < overlapRight) {
                        x = platBounds.getMinX() - getWidth();
                        velocityX = 0;
                    } else {
                        x = platBounds.getMaxX();
                        velocityX = 0;
                    }
                }

                // Update bounds after collision resolution
                playerBounds = getBounds();
            }
        }
    }

    private void cleanupExpiredDecorators() {
        Iterator<PowerUpDecorator> it = activeDecorators.iterator();
        while (it.hasNext()) {
            PowerUpDecorator decorator = it.next();
            if (decorator.isExpired()) {
                it.remove();
            }
        }

        // Rebuild component chain
        if (activeDecorators.isEmpty()) {
            component = new BasePlayer();
        } else {
            component = activeDecorators.get(activeDecorators.size() - 1);
        }
    }

    /**
     * Render the player
     */
    public void render(GraphicsContext gc, double cameraX, double cameraY) {
        if (isDead)
            return;

        double screenX = x - cameraX;
        double screenY = y - cameraY;
        double width = getWidth();
        double height = getHeight();

        // Star power effect (flashing)
        boolean visible = true;
        if (component.isInvincible()) {
            visible = ((int) (animationTime * 10) % 2) == 0;
        }

        if (visible) {
            // Shield effect
            if (component.hasShield()) {
                gc.setFill(Color.rgb(100, 150, 255, 0.3));
                gc.fillOval(screenX - 5, screenY - 5, width + 10, height + 10);
            }

            // Body color based on power-ups
            Color bodyColor = Color.RED;
            if (component.canShoot()) {
                bodyColor = Color.ORANGE;
            }

            // Body
            gc.setFill(bodyColor);
            gc.fillRect(screenX + width * 0.2, screenY + height * 0.35, width * 0.6, height * 0.45);

            // Head
            gc.setFill(Color.rgb(255, 200, 150));
            gc.fillOval(screenX + width * 0.15, screenY, width * 0.7, height * 0.4);

            // Hat
            gc.setFill(bodyColor);
            gc.fillRect(screenX, screenY + height * 0.15, width, height * 0.1);
            gc.fillOval(screenX + width * 0.1, screenY, width * 0.5, height * 0.2);

            // Eyes
            gc.setFill(Color.WHITE);
            double eyeX = facingRight ? screenX + width * 0.45 : screenX + width * 0.25;
            gc.fillOval(eyeX, screenY + height * 0.15, width * 0.25, height * 0.12);
            gc.setFill(Color.BLACK);
            gc.fillOval(eyeX + (facingRight ? width * 0.1 : 0), screenY + height * 0.17, width * 0.1, height * 0.08);

            // Mustache
            gc.setFill(Color.rgb(80, 50, 30));
            gc.fillRect(screenX + width * 0.25, screenY + height * 0.28, width * 0.5, height * 0.05);

            // Legs animation
            double legOffset = onGround ? Math.sin(animationTime * 15) * 5 : 0;
            gc.setFill(Color.BLUE);
            gc.fillRect(screenX + width * 0.2, screenY + height * 0.8 + legOffset, width * 0.25, height * 0.2);
            gc.fillRect(screenX + width * 0.55, screenY + height * 0.8 - legOffset, width * 0.25, height * 0.2);
        }
    }

    /**
     * Handle taking damage
     */
    public void takeDamage() {
        if (component.isInvincible())
            return;

        if (component.hasShield()) {
            // Shield absorbs damage
            GameLogger.logPlayer("Shield absorbed damage", "");
            return;
        }

        // Remove power-ups on damage
        if (!activeDecorators.isEmpty()) {
            activeDecorators.clear();
            component = new BasePlayer();
            com.mario.utils.SoundManager.getInstance().playPowerUp(); // Down-grade sound
            GameLogger.logPlayer("Lost power-ups", "");
        } else {
            isDead = true;
            com.mario.utils.SoundManager.getInstance().playDeath();
            GameLogger.logPlayer("Death", "Took damage");
        }
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, getWidth(), getHeight());
    }

    // Getters
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public boolean isDead() {
        return isDead;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public PlayerComponent getComponent() {
        return component;
    }

    public boolean canShoot() {
        return component.canShoot();
    }

    // Setters
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setDead(boolean dead) {
        this.isDead = dead;
    }

    public void reset(double x, double y) {
        this.x = x;
        this.y = y;
        this.velocityX = 0;
        this.velocityY = 0;
        this.isDead = false;
        this.onGround = false;
        this.activeDecorators.clear();
        this.component = new BasePlayer();
    }
}
