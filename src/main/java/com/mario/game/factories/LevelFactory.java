package com.mario.game.factories;

import com.mario.game.enemies.Enemy;
import com.mario.game.levels.*;
import com.mario.utils.GameLogger;

/**
 * FACTORY PATTERN - Level Factory
 * 
 * Creates pre-designed levels with enemies, platforms, coins, and power-ups.
 */
public class LevelFactory {

    private static final int TOTAL_LEVELS = 3;

    /**
     * Create Level 1 - Grassland (Tutorial level)
     */
    public static Level createLevel1() {
        Level level = new Level("World 1-1: Grassland", 1, 3000, 600);
        level.setPlayerStart(50, 400);
        level.setGoalX(2850);

        // Ground platforms
        level.addPlatform(new Platform(0, 500, 800, 100, "ground"));
        level.addPlatform(new Platform(900, 500, 500, 100, "ground"));
        level.addPlatform(new Platform(1500, 500, 800, 100, "ground"));
        level.addPlatform(new Platform(2400, 500, 600, 100, "ground"));

        // Floating platforms
        level.addPlatform(new Platform(300, 380, 100, 20, "brick"));
        level.addPlatform(new Platform(500, 320, 100, 20, "question"));
        level.addPlatform(new Platform(700, 280, 100, 20, "brick"));
        level.addPlatform(new Platform(1100, 350, 150, 20, "brick"));
        level.addPlatform(new Platform(1600, 380, 100, 20, "question"));
        level.addPlatform(new Platform(1900, 300, 200, 20, "brick"));
        level.addPlatform(new Platform(2200, 350, 100, 20, "question"));

        // Pipes
        level.addPlatform(new Platform(600, 440, 60, 60, "pipe"));
        level.addPlatform(new Platform(1300, 420, 60, 80, "pipe"));
        level.addPlatform(new Platform(2100, 400, 60, 100, "pipe"));

        // Coins
        level.addCoin(new Coin(320, 350));
        level.addCoin(new Coin(360, 350));
        level.addCoin(new Coin(400, 350));
        level.addCoin(new Coin(520, 290));
        level.addCoin(new Coin(560, 290));
        level.addCoin(new Coin(1150, 320));
        level.addCoin(new Coin(1190, 320));
        level.addCoin(new Coin(1950, 270));
        level.addCoin(new Coin(2000, 270));
        level.addCoin(new Coin(2050, 270));

        // Power-ups
        level.addPowerUpItem(new PowerUpItem(530, 290, PowerUpItem.PowerUpType.MUSHROOM));
        level.addPowerUpItem(new PowerUpItem(1630, 350, PowerUpItem.PowerUpType.FIRE_FLOWER));
        level.addPowerUpItem(new PowerUpItem(2230, 320, PowerUpItem.PowerUpType.STAR));

        // Enemies
        level.addEnemy(EnemyFactory.createEnemy(EnemyFactory.EnemyType.GOOMBA, 400, 460));
        level.addEnemy(EnemyFactory.createEnemy(EnemyFactory.EnemyType.GOOMBA, 800, 460));
        level.addEnemy(EnemyFactory.createEnemy(EnemyFactory.EnemyType.KOOPA, 1200, 450));
        level.addEnemy(EnemyFactory.createEnemy(EnemyFactory.EnemyType.GOOMBA, 1700, 460));
        level.addEnemy(EnemyFactory.createEnemy(EnemyFactory.EnemyType.GOOMBA, 2300, 460));

        // Checkpoint
        level.addCheckpoint(new Checkpoint(1400, 436));

        GameLogger.logFactoryCreation("LevelFactory", "Level 1");
        return level;
    }

    /**
     * Create Level 2 - Underground
     */
    public static Level createLevel2() {
        Level level = new Level("World 1-2: Underground", 2, 3500, 600);
        level.setPlayerStart(50, 400);
        level.setGoalX(3350);

        // Ground
        level.addPlatform(new Platform(0, 500, 600, 100, "ground"));
        level.addPlatform(new Platform(700, 500, 500, 100, "ground"));
        level.addPlatform(new Platform(1300, 500, 400, 100, "ground"));
        level.addPlatform(new Platform(1800, 500, 600, 100, "ground"));
        level.addPlatform(new Platform(2500, 500, 500, 100, "ground"));
        level.addPlatform(new Platform(3100, 500, 400, 100, "ground"));

        // Elevated platforms
        level.addPlatform(new Platform(200, 350, 150, 20, "brick"));
        level.addPlatform(new Platform(400, 280, 100, 20, "brick"));
        level.addPlatform(new Platform(800, 380, 200, 20, "brick"));
        level.addPlatform(new Platform(1100, 300, 100, 20, "question"));
        level.addPlatform(new Platform(1400, 350, 150, 20, "brick"));
        level.addPlatform(new Platform(1900, 320, 300, 20, "brick"));
        level.addPlatform(new Platform(2300, 380, 100, 20, "question"));
        level.addPlatform(new Platform(2600, 300, 200, 20, "brick"));

        // Pipes
        level.addPlatform(new Platform(500, 400, 60, 100, "pipe"));
        level.addPlatform(new Platform(1600, 420, 60, 80, "pipe"));
        level.addPlatform(new Platform(2800, 380, 60, 120, "pipe"));

        // Coins
        for (int i = 0; i < 5; i++) {
            level.addCoin(new Coin(220 + i * 25, 320));
        }
        for (int i = 0; i < 4; i++) {
            level.addCoin(new Coin(1920 + i * 30, 290));
        }

        // Power-ups
        level.addPowerUpItem(new PowerUpItem(1130, 270, PowerUpItem.PowerUpType.MUSHROOM));
        level.addPowerUpItem(new PowerUpItem(2330, 350, PowerUpItem.PowerUpType.SPEED_BOOST));

        // Enemies
        level.addEnemy(EnemyFactory.createEnemy(EnemyFactory.EnemyType.GOOMBA, 350, 460));
        level.addEnemy(EnemyFactory.createEnemy(EnemyFactory.EnemyType.KOOPA, 900, 450));
        level.addEnemy(EnemyFactory.createEnemy(EnemyFactory.EnemyType.GOOMBA, 1200, 460));
        level.addEnemy(EnemyFactory.createEnemy(EnemyFactory.EnemyType.KOOPA, 2000, 450));
        level.addEnemy(EnemyFactory.createEnemy(EnemyFactory.EnemyType.GOOMBA, 2400, 460));
        level.addEnemy(EnemyFactory.createEnemy(EnemyFactory.EnemyType.GOOMBA, 2700, 460));

        // Checkpoint
        level.addCheckpoint(new Checkpoint(1700, 436));

        GameLogger.logFactoryCreation("LevelFactory", "Level 2");
        return level;
    }

    /**
     * Create Level 3 - Castle (Boss level)
     */
    public static Level createLevel3() {
        Level level = new Level("World 1-3: Castle", 3, 2500, 600);
        level.setPlayerStart(50, 400);
        level.setGoalX(2350);

        // Ground
        level.addPlatform(new Platform(0, 500, 500, 100, "ground"));
        level.addPlatform(new Platform(600, 500, 600, 100, "ground"));
        level.addPlatform(new Platform(1300, 500, 500, 100, "ground"));
        level.addPlatform(new Platform(1900, 500, 600, 100, "ground"));

        // Platforms
        level.addPlatform(new Platform(250, 380, 100, 20, "brick"));
        level.addPlatform(new Platform(400, 300, 100, 20, "question"));
        level.addPlatform(new Platform(700, 350, 200, 20, "brick"));
        level.addPlatform(new Platform(1000, 280, 150, 20, "brick"));
        level.addPlatform(new Platform(1400, 350, 100, 20, "question"));
        level.addPlatform(new Platform(1600, 300, 100, 20, "brick"));

        // Coins
        for (int i = 0; i < 3; i++) {
            level.addCoin(new Coin(720 + i * 30, 320));
        }

        // Power-ups for boss fight
        level.addPowerUpItem(new PowerUpItem(430, 270, PowerUpItem.PowerUpType.MUSHROOM));
        level.addPowerUpItem(new PowerUpItem(1430, 320, PowerUpItem.PowerUpType.FIRE_FLOWER));
        level.addPowerUpItem(new PowerUpItem(1700, 460, PowerUpItem.PowerUpType.STAR));

        // Enemies leading to boss
        level.addEnemy(EnemyFactory.createEnemy(EnemyFactory.EnemyType.GOOMBA, 300, 460));
        level.addEnemy(EnemyFactory.createEnemy(EnemyFactory.EnemyType.KOOPA, 800, 450));
        level.addEnemy(EnemyFactory.createEnemy(EnemyFactory.EnemyType.GOOMBA, 1100, 460));

        // BOSS
        level.addEnemy(EnemyFactory.createEnemy(EnemyFactory.EnemyType.BOSS, 2000, 400));

        // Checkpoint before boss
        level.addCheckpoint(new Checkpoint(1800, 436));

        GameLogger.logFactoryCreation("LevelFactory", "Level 3 (Boss)");
        return level;
    }

    /**
     * Create a world with all levels
     */
    public static World createWorld1() {
        World world = new World("Super Mario World 1");
        world.addLevel(createLevel1());
        world.addLevel(createLevel2());
        world.addLevel(createLevel3());
        return world;
    }

    /**
     * Utility to build a level by number while preserving existing creation logic.
     */
    public static Level createLevelByNumber(int levelNumber) {
        switch (levelNumber) {
            case 1:
                return createLevel1();
            case 2:
                return createLevel2();
            case 3:
                return createLevel3();
            default:
                throw new IllegalArgumentException("Unknown level number: " + levelNumber);
        }
    }

    /**
     * Total count of predefined levels.
     */
    public static int getTotalLevels() {
        return TOTAL_LEVELS;
    }

    /**
     * Helper to list predefined level names for UI/logging.
     */
    public static String[] listLevelNames() {
        return new String[] {
                "World 1-1: Grassland",
                "World 1-2: Underground",
                "World 1-3: Castle"
        };
    }
}
