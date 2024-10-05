/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model.Square;

import com.mycompany.snake.model.Square.CellConfiguration.CellType;
import com.mycompany.snake.model.Square.CellConfiguration.SpecificCellType;
import java.awt.Color;
import java.awt.Point;

/**
 *
 * @author Eduard
 */
public class StatueDimensionSquare extends StatueSquare implements DimensionSquareInterface {
    
    protected boolean otherDimension;
    
    public StatueDimensionSquare(int col, int row, boolean otherDimension) {
        super(col, row);
        this.otherDimension = otherDimension;
    }
    
    public StatueDimensionSquare(Point pos, boolean otherDimension) {
        super(pos);
        this.otherDimension = otherDimension;
    }
    
    public StatueDimensionSquare(int col, int row, SpecificCellType specificCellType, boolean otherDimension) {
        super(col, row, specificCellType);
        this.otherDimension = otherDimension;
    }
    
    public StatueDimensionSquare(Point pos, SpecificCellType specificCellType, boolean otherDimension) {
        super(pos, specificCellType);
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
        
        Color squareColor = super.getColor();
        
        if (otherDimension) {
            return new Color(squareColor.getRed(), squareColor.getGreen(), squareColor.getBlue(), DimensionSquare.ALPHA_OTHER_DIMENSION);
        } else {
            return squareColor;
        }
    }
}
