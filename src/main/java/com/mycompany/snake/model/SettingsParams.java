/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model;

/**
 *
 * @author Eduard
 */
public class SettingsParams {
    
    public static final int DEFAULT_SELECTED_INDEX = 0;
    
    public static final String[] BOARD_NAMES = {"Medium", "Easy", "Difficult"}; // col & row = 17 15 / 10 9 / 24 21
    public static final int[][] BOARD_VALUES = {{595, 525, 35}, {580, 522, 58}, {600, 525, 25}}; // { Board_Width, Board_Height, Square_Size }
    
    public static final String[] SPEED_NAMES = {"Normal", "Fast", "Slow"};
    public static final int[] SPEED_VALUES = {127, 85, 170};
    
    public static final String[] FOOD_NAMES = {"1", "3", "5", "Random"};
    public static final int[] FOOD_VALUES = {1, 3, 5, -1};
    
    public static final String[] MODE_NAMES = {"Classic", "Wall", "Cheese", "Winged", "Boundless", "Statue", "Glow"};
    public static final String[] SPAWN_RADIUS_MODES = {"Wall"};
    public static final int SPAWN_RADIUS_WIDTH = 7;
}
