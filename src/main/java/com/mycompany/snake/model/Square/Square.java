/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model.Square;

import com.mycompany.snake.model.Square.CellConfiguration.CellType;
import com.mycompany.snake.model.Square.CellConfiguration.SpecificCellType;
import static com.mycompany.snake.model.Square.CellConfiguration.defaultSpecificTypes;
import static com.mycompany.snake.model.Square.CellConfiguration.specificTypeColors;
import static com.mycompany.snake.model.Square.CellConfiguration.validateSpecificCellType;
import java.awt.Color;
import java.awt.Point;

/**
 *
 * @author Eduard
 */
public class Square extends Point {
    
    private CellType cellType;
    private SpecificCellType specificCellType;
    
    public Square(CellType cellType) {
        this.cellType = cellType;
        specificCellType = defaultSpecificTypes.get(cellType);
    }
    
    public Square(int col, int row, CellType cellType) {
        super(col, row);
        this.cellType = cellType;
        specificCellType = defaultSpecificTypes.get(cellType);
    }
    
    public Square(Point pos, CellType cellType) {
        super(pos);
        this.cellType = cellType;
        specificCellType = defaultSpecificTypes.get(cellType);
    }
    
    public Square(CellType cellType, SpecificCellType specificCellType) {
        validateSpecificCellType(cellType, specificCellType);
        this.cellType = cellType;
        this.specificCellType = specificCellType;
    }
    
    public Square(int col, int row, CellType cellType, SpecificCellType specificCellType) {
        super(col, row);
        validateSpecificCellType(cellType, specificCellType);
        this.cellType = cellType;
        this.specificCellType = specificCellType;
    }
    
    public Square(Point pos, CellType cellType, SpecificCellType specificCellType) {
        super(pos);
        validateSpecificCellType(cellType, specificCellType);
        this.cellType = cellType;
        this.specificCellType = specificCellType;
    }

    public SpecificCellType getSpecificCellType() {
        return specificCellType;
    }

    public void setSpecificCellType(SpecificCellType specificCellType) {
        validateSpecificCellType(cellType, specificCellType);
        this.specificCellType = specificCellType;
    }
    
    // Obtener el color asociado al tipo específico de celda
    public Color getColor() {
        return specificTypeColors.get(specificCellType);
    }
}