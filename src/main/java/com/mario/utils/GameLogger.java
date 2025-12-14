package com.mario.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

/**
 * Centralized logging utility for the game.
 * Uses markers to categorize different types of log messages.
 */
public class GameLogger {
    private static final Logger logger = LogManager.getLogger(GameLogger.class);

    // Markers for different log categories
    public static final Marker STATE = MarkerManager.getMarker("STATE");
    public static final Marker DECORATOR = MarkerManager.getMarker("DECORATOR");
    public static final Marker FACTORY = MarkerManager.getMarker("FACTORY");
    public static final Marker OBSERVER = MarkerManager.getMarker("OBSERVER");
    public static final Marker AUTH = MarkerManager.getMarker("AUTH");
    public static final Marker LEVEL = MarkerManager.getMarker("LEVEL");
    public static final Marker PLAYER = MarkerManager.getMarker("PLAYER");
    public static final Marker ERROR = MarkerManager.getMarker("ERROR");

    /**
     * Log a game state transition
     */
    public static void logStateChange(String entity, String fromState, String toState) {
        logger.info(STATE, "{}: {} → {}", entity, fromState, toState);
    }

    /**
     * Log a decorator (power-up) being applied
     */
    public static void logDecoratorApplied(String decoratorName, String target) {
        logger.info(DECORATOR, "{} applied to {}", decoratorName, target);
    }

    /**
     * Log a decorator (power-up) being removed
     */
    public static void logDecoratorRemoved(String decoratorName, String target) {
        logger.info(DECORATOR, "{} removed from {}", decoratorName, target);
    }

    /**
     * Log factory creation
     */
    public static void logFactoryCreation(String factoryType, String objectType) {
        logger.debug(FACTORY, "{} created: {}", factoryType, objectType);
    }

    /**
     * Log observer notification
     */
    public static void logObserverNotification(String event, Object data) {
        logger.debug(OBSERVER, "Event: {} - Data: {}", event, data);
    }

    /**
     * Log authentication events
     */
    public static void logAuth(String action, String username) {
        logger.info(AUTH, "{}: {}", action, username);
    }

    /**
     * Log level events
     */
    public static void logLevel(String action, String levelInfo) {
        logger.info(LEVEL, "{}: {}", action, levelInfo);
    }

    /**
     * Log player events
     */
    public static void logPlayer(String action, String details) {
        logger.info(PLAYER, "{}: {}", action, details);
    }

    /**
     * Log errors
     */
    public static void logError(String message, Throwable exception) {
        logger.error(ERROR, message, exception);
    }

    /**
     * Log general info
     */
    public static void info(String message) {
        logger.info(message);
    }

    /**
     * Log debug info
     */
    public static void debug(String message) {
        logger.debug(message);
    }

    // Sound marker
    public static final Marker SOUND = MarkerManager.getMarker("SOUND");

    /**
     * Log sound events
     */
    public static void logSound(String type, String soundName) {
        logger.debug(SOUND, "{}: {}", type, soundName);
    }
}
