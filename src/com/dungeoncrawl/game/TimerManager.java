package com.dungeoncrawl.game;

public class TimerManager {
    private long startTime;
    private long endTime;
    private boolean timerRunning = false;
    private boolean timerStarted = false;

    public void start() {
        if (!this.timerStarted) {
            this.timerStarted = true;
            this.timerRunning = true;
            this.startTime = System.nanoTime();
            System.out.println("Timer started");  // Log statement
        }
    }


    public void stop() {
        if (this.timerRunning) {
            this.timerRunning = false;
            this.endTime = System.nanoTime();
        }
    }

    public boolean isRunning() {
        return this.timerRunning;
    }

    public boolean isStarted() {
        return this.timerStarted;
    }

    public double getElapsedTime() {
        if (!this.timerStarted) {
            return 0.0;
        } else {
            return this.timerRunning ? (double)(System.nanoTime() - this.startTime) / 1.0E9 : (double)(this.endTime - this.startTime) / 1.0E9;
        }
    }

    public String formatElapsedTime() {
        double elapsedTime = this.getElapsedTime();
        int minutes = (int)(elapsedTime / 60.0);
        int seconds = (int)(elapsedTime % 60.0);
        return String.format("%d:%02d", minutes, seconds);
    }
}