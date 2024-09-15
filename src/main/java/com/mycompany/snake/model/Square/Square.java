/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model.Square;

import java.awt.Color;
import java.awt.Point;

/**
 *
 * @author Eduard
 */
public class Square extends Point {
    
    protected CellType cellType;
    
    public Square() {
        super();
    }
    
    public Square(int col, int row, CellType cellType) {
        super(col, row);
        this.cellType = cellType;
    }
    
    public Square(Point pos, CellType cellType) {
        super(pos);
        this.cellType = cellType;
    }
    
    public CellType getCellType() {
        return cellType;
    }

    public void setCellType(CellType cellType) {
        this.cellType = cellType;
    }
    
    public Color getColor() {
        return cellType.getColor();
    }
}