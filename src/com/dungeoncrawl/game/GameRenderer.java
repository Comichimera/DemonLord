package com.dungeoncrawl.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;
import javax.swing.JPanel;

public class GameRenderer {
    private WorldRenderer worldRenderer;
    private GamePanel gamePanel;
    private LocalizationManager localizationManager;
    private TimerManager timerManager;

    public GameRenderer(WorldRenderer worldRenderer, GamePanel gamePanel, LocalizationManager localizationManager, TimerManager timerManager) {
        this.worldRenderer = worldRenderer;
        this.gamePanel = gamePanel;
        this.localizationManager = localizationManager;
        this.timerManager = timerManager;
    }

    public void render(Graphics g, int width, int height) {
        if (this.gamePanel.isShowEndScreen()) {
            showEndScreen(g, width, height);
        } else {
            worldRenderer.render(g, width, height);
        }
    }

    private void showEndScreen(Graphics g, int width, int height) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.drawString(this.localizationManager.getString("level_complete"), width / 2 - 100, height / 2 - 150);
        g.setFont(new Font("Arial", Font.PLAIN, 24));
        g.drawString(this.localizationManager.getString("time") + ": " + this.timerManager.formatElapsedTime(), width / 2 - 100, height / 2 - 50);
        g.drawString(this.localizationManager.getString("kills") + ": 0", width / 2 - 100, height / 2);
        g.drawString(this.localizationManager.getString("score") + ": 0", width / 2 - 100, height / 2 + 50);
        g.drawString(this.localizationManager.getString("secrets") + ": 0", width / 2 - 100, height / 2 + 100);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("Press SPACE to continue", width / 2 - 100, height / 2 + 150);
    }

    public List<Sprite> getSprites() {
        return worldRenderer.getSprites();
    }
}
