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
    
    public void boardColorChanged(Color newColor) {
        
        if (newColor.equals(Color.BLACK)) {
            CellType.EMPTY.setColor(newColor);
            CellType.WALL_SIMPLE.setColor(Color.GRAY);
        } else {
            int colorRed = newColor.getRed();
            int colorGreen = newColor.getGreen();
            int colorBlue = newColor.getBlue();

            double wallFactor = 0.5;

            CellType.EMPTY.setColor(newColor);
            CellType.WALL_SIMPLE.setColor(new Color((int) (colorRed * wallFactor), (int) (colorGreen * wallFactor), (int) (colorBlue * wallFactor)));
        }
        
        // TEST LINE TODO
        CellType.TEST.setColor(Color.CYAN);
    }
    
    public void foodColorChanged(Color newColor) {
        CellType.FOOD.setColor(newColor);
    }
    
    public void snakeColorChanged(Color newColor) {
        
        int colorRed = newColor.getRed();
        int colorGreen = newColor.getGreen();
        int colorBlue = newColor.getBlue();
        
        double headFactor = 0.5;
        double statueFactor = 0.3;
        double crackedStatueFactor = 0.8; // TODO color cracked más claro que el original (más pastel?) combinar rgb con algo de blanco?
        
        CellType.SNAKE_BODY.setColor(newColor);
        CellType.SNAKE_HEAD.setColor(new Color((int) (colorRed * headFactor), (int) (colorGreen * headFactor), (int) (colorBlue * headFactor)));
        
        Color statueColor = new Color((int) (colorRed * statueFactor), (int) (colorGreen * statueFactor), (int) (colorBlue * statueFactor));
        
        CellType.WALL_FILLED.setColor(statueColor);
        CellType.WALL_STATUE.setColor(statueColor);
        CellType.WALL_CRACKED.setColor(new Color((int) (colorRed * crackedStatueFactor), (int) (colorGreen * crackedStatueFactor), (int) (colorBlue * crackedStatueFactor)));
    }
}
