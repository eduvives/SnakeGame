/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model;

import com.mycompany.snake.model.SettingsParams.CellType;
import java.awt.Point;

/**
 *
 * @author Eduard
 */
public class Square extends Point {
    
    private CellType cellType;

    public CellType getCellType() {
        return cellType;
    }

    public void setCellType(CellType cellType) {
        this.cellType = cellType;
    }
    
}