# Arkanoid Game – Object-Oriented Programming Project

## Author
**Group 6 – Class INT2204 5**
1. Trương Bá Hải Nam – 24020248
2. Ngô Minh Triết – 24020329
3. Đinh Văn Tiến – 24020320
4. Trần Quang Trường – 24020338

**Instructor:** Nguyễn Thu Trang  
**Semester:** HK1 – 2025

---

![Class Diagram](Arkanoid/src/main/resources/classDiagram.png)

## Description
This is a modern remake of the classic **Arkanoid** game built with **JavaFX**.
It demonstrates object-oriented design and the application of multiple design patterns.

### Key Features
1. Built with **Java 25** and **JavaFX**.
2. Implements core OOP principles: *Encapsulation, Inheritance, Polymorphism, Abstraction*.
3. Applies patterns: **Singleton**, **Factory Method**, **Command**, **Observer**, **State**.
4. Power-up system created via **Factory Pattern**.
5. Supports both **Single Player** and **Two Player Mode**.
6. Integrated **sound effects** and **background music** (SoundManager).
7. Includes **fade transitions**, **collision effects**, and dynamic UI.
8. Uses a **State Machine** to manage game flow.
9. Smooth gameplay with delta-time–based game loop.

---

## Game Mechanics
- Move the **Paddle** to bounce the ball and break bricks.
- Catch **Power-Ups** to activate temporary abilities.
- Each level layout is read from text files in `/map/`.
- In Two Player mode, both players progress independently.
- Lose when all lives are gone; win by clearing all 5 levels.

---

## Design Patterns Used

| Pattern | Class / Module | Purpose |
|----------|----------------|----------|
| **Singleton** | `Paddle`, `EventLoader` | Maintain single instance |
| **Factory Method** | `GameplayModel.spawnPowerUp()` | Create random Power-Up objects |
| **Command** | `ChangeStateCmd`, `Button.activate()` | Encapsulate actions triggered by buttons |
| **Observer** | `GameEventListener`, `EventLoader` | Notify controllers and SoundManager of events |
| **State** | `GameModel.State` | Manage Menu, Fade, Playing, Loss, Victory |

---

## Architecture Overview

| Layer | Responsibility |
|-------|----------------|
| **Model** | Game logic and entities (Ball, Paddle, Brick, PowerUp...) |
| **View** | Rendering visuals, UI, and effects |
| **Controller** | Handles input, transitions, and synchronization |

### Multithreading
| Thread | Description |
|---------|-------------|
| **Game UI** | Process UI in JavaFx Application Thread |
| **Game Loop Timer** | Updates logic every frame (≈60 FPS) |
| **Audio Thread** | Plays SFX asynchronously |

---

## Controls

| Key | Action |
|-----|--------|
| `A` / `←` | Move left |
| `D` / `→` | Move right |
| `SPACE` | Launch ball / Shoot laser |
| `ENTER` | Launch ball (Player 2) |
| `ESC` | Pause |

---

## Power-Up System

| Power-Up | Effect | Duration |
|-----------|---------|----------|
| **Expand Paddle** | Increases paddle width by 1.5× | 5s       |
| **Multi Ball** | Spawns extra balls | –        |
| **Laser Gun** | Shoot lasers from paddle sides | 5s       |
| **Extra Life** | +1 life | –        |
| **Shield** | Barrier under paddle | 10s      |
| **Bomb Ball** | Explodes nearby bricks | 6s       |
| **Score ×2** | Doubles points | 10s      |

---

## Audio System

- **Menu BGM:** `MainTheme.mp3`
- **Gameplay BGM:** `GameTheme.mp3` (auto pauses/resumes when pausing the game)
- **SFX Events:**
    - `hit.wav` — Ball hits
    - `win.wav` — Victory
    - `lose.wav` — Game Over
    - `powerup.wav` — Power-up collected
    - `balllost.wav` — Ball lost
    - `finishlevel.wav` — Level complete

Volume adjustable in **Settings** (step ±0.2).

---

## Scoring

| Action | Score |
|---------|--------|
| Strong brick | +300 |
| Consecutive hits | Combo ×2, ×3, ×4... |
|Laser| Provides Scorepoints but does not contribute to Combo multiplier |

---

---

## Multiplayer Mode

The game includes a complete **Two Player Mode** rendered in split-screen format.

| Feature | Description |
|----------|--------------|
| **Independent Gameplay** | Each player has their own `GameplayModel`, ball, paddle, score, and power-ups. |
| **Split-Screen Rendering** | The screen is divided into left and right halves, each drawn separately in `TwoPlayerView`. |
| **Separate Controls** | Player 1 uses `A`, `D`, `SPACE`; Player 2 uses `←`, `→`, `ENTER`. |
| **Independent Level Flow** | Each player advances to the next level individually after finishing their side. |
| **Shared End Condition** | The match ends only when both players complete all levels or one loses all lives. |

---

## Custom Textures System

Players can customize visual assets for **paddle**, **ball**, and **background**.

| Element | Description          | 
|----------|----------------------|
| **Paddle Skin** | Selected in Settings | 
| **Ball Skin** | Selected in Settings | 
| **Backgrounds** | Selected in Settings | 


## Future Improvements

1. **Game modes:** Time Attack, Endless, Co-op.
2. **Gameplay:** Boss battles, new power-ups, achievement system.
3. **Technical:** LibGDX migration, particle effects, AI paddle, online leaderboard.

---

## Technologies Used

| Technology | Version | Purpose |
|-------------|-----|----------|
| Java | 25 | Core language |
| JavaFX | 21.0+ | GUI & rendering |
| Maven | 3.9+ | Dependency management |
| OpenJFX Media | 21+ | Audio system |
| Git / GitHub | – | Version control |

---

## Installation

- Download the source files
- Unzip
- Run using your IDE (apply the maven javafx:run)
