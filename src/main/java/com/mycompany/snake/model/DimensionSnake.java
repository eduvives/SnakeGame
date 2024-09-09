/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model;

import java.awt.Point;

/**
 *
 * @author Eduard
 */
public class DimensionSnake extends Snake {
    
    public DimensionSnake() {
        super();
    }
    
    public DimensionSnake(Snake snake) {
        super(snake);
    }
    
    @Override
    protected Square createSquare(int col, int row, CellType cellType){
        return new DimensionSquare(col, row, cellType, false);
    }
    
    @Override
    protected Square createSquare(Point pos, CellType cellType) {
        return new DimensionSquare(pos, cellType, false);
    }
}
