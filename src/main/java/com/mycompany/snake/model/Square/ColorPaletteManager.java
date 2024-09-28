/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model.Square;

import java.awt.Color;

/**
 *
 * @author Eduard
 */
public class ColorPaletteManager {
    
    public static final int DEFAULT_SELECTED_INDEX = 0;
    
    public static final Color[] BOARD_COLOR_PALETTE = {
        Color.BLACK, new Color(157, 226, 113), new Color(104, 100, 120), new Color(222, 236, 237),
        new Color(255, 120, 120), new Color(249, 227, 146), new Color(104, 162, 106), 
        new Color(135, 206, 250), new Color(149, 117, 180)};
    
    public static final Color[] FOOD_COLOR_PALETTE = {
        Color.RED, new Color(231, 71, 29),
        new Color(152, 35, 175), new Color(0, 153, 0), new Color(239, 206, 19),
        new Color(254, 138, 0), new Color(255, 136, 108), new Color(0, 112, 180)};
    
    public static final Color[] SNAKE_COLOR_PALETTE = {
        Color.GREEN, new Color(73, 119, 237), new Color(25, 213, 227), new Color(178, 67, 239), 
        new Color(233, 64, 177), new Color(242, 56, 59), new Color(245, 153, 55),
        new Color(234, 212, 19), new Color(52, 178, 60), new Color(103, 103, 103), 
        new Color(239, 239, 239)};
    
    public static final double BORDER_FACTOR = 0.5;
    public static final double TOP_MENU_FACTOR = 0.3;
    public static final double WALL_FACTOR = 0.5;
    
    public void boardColorChanged(Color newColor) {
        
        CellType.EMPTY.setColor(newColor);
        
        if (newColor.equals(Color.BLACK)) {
            CellType.WALL_SIMPLE.setColor(Color.GRAY);
        } else {
            CellType.WALL_SIMPLE.setColor(darkenColor(newColor, WALL_FACTOR));
        }
        
        // TEST LINE TODO
        CellType.TEST.setColor(Color.CYAN);
    }
    
    public void foodColorChanged(Color newColor) {
        CellType.FOOD.setColor(newColor);
    }
    
    public static final double HEAD_FACTOR = 0.60;
    public static final double STATUE_FACTOR = 0.4;
    public static final double CRACKED_STATUE_FACTOR = 0.65;
    
    public void snakeColorChanged(Color newColor) {
        
        CellType.SNAKE_BODY.setColor(newColor);
        CellType.SNAKE_HEAD.setColor(darkenColor(newColor, HEAD_FACTOR));
        
        Color statueColor = darkenColor(newColor, STATUE_FACTOR);
        
        CellType.WALL_FILLED.setColor(statueColor);
        CellType.WALL_STATUE.setColor(statueColor);
        CellType.WALL_CRACKED.setColor(lightenColor(newColor, CRACKED_STATUE_FACTOR));
    }
    
    public static Color darkenColor (Color color, double darkenFactor) {
        
        int newColorRed = (int) (color.getRed() * darkenFactor);
        int newColorGreen = (int) (color.getGreen() * darkenFactor);
        int newColorBlue = (int) (color.getBlue() * darkenFactor);
        
        return new Color(newColorRed, newColorGreen, newColorBlue);
    }
    
    public static Color lightenColor (Color color, double lightenFactor) {
        
        int newColorRed = (int) (color.getRed() * lightenFactor + 255 * (1 - lightenFactor));
        int newColorGreen = (int) (color.getGreen() * lightenFactor + 255 * (1 - lightenFactor));
        int newColorBlue = (int) (color.getBlue() * lightenFactor + 255 * (1 - lightenFactor));
        
        // Comprobación no necesaria, pero se mantiene como buena práctica para proporcionar 
        // mayor seguridad y robustez a largo plazo
        newColorRed = Math.min(newColorRed, 255);
        newColorGreen = Math.min(newColorGreen, 255);
        newColorBlue = Math.min(newColorBlue, 255);
        
        return new Color(newColorRed, newColorGreen, newColorBlue);
    }
}
