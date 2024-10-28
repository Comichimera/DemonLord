package com.dungeoncrawl.game;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InputLogger {
    private BufferedWriter writer;
    private String lastAction = "";
    private int frameCount = 0;
    private boolean firstInputLogged = false;

    public InputLogger() {
        String fileName = "game_log_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt";
        // writer = new BufferedWriter(new FileWriter(fileName, true));
    }

    public void logAction(String action) {
        if (writer != null) {
            if (action.equals(lastAction)) {
                frameCount++;
            } else {
                flushLog();
                lastAction = action;
                frameCount = 1;
            }
        }
    }

    public void flushLog() {
        try {
            if (writer != null && frameCount > 0) {
                if (!firstInputLogged && lastAction.equals("N")) {
                    // Skip logging the initial "N" action
                    frameCount = 0;
                } else {
                    if (!firstInputLogged && !lastAction.equals("N")) {
                        firstInputLogged = true;
                    }
                    writer.write(lastAction + " x" + frameCount);
                    writer.newLine();
                    lastAction = "";
                    frameCount = 0;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            flushLog();
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
