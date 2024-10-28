package com.dungeoncrawl.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SpriteRenderer {
    private Player player;
    private List<Sprite> sprites;

    public void initializeSpritesFromMap(int[][] map, BufferedImage spriteImage) {
        for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
                if (map[row][col] == 5) {  // Number 5 represents a sprite location
                    // Ensure row (Y) and col (X) coordinates match the game world's orientation
                    sprites.add(new Sprite(row + 0.5, col + 0.5, spriteImage, map, player));  // Swap col and row here if needed
                    System.out.println("Sprite added at row: " + row + ", col: " + col); // Debugging line
                }
            }
        }
    }

    public SpriteRenderer(Player player, List<Sprite> sprites) {
        this.player = player;
        this.sprites = sprites;
    }

    public void renderSprites(Graphics g, int width, int height, double[] zBuffer) {
        // Sort sprites by distance from the player
        for (Sprite sprite : sprites) {
            sprite.setDistance((player.x - sprite.getX()) * (player.x - sprite.getX()) +
                    (player.y - sprite.getY()) * (player.y - sprite.getY()));
        }

        Collections.sort(sprites, new Comparator<Sprite>() {
            @Override
            public int compare(Sprite s1, Sprite s2) {
                return Double.compare(s2.getDistance(), s1.getDistance());
            }
        });

        // Render each sprite with depth check
        for (Sprite sprite : sprites) {
            renderSprite(g, sprite, width, height, zBuffer);
        }
    }

    private void renderSprite(Graphics g, Sprite sprite, int width, int height, double[] zBuffer) {
        double spriteX = sprite.getX() - player.x;
        double spriteY = sprite.getY() - player.y;

        double invDet = 1.0 / (player.planeX * player.dirY - player.dirX * player.planeY);

        double transformX = invDet * (player.dirY * spriteX - player.dirX * spriteY);
        double transformY = invDet * (-player.planeY * spriteX + player.planeX * spriteY);

        int spriteScreenX = (int) ((width / 2) * (1 + transformX / transformY));

        int spriteHeight = Math.abs((int) (height / transformY));
        int drawStartY = -spriteHeight / 2 + height / 2;
        if (drawStartY < 0) drawStartY = 0;
        int drawEndY = spriteHeight / 2 + height / 2;
        if (drawEndY >= height) drawEndY = height - 1;

        int spriteWidth = Math.abs((int) (height / transformY));
        int drawStartX = -spriteWidth / 2 + spriteScreenX;
        if (drawStartX < 0) drawStartX = 0;
        int drawEndX = spriteWidth / 2 + spriteScreenX;
        if (drawEndX >= width) drawEndX = width - 1;

        for (int stripe = drawStartX; stripe < drawEndX; stripe++) {
            int texX = (int) (256 * (stripe - (-spriteWidth / 2 + spriteScreenX)) * sprite.getImage().getWidth() / spriteWidth) / 256;
            if (transformY > 0 && stripe > 0 && stripe < width && transformY < zBuffer[stripe] + 0.1) {  // Added small offset
                for (int y = drawStartY; y < drawEndY; y++) {
                    int d = y * 256 - height * 128 + spriteHeight * 128;
                    int texY = ((d * sprite.getImage().getHeight()) / spriteHeight) / 256;
                    int color = sprite.getImage().getRGB(texX, texY);

                    if ((color & 0xFF000000) != 0) {
                        g.setColor(new Color(color, true));
                        g.drawLine(stripe, y, stripe, y);
                    }
                }
            }
        }
    }
}
