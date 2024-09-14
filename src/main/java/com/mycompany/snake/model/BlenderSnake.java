/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model;

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
    
    protected void setBlenderSnakeModes(List<String> modes) {
        this.modes = modes;
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

    protected Square createSnakeBodyPart(int col, int row) {
        
        if (modes.contains("Dimension")) {
            return new DimensionSquare(col, row, CellType.SNAKE_BODY, false);
        } else {
            return new Square(col, row, CellType.SNAKE_BODY);
        }   
    }

    protected Square createSnakeBodyPart(Point pos) {
        
        if (modes.contains("Dimension")) {
            return new DimensionSquare(pos, CellType.SNAKE_BODY, false);
        } else {
            return new Square(pos, CellType.SNAKE_BODY);
        }   
    }
    
    // CheeseSnake - DimensionSnake
    
    @Override
    public void initializeBody() {
        
        if (modes.contains("Cheese")) {
            initializeBodyCheeseDimension();
        } else {
            initializeBodyBlender();
        }
    }
    
    private void initializeBodyCheeseDimension() {
        
        for (int i = 1; i <= CheeseSnake.CHEESE_START_LENGTH - 1; i++) {
            
            int posX = head.x - (i * 2);
            
            cheeseSnake.emptyBody.addLast(new Square(posX + 1, head.y, CellType.EMPTY));
            addLastBody(createSnakeBodyPart(posX, head.y));
        }
        
        cheeseSnake.nextBodyPartSnake = true;
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
        
        if (cheeseSnake.nextBodyPartSnake && cheeseSnake.growCount <= 0) {
            removeLastBody();
        }
        
        if (!cheeseSnake.nextBodyPartSnake && cheeseSnake.growCount <= 0) {
            cheeseSnake.emptyBody.removeLast();
        }
        
        Point previousHeadPos = head.getLocation();
        
        setLocationHead(previousHeadPos, newPos);
        
        if (cheeseSnake.nextBodyPartSnake) {
            addFirstBody(createSnakeBodyPart(previousHeadPos));
        } else {
            cheeseSnake.emptyBody.addFirst(new Square(previousHeadPos, CellType.EMPTY));
        }
        
        if (cheeseSnake.growCount > 0) {
            cheeseSnake.growCount--;
        }
        
        cheeseSnake.nextBodyPartSnake = !cheeseSnake.nextBodyPartSnake;
    }
    
    private void moveBlender(Point newPos, boolean grow) {
        
        if(!grow) removeLastBody();
        
        Point previousHeadPos = head.getLocation();
        
        setLocationHead(previousHeadPos, newPos);
        addFirstBody(createSnakeBodyPart(previousHeadPos));
    }
    
    // TwinSnake
    
    protected void switchSidesTwinCheeseDimension() {

        boolean isFirstBodyPartSnake = !cheeseSnake.nextBodyPartSnake;
        boolean isLastBodyPartSnake = (!isFirstBodyPartSnake && cheeseSnake.growCount % 2 == 0) || (isFirstBodyPartSnake && cheeseSnake.growCount % 2 == 1);

        Point previousHeadPos = head.getLocation();
        
        if (isLastBodyPartSnake) {
            setLocationHead(previousHeadPos, removeLastBody());
            restoreDirectionBlender(head, cheeseSnake.emptyBody.getLast());
        } else {
            setLocationHead(previousHeadPos, cheeseSnake.emptyBody.removeLast());
            restoreDirectionBlender(head, body.getLast());
        }
        
        if (isFirstBodyPartSnake) {
            cheeseSnake.emptyBody.addFirst(new Square(previousHeadPos, CellType.EMPTY));
        } else {
            addFirstBody(createSnakeBodyPart(previousHeadPos));
        }
        
        cheeseSnake.setNextBodyPartSnake(isLastBodyPartSnake);

        Collections.reverse(body);
        Collections.reverse(cheeseSnake.emptyBody);
    }
    
    protected void switchSidesBlender() {
        
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
