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
    private DimensionSnake dimensionSnake;
    private TwinSnake twinSnake;
    
    public BlenderSnake(List<String> modes) {
        super();
        
        cheeseSnake = new CheeseSnake(this);
        dimensionSnake = new DimensionSnake(this);
        twinSnake = new TwinSnake(this);
        
        setBlenderSnakeModes(modes);
    }
    
    private void setBlenderSnakeModes(List<String> modes) {
        this.modes = modes;
    }
    
    @Override
    public void setListener(SnakeListener listener) {
        this.listener = listener;
        cheeseSnake.listener = listener;
        dimensionSnake.listener = listener;
        twinSnake.listener = listener;
    }

    public CheeseSnake getCheeseSnake() {
        return cheeseSnake;
    }
    
    public DimensionSnake getDimensionSnake() {
        return dimensionSnake;
    }
    
    public TwinSnake getTwinSnake() {
        return twinSnake;
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
        
        cheeseSnake.nextBodyPartSnake = cheeseSnake.cheeseBody.getFirst().getCellType() == CellType.EMPTY;
    }
    
    private void initializeBodyBlender() { 
        for (int i = 1; i <= START_LENGTH - 1; i++) {
            addLastBody(createSnakeBodyPart(head.x - i, head.y));
        }
    }
    
    @Override
    public void move(Point newPos, boolean isFoodCollision) {
        
        if (modes.contains("Cheese")) {
            moveCheeseDimension(newPos, isFoodCollision);
        } else {
            moveBlender(newPos, isFoodCollision);
        }
    }
    
    private void moveCheeseDimension(Point newPos, boolean grow) {
        
        if (grow) cheeseSnake.growCount += 2;
        
        if (cheeseSnake.growCount <= 0) {
            cheeseSnake.removeLastBody();
        }
        
        Point previousHeadPos = head.getLocation();
        
        setLocationHead(previousHeadPos, newPos);
        
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
    
    private void moveBlender(Point newPos, boolean grow) {
        
        if(!grow) removeLastBody();
        
        Point previousHeadPos = head.getLocation();
        
        setLocationHead(previousHeadPos, newPos);
        addFirstBody(createSnakeBodyPart(previousHeadPos));
    }
    
    // TwinSnake - CheeseSnake - DimensionSnake
    
    public void switchSidesTwinCheeseDimension() {
        
        Point previousHeadPos = head.getLocation();
        Square lastBodyPart = cheeseSnake.removeLastBody();
        
        // Set Snake Head
        setLocationHead(previousHeadPos, lastBodyPart);
        restoreDirectionBlender(head, cheeseSnake.cheeseBody.getLast());

        // Set Snake Tail
        if (cheeseSnake.isNextBodyPartSnake()) {
            cheeseSnake.addFirstBody(createSnakeBodyPart(previousHeadPos));
        } else {
            cheeseSnake.addFirstBody(new Square(previousHeadPos, CellType.EMPTY));
        }

        Collections.reverse(body);
        Collections.reverse(cheeseSnake.cheeseBody);
        
        cheeseSnake.nextBodyPartSnake = lastBodyPart.getCellType() == CellType.SNAKE_BODY;
    }
    
    // TwinSnake - DimensionSnake
    
    public void switchSidesBlender() {
        
        Point previousHeadPos = head.getLocation();
        
        setLocationHead(previousHeadPos, removeLastBody());
        addFirstBody(createSnakeBodyPart(previousHeadPos));
        
        Collections.reverse(body);
        
        restoreDirectionBlender(head, body.getFirst());
    }
    
    // TwinSnake - BoundlessGame
    
    private void restoreDirectionBlender(Point snakeHead, Point snakeFirstBodyPartPos) {
        
        Point defaultDirection = getDefaultDirection(snakeHead, snakeFirstBodyPartPos);
        
        if (modes.contains("Boundless")) {
            
            // Ajustar por teletransporte en el eje X
            if (defaultDirection.x > 1) {
                defaultDirection.x = -1;
            } else if (defaultDirection.x < -1) {
                defaultDirection.x = 1;
            }

            // Ajustar por teletransporte en el eje Y
            if (defaultDirection.y > 1) {
                defaultDirection.y = -1;
            } else if (defaultDirection.y < -1) {
                defaultDirection.y = 1;
            }
        }
        
        direction.setLocation(defaultDirection.x, defaultDirection.y);
    }
}
