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
    
    // CheeseSnake - DimensionSnake
    
    @Override
    public void initializeBody() {
        
        if (modes.contains("Cheese") && modes.contains("Dimension")) {
            initializeBodyCheeseDimensionSnake();
        } else if (modes.contains("Cheese")) {
            cheeseSnake.initializeBody();
        } else if (modes.contains("Dimension")) {
            dimensionSnake.initializeBody();
        } else {
            super.initializeBody();
        }
    }
    
    private void initializeBodyCheeseDimensionSnake() {
        
        for (int i = 1; i <= CheeseSnake.CHEESE_START_LENGTH - 1; i++) {
            
            int posX = cheeseSnake.head.x - (i * 2);
            
            cheeseSnake.emptyBody.addLast(new Square(posX + 1, cheeseSnake.head.y, CellType.EMPTY));
            cheeseSnake.body.addLast(new DimensionSquare(posX, cheeseSnake.head.y, CellType.SNAKE_BODY, false));
        }
        
        cheeseSnake.nextBodyPartSnake = true;
    }
    
    @Override
    public void move(Point newPos, boolean isFoodCollision) {
        
        if (modes.contains("Cheese") && modes.contains("Dimension")) {
            moveCheeseDimensionSnake(newPos, isFoodCollision);
        } else if (modes.contains("Cheese")) {
            cheeseSnake.move(newPos, isFoodCollision);
        } else if (modes.contains("Dimension")) {
            dimensionSnake.move(newPos, isFoodCollision);
        } else {
            super.move(newPos, isFoodCollision);
        }
    }
    
    private void moveCheeseDimensionSnake(Point newPos, boolean grow) {
        
        if (grow) cheeseSnake.growCount += 2;
        
        if (cheeseSnake.nextBodyPartSnake) {
            cheeseSnake.body.addFirst(new DimensionSquare(cheeseSnake.head, CellType.SNAKE_BODY, false));

            if (cheeseSnake.growCount <= 0) {
                cheeseSnake.body.removeLast();
            } 
        } else {
            cheeseSnake.emptyBody.addFirst(new Square(cheeseSnake.head, CellType.EMPTY));
            
            if (cheeseSnake.growCount <= 0) {
                cheeseSnake.emptyBody.removeLast();
            }
        }
        
        if (cheeseSnake.growCount > 0) {
            cheeseSnake.growCount--;
        }
        
        cheeseSnake.head.setLocation(newPos);
        
        cheeseSnake.nextBodyPartSnake = !cheeseSnake.nextBodyPartSnake;
    }
}
