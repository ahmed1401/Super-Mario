package com.mario.game.factories;

import com.mario.game.enemies.*;
import com.mario.utils.GameLogger;

/**
 * FACTORY PATTERN - Concrete Factory
 * 
 * Creates enemy objects based on type.
 * Centralizes enemy creation logic and provides flexibility for adding new
 * enemy types.
 */
public class EnemyFactory {

    public enum EnemyType {
        GOOMBA, KOOPA, BOSS
    }

    /**
     * Create an enemy of the specified type
     * 
     * @param type Enemy type
     * @param x    X position
     * @param y    Y position
     * @return Created enemy
     */
    public static Enemy createEnemy(EnemyType type, double x, double y) {
        Enemy enemy;

        switch (type) {
            case GOOMBA:
                enemy = new Goomba(x, y);
                break;
            case KOOPA:
                enemy = new Koopa(x, y);
                break;
            case BOSS:
                enemy = new Boss(x, y);
                break;
            default:
                throw new IllegalArgumentException("Unknown enemy type: " + type);
        }

        GameLogger.logFactoryCreation("EnemyFactory", type.name());
        return enemy;
    }

    /**
     * Create an enemy from string type name
     * 
     * @param typeName Enemy type name
     * @param x        X position
     * @param y        Y position
     * @return Created enemy
     */
    public static Enemy createEnemy(String typeName, double x, double y) {
        EnemyType type = EnemyType.valueOf(typeName.toUpperCase());
        return createEnemy(type, x, y);
    }

    /**
     * Create a random enemy
     * 
     * @param x X position
     * @param y Y position
     * @return Created enemy
     */
    public static Enemy createRandomEnemy(double x, double y) {
        EnemyType[] types = { EnemyType.GOOMBA, EnemyType.KOOPA };
        int index = (int) (Math.random() * types.length);
        return createEnemy(types[index], x, y);
    }
}
