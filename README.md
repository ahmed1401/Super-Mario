# 🎮 Super Mario - Design Patterns Demo

A fully-featured 2D platformer game inspired by Super Mario, built in **Java with JavaFX** for educational purposes. Demonstrates **5 major Design Patterns** with clean architecture.

![Java](https://img.shields.io/badge/Java-17+-orange)
![JavaFX](https://img.shields.io/badge/JavaFX-21-blue)
![Maven](https://img.shields.io/badge/Maven-3.9+-red)
![Patterns](https://img.shields.io/badge/Design%20Patterns-5-green)

---

## 🎯 Quick Start

```bash
# Clone and run
cd SuperMario
mvn javafx:run
```

---

## ✨ Features

| Category | Details |
|----------|---------|
| **Levels** | 3 worlds (Grassland → Underground → Castle Boss) |
| **Power-ups** | Mushroom, Fire Flower, Star, Speed Boost, Shield |
| **Enemies** | Goomba, Koopa (shell), Boss |
| **Systems** | Login, Register, Leaderboard, Save Progress |
| **UI** | Mouse support, Fullscreen (F11), Professional styling |

---

## 🧩 Design Patterns

| Pattern | Location | Purpose |
|---------|----------|---------|
| **State** | `game/states/` | 9 game states with logged transitions |
| **Decorator** | `game/powerups/` | 5 stackable power-ups |
| **Composite** | `game/levels/` | World → Level → Components hierarchy |
| **Factory** | `game/factories/` | Enemy, PowerUp, Level creation |
| **Observer** | `game/observers/` | HUD updates on game events |

---

## 🎮 Controls

| Key | Action |
|-----|--------|
| **← → / A D** | Move |
| **↑ W Space** | Jump |
| **ESC / P** | Pause |
| **F11** | Fullscreen |
| **Mouse** | Click buttons |
| **Tab** | Switch login fields |

---

## 📁 Project Structure

```
src/main/java/com/mario/
├── Main.java           # Entry point
├── auth/               # Authentication
├── game/
│   ├── states/         # STATE PATTERN (9 states)
│   ├── player/         # Player + DECORATOR base
│   ├── powerups/       # DECORATOR PATTERN (5 power-ups)
│   ├── enemies/        # Enemy types
│   ├── levels/         # COMPOSITE PATTERN
│   ├── factories/      # FACTORY PATTERN
│   └── observers/      # OBSERVER PATTERN
└── utils/
    ├── GameLogger.java # Logging utility
    └── SoundManager.java
```

---

## 📊 Sample Log Output

```
[STATE] Game: LOGIN → MENU
[AUTH] Login successful: player1
[DECORATOR] Mushroom applied to Player
[PLAYER] Enemy defeated: Goomba
[OBSERVER] Event: Coin collected - Data: 5
[STATE] Game: PLAYING → VICTORY
```

---

## 📚 Documentation

- **[submission_report.md](submission_report.md)** - Comprehensive project documentation with UML diagrams
- **[complete_class_diagram.md](complete_class_diagram.md)** - Complete class diagram of the project

---

## 🔧 Build Commands

```bash
mvn clean compile    # Compile
mvn javafx:run       # Run game
mvn package          # Create JAR
```

---

