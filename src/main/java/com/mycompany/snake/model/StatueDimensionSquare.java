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
public class StatueDimensionSquare extends StatueSquare implements DimensionSquareInterface {
    
    protected boolean otherDimension;
    
    public StatueDimensionSquare(int col, int row, CellType cellType, boolean otherDimension) {
        super(col, row, cellType);
        this.otherDimension = otherDimension;
    }
    
    public StatueDimensionSquare(Point pos, CellType cellType, boolean otherDimension) {
        super(pos, cellType);
        this.otherDimension = otherDimension;
    }
    
    @Override
    public boolean isOtherDimension() {
        return otherDimension;
    }

    @Override
    public void toggleDimension() {
        otherDimension = !otherDimension;
    }
    
    @Override
    public Color getColor() {
        
        Color squareColor = cellType.getColor();
        
        if (otherDimension) {
            return new Color(squareColor.getRed(), squareColor.getGreen(), squareColor.getBlue(), DimensionSquare.ALPHA_OTHER_DIMENSION);
        } else {
            return squareColor;
        }
    }
}
