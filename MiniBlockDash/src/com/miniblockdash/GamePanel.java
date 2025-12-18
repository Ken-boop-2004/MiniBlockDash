package com.miniblockdash;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    Timer timer;
    Player player;
    ObstacleManager obstacleManager;
    AudioManager audioManager;
    ParticleSystem particles;
    GameSettings settings;

    GameState state = GameState.MENU;
    private int settingsSelection = 0;
    private static final int SETTINGS_COUNT = 4;
    private long lastFrameTime = System.nanoTime();
    private int fps = 0;
    private int frameCount = 0;
    private long fpsTimer = 0;
    private int levelCompleteTimer = 0;

    // Level progress (100% = win)
    private static final int LEVEL_LENGTH = 10000; // Score needed for 100%

    int score = 0;
    int gameSpeed = 5;
    int portalTimer = 0;
    int baseSpeed = 5;
    int portalBoost = 0;
    boolean invertedGravity = false;

    // Testing mode
    boolean testingMode = false;
    int collisionFlashTimer = 0;

    // Visual effects
    private double beatPulse = 0;
    private double backgroundScroll = 0;
    private int groundLineOffset = 0;
    private double menuPulse = 0;
    private int deathFlashTimer = 0;

    // Theme colors based on progress
    private final Color[][] themeColors = {
        // Phase 1: Cyan (0-20%)
        {new Color(5, 15, 30), new Color(0, 40, 50)},
        // Phase 2: Green (20-40%)
        {new Color(5, 25, 15), new Color(0, 50, 30)},
        // Phase 3: Magenta (40-55%)
        {new Color(25, 5, 30), new Color(50, 0, 50)},
        // Phase 4: Orange (55-70%)
        {new Color(30, 15, 5), new Color(50, 30, 0)},
        // Phase 5: Purple (70-85%)
        {new Color(20, 5, 35), new Color(40, 0, 60)},
        // Phase 6: Red/Gold Finale (85-100%)
        {new Color(35, 10, 10), new Color(50, 20, 0)}
    };
    private final Color[] groundColors = {
        NeonColors.CYAN,    // Phase 1
        NeonColors.GREEN,   // Phase 2
        NeonColors.MAGENTA, // Phase 3
        NeonColors.ORANGE,  // Phase 4
        NeonColors.PURPLE,  // Phase 5
        NeonColors.YELLOW   // Phase 6 - Finale
    };
    private int currentTheme = 0;

    public GamePanel() {
        setPreferredSize(new Dimension(800, 400));
        setBackground(Color.BLACK);

        settings = new GameSettings();
        player = new Player();
        player.setColor(settings.getPlayerColor());
        obstacleManager = new ObstacleManager();
        particles = new ParticleSystem();

        audioManager = new AudioManager();
        audioManager.loadBackgroundMusic("/com/miniblockdash/resources/music.wav");
        audioManager.setVolume(settings.musicVolume);

        timer = new Timer(12, this);
        timer.start();

        addKeyListener(this);
        setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw neon background
        drawNeonBackground(g2);

        if (state == GameState.MENU) {
            drawMenu(g2);
        } else if (state == GameState.SETTINGS) {
            drawSettings(g2);
        } else if (state == GameState.PLAYING) {
            drawGame(g2);
            if (testingMode) {
                drawTestingModeOverlay(g2);
            }
            if (settings.showFPS) {
                drawFPS(g2);
            }
        } else if (state == GameState.GAME_OVER) {
            drawGame(g2);
            drawGameOver(g2);
        } else if (state == GameState.LEVEL_COMPLETE) {
            drawGame(g2);
            drawLevelComplete(g2);
        }
    }

    private void drawNeonBackground(Graphics2D g2) {
        // Update theme based on progress
        if (state == GameState.PLAYING) {
            int progress = obstacleManager.getProgress();
            if (progress < 20) currentTheme = 0;
            else if (progress < 40) currentTheme = 1;
            else if (progress < 55) currentTheme = 2;
            else if (progress < 70) currentTheme = 3;
            else if (progress < 85) currentTheme = 4;
            else currentTheme = 5;
        }

        // Gradient background based on current theme
        Color bgTop = themeColors[currentTheme][0];
        Color bgBottom = themeColors[currentTheme][1];

        GradientPaint gradient = new GradientPaint(0, 0, bgTop, 0, getHeight(), bgBottom);
        g2.setPaint(gradient);
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Beat-reactive pulse overlay
        if (beatPulse > 0) {
            int alpha = (int)(beatPulse * 30);
            g2.setColor(new Color(100, 50, 150, alpha));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        // Grid lines (scrolling)
        drawGrid(g2);

        // Ground line with glow
        drawGroundLine(g2);
    }

    private void drawGrid(Graphics2D g2) {
        g2.setColor(new Color(50, 30, 80, 40));
        g2.setStroke(new BasicStroke(1));

        // Vertical lines (scrolling)
        int spacing = 80;
        int offset = (int)(backgroundScroll % spacing);
        for (int x = -offset; x < getWidth() + spacing; x += spacing) {
            g2.drawLine(x, 0, x, getHeight());
        }

        // Horizontal lines
        for (int y = 0; y < getHeight(); y += spacing) {
            g2.drawLine(0, y, getWidth(), y);
        }
    }

    private void drawGroundLine(Graphics2D g2) {
        int groundY = 330;
        Color groundColor = groundColors[currentTheme];

        // Glow effect
        for (int i = 3; i >= 0; i--) {
            int alpha = 20 + (3 - i) * 15;
            g2.setColor(new Color(groundColor.getRed(), groundColor.getGreen(), groundColor.getBlue(), alpha));
            g2.setStroke(new BasicStroke(4 + i * 2));
            g2.drawLine(0, groundY + i, getWidth(), groundY + i);
        }

        // Main ground line
        g2.setColor(groundColor);
        g2.setStroke(new BasicStroke(3));
        g2.drawLine(0, groundY, getWidth(), groundY);

        // Moving dashes on ground
        g2.setColor(new Color(groundColor.getRed(), groundColor.getGreen(), groundColor.getBlue(), 150));
        g2.setStroke(new BasicStroke(2));
        int dashSpacing = 60;
        for (int x = -groundLineOffset; x < getWidth() + dashSpacing; x += dashSpacing) {
            g2.drawLine(x, groundY + 5, x + 20, groundY + 5);
        }
    }

    private void drawGame(Graphics2D g2) {
        // Draw particles behind player
        particles.draw(g2);

        // Draw obstacles
        obstacleManager.draw(g2, invertedGravity);

        // Draw player
        player.draw(g2, invertedGravity);

        // Draw score
        drawScore(g2);

        // Death flash effect
        if (deathFlashTimer > 0) {
            int alpha = (int)(deathFlashTimer * 15);
            g2.setColor(new Color(255, 50, 50, Math.min(alpha, 200)));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        // Testing mode collision flash (yellow instead of death)
        if (collisionFlashTimer > 0) {
            int alpha = (int)(collisionFlashTimer * 20);
            g2.setColor(new Color(255, 255, 0, Math.min(alpha, 150)));
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private void drawTestingModeOverlay(Graphics2D g2) {
        double pulse = 0.7 + 0.3 * Math.sin(menuPulse * 2);

        // Testing mode banner at top
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, getWidth(), 35);

        // Glowing border
        g2.setColor(NeonColors.glow(NeonColors.YELLOW, (int)(100 * pulse)));
        g2.setStroke(new BasicStroke(3));
        g2.drawLine(0, 35, getWidth(), 35);

        // Testing mode text
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.setColor(NeonColors.pulse(NeonColors.YELLOW, pulse));
        g2.drawString("TESTING MODE - Invincible", 20, 24);

        // Exit instruction
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        g2.setColor(NeonColors.WHITE);
        g2.drawString("Press ESC to Exit", getWidth() - 130, 24);
    }

    private void drawMenu(Graphics2D g2) {
        double pulse = 0.7 + 0.3 * Math.sin(menuPulse);

        // Title with glow
        g2.setFont(new Font("Arial", Font.BOLD, 48));
        String title = "MINI BLOCK DASH";

        // Glow layers
        for (int i = 4; i >= 0; i--) {
            int alpha = (int)(30 * pulse * (5 - i) / 5);
            g2.setColor(new Color(0, 255, 255, alpha));
            drawCenteredString(g2, title, getWidth()/2, 120 - i, g2.getFont());
        }

        g2.setColor(NeonColors.pulse(NeonColors.CYAN, pulse));
        drawCenteredString(g2, title, getWidth()/2, 120, g2.getFont());

        // Subtitle
        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        g2.setColor(NeonColors.WHITE);
        drawCenteredString(g2, "A Block Dash Gameplay", getWidth()/2, 160, g2.getFont());

        // Instructions with pulsing
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.setColor(NeonColors.pulse(NeonColors.YELLOW, pulse));
        drawCenteredString(g2, "Press SPACE to Start", getWidth()/2, 230, g2.getFont());

        // Testing mode option
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.setColor(NeonColors.pulse(NeonColors.GREEN, pulse));
        drawCenteredString(g2, "Press T for Testing Mode", getWidth()/2, 265, g2.getFont());

        // Settings option
        g2.setColor(NeonColors.pulse(NeonColors.MAGENTA, pulse));
        drawCenteredString(g2, "Press S for Settings", getWidth()/2, 300, g2.getFont());

        // Controls
        g2.setFont(new Font("Arial", Font.PLAIN, 16));
        g2.setColor(new Color(150, 150, 180));
        drawCenteredString(g2, "SPACE = Jump  |  Avoid obstacles  |  Survive!", getWidth()/2, 340, g2.getFont());

        // Decorative cubes
        drawDecorativeCube(g2, 100, 280, pulse, NeonColors.MAGENTA);
        drawDecorativeCube(g2, 700, 280, pulse, NeonColors.GREEN);
        drawDecorativeCube(g2, 150, 180, pulse * 0.8, NeonColors.ORANGE);
        drawDecorativeCube(g2, 650, 180, pulse * 0.8, NeonColors.PINK);
    }

    private void drawDecorativeCube(Graphics2D g2, int x, int y, double pulse, Color color) {
        int size = 20;
        g2.setColor(NeonColors.glow(color, (int)(60 * pulse)));
        g2.fillRect(x - 4, y - 4, size + 8, size + 8);
        g2.setColor(NeonColors.pulse(color, pulse));
        g2.fillRect(x, y, size, size);
        g2.setColor(NeonColors.WHITE);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(x, y, size, size);
    }

    private void drawGameOver(Graphics2D g2) {
        // Dark overlay
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, getWidth(), getHeight());

        double pulse = 0.7 + 0.3 * Math.sin(menuPulse);

        // Game Over text with glow
        g2.setFont(new Font("Arial", Font.BOLD, 48));
        for (int i = 3; i >= 0; i--) {
            g2.setColor(new Color(255, 50, 50, 30 * (4 - i)));
            drawCenteredString(g2, "GAME OVER", getWidth()/2, 140 - i, g2.getFont());
        }
        g2.setColor(NeonColors.RED);
        drawCenteredString(g2, "GAME OVER", getWidth()/2, 140, g2.getFont());

        // Score
        g2.setFont(new Font("Arial", Font.BOLD, 32));
        g2.setColor(NeonColors.CYAN);
        drawCenteredString(g2, "Score: " + score, getWidth()/2, 200, g2.getFont());

        // Restart prompt
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.setColor(NeonColors.pulse(NeonColors.YELLOW, pulse));
        drawCenteredString(g2, "Press R to Restart", getWidth()/2, 250, g2.getFont());

        // Exit to menu prompt
        g2.setColor(NeonColors.pulse(NeonColors.MAGENTA, pulse));
        drawCenteredString(g2, "Press ESC for Menu", getWidth()/2, 290, g2.getFont());
    }

    private void drawScore(Graphics2D g2) {
        // Progress bar at top - use obstacle manager progress
        int progress = obstacleManager.getProgress();
        int barWidth = 300;
        int barHeight = 20;
        int barX = (getWidth() - barWidth) / 2;
        int barY = 10;

        // Bar background
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRoundRect(barX - 5, barY - 5, barWidth + 10, barHeight + 10, 10, 10);

        // Bar outline
        g2.setColor(new Color(80, 80, 100));
        g2.fillRoundRect(barX, barY, barWidth, barHeight, 8, 8);

        // Progress fill with gradient
        int fillWidth = (barWidth * progress) / 100;
        if (fillWidth > 0) {
            GradientPaint progressGradient = new GradientPaint(
                barX, barY, NeonColors.GREEN,
                barX + barWidth, barY, NeonColors.CYAN
            );
            g2.setPaint(progressGradient);
            g2.fillRoundRect(barX, barY, fillWidth, barHeight, 8, 8);
        }

        // Progress text
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.setColor(NeonColors.WHITE);
        String progressText = progress + "%";
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(progressText, barX + (barWidth - fm.stringWidth(progressText)) / 2, barY + 15);

        // Glow effect on progress bar
        g2.setColor(NeonColors.glow(NeonColors.CYAN, 50));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(barX, barY, barWidth, barHeight, 8, 8);

        // Score (bottom left)
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRoundRect(10, 45, 100, 25, 8, 8);
        g2.setColor(NeonColors.WHITE);
        g2.drawString("Score: " + score, 18, 63);

        // ESC hint (top right)
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        g2.setColor(new Color(150, 150, 150));
        g2.drawString("ESC = Menu", getWidth() - 85, 25);
    }

    private void drawCenteredString(Graphics2D g2, String text, int x, int y, Font font) {
        FontMetrics fm = g2.getFontMetrics(font);
        int textX = x - fm.stringWidth(text) / 2;
        g2.drawString(text, textX, y);
    }

    private void drawSettings(Graphics2D g2) {
        double pulse = 0.7 + 0.3 * Math.sin(menuPulse);

        // Title
        g2.setFont(new Font("Arial", Font.BOLD, 40));
        g2.setColor(NeonColors.pulse(NeonColors.CYAN, pulse));
        drawCenteredString(g2, "SETTINGS", getWidth()/2, 80, g2.getFont());

        int startY = 140;
        int spacing = 50;

        // Setting items
        String[] labels = {"Music Volume", "Particles", "Player Color", "Show FPS"};
        String[] values = {
            (int)(settings.musicVolume * 100) + "%",
            settings.particlesEnabled ? "ON" : "OFF",
            settings.getPlayerColorName(),
            settings.showFPS ? "ON" : "OFF"
        };

        for (int i = 0; i < SETTINGS_COUNT; i++) {
            int y = startY + i * spacing;
            boolean selected = (i == settingsSelection);

            // Selection highlight
            if (selected) {
                g2.setColor(new Color(50, 50, 80, 150));
                g2.fillRoundRect(150, y - 25, 500, 40, 10, 10);
                g2.setColor(NeonColors.pulse(NeonColors.CYAN, pulse));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(150, y - 25, 500, 40, 10, 10);
            }

            // Label
            g2.setFont(new Font("Arial", Font.BOLD, 20));
            g2.setColor(selected ? NeonColors.WHITE : new Color(150, 150, 180));
            g2.drawString(labels[i], 180, y);

            // Value with arrows
            g2.setColor(selected ? NeonColors.YELLOW : new Color(200, 200, 200));
            String valueText = "< " + values[i] + " >";
            g2.drawString(valueText, 480, y);

            // Color preview for player color
            if (i == 2) {
                g2.setColor(settings.getPlayerColor());
                g2.fillRect(600, y - 15, 25, 25);
                g2.setColor(NeonColors.WHITE);
                g2.drawRect(600, y - 15, 25, 25);
            }
        }

        // Instructions
        g2.setFont(new Font("Arial", Font.PLAIN, 16));
        g2.setColor(new Color(150, 150, 180));
        drawCenteredString(g2, "UP/DOWN = Select  |  LEFT/RIGHT = Change  |  ESC = Back", getWidth()/2, 360, g2.getFont());
    }

    private void drawFPS(Graphics2D g2) {
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.setColor(NeonColors.GREEN);
        g2.drawString("FPS: " + fps, getWidth() - 60, 80);
    }

    private void drawLevelComplete(Graphics2D g2) {
        // Dark overlay
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRect(0, 0, getWidth(), getHeight());

        double pulse = 0.7 + 0.3 * Math.sin(menuPulse);

        // Congratulations text with glow
        g2.setFont(new Font("Arial", Font.BOLD, 36));
        for (int i = 3; i >= 0; i--) {
            g2.setColor(new Color(255, 215, 0, 30 * (4 - i)));
            drawCenteredString(g2, "CONGRATULATIONS!", getWidth()/2, 80 - i, g2.getFont());
        }
        g2.setColor(NeonColors.YELLOW);
        drawCenteredString(g2, "CONGRATULATIONS!", getWidth()/2, 80, g2.getFont());

        // You Won text
        g2.setFont(new Font("Arial", Font.BOLD, 48));
        for (int i = 3; i >= 0; i--) {
            g2.setColor(new Color(0, 255, 128, 30 * (4 - i)));
            drawCenteredString(g2, "YOU WON!", getWidth()/2, 140 - i, g2.getFont());
        }
        g2.setColor(NeonColors.GREEN);
        drawCenteredString(g2, "YOU WON!", getWidth()/2, 140, g2.getFont());

        // 100% text
        g2.setFont(new Font("Arial", Font.BOLD, 50));
        g2.setColor(NeonColors.pulse(NeonColors.CYAN, pulse));
        drawCenteredString(g2, "100%", getWidth()/2, 200, g2.getFont());

        // Map complete message
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.setColor(NeonColors.WHITE);
        drawCenteredString(g2, "You have finished the map!", getWidth()/2, 245, g2.getFont());

        // Final score
        g2.setFont(new Font("Arial", Font.PLAIN, 18));
        g2.setColor(NeonColors.CYAN);
        drawCenteredString(g2, "Final Score: " + score, getWidth()/2, 280, g2.getFont());

        // Options - Restart or Menu
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.setColor(NeonColors.pulse(NeonColors.YELLOW, pulse));
        drawCenteredString(g2, "Press R to Play Again", getWidth()/2, 325, g2.getFont());
        
        g2.setColor(NeonColors.pulse(NeonColors.MAGENTA, pulse));
        drawCenteredString(g2, "Press ESC for Menu", getWidth()/2, 355, g2.getFont());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // FPS calculation
        frameCount++;
        long now = System.nanoTime();
        fpsTimer += now - lastFrameTime;
        lastFrameTime = now;
        if (fpsTimer >= 1_000_000_000) {
            fps = frameCount;
            frameCount = 0;
            fpsTimer = 0;
        }

        menuPulse += 0.08;

        if (state == GameState.PLAYING) {
            player.update(invertedGravity);
            obstacleManager.update(gameSpeed, score);
            particles.update();

            // Emit trail particles (if enabled)
            int playerDrawY = invertedGravity ? 400 - player.y - player.size : player.y;
            if (settings.particlesEnabled) {
                particles.emitTrail(player.x, playerDrawY, player.getColor());

                // Jump particles
                if (player.didJustJump()) {
                    particles.emitJump(player.x, playerDrawY, player.getColor());
                }
            }

            // Update visual effects
            backgroundScroll += gameSpeed * 0.5;
            groundLineOffset = (groundLineOffset + gameSpeed) % 60;
            beatPulse = Math.max(0, beatPulse - 0.05);

            // Simulate beat pulse (every ~60 frames)
            if (score % 60 == 0) {
                beatPulse = 1.0;
            }

            score++;

            // Update base speed from score (smooth difficulty scaling)
            if (score < 600) baseSpeed = 5;
            else if (score < 1400) baseSpeed = 6;
            else if (score < 2600) baseSpeed = 7;
            else if (score < 4200) baseSpeed = 8;
            else if (score < 6000) baseSpeed = 9;
            else baseSpeed = 10;

            // Handle portal timer
            if (portalTimer > 0) {
                portalTimer--;
            } else {
                portalBoost = 0;
            }

            gameSpeed = baseSpeed + portalBoost;

            if (obstacleManager.checkCollision(player, this)) {
                if (testingMode) {
                    // In testing mode, just flash yellow and continue
                    collisionFlashTimer = 8;
                } else {
                    state = GameState.GAME_OVER;
                    audioManager.stopBackgroundMusic();
                    deathFlashTimer = 15;

                    // Death burst particles (if enabled)
                    if (settings.particlesEnabled) {
                        int py = invertedGravity ? 400 - player.y - player.size : player.y;
                        particles.emitBurst(player.x + player.size/2, py + player.size/2, player.getColor(), 30);
                    }
                }
            }
        }

        if (deathFlashTimer > 0) deathFlashTimer--;
        if (collisionFlashTimer > 0) collisionFlashTimer--;

        // Level complete - no auto return, player chooses when to exit

        repaint();
    }

    public void flipGravity() {
        invertedGravity = !invertedGravity;
        beatPulse = 1.0; // Visual feedback
    }

    public void applySpeedPortal(int boost) {
        portalBoost = boost;
        portalTimer = 300;
        beatPulse = 1.0;
    }

    public void triggerJumpPadEffect() {
        beatPulse = 1.0;
    }

    public void triggerLevelComplete() {
        if (state == GameState.PLAYING) {
            state = GameState.LEVEL_COMPLETE;
            audioManager.stopBackgroundMusic();
            levelCompleteTimer = 180;
        }
    }

    private void resetGame() {
        score = 0;
        gameSpeed = 5;
        baseSpeed = 5;
        portalBoost = 0;
        portalTimer = 0;
        invertedGravity = false;
        deathFlashTimer = 0;
        collisionFlashTimer = 0;
        obstacleManager.reset();
        player.reset();
        particles.clear();
        audioManager.restartBackgroundMusic();
        state = GameState.PLAYING;
    }

    private void exitToMenu() {
        state = GameState.MENU;
        testingMode = false;
        score = 0;
        gameSpeed = 5;
        baseSpeed = 5;
        portalBoost = 0;
        portalTimer = 0;
        invertedGravity = false;
        deathFlashTimer = 0;
        collisionFlashTimer = 0;
        obstacleManager.reset();
        player.reset();
        particles.clear();
        audioManager.stopBackgroundMusic();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // Settings menu navigation
        if (state == GameState.SETTINGS) {
            if (key == KeyEvent.VK_UP) {
                settingsSelection = (settingsSelection - 1 + SETTINGS_COUNT) % SETTINGS_COUNT;
            } else if (key == KeyEvent.VK_DOWN) {
                settingsSelection = (settingsSelection + 1) % SETTINGS_COUNT;
            } else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
                int dir = (key == KeyEvent.VK_RIGHT) ? 1 : -1;
                switch (settingsSelection) {
                    case 0: // Music Volume
                        settings.musicVolume = Math.max(0, Math.min(1, settings.musicVolume + dir * 0.1f));
                        audioManager.setVolume(settings.musicVolume);
                        break;
                    case 1: // Particles
                        settings.particlesEnabled = !settings.particlesEnabled;
                        break;
                    case 2: // Player Color
                        if (dir > 0) settings.nextPlayerColor();
                        else settings.prevPlayerColor();
                        player.setColor(settings.getPlayerColor());
                        break;
                    case 3: // Show FPS
                        settings.showFPS = !settings.showFPS;
                        break;
                }
            } else if (key == KeyEvent.VK_ESCAPE) {
                state = GameState.MENU;
            }
            return;
        }

        if (key == KeyEvent.VK_SPACE) {
            if (state == GameState.MENU) {
                testingMode = false;
                state = GameState.PLAYING;
                if (settings.musicEnabled) audioManager.playBackgroundMusic();
            } else if (state == GameState.PLAYING) {
                player.jump(invertedGravity);
            }
        }

        // S for Settings from menu
        if (key == KeyEvent.VK_S && state == GameState.MENU) {
            state = GameState.SETTINGS;
        }

        // T for Testing Mode from menu
        if (key == KeyEvent.VK_T && state == GameState.MENU) {
            testingMode = true;
            state = GameState.PLAYING;
            if (settings.musicEnabled) audioManager.playBackgroundMusic();
        }

        // ESC to exit to menu (from playing, game over, or level complete)
        if (key == KeyEvent.VK_ESCAPE) {
            if (state == GameState.PLAYING || state == GameState.GAME_OVER || state == GameState.LEVEL_COMPLETE) {
                exitToMenu();
            }
        }

        // R to restart (from game over or level complete)
        if (key == KeyEvent.VK_R && (state == GameState.GAME_OVER || state == GameState.LEVEL_COMPLETE)) {
            resetGame();
        }
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
}
