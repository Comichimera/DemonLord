package com.dungeoncrawl.game;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class ImageLoader {
    public static BufferedImage loadImage(String path) {
        BufferedImage image = null;
        try {
            InputStream is = ImageLoader.class.getResourceAsStream(path);
            if (is != null) {
                image = ImageIO.read(is);
                System.out.println("Image loaded from path: " + path);
            } else {
                System.out.println("Image not found at path: " + path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
