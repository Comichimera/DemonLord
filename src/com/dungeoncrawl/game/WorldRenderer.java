package com.dungeoncrawl.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class WorldRenderer {
    private WallRenderer wallRenderer;
    private SpriteRenderer spriteRenderer;
    private String version;
    private TimerManager timerManager;
    private LocalizationManager localizationManager;
    private List<Sprite> sprites;
    private Player player;
    private BufferedImage invSlotTexture;
    private BufferedImage healthSlotTexture;
    private int healthBarLength = 200;
    private int healthBarX = 575;
    private int healthBarY = 494;
    private int healthBarTextOffset = -7;

    private int auraBarLength = 200;
    private int auraBarX = 575;
    private int auraBarY = 532;
    private int auraBarTextOffset = -7;

    public WorldRenderer(Player player, BufferedImage wallTexture, BufferedImage doorTexture, BufferedImage skeletonSprite, int[][] map, String version, TimerManager timerManager, LocalizationManager localizationManager, List<Sprite> sprites, BufferedImage invSlotTexture, BufferedImage healthSlotTexture) {
        this.wallRenderer = new WallRenderer(player, wallTexture, doorTexture, map);
        this.spriteRenderer = new SpriteRenderer(player, sprites);
        this.version = version;
        this.timerManager = timerManager;
        this.localizationManager = localizationManager;
        this.player = player;
        this.sprites = sprites;
        this.invSlotTexture = invSlotTexture;
        this.healthSlotTexture = healthSlotTexture;
    }

    public List<Sprite> getSprites() {
        return this.sprites;
    }

    public void render(Graphics g, int width, int height) {
        double[] zBuffer = new double[width]; // Create zBuffer for depth checks
        wallRenderer.renderWallsAndDoors(g, width, height, zBuffer); // Ensure this method fills zBuffer
        spriteRenderer.renderSprites(g, width, height, zBuffer); // Pass zBuffer for depth checks

        g.setColor(Color.WHITE);
        g.drawString(this.version, 10, 20);
        g.drawString(this.localizationManager.getString("time") + ": " + String.format("%.1f s", this.timerManager.getElapsedTime()), width - 100, 20);

        renderInventorySlots(g, width, height);
        renderHealthBar(g);
        renderAuraBar(g);
    }

    private void renderInventorySlots(Graphics g, int width, int height) {
        if (invSlotTexture != null) {
            Graphics2D g2d = (Graphics2D) g; // Cast Graphics to Graphics2D
            int invSlotWidth = invSlotTexture.getWidth();
            int invSlotHeight = invSlotTexture.getHeight();
            int scaledWidth = (int) (invSlotWidth * 2.5);
            int scaledHeight = (int) (invSlotHeight * 2.5);

            for (int i = 0; i < 7; i++) {
                int x = i * scaledWidth; // Calculate x position for each slot without padding
                int y = height - scaledHeight;
                g2d.drawImage(invSlotTexture, x, y, scaledWidth, scaledHeight, null);
            }

            // Render health slots after inventory slots
            if (healthSlotTexture != null) {
                int healthSlotWidth = healthSlotTexture.getWidth();
                int healthSlotHeight = healthSlotTexture.getHeight();
                int scaledHealthWidth = (int) (healthSlotWidth * 2.5);
                int scaledHealthHeight = (int) (healthSlotHeight * 2.5);

                for (int i = 0; i < 3; i++) {
                    int x = 7 * scaledWidth + i * scaledHealthWidth; // Calculate x position after inventory slots
                    int y = height - scaledHealthHeight;
                    g2d.drawImage(healthSlotTexture, x, y, scaledHealthWidth, scaledHealthHeight, null);
                }
            }
        }
    }

    private void renderHealthBar(Graphics g) {
        // Draw the health text
        g.setColor(Color.WHITE);
        g.drawString("HEALTH", healthBarX, healthBarY);

        // Draw the health bar
        g.setColor(Color.RED);
        g.fillRect(healthBarX, healthBarY + healthBarTextOffset + 10, healthBarLength, 20);
    }

    private void renderAuraBar(Graphics g) {
        // Draw the aura text
        g.setColor(Color.WHITE);
        g.drawString("AURA", auraBarX, auraBarY);

        // Draw the aura bar
        g.setColor(new Color(0, 123, 167)); // Cerulean color
        g.fillRect(auraBarX, auraBarY + auraBarTextOffset + 10, auraBarLength, 20);
    }

    // Methods to adjust health bar position and length
    public void setHealthBarPosition(int x, int y) {
        this.healthBarX = x;
        this.healthBarY = y;
    }

    public void setHealthBarLength(int length) {
        this.healthBarLength = length;
    }

    public void setHealthBarTextOffset(int offset) {
        this.healthBarTextOffset = offset;
    }

    // Methods to adjust aura bar position and length
    public void setAuraBarPosition(int x, int y) {
        this.auraBarX = x;
        this.auraBarY = y;
    }

    public void setAuraBarLength(int length) {
        this.auraBarLength = length;
    }

    public void setAuraBarTextOffset(int offset) {
        this.auraBarTextOffset = offset;
    }
}
