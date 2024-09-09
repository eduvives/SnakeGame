/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model;

import java.awt.Point;
import java.util.List;

/**
 *
 * @author Eduard
 */
public class BlenderSnake extends Snake {
    
    private List<String> modes;
    private CheeseSnake cheeseSnake;
    private DimensionSnake dimensionSnake;
    
    public BlenderSnake(List<String> modes) {
        super();

        cheeseSnake = new CheeseSnake(this);
        dimensionSnake = new DimensionSnake(this);
        
        setBlenderSnakeModes(modes);
    }
    
    protected void setBlenderSnakeModes(List<String> modes) {
        this.modes = modes;
    }

    public CheeseSnake getCheeseSnake() {
        return cheeseSnake;
    }
    
    public DimensionSnake getDimensionSnake() {
        return dimensionSnake;
    }
    
    // CheeseSnake
    
    @Override
    public void initializeBody() {
        if (!modes.contains("Cheese")) {
            super.initializeBody();
        }
    }
    
    @Override
    public void move(Point newPos, boolean isFoodCollision) {
        
        if (modes.contains("Cheese")) {
            cheeseSnake.move(newPos, isFoodCollision);
        } else {
            super.move(newPos, isFoodCollision);
        }
    }
    
    // DimensionSnake
    
    @Override
    protected Square createSquare(int col, int row, CellType cellType){
        
        if (modes.contains("Dimension")) {
            return dimensionSnake.createSquare(col, row, cellType);
        } else {
            return super.createSquare(col, row, cellType);
        }
    }
    
    @Override
    protected Square createSquare(Point pos, CellType cellType) {
        
        if (modes.contains("Dimension")) {
            return dimensionSnake.createSquare(pos, cellType);
        } else {
            return super.createSquare(pos, cellType);
        }
    }
}
