/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model;

import java.awt.Color;
import java.awt.Point;

/**
 *
 * @author Eduard
 */
public class DimensionSquare extends Square {
    
    protected boolean otherDimension;
    private int alphaOtherDimension = 128; // 50% de transparencia
    
    public DimensionSquare(int col, int row, CellType cellType, boolean otherDimension) {
        super(col, row, cellType);
        this.otherDimension = otherDimension;
    }
    
    public DimensionSquare(Point pos, CellType cellType, boolean otherDimension) {
        super(pos, cellType);
        this.otherDimension = otherDimension;
    }

    public boolean isOtherDimension() {
        return otherDimension;
    }

    public void toggleDimension() {
        otherDimension = !otherDimension;
    }
    
    @Override
    public Color getColor() {
        
        Color squareColor = cellType.getColor();
        
        if (otherDimension) {
            return new Color(squareColor.getRed(), squareColor.getGreen(), squareColor.getBlue(), alphaOtherDimension);
        } else {
            return squareColor;
        }
    }
}
