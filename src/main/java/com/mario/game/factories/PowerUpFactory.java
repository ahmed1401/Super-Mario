package com.mario.game.factories;

import com.mario.game.player.PlayerComponent;
import com.mario.game.powerups.*;
import com.mario.utils.GameLogger;

/**
 * FACTORY PATTERN - Concrete Factory
 * 
 * Creates power-up decorators based on type.
 * Centralizes power-up creation logic.
 */
public class PowerUpFactory {

    public enum PowerUpType {
        MUSHROOM, FIRE_FLOWER, STAR, SPEED_BOOST, SHIELD
    }

    /**
     * Create a power-up decorator of the specified type
     * 
     * @param type      Power-up type
     * @param component Player component to wrap
     * @return Created power-up decorator
     */
    public static PowerUpDecorator createPowerUp(PowerUpType type, PlayerComponent component) {
        PowerUpDecorator decorator;

        switch (type) {
            case MUSHROOM:
                decorator = new MushroomDecorator(component);
                break;
            case FIRE_FLOWER:
                decorator = new FireFlowerDecorator(component);
                break;
            case STAR:
                decorator = new StarDecorator(component);
                break;
            case SPEED_BOOST:
                decorator = new SpeedBoostDecorator(component);
                break;
            case SHIELD:
                decorator = new ShieldDecorator(component);
                break;
            default:
                throw new IllegalArgumentException("Unknown power-up type: " + type);
        }

        GameLogger.logFactoryCreation("PowerUpFactory", type.name());
        return decorator;
    }

    /**
     * Create power-up from string type name
     */
    public static PowerUpDecorator createPowerUp(String typeName, PlayerComponent component) {
        PowerUpType type = parseType(typeName);
        return createPowerUp(type, component);
    }

    /**
     * Create a timed power-up with custom duration
     */
    public static PowerUpDecorator createTimedPowerUp(PowerUpType type, PlayerComponent component, double duration) {
        switch (type) {
            case STAR:
                return new StarDecorator(component, duration);
            case SPEED_BOOST:
                return new SpeedBoostDecorator(component, duration);
            default:
                return createPowerUp(type, component);
        }
    }

    /**
     * Overload for string input with friendlier errors.
     */
    public static PowerUpDecorator createTimedPowerUp(String typeName, PlayerComponent component, double duration) {
        PowerUpType type = parseType(typeName);
        return createTimedPowerUp(type, component, duration);
    }

    /**
     * Check if a string maps to a supported power-up.
     */
    public static boolean isSupported(String typeName) {
        if (typeName == null) {
            return false;
        }
        try {
            parseType(typeName);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    /**
     * Validate and parse a power-up type name.
     */
    public static PowerUpType parseType(String typeName) {
        if (typeName == null) {
            throw new IllegalArgumentException("Power-up type cannot be null");
        }
        String normalized = typeName.trim().toUpperCase();
        try {
            return PowerUpType.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Unknown power-up type: " + typeName + ". Supported: "
                    + String.join(", ", listAvailableTypes()), ex);
        }
    }

    /**
     * List available power-up identifiers for UI/help.
     */
    public static String[] listAvailableTypes() {
        PowerUpType[] values = PowerUpType.values();
        String[] names = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            names[i] = values[i].name();
        }
        return names;
    }
}
