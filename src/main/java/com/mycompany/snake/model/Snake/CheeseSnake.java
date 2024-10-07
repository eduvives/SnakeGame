/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model.Snake;

import com.mycompany.snake.model.Square.CellConfiguration.CellType;
import com.mycompany.snake.model.Square.CellConfiguration.SpecificCellType;
import com.mycompany.snake.model.Square.Square;
import java.awt.Point;
import java.util.LinkedList;

/**
 *
 * @author Eduard
 */
public class CheeseSnake extends Snake {
    
    protected LinkedList<Square> cheeseBody;
    
    public static final int CHEESE_START_LENGTH = 3;
    protected int growCount;
    protected boolean nextBodyPartSnake;
    
    public CheeseSnake() {
        super();
        cheeseBody = new LinkedList<>();
        growCount = 0;
    }

    public CheeseSnake(Snake snake) {
        super(snake);
        cheeseBody = new LinkedList<>();
        growCount = 0;
    }

    public LinkedList<Square> getCheeseBody() {
        return cheeseBody;
    }

    public int getGrowCount() {
        return growCount;
    }
    
    public boolean isNextBodyPartSnake() {
        return nextBodyPartSnake;
    }
    
    public void invertNextBodyPartSnake() {
        nextBodyPartSnake = !nextBodyPartSnake;
    }
    
    @Override
    protected void addLastBody(Square square) {
        
        cheeseBody.addLast(square);
        
        if (square.getSpecificCellType() == SpecificCellType.SNAKE_BODY) {
            body.addLast(square);
            listener.onPositionAdded(square);
        }
    }
    
    @Override
    protected void addFirstBody(Square square) {
        
        cheeseBody.addFirst(square);
        
        if (square.getSpecificCellType() == SpecificCellType.SNAKE_BODY) {
            body.addFirst(square);
            listener.onPositionAdded(square);
        }
    }
    
    @Override
    protected Square removeLastBody() {
        
        Square lastBodyPart = cheeseBody.removeLast();
        
        if (lastBodyPart.getSpecificCellType() == SpecificCellType.SNAKE_BODY) {
            listener.onPositionRemoved(body.removeLast().getLocation());
        }
        
        return lastBodyPart;
    }
    
    @Override
    protected Square removeFirstBody() {
        
        Square firstBodyPart = cheeseBody.removeFirst();
        
        if (firstBodyPart.getSpecificCellType() == SpecificCellType.SNAKE_BODY) {
            listener.onPositionRemoved(body.removeFirst().getLocation());
        }
        
        return firstBodyPart;
    }
    
    @Override
    public Point getDefaultDirection() {
        return new Point(head.x - cheeseBody.getFirst().x, head.y - cheeseBody.getFirst().y);
    }

    @Override
    protected void initializeBody() {
        
        for (int i = 1; i <= CHEESE_START_LENGTH - 1; i++) {
            
            int posX = head.x - (i * 2);
            
            addLastBody(new Square(posX + 1, head.y, CellType.SNAKE, SpecificCellType.EMPTY_BODY));
            addLastBody(createSnakeBodyPart(posX, head.y));
        }
        
        nextBodyPartSnake = cheeseBody.getFirst().getSpecificCellType() != SpecificCellType.SNAKE_BODY;
    }
    
    @Override
    public void move(Point newHeadPos, boolean grow) {
        
        if (grow) growCount += 2;
        
        if (growCount <= 0) {
            removeLastBody();
        }
        
        Point previousHeadPos = head.getLocation();
        
        setLocationHead(previousHeadPos, newHeadPos);
        
        if (nextBodyPartSnake) { // If Next Body Part is Snake
            addFirstBody(createSnakeBodyPart(previousHeadPos));
        } else {
            addFirstBody(new Square(previousHeadPos, CellType.SNAKE, SpecificCellType.EMPTY_BODY));
        }
        
        if (growCount > 0) {
            growCount--;
        }
        
        invertNextBodyPartSnake();
    }
}
