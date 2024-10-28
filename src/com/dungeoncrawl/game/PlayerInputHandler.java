package com.dungeoncrawl.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PlayerInputHandler implements KeyListener {
    private Player player;
    private TimerManager timerManager;
    private GamePanel gamePanel;

    public PlayerInputHandler(Player player, TimerManager timerManager, GamePanel gamePanel) {
        this.player = player;
        this.timerManager = timerManager;
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        timerManager.start(); // Ensure the timer starts on any key press


        switch (key) {
            case KeyEvent.VK_W:
                player.setCurrentAction("F");
                player.moveForward();
                break;
            case KeyEvent.VK_S:
                player.setCurrentAction("B");
                player.moveBackward();
                break;
            case KeyEvent.VK_A:
                player.setCurrentAction("L");
                player.turnLeft();
                break;
            case KeyEvent.VK_D:
                player.setCurrentAction("R");
                player.turnRight();
                break;
            case KeyEvent.VK_SPACE:
                player.setCurrentAction("I");
                if (gamePanel.isShowEndScreen()) {
                    gamePanel.handleEndScreenKeyPress();
                } else if (gamePanel.showIntroScreen) {  // Check for intro screen
                    System.out.println("Pressed");
                    gamePanel.playAudio("src/resources/media/skelly_dance.wav");
                    gamePanel.handleIntroScreenKeyPress();
                } else if (player.isAtExit()) {
                    timerManager.stop();
                    gamePanel.update();
                } else {
                    player.openAdjacentDoor();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
