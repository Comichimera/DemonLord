package com.dungeoncrawl.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class WorldRenderer {
    private WallRenderer wallRenderer;
    private SpriteRenderer spriteRenderer;
    private String version;
    private TimerManager timerManager;
    private LocalizationManager localizationManager;
    private List<Sprite> sprites;
    private Player player;

    public WorldRenderer(Player player, BufferedImage wallTexture, BufferedImage doorTexture, BufferedImage skeletonSprite, int[][] map, String version, TimerManager timerManager, LocalizationManager localizationManager, List<Sprite> sprites) {
        this.wallRenderer = new WallRenderer(player, wallTexture, doorTexture, map);
        this.spriteRenderer = new SpriteRenderer(player, sprites);
        this.version = version;
        this.timerManager = timerManager;
        this.localizationManager = localizationManager;
        this.player = player;
        this.sprites = sprites;
    }

    public List<Sprite> getSprites() {
        return this.sprites;
    }

    public void render(Graphics g, int width, int height) {
        wallRenderer.renderWallsAndDoors(g, width, height);
        spriteRenderer.renderSprites(g, width, height);

        g.setColor(Color.WHITE);
        g.drawString(this.version, 10, 20);
        g.drawString(this.localizationManager.getString("time") + ": " + String.format("%.1f s", this.timerManager.getElapsedTime()), width - 100, 20);
    }
}
