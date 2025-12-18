# Mini Block Dash

A Geometry Dash-inspired 2D platformer game built with Java Swing, featuring neon visuals, particle effects, and rhythm-based gameplay.

## Project Overview

Mini Block Dash is an Object-Oriented Programming (OOP) project that demonstrates key Java concepts including inheritance, polymorphism, encapsulation, and abstraction through a fun and challenging platformer game.

## Features

- **Neon Visual Style**: Glowing effects, pulsing colors, and particle systems
- **Multiple Obstacle Types**: Spikes, blocks, rotating saws, jump pads, and gravity portals
- **Gravity Mechanics**: Flip gravity to walk on ceilings and navigate inverted sections
- **Safe Platforms**: Green platforms to land on and avoid danger
- **Progressive Difficulty**: Speed increases as you progress through the level
- **Theme Color Changes**: Background colors shift through 6 phases (Cyan → Green → Magenta → Orange → Purple → Red/Gold)
- **Testing Mode**: Practice the level with invincibility enabled
- **Settings Menu**: Customize music volume, particles, player color, and FPS display
- **Victory Screen**: Reach 100% to see the victory image and complete the game

## OOP Structure

### Class Hierarchy

```
Obstacle (Abstract Base Class)
├── Block
├── TallBlock
├── Spike
├── DoubleSpike
├── CeilingBlock
├── CeilingSpike
├── RotatingSaw
├── JumpPad
├── SafePlatform
├── GravityPortal
└── EndMarker
```

### Key Classes

| Class | Description |
|-------|-------------|
| `Main` | Entry point, creates the game window |
| `GamePanel` | Main game loop, rendering, input handling |
| `Player` | Player cube with physics, jumping, gravity |
| `Obstacle` | Abstract base class for all obstacles |
| `ObstacleManager` | Manages obstacle updates and collision detection |
| `LevelData` | Hand-crafted level design with 6 phases |
| `ParticleSystem` | Visual particle effects (trails, bursts, jumps) |
| `AudioManager` | Background music playback and volume control |
| `GameSettings` | User preferences (volume, colors, etc.) |
| `NeonColors` | Color utilities for glow and pulse effects |
| `GameState` | Enum for game states (MENU, PLAYING, etc.) |

## Controls

| Key | Action |
|-----|--------|
| SPACE | Jump / Start Game |
| T | Start Testing Mode (from menu) |
| S | Open Settings (from menu) |
| ESC | Return to Menu |
| R | Restart (after game over or victory) |
| ↑/↓ | Navigate settings |
| ←/→ | Change setting values |

## How to Run

### Prerequisites
- Java JDK 11 or higher installed
- Command line access

### Compile and Run

1. Open a terminal/command prompt

2. Navigate to the project directory:
   ```
   cd MiniBlockDash/MiniBlockDash
   ```

3. Compile all Java files:
   ```
   javac -d out src/com/miniblockdash/*.java
   ```

4. Copy resources (images, music) to output folder:
   ```
   # Windows (PowerShell)
   Copy-Item -Path "src/com/miniblockdash/resources" -Destination "out/com/miniblockdash/resources" -Recurse -Force
   
   # Windows (CMD)
   xcopy /E /I /Y src\com\miniblockdash\resources out\com\miniblockdash\resources
   
   # Linux/Mac
   cp -r src/com/miniblockdash/resources out/com/miniblockdash/resources
   ```

5. Run the game:
   ```
   java -cp out com.miniblockdash.Main
   ```

## Game Mechanics

### Obstacles
- **Spike/DoubleSpike**: Triangular hazards on the ground
- **Block/TallBlock**: Rectangular obstacles to jump over
- **CeilingBlock/CeilingSpike**: Hazards on the ceiling (encountered when gravity is inverted)
- **RotatingSaw**: Spinning circular hazard
- **JumpPad**: Yellow pad that launches player higher
- **SafePlatform**: Green platform safe to land on
- **GravityPortal**: Purple portal flips gravity up, Cyan portal returns to normal

### Progress System
- Progress bar shows 0-100% completion
- Reach the EndMarker (Victory Image) to complete the level
- Score increases continuously during gameplay

## Resources

- `background.png` / `background.gif` - Background images
- `music.wav` - Background music
- `VictoryIMG.jpg` - Victory screen image at finish line

## Development Notes

This project was developed iteratively with the following major features added:
1. Basic game loop and player movement
2. Neon visual overhaul with particle effects
3. Multiple obstacle types with inheritance
4. Gravity portal mechanics
5. Testing mode for level practice
6. Settings menu with customization options
7. Fixed level design with progressive difficulty
8. Victory condition and end screen

## License

Educational project for learning Object-Oriented Programming in Java.
