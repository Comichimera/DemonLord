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
    private List<Sprite> sprites;

    public GameInitializer(GamePanel gamePanel, TimerManager timerManager, LevelManager levelManager) {
        this.gamePanel = gamePanel;
        this.timerManager = timerManager;
        this.levelManager = levelManager;
    }

    public void loadResources() {
        System.out.println("Loading resources...");
        Properties properties = PropertiesLoader.loadProperties("/resources/config/game.properties");
        int windowWidth = Integer.parseInt(properties.getProperty("window_width"));
        int windowHeight = Integer.parseInt(properties.getProperty("window_height"));
        this.moveSpeed = Double.parseDouble(properties.getProperty("movement_speed"));
        this.rotSpeed = Double.parseDouble(properties.getProperty("rotation_speed"));
        this.version = properties.getProperty("version");
        this.loadLevelResources();
    }

    public void loadLevelResources() {
        System.out.println("Loading level resources...");
        this.wallTexture = ImageLoader.loadImage(this.levelManager.getCurrentLevelTexture());
        this.doorTexture = ImageLoader.loadImage("/resources/assets/textures/door.png");
        this.skeletonSprite = ImageLoader.loadImage("/resources/assets/sprites/skeleton.png");
        if (this.skeletonSprite != null) {
            System.out.println("Skeleton sprite loaded successfully.");
        } else {
            System.out.println("Failed to load skeleton sprite.");
        }
        this.map = MapLoader.loadMap(this.levelManager.getCurrentLevelMap(), this.playerStart);
        this.sprites = initializeSprites();
        this.player = new Player(this.playerStart[0], this.playerStart[1], 0.0, this.wallTexture, this.doorTexture, this.map, this.moveSpeed, this.rotSpeed, this.gamePanel, this.timerManager, this.sprites);
    }

    private List<Sprite> initializeSprites() {
        List<Sprite> sprites = new ArrayList<>();
        for (int i = 0; i < this.map.length; i++) {
            for (int j = 0; j < this.map[i].length; j++) {
                if (this.map[i][j] == 5) {
                    sprites.add(new Sprite(i + 0.5, j + 0.5, this.skeletonSprite, this.map));
                    this.map[i][j] = 0; // Mark original spawn square as walkable
                    System.out.println("Sprite initialized at: (" + (i + 0.5) + ", " + (j + 0.5) + ")");
                }
            }
        }
        System.out.println("Initialized " + sprites.size() + " sprites.");
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
}
