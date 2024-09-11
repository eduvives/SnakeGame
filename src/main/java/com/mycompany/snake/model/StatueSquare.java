/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model;

import java.awt.Point;
import java.util.Set;

/**
 *
 * @author Eduard
 */
public class StatueSquare extends Square {
    
    protected static final Set<CellType> ALLOWED_CELL_TYPES = Set.of(
        CellType.WALL_FILLED, 
        CellType.WALL_STATUE, 
        CellType.WALL_CRACKED
    );
    
    private int foodBeforeBreak;
    
    public StatueSquare(int col, int row, CellType cellType) {
        super(col, row, validateCellType(cellType));
    }
    
    public StatueSquare(Point pos, CellType cellType) {
        super(pos, validateCellType(cellType));
    }

    public int getFoodBeforeBreak() {
        return foodBeforeBreak;
    }

    public void setFoodBeforeBreak(int foodBeforeBreak) {
        this.foodBeforeBreak = foodBeforeBreak;
    }
    
    protected static CellType validateCellType(CellType cellType) {
        if (!ALLOWED_CELL_TYPES.contains(cellType)) {
            throw new IllegalArgumentException("Invalid CellType for StatueSquare: " + cellType);
        }
        return cellType;
    }
}
