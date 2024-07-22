package com.dungeoncrawl.game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

public class SpriteRenderer {
    private Player player;
    private List<Sprite> sprites;

    public SpriteRenderer(Player player, List<Sprite> sprites) {
        this.player = player;
        this.sprites = sprites;
    }

    public void renderSprites(Graphics g, int width, int height) {

        for (Sprite sprite : sprites) {

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
                if (transformY > 0 && stripe > 0 && stripe < width) {
                    for (int y = drawStartY; y < drawEndY; y++) {
                        int d = y * 256 - height * 128 + spriteHeight * 128;
                        int texY = ((d * sprite.getImage().getHeight()) / spriteHeight) / 256;
                        int color = sprite.getImage().getRGB(texX, texY);

                        // Filter out transparent pixels
                        if ((color & 0xFF000000) != 0) {
                            g.setColor(new Color(color, true)); // true to respect alpha channel
                            g.drawLine(stripe, y, stripe, y);
                        }
                    }
                }
            }
        }
    }
}
