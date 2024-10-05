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
public class DimensionSquare extends Square implements DimensionSquareInterface {
    
    protected boolean otherDimension;
    protected static final int ALPHA_OTHER_DIMENSION = 128; // 50% de transparencia
    
    public DimensionSquare(int col, int row, CellType cellType, boolean otherDimension) {
        super(col, row, cellType);
        this.otherDimension = otherDimension;
    }
    
    public DimensionSquare(Point pos, CellType cellType, boolean otherDimension) {
        super(pos, cellType);
        this.otherDimension = otherDimension;
    }
    
    public DimensionSquare(int col, int row, CellType cellType, SpecificCellType specificCellType, boolean otherDimension) {
        super(col, row, cellType, specificCellType);
        this.otherDimension = otherDimension;
    }
    
    public DimensionSquare(Point pos, CellType cellType, SpecificCellType specificCellType, boolean otherDimension) {
        super(pos, cellType, specificCellType);
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
            return new Color(squareColor.getRed(), squareColor.getGreen(), squareColor.getBlue(), ALPHA_OTHER_DIMENSION);
        } else {
            return squareColor;
        }
    }
}
