package com.dungeoncrawl.game;

import java.awt.image.BufferedImage;
import java.util.Random;

public class Sprite {
    private double x;
    private double y;
    private BufferedImage image;
    private int[][] map;
    private double moveSpeed = 0.03;

    private double dirX;
    private double dirY;
    private long moveTime; // Time when the sprite should change direction
    private long moveDuration = 1000; // Duration in milliseconds to move in one direction
    private Random random = new Random();

    public Sprite(double x, double y, BufferedImage image, int[][] map) {
        this.x = x;
        this.y = y;
        this.image = image;
        this.map = map;
        setRandomDirection();
    }

    private void setRandomDirection() {
        dirX = random.nextDouble() * 2 - 1; // Random direction X
        dirY = random.nextDouble() * 2 - 1; // Random direction Y
        // Normalize the direction vector
        double length = Math.sqrt(dirX * dirX + dirY * dirY);
        if (length != 0) {
            dirX /= length;
            dirY /= length;
        }
        moveTime = System.currentTimeMillis(); // Reset moveTime when direction changes
    }

    public void update() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - moveTime > moveDuration) {
            setRandomDirection(); // Change direction after moveDuration
        }

        double newX = x + dirX * moveSpeed;
        double newY = y + dirY * moveSpeed;

        // Ensure new position is within bounds and not colliding with walls
        if (map[(int)newX][(int)newY] == 0) {
            x = newX;
            y = newY;
        }
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
}