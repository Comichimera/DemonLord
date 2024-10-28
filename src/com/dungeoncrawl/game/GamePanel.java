package com.dungeoncrawl.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Locale;
import javax.swing.JPanel;
import java.util.List;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
import java.io.File;

public class GamePanel extends JPanel {
    private GameLoop gameLoop;
    private Player player;
    private GameInitializer initializer;
    private GameUpdater updater;
    private WorldRenderer renderer;
    private TimerManager timerManager;
    private LocalizationManager localizationManager;
    private LevelManager levelManager;
    private boolean showEndScreen;
    private WorldRenderer worldRenderer;
    public boolean showIntroScreen = true;
    private boolean isIntroActive = true;

    public GamePanel(Locale locale) {
        this.setPreferredSize(new Dimension(800, 600));
        this.setBackground(Color.BLACK);
        this.localizationManager = new LocalizationManager(locale);
        this.levelManager = new LevelManager();
        this.setFocusable(true);
        this.requestFocusInWindow();
        System.out.println("GamePanel initialized.");

        this.init();
        this.startGame();  // Start game loop and initialize
        this.showIntroScreen = true; // Show intro screen initially
    }

    private void init() {
        this.timerManager = new TimerManager();
        this.initializer = new GameInitializer(this, this.timerManager, this.levelManager, this.localizationManager);
        this.initializer.loadResources();
        this.player = this.initializer.getPlayer();
        this.renderer = new WorldRenderer(this.player, this.initializer.getWallTexture(), this.initializer.getDoorTexture(), this.initializer.getSkeletonSprite(), this.initializer.getMap(), this.initializer.getVersion(), this.timerManager, this.localizationManager, this.initializer.getSprites(), this.initializer.getInvSlotTexture(), this.initializer.getHealthSlotTexture());
        this.addKeyListener(new PlayerInputHandler(this.player, this.timerManager, this)); // Moved here to ensure re-initialization
        this.setFocusable(true);
        this.requestFocus();
        this.showEndScreen = false;

        renderer.setHealthBarPosition(575, 494);
        renderer.setHealthBarLength(200);
        renderer.setHealthBarTextOffset(-7);

        renderer.setAuraBarPosition(575, 532);
        renderer.setAuraBarLength(200);
        renderer.setAuraBarTextOffset(-7);
    }

    public void startGame() {
        this.init(); // Only initialize after pressing space
        this.gameLoop = new GameLoop(this);
        this.gameLoop.start();
    }

    public void stopGame() {
        this.gameLoop.stop();
        if (this.player != null) {
            this.player.closeLogger();
        }
    }

    public void loadNextLevel() {
        if (this.levelManager.hasNextLevel()) {
            this.levelManager.loadNextLevel();
            this.removeKeyListener(this.getKeyListeners()[0]); // Remove old key listener
            this.init();
            this.showEndScreen = false;
        } else {
            this.stopGame();
        }
    }

    public void update() {
        if (!this.showEndScreen) {
            this.player.update();
            if (this.timerManager.isRunning() && this.player.isAtExit()) {
                this.timerManager.stop();
                this.showEndScreen = true;
                this.player.closeLogger();
            }
            this.updateSprites();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (showIntroScreen) {
            showIntroScreen(g);  // Display the intro screen
        } else {
            if (worldRenderer != null) {
                worldRenderer.render(g, getWidth(), getHeight());
            }
            if (showEndScreen) {
                showEndScreen(g);
            }
        }
    }

    private void showEndScreen(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.drawString(this.localizationManager.getString("level_complete"), this.getWidth() / 2 - 100, this.getHeight() / 2 - 150);
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        g.drawString(this.localizationManager.getString("time") + ": " + this.timerManager.formatElapsedTime(), this.getWidth() / 2 - 100, this.getHeight() / 2 - 50);
        g.drawString(this.localizationManager.getString("kills") + ": 0", this.getWidth() / 2 - 100, this.getHeight() / 2);
        g.drawString(this.localizationManager.getString("score") + ": 0", this.getWidth() / 2 - 100, this.getHeight() / 2 + 50);
        g.drawString(this.localizationManager.getString("secrets") + ": 0", this.getWidth() / 2 - 100, this.getHeight() / 2 + 100);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("Press SPACE to continue", this.getWidth() / 2 - 100, this.getHeight() / 2 + 150);
    }

    private void showIntroScreen(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        // Main Intro Text
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.drawString("Level One", this.getWidth() / 2 - 100, this.getHeight() / 2 - 100);
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        g.drawString("Press SPACE to continue", this.getWidth() / 2 - 120, this.getHeight() / 2);

        // Tip Section
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Tip:", this.getWidth() / 2 - 100, this.getHeight() - 80);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("You died?? Git gud LOSER!", this.getWidth() / 2 - 100, this.getHeight() - 50);
    }

    public void handleIntroScreenKeyPress() {
        if (showIntroScreen) {
            showIntroScreen = false;
            this.startGame(); // Initialize and start the game loop
            repaint();
        }
    }

    public void playAudio(String filePath) {
        try {
            File audioFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start(); // Play the audio
        } catch (Exception e) {
            e.printStackTrace(); // Print error if loading fails
        }
    }

    public void handleEndScreenKeyPress() {
        if (this.showEndScreen) {
            this.loadNextLevel();
        }
    }

    public boolean isShowEndScreen() {
        return this.showEndScreen;
    }

    public void setShowEndScreen(boolean showEndScreen) {
        this.showEndScreen = showEndScreen;
    }

    public void updateSprites() {
        for (Sprite sprite : renderer.getSprites()) {
            sprite.update();
        }
    }

    public List<Sprite> getSprites() {
        return renderer.getSprites();
    }

    public void setWorldRenderer(WorldRenderer worldRenderer) {
        this.worldRenderer = worldRenderer;
    }
}