/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model.Snake;

import com.mycompany.snake.model.GameMode.SnakeListener;
import com.mycompany.snake.model.Square.CellConfiguration.CellType;
import com.mycompany.snake.model.Square.CellConfiguration.SpecificCellType;
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

    @Override
    protected Square createSnakeBodyPart(int col, int row) {
        
        if (modes.contains("Dimension")) {
            return dimensionSnake.createSnakeBodyPart(col, row);
        } else {
            return super.createSnakeBodyPart(col, row);
        }   
    }

    @Override
    protected Square createSnakeBodyPart(Point pos) {
        
        if (modes.contains("Dimension")) {
            return dimensionSnake.createSnakeBodyPart(pos);
        } else {
            return super.createSnakeBodyPart(pos);
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
            
            cheeseSnake.addLastBody(new Square(posX + 1, head.y, CellType.SNAKE, SpecificCellType.EMPTY_BODY));
            cheeseSnake.addLastBody(createSnakeBodyPart(posX, head.y));
        }
        
        cheeseSnake.nextBodyPartSnake = cheeseSnake.cheeseBody.getFirst().getSpecificCellType() != SpecificCellType.SNAKE_BODY;
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
        
        if (!modes.contains("Shrink") || !shrinkSnake.collision){
            
            Point previousHeadPos = head.getLocation();

            setLocationHead(previousHeadPos, newHeadPos);

            if (cheeseSnake.nextBodyPartSnake) { // If Next Body Part is Snake
                cheeseSnake.addFirstBody(createSnakeBodyPart(previousHeadPos));
            } else {
                cheeseSnake.addFirstBody(new Square(previousHeadPos, CellType.SNAKE, SpecificCellType.EMPTY_BODY));
            }
        } else { // If isShrinkMode + isCollision
            listener.onShrink();
        }
        
        if (cheeseSnake.growCount > 0) {
            cheeseSnake.growCount--;
        }
        
        cheeseSnake.invertNextBodyPartSnake();
    }
    
    private void moveBlender(Point newHeadPos, boolean grow) {
        
        if(!grow) removeLastBody();
        
        if (!modes.contains("Shrink") || !shrinkSnake.collision){
            
            Point previousHeadPos = head.getLocation();

            setLocationHead(previousHeadPos, newHeadPos);
            addFirstBody(createSnakeBodyPart(previousHeadPos));
            
        } else { // If isShrinkMode + isCollision
            listener.onShrink();
        }
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
            cheeseSnake.addFirstBody(new Square(previousHeadPos, CellType.SNAKE, SpecificCellType.EMPTY_BODY));
        }

        Collections.reverse(body);
        Collections.reverse(cheeseSnake.cheeseBody);
        
        restoreDirection();
        cheeseSnake.nextBodyPartSnake = lastBodyPart.getSpecificCellType() == SpecificCellType.SNAKE_BODY;
    }
    
    // TwinSnake - DimensionSnake
    
    public void switchSidesBlender() {
        
        Point previousHeadPos = head.getLocation();
        
        setLocationHead(previousHeadPos, removeLastBody());
        addFirstBody(createSnakeBodyPart(previousHeadPos)); // Llama al método "createSnakeBodyPart" de Blender
        
        Collections.reverse(body);
        
        restoreDirection(); // Llama al método "restoreDirection" de Snake + "getDefaultDirection" de Blender
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
