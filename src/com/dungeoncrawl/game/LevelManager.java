package com.dungeoncrawl.game;

public class LevelManager {
    private int currentLevel = 0;
    private String[] levels = new String[]{"/resources/assets/maps/map.txt", "/resources/assets/maps/leveltwo.txt"};
    private String[] textures = new String[]{"/resources/assets/textures/wall.png", "/resources/assets/textures/ground.png"};

    public LevelManager() {
    }

    public String getCurrentLevelMap() {
        return this.levels[this.currentLevel];
    }

    public String getCurrentLevelTexture() {
        return this.textures[this.currentLevel];
    }

    public void loadNextLevel() {
        if (this.currentLevel < this.levels.length - 1) {
            ++this.currentLevel;
        }

    }

    public boolean hasNextLevel() {
        return this.currentLevel < this.levels.length - 1;
    }

    public void reset() {
        this.currentLevel = 0;
    }
}
