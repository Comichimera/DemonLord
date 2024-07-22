package com.dungeoncrawl.game;

import java.awt.Component;
import java.util.Locale;
import javax.swing.JFrame;

public class GameWindow extends JFrame {
    public GameWindow() {
        this.setTitle("Dungeon Crawler");
        this.setSize(800, 600);
        this.setDefaultCloseOperation(3);
        this.setLocationRelativeTo((Component)null);
        Locale locale = new Locale("en");
        GamePanel gamePanel = new GamePanel(locale);
        this.add(gamePanel);
    }
}
