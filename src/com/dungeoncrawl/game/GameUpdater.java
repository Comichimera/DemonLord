package com.dungeoncrawl.game;

import java.util.List;

public class GameUpdater {
    private GamePanel gamePanel;
    private Player player;
    private TimerManager timerManager;
    private List<Sprite> sprites;

    public GameUpdater(GamePanel gamePanel, Player player, TimerManager timerManager, List<Sprite> sprites) {
        this.gamePanel = gamePanel;
        this.player = player;
        this.timerManager = timerManager;
        this.sprites = sprites;
    }

    public void update() {
        System.out.println("Testing");
        player.update();
        for (Sprite sprite : sprites) {
            sprite.update();
        }
        if (timerManager.isRunning() && player.isAtExit()) {
            timerManager.stop();
            gamePanel.setShowEndScreen(true);
            player.closeLogger();
        }
    }
}
