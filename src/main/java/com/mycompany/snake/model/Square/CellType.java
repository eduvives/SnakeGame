/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.mycompany.snake.model.Square;

import java.awt.Color;

/**
 *
 * @author Eduard
 */
public enum CellType {

    EMPTY,
    SNAKE_HEAD,
    SNAKE_BODY,
    FOOD,
    WALL_SIMPLE,
    WALL_FILLED,
    WALL_STATUE,   
    WALL_CRACKED,
    TEST;
    
    private Color color;

    // Método para obtener el color asociado
    public Color getColor() {
        return color;
    }
    
    // Método para modificar el color asociado
    public void setColor(Color color) {
        this.color = color;
    }
}
