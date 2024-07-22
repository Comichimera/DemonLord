package com.dungeoncrawl.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Locale;
import javax.swing.JPanel;
import java.util.List;

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

    public GamePanel(Locale locale) {
        this.setPreferredSize(new Dimension(800, 600));
        this.setBackground(Color.BLACK);
        this.localizationManager = new LocalizationManager(locale);
        this.levelManager = new LevelManager();
        this.setFocusable(true); // Ensure the panel is focusable
        this.requestFocusInWindow(); // Request focus
        System.out.println("GamePanel initialized."); // Debug statement
        this.init();
        this.startGame();
    }

    private void init() {
        this.timerManager = new TimerManager();
        this.initializer = new GameInitializer(this, this.timerManager, this.levelManager);
        this.initializer.loadResources();
        this.player = this.initializer.getPlayer();
        this.renderer = new WorldRenderer(this.player, this.initializer.getWallTexture(), this.initializer.getDoorTexture(), this.initializer.getSkeletonSprite(), this.initializer.getMap(), this.initializer.getVersion(), this.timerManager, this.localizationManager, this.initializer.getSprites());
        this.addKeyListener(new PlayerInputHandler(this.player, this.timerManager, this)); // Moved here to ensure re-initialization
        this.setFocusable(true);
        this.requestFocus();
        this.showEndScreen = false;
    }

    private void startGame() {
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

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.showEndScreen) {
            this.showEndScreen(g);
        } else {
            this.renderer.render(g, this.getWidth(), this.getHeight());
        }
    }

    private void showEndScreen(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", 1, 36));
        g.drawString(this.localizationManager.getString("level_complete"), this.getWidth() / 2 - 100, this.getHeight() / 2 - 150);
        g.setFont(new Font("Arial", 0, 24));
        g.drawString(this.localizationManager.getString("time") + ": " + this.timerManager.formatElapsedTime(), this.getWidth() / 2 - 100, this.getHeight() / 2 - 50);
        g.drawString(this.localizationManager.getString("kills") + ": 0", this.getWidth() / 2 - 100, this.getHeight() / 2);
        g.drawString(this.localizationManager.getString("score") + ": 0", this.getWidth() / 2 - 100, this.getHeight() / 2 + 50);
        g.drawString(this.localizationManager.getString("secrets") + ": 0", this.getWidth() / 2 - 100, this.getHeight() / 2 + 100);
        g.setFont(new Font("Arial", 0, 18));
        g.drawString("Press SPACE to continue", this.getWidth() / 2 - 100, this.getHeight() / 2 + 150);
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
}