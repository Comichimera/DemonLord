package com.dungeoncrawl.game;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

public class Sprite {
    private double x;
    private double y;
    private BufferedImage image;
    private int[][] map;
    private double moveSpeed = 0.03;
    private boolean aggro = false; // Track aggro state
    private Player player;
    private double distance; // Add this field

    private static final double FOV_ANGLE = Math.PI / 3; // 60 degrees
    private static final double VIEW_DISTANCE = 100000.0; // Adjust as needed

    public Sprite(double x, double y, BufferedImage image, int[][] map, Player player) {
        this.x = x;
        this.y = y;
        this.image = image;
        this.map = map;
        this.player = player;
    }

    public void update() {
        printMap(); // Add this line to print the map each update
        if (aggro) {
            moveToPlayer();
        } else {
            if (playerInSight()) {
                aggro = true; // Player detected, aggro the skeleton
            } else {
            }
        }
    }

    private boolean playerInSight() {
        Set<Point> visibleTiles = calculateVisibleTiles();
        int playerX = (int) player.x;
        int playerY = (int) player.y;

        return visibleTiles.contains(new Point(playerX, playerY));
    }

    private Set<Point> calculateVisibleTiles() {
        Set<Point> visibleTiles = new HashSet<>();
        int numRays = 100; // Number of rays to cast in 360 degrees

        // Iterate over angles from 0 to 2π to achieve a full 360° coverage
        for (int i = 0; i < numRays; i++) {
            double angle = (i / (double) numRays) * 2 * Math.PI; // 0 to 2π
            double rayDirX = Math.cos(angle);
            double rayDirY = Math.sin(angle);

            // Cast the ray in the direction defined by rayDirX and rayDirY
            castRayForVisibleTiles((int) x, (int) y, rayDirX, rayDirY, visibleTiles);
        }

        return visibleTiles;
    }

    private void castRayForVisibleTiles(int x0, int y0, double rayDirX, double rayDirY, Set<Point> visibleTiles) {
        double rayX = x0;
        double rayY = y0;
        double stepSize = 0.1;

        while (true) {
            rayX += rayDirX * stepSize;
            rayY += rayDirY * stepSize;

            int tileX = (int) rayX;
            int tileY = (int) rayY;

            // Check if the ray is out of bounds or beyond the view distance
            if (rayX < 0 || rayY < 0 || rayX >= map.length || rayY >= map[0].length ||
                    Math.sqrt((tileX - x0) * (tileX - x0) + (tileY - y0) * (tileY - y0)) > VIEW_DISTANCE) {
                break;
            }

            visibleTiles.add(new Point(tileX, tileY));

            // Stop if ray hits a wall
            if (map[tileX][tileY] != 0) {
                break;
            }
        }
    }

    private void moveToPlayer() {
        double targetX = player.x;
        double targetY = player.y;
        double dirX = targetX - x;
        double dirY = targetY - y;
        double length = Math.sqrt(dirX * dirX + dirY * dirY);

        // Define a minimum distance to stop near the player
        double minDistance = 1.0; // Adjust this value as needed (1.0 means 1 unit away)

        // Check if the sprite is close enough to the player
        if (length <= minDistance) {
            return; // Stop moving if within the minimum distance
        }

        // Normalize the direction if not within stopping distance
        if (length != 0) {
            dirX /= length;
            dirY /= length;
        }

        // Update position
        x += dirX * moveSpeed;
        y += dirY * moveSpeed;
    }

    private void printMap() {
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public BufferedImage getImage() {
        return image;
    }

    public double getDistance() { // Add this getter
        return distance;
    }

    public void setDistance(double distance) { // Add this setter
        this.distance = distance;
    }
}
