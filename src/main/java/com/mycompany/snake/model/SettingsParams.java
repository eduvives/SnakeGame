/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Eduard
 */
public class SettingsParams {
    
    public static final int DEFAULT_SELECTED_INDEX = 0;
    
    public static final Map<String, int[]> BOARDS = new LinkedHashMap<>(); // col & row = 17 15 / 10 9 / 24 21
    
    static {
        BOARDS.put("Medium", new int[]{595, 525, 35}); // { Board_Width, Board_Height, Square_Size }
        BOARDS.put("Easy", new int[]{580, 522, 58}); 
        BOARDS.put("Difficult", new int[]{600, 525, 25});
    }
    
    public static String[] getBoardNames() {
        return BOARDS.keySet().toArray(String[]::new);
    }
    
    public static final Map<String, Integer> SPEEDS = new LinkedHashMap<>();
    
    static {
        SPEEDS.put("Normal", 135);
        SPEEDS.put("Fast", 89);
        SPEEDS.put("Slow", 179);
    }
    
    public static String[] getSpeedNames() {
        return SPEEDS.keySet().toArray(String[]::new);
    }
    
    public static final Map<String, Integer> FOODS = new LinkedHashMap<>();
    
    static {
        FOODS.put("1", 1);
        FOODS.put("3", 3);
        FOODS.put("5", 5);
        FOODS.put("Random", -1);
    }
    
    public static String[] getFoodNames() {
        return FOODS.keySet().toArray(String[]::new);
    }
    
    public static final String[] MODE_NAMES = {"Classic", "Wall", "Cheese", "Boundless", "Shrink", "Twin", "Statue", "Dimension", "Peaceful", "Blender"};
    public static final String[] BLENDER_MODE_EXCLUDED_MODES = {"Classic", "Blender"};
}
