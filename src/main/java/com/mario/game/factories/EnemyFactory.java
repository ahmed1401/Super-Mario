package com.mario.game.factories;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
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

    private static final String CONFIG_PATH = "data/enemies.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static volatile Map<EnemyType, EnemyConfig> ENEMY_CONFIGS = loadConfigs();

    static {
        logConfigSummary(ENEMY_CONFIGS);
    }

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

        applyConfig(type, enemy);
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

    /**
     * Reload enemy configs from disk. Does not change logic unless callers invoke it.
     */
    public static void reloadConfigs() {
        ENEMY_CONFIGS = loadConfigs();
        logConfigSummary(ENEMY_CONFIGS);
    }

    /**
     * Describe config for diagnostics without exposing mutable state.
     */
    public static String describeConfig(EnemyType type) {
        EnemyConfig config = ENEMY_CONFIGS.get(type);
        return describe(type, config).replace('=', ' ').replace('(', ' ').replace(')', ' ');
    }

    private static void applyConfig(EnemyType type, Enemy enemy) {
        EnemyConfig config = ENEMY_CONFIGS.get(type);
        if (config == null) {
            return;
        }

        if (enemy instanceof Boss boss) {
            boss.setMaxHealth(config.health);
        }

        enemy.applyStats(config.health, config.damage, config.scoreValue, config.speed);
    }

    private static Map<EnemyType, EnemyConfig> loadConfigs() {
        Map<EnemyType, EnemyConfig> defaults = defaultConfigs();
        Path path = Paths.get(CONFIG_PATH);

        if (!Files.exists(path)) {
            return defaults;
        }

        try {
            String json = Files.readString(path);
            Type type = new TypeToken<Map<String, EnemyConfig>>() {
            }.getType();
            Map<String, EnemyConfig> fileConfigs = GSON.fromJson(json, type);

            if (fileConfigs == null) {
                return defaults;
            }

            for (Map.Entry<String, EnemyConfig> entry : fileConfigs.entrySet()) {
                try {
                    EnemyType enemyType = EnemyType.valueOf(entry.getKey().toUpperCase());
                    EnemyConfig normalized = entry.getValue() == null ? null : entry.getValue().normalize();
                    if (normalized != null) {
                        defaults.put(enemyType, normalized);
                    }
                } catch (IllegalArgumentException e) {
                    GameLogger.logError("Unknown enemy type in enemy config: " + entry.getKey(), e);
                }
            }
        } catch (IOException | RuntimeException e) {
            GameLogger.logError("Failed to load enemy configs, using defaults", e);
        }

        return defaults;
    }

    private static void logConfigSummary(Map<EnemyType, EnemyConfig> configs) {
        StringBuilder sb = new StringBuilder("Enemy configs loaded: ");
        boolean first = true;
        for (Map.Entry<EnemyType, EnemyConfig> entry : configs.entrySet()) {
            if (!first) {
                sb.append(" | ");
            }
            sb.append(describe(entry.getKey(), entry.getValue()));
            first = false;
        }
        GameLogger.info(sb.toString());
    }

    private static String describe(EnemyType type, EnemyConfig config) {
        if (config == null) {
            return type.name() + "=<missing>";
        }
        return type.name() + "(h=" + config.health + ",d=" + config.damage + ",s=" + config.scoreValue + ",v="
                + config.speed + ")";
    }

    private static Map<EnemyType, EnemyConfig> defaultConfigs() {
        Map<EnemyType, EnemyConfig> defaults = new EnumMap<>(EnemyType.class);
        defaults.put(EnemyType.GOOMBA, new EnemyConfig(1, 1, 75, 35));
        defaults.put(EnemyType.KOOPA, new EnemyConfig(3, 1, 150, 28));
        defaults.put(EnemyType.BOSS, new EnemyConfig(8, 3, 1500, 22));
        return defaults;
    }

    private static class EnemyConfig {
        int health;
        int damage;
        int scoreValue;
        double speed;

        EnemyConfig() {
        }

        EnemyConfig(int health, int damage, int scoreValue, double speed) {
            this.health = health;
            this.damage = damage;
            this.scoreValue = scoreValue;
            this.speed = speed;
        }

        EnemyConfig normalize() {
            int normHealth = Math.max(1, health);
            int normDamage = Math.max(0, damage);
            int normScore = Math.max(0, scoreValue);
            double normSpeed = speed <= 0 ? 1.0 : speed;
            return new EnemyConfig(normHealth, normDamage, normScore, normSpeed);
        }
    }
}
