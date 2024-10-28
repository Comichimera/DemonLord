package com.dungeoncrawl.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class WallRenderer {
    private BufferedImage wallTexture;
    private BufferedImage doorTexture;
    private int[][] map;
    private Player player;

    public WallRenderer(Player player, BufferedImage wallTexture, BufferedImage doorTexture, int[][] map) {
        this.player = player;
        this.wallTexture = wallTexture;
        this.doorTexture = doorTexture;
        this.map = map;
    }

    public void renderWallsAndDoors(Graphics g, int width, int height, double[] zBuffer) {
        Color floorColor = Color.RED;
        Color ceilingColor = Color.LIGHT_GRAY;
        Color exitColor = Color.YELLOW;
        g.setColor(ceilingColor);
        g.fillRect(0, 0, width, height / 2);

        for (int x = 0; x < width; x++) {
            double cameraX = 2 * x / (double) width - 1;
            double rayDirX = this.player.dirX + this.player.planeX * cameraX;
            double rayDirY = this.player.dirY + this.player.planeY * cameraX;

            int mapX = (int) this.player.x;
            int mapY = (int) this.player.y;

            double deltaDistX = Math.abs(1 / rayDirX);
            double deltaDistY = Math.abs(1 / rayDirY);

            boolean hit = false;
            boolean side = false;
            double sideDistX;
            double sideDistY;

            int stepX;
            int stepY;

            if (rayDirX < 0) {
                stepX = -1;
                sideDistX = (this.player.x - mapX) * deltaDistX;
            } else {
                stepX = 1;
                sideDistX = (mapX + 1.0 - this.player.x) * deltaDistX;
            }

            if (rayDirY < 0) {
                stepY = -1;
                sideDistY = (this.player.y - mapY) * deltaDistY;
            } else {
                stepY = 1;
                sideDistY = (mapY + 1.0 - this.player.y) * deltaDistY;
            }

            while (!hit) {
                if (sideDistX < sideDistY) {
                    sideDistX += deltaDistX;
                    mapX += stepX;
                    side = false;
                } else {
                    sideDistY += deltaDistY;
                    mapY += stepY;
                    side = true;
                }
                if (map[mapX][mapY] == 1 || map[mapX][mapY] == 4) {
                    hit = true;
                }
            }

            double perpWallDist;
            if (!side) {
                perpWallDist = (mapX - this.player.x + (1 - stepX) / 2) / rayDirX;
            } else {
                perpWallDist = (mapY - this.player.y + (1 - stepY) / 2) / rayDirY;
            }

            zBuffer[x] = perpWallDist;

            int lineHeight = (int) (height / perpWallDist);

            int drawStart = -lineHeight / 2 + height / 2;
            if (drawStart < 0) {
                drawStart = 0;
            }

            int drawEnd = lineHeight / 2 + height / 2;
            if (drawEnd >= height) {
                drawEnd = height - 1;
            }

            int textureX;
            if (!side) {
                textureX = (int) ((this.player.y + perpWallDist * rayDirY) * 32) % 32;
            } else {
                textureX = (int) ((this.player.x + perpWallDist * rayDirX) * 32) % 32;
            }

            BufferedImage texture = map[mapX][mapY] == 4 ? this.doorTexture : this.wallTexture;

            for (int y = drawStart; y < drawEnd; y++) {
                int d = y * 256 - height * 128 + lineHeight * 128;
                int textureY = d * 32 / lineHeight / 256;
                int color = texture.getRGB(textureX, textureY);
                g.setColor(new Color(color));
                g.drawLine(x, y, x, y);
            }

            for (int y = drawEnd + 1; y < height; y++) {
                double cameraZ = 0.5 * height;
                double rowDistance = cameraZ / (y - height / 2);
                double floorX = this.player.x + rowDistance * rayDirX;
                double floorY = this.player.y + rowDistance * rayDirY;
                int floorMapX = (int) floorX;
                int floorMapY = (int) floorY;

                // Render the floor even if there are adjacent walls
                if (floorMapX >= 0 && floorMapX < map.length && floorMapY >= 0 && floorMapY < map[0].length) {
                    if (map[floorMapX][floorMapY] == 2) {
                        g.setColor(exitColor);
                    } else if (map[floorMapX][floorMapY] == 0 || map[floorMapX][floorMapY] == 5) {  // Ensure floor renders under sprites
                        g.setColor(floorColor);
                    }
                    g.drawLine(x, y, x, y);
                }
            }

        }
    }
}
