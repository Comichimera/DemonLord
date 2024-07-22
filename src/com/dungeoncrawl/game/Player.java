package com.dungeoncrawl.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.List;

public class Player implements KeyListener {
    public double x;
    public double y;
    public double dirX;
    public double dirY;
    public double planeX;
    public double planeY;
    public int[][] map;
    public BufferedImage wallTexture;
    public BufferedImage doorTexture;
    private double moveSpeed;
    private double rotSpeed;
    private GamePanel gamePanel;
    private TimerManager timerManager;
    private InputLogger inputLogger;
    private String currentAction = "N";
    private List<Sprite> sprites;

    public Player(double x, double y, double dirAngle, BufferedImage wallTexture, BufferedImage doorTexture, int[][] map, double moveSpeed, double rotSpeed, GamePanel gamePanel, TimerManager timerManager, List<Sprite> sprites) {
        this.x = x;
        this.y = y;
        this.dirX = Math.cos(dirAngle);
        this.dirY = Math.sin(dirAngle);
        this.planeX = -0.66 * this.dirY;
        this.planeY = 0.66 * this.dirX;
        this.wallTexture = wallTexture;
        this.doorTexture = doorTexture;
        this.map = map;
        this.moveSpeed = moveSpeed;
        this.rotSpeed = rotSpeed;
        this.gamePanel = gamePanel;
        this.timerManager = timerManager;
        this.sprites = sprites;
        this.inputLogger = new InputLogger();
    }

    public void update() {
        inputLogger.logAction(currentAction);
        currentAction = "N";  // Reset action for the next frame
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        System.out.println("Player key pressed: " + key);  // Debug statement
        this.timerManager.start();  // Timer start
        System.out.println("Timer start called.");  // Debug statement

        if (key == 87) { // W key
            setCurrentAction("F");
            moveForward();
        } else if (key == 83) { // S key
            setCurrentAction("B");
            moveBackward();
        } else if (key == 65) { // A key
            setCurrentAction("L");
            turnLeft();
        } else if (key == 68) { // D key
            setCurrentAction("R");
            turnRight();
        } else if (key == 32) { // Space key
            setCurrentAction("I"); // Interaction
            if (this.gamePanel.isShowEndScreen()) {
                this.gamePanel.handleEndScreenKeyPress();
            } else if (this.isAtExit()) {
                this.timerManager.stop();
                System.out.println("Timer stopped. Player reached the exit."); // Debug statement
                this.gamePanel.update();
            } else {
                openAdjacentDoor();
            }
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void setCurrentAction(String action) {
        this.currentAction = action;
    }

    public void moveForward() {
        if (canMoveTo(this.x + this.dirX * this.moveSpeed, this.y)) {
            this.x += this.dirX * this.moveSpeed;
        }

        if (canMoveTo(this.x, this.y + this.dirY * this.moveSpeed)) {
            this.y += this.dirY * this.moveSpeed;
        }
    }

    public void moveBackward() {
        if (canMoveTo(this.x - this.dirX * this.moveSpeed, this.y)) {
            this.x -= this.dirX * this.moveSpeed;
        }

        if (canMoveTo(this.x, this.y - this.dirY * this.moveSpeed)) {
            this.y -= this.dirY * this.moveSpeed;
        }
    }

    public void turnLeft() {
        double oldDirX = this.dirX;
        this.dirX = this.dirX * Math.cos(-this.rotSpeed) - this.dirY * Math.sin(-this.rotSpeed);
        this.dirY = oldDirX * Math.sin(-this.rotSpeed) + this.dirY * Math.cos(-this.rotSpeed);
        double oldPlaneX = this.planeX;
        this.planeX = this.planeX * Math.cos(-this.rotSpeed) - this.planeY * Math.sin(-this.rotSpeed);
        this.planeY = oldPlaneX * Math.sin(-this.rotSpeed) + this.planeY * Math.cos(-this.rotSpeed);
    }

    public void turnRight() {
        double oldDirX = this.dirX;
        this.dirX = this.dirX * Math.cos(this.rotSpeed) - this.dirY * Math.sin(this.rotSpeed);
        this.dirY = oldDirX * Math.sin(this.rotSpeed) + this.dirY * Math.cos(this.rotSpeed);
        double oldPlaneX = this.planeX;
        this.planeX = this.planeX * Math.cos(this.rotSpeed) - this.planeY * Math.sin(this.rotSpeed);
        this.planeY = oldPlaneX * Math.sin(this.rotSpeed) + this.planeY * Math.cos(this.rotSpeed);
    }

    public void openAdjacentDoor() {
        int playerX = (int)this.x;
        int playerY = (int)this.y;
        if (this.map[playerX + 1][playerY] == 4) {
            this.map[playerX + 1][playerY] = 0;
        } else if (this.map[playerX - 1][playerY] == 4) {
            this.map[playerX - 1][playerY] = 0;
        } else if (this.map[playerX][playerY + 1] == 4) {
            this.map[playerX][playerY + 1] = 0;
        } else if (this.map[playerX][playerY - 1] == 4) {
            this.map[playerX][playerY - 1] = 0;
        }
    }

    public boolean isAtExit() {
        int playerX = (int)this.x;
        int playerY = (int)this.y;
        boolean atExit = this.map[playerX][playerY] == 2;
        return atExit;
    }

    private boolean canMoveTo(double newX, double newY) {
        int newMapX = (int) newX;
        int newMapY = (int) newY;
        for (Sprite sprite : sprites) {
            if ((int) sprite.getX() == newMapX && (int) sprite.getY() == newMapY) {
                return false;
            }
        }
        return this.map[newMapX][newMapY] == 0 || this.map[newMapX][newMapY] == 2;
    }

    public void closeLogger() {
        inputLogger.flushLog();
        inputLogger.close();
    }
}
