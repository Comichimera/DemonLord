package com.dungeoncrawl.game;

public class GameLoop implements Runnable {
    private GamePanel gamePanel;
    private Thread gameThread;
    private boolean running = false;
    private final int FPS = 60;
    private final long targetTime = 1000 / FPS;

    public GameLoop(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void start() {
        this.running = true;
        this.gameThread = new Thread(this);
        this.gameThread.start();
    }

    public void stop() {
        this.running = false;
        try {
            this.gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (this.running) {
            long start = System.nanoTime();

            this.gamePanel.update();
            this.gamePanel.repaint();

            long elapsed = System.nanoTime() - start;
            long wait = this.targetTime - elapsed / 1000000L;

            if (wait > 0) {
                try {
                    Thread.sleep(wait);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
