/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model.Square;

import com.mycompany.snake.model.Square.CellConfiguration.CellType;
import com.mycompany.snake.model.Square.CellConfiguration.SpecificCellType;
import java.awt.Point;

/**
 *
 * @author Eduard
 */
public class StatueSquare extends Square {

    private int foodBeforeBreak;
    
    public StatueSquare(int col, int row) {
        super(col, row, CellType.STATUE);
    }
    
    public StatueSquare(Point pos) {
        super(pos, CellType.STATUE);
    }
    
    public StatueSquare(int col, int row, SpecificCellType specificCellType) {
        super(col, row, CellType.STATUE, specificCellType);
    }
    
    public StatueSquare(Point pos, SpecificCellType specificCellType) {
        super(pos, CellType.STATUE, specificCellType);
    }

    public int getFoodBeforeBreak() {
        return foodBeforeBreak;
    }

    public void setFoodBeforeBreak(int foodBeforeBreak) {
        this.foodBeforeBreak = foodBeforeBreak;
    }
    
    public void decreaseFoodBeforeBreak() {
        foodBeforeBreak -= 1;
    }
}
