/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model.Snake;

import com.mycompany.snake.model.GameMode.SnakeListener;
import com.mycompany.snake.model.Square.DimensionSquare;
import com.mycompany.snake.model.Square.CellType;
import com.mycompany.snake.model.Square.Square;
import java.awt.Point;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Eduard
 */
public class BlenderSnake extends Snake {
    
    private List<String> modes;
    private CheeseSnake cheeseSnake;
    private BoundlessSnake boundlessSnake;
    private ShrinkSnake shrinkSnake;
    private TwinSnake twinSnake;
    private DimensionSnake dimensionSnake;
    
    public BlenderSnake(List<String> modes) {
        super();
        
        cheeseSnake = new CheeseSnake(this);
        boundlessSnake = new BoundlessSnake(this);
        shrinkSnake = new ShrinkSnake(this);
        twinSnake = new TwinSnake(this);
        dimensionSnake = new DimensionSnake(this);
        
        setBlenderSnakeModes(modes);
    }
    
    private void setBlenderSnakeModes(List<String> modes) {
        this.modes = modes;
    }
    
    @Override
    public void setListener(SnakeListener listener) {
        
        super.setListener(listener);
        
        cheeseSnake.listener = listener;
        boundlessSnake.listener = listener;
        shrinkSnake.listener = listener;
        twinSnake.listener = listener;
        dimensionSnake.listener = listener;
    }

    public CheeseSnake getCheeseSnake() {
        return cheeseSnake;
    }
    
    public BoundlessSnake getBoundlessSnake() {
        return boundlessSnake;
    }

    public ShrinkSnake getShrinkSnake() {
        return shrinkSnake;
    }
    
    public TwinSnake getTwinSnake() {
        return twinSnake;
    }
    
    public DimensionSnake getDimensionSnake() {
        return dimensionSnake;
    }
    
    // BlenderSnake - DimensionSnake

    private Square createSnakeBodyPart(int col, int row) {
        
        if (modes.contains("Dimension")) {
            return new DimensionSquare(col, row, CellType.SNAKE_BODY, false);
        } else {
            return new Square(col, row, CellType.SNAKE_BODY);
        }   
    }

    private Square createSnakeBodyPart(Point pos) {
        
        if (modes.contains("Dimension")) {
            return new DimensionSquare(pos, CellType.SNAKE_BODY, false);
        } else {
            return new Square(pos, CellType.SNAKE_BODY);
        }   
    }
    
    // BlenderSnake - ShrinkSnake
    
    public void reduce() {
        if (modes.contains("Cheese")) {
            reduceCheeseShrink();
        } else {
            shrinkSnake.reduce();
        }
    }
    
    private void reduceCheeseShrink() {
        
        Square firstBodyPart = cheeseSnake.removeFirstBody();
                
        setLocationHead(head.getLocation(), firstBodyPart);
        cheeseSnake.nextBodyPartSnake = firstBodyPart.getCellType() == CellType.SNAKE_BODY;
    }
    
    // CheeseSnake - DimensionSnake
    
    @Override
    protected void initializeBody() {
        
        if (modes.contains("Cheese")) {
            initializeBodyCheeseDimension();
        } else {
            initializeBodyBlender();
        }
    }
    
    private void initializeBodyCheeseDimension() {
        
        for (int i = 1; i <= CheeseSnake.CHEESE_START_LENGTH - 1; i++) {
            
            int posX = head.x - (i * 2);
            
            cheeseSnake.addLastBody(new Square(posX + 1, head.y, CellType.EMPTY));
            cheeseSnake.addLastBody(createSnakeBodyPart(posX, head.y));
        }
        
        cheeseSnake.nextBodyPartSnake = cheeseSnake.cheeseBody.getFirst().getCellType() != CellType.SNAKE_BODY;
    }
    
    private void initializeBodyBlender() { 
        for (int i = 1; i <= START_LENGTH - 1; i++) {
            addLastBody(createSnakeBodyPart(head.x - i, head.y));
        }
    }
    
    @Override
    public void move(Point newHeadPos, boolean isFoodCollision) {
        
        if (modes.contains("Cheese")) {
            moveCheeseDimension(newHeadPos, isFoodCollision);
        } else {
            moveBlender(newHeadPos, isFoodCollision);
        }
    }
    
    private void moveCheeseDimension(Point newHeadPos, boolean grow) {
        
        if (grow) cheeseSnake.growCount += 2;
        
        if (cheeseSnake.growCount <= 0) {
            cheeseSnake.removeLastBody();
        }
        
        Point previousHeadPos = head.getLocation();
        
        setLocationHead(previousHeadPos, newHeadPos);
        
        if (cheeseSnake.nextBodyPartSnake) { // If Next Body Part is Snake
            cheeseSnake.addFirstBody(createSnakeBodyPart(previousHeadPos));
        } else {
            cheeseSnake.addFirstBody(new Square(previousHeadPos, CellType.EMPTY));
        }
        
        if (cheeseSnake.growCount > 0) {
            cheeseSnake.growCount--;
        }
        
        cheeseSnake.invertNextBodyPartSnake();
    }
    
    private void moveBlender(Point newHeadPos, boolean grow) {
        
        if(!grow) removeLastBody();
        
        Point previousHeadPos = head.getLocation();
        
        setLocationHead(previousHeadPos, newHeadPos);
        addFirstBody(createSnakeBodyPart(previousHeadPos));
    }
    
    // TwinSnake - CheeseSnake - DimensionSnake
    
    public void switchSidesTwinCheeseDimension() {
        
        Point previousHeadPos = head.getLocation();
        Square lastBodyPart = cheeseSnake.removeLastBody();
        
        // Set Snake Head
        setLocationHead(previousHeadPos, lastBodyPart);

        // Set Snake Tail
        if (cheeseSnake.isNextBodyPartSnake()) {
            cheeseSnake.addFirstBody(createSnakeBodyPart(previousHeadPos));
        } else {
            cheeseSnake.addFirstBody(new Square(previousHeadPos, CellType.EMPTY));
        }

        Collections.reverse(body);
        Collections.reverse(cheeseSnake.cheeseBody);
        
        restoreDirection();
        cheeseSnake.nextBodyPartSnake = lastBodyPart.getCellType() == CellType.SNAKE_BODY;
    }
    
    // TwinSnake - DimensionSnake
    
    public void switchSidesBlender() {
        
        Point previousHeadPos = head.getLocation();
        
        setLocationHead(previousHeadPos, removeLastBody());
        addFirstBody(createSnakeBodyPart(previousHeadPos));
        
        Collections.reverse(body);
        
        restoreDirection();
    }
    
    // TwinSnake - BoundlessGame
    
    @Override
    public Point getDefaultDirection() {
        
        Point defaultDirection;
        
        if (modes.contains("Cheese")) {
            defaultDirection = cheeseSnake.getDefaultDirection();
        } else {
            defaultDirection = super.getDefaultDirection();
        }
        
        if (modes.contains("Boundless") || modes.contains("Peaceful")) {
            
            defaultDirection = boundlessSnake.getDefaultDirectionBoundless(defaultDirection);
        }
        
        return defaultDirection;
    }
}
