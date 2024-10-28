package com.dungeoncrawl.game;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class GameInitializer {
    private BufferedImage wallTexture;
    private BufferedImage doorTexture;
    private BufferedImage skeletonSprite;
    private int[][] map;
    private double moveSpeed;
    private double rotSpeed;
    private String version;
    private Player player;
    private double[] playerStart = new double[2];
    private GamePanel gamePanel;
    private TimerManager timerManager;
    private LevelManager levelManager;
    private LocalizationManager localizationManager; // Add this line
    private List<Sprite> sprites;
    private BufferedImage invSlotTexture;
    private BufferedImage healthSlotTexture;

    public GameInitializer(GamePanel gamePanel, TimerManager timerManager, LevelManager levelManager, LocalizationManager localizationManager) { // Add localizationManager to constructor
        this.gamePanel = gamePanel;
        this.timerManager = timerManager;
        this.levelManager = levelManager;
        this.localizationManager = localizationManager; // Initialize it here
    }

    public void loadResources() {
        System.out.println("Loading resources...");
        Properties properties = PropertiesLoader.loadProperties("/resources/config/game.properties");
        int windowWidth = Integer.parseInt(properties.getProperty("window_width"));
        int windowHeight = Integer.parseInt(properties.getProperty("window_height"));
        this.moveSpeed = Double.parseDouble(properties.getProperty("movement_speed"));
        this.rotSpeed = Double.parseDouble(properties.getProperty("rotation_speed"));
        this.version = properties.getProperty("version");
        this.wallTexture = ImageLoader.loadImage(this.levelManager.getCurrentLevelTexture());
        this.doorTexture = ImageLoader.loadImage("/resources/assets/textures/door.png");
        this.skeletonSprite = ImageLoader.loadImage("/resources/assets/sprites/skeleton.png");
        this.invSlotTexture = ImageLoader.loadImage("/resources/assets/textures/invslot.png");
        this.healthSlotTexture = ImageLoader.loadImage("/resources/assets/textures/healthslot.png");
        this.loadLevelResources();
    }

    public void loadLevelResources() {
        this.map = MapLoader.loadMap(this.levelManager.getCurrentLevelMap(), this.playerStart);
        this.player = new Player(this.playerStart[0], this.playerStart[1], 0.0, this.wallTexture, this.doorTexture, this.map, this.moveSpeed, this.rotSpeed, this.gamePanel, this.timerManager, new ArrayList<>());
        this.sprites = new ArrayList<>();

        // Initialize sprites based on map data (wherever '5' appears on the map)
        SpriteRenderer spriteRenderer = new SpriteRenderer(this.player, this.sprites);
        spriteRenderer.initializeSpritesFromMap(this.map, this.skeletonSprite);

        this.player.setSprites(this.sprites);
        WorldRenderer worldRenderer = new WorldRenderer(this.player, this.wallTexture, this.doorTexture, this.skeletonSprite, this.map, this.version, this.timerManager, this.localizationManager, this.sprites, this.invSlotTexture, this.healthSlotTexture);
        this.gamePanel.setWorldRenderer(worldRenderer);
    }

    private List<Sprite> initializeSprites() {
        List<Sprite> sprites = new ArrayList<>();
        return sprites;
    }

    public BufferedImage getWallTexture() {
        return this.wallTexture;
    }

    public BufferedImage getDoorTexture() {
        return this.doorTexture;
    }

    public BufferedImage getSkeletonSprite() {
        return this.skeletonSprite;
    }

    public int[][] getMap() {
        return this.map;
    }

    public String getVersion() {
        return this.version;
    }

    public Player getPlayer() {
        return this.player;
    }

    public List<Sprite> getSprites() {
        return this.sprites;
    }

    public BufferedImage getInvSlotTexture() {
        return this.invSlotTexture;
    }

    public BufferedImage getHealthSlotTexture() {
        return this.healthSlotTexture;
    }
}