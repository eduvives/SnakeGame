/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.mycompany.snake.model;

import java.awt.Color;

/**
 *
 * @author Eduard
 */
public enum CellType {
    
    EMPTY(Color.BLACK),
    SNAKE_HEAD(new Color(0,128,0)),
    SNAKE_BODY(Color.GREEN),
    FOOD(Color.RED),
    WALL_SIMPLE(Color.GRAY),
    WALL_FILLED(Color.DARK_GRAY),
    WALL_STATUE(Color.DARK_GRAY),   
    WALL_CRACKED(Color.LIGHT_GRAY),
    TEST(Color.CYAN);

    private final Color color;

    // Constructor para asociar el color con cada tipo de celda
    CellType(Color color) {
        this.color = color;
    }

    // Método para obtener el color asociado
    public Color getColor() {
        return color;
    }
}
