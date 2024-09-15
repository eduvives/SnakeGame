/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model.Snake;

import com.mycompany.snake.model.Square.CellType;
import com.mycompany.snake.model.Square.Square;
import java.awt.Point;
import java.util.LinkedList;

/**
 *
 * @author Eduard
 */
public class CheeseSnake extends Snake {
    
    protected LinkedList<Square> emptyBody;
    
    protected static final int CHEESE_START_LENGTH = 3;
    protected int growCount;
    protected boolean nextBodyPartSnake;
    
    public CheeseSnake() {
        super();
        emptyBody = new LinkedList<>();
        growCount = 0;
    }

    public CheeseSnake(Snake snake) {
        super(snake);
        emptyBody = new LinkedList<>();
        growCount = 0;
    }

    public LinkedList<Square> getEmptyBody() {
        return emptyBody;
    }

    public int getGrowCount() {
        return growCount;
    }

    public boolean isNextBodyPartSnake() {
        return nextBodyPartSnake;
    }

    public void setNextBodyPartSnake(boolean nextBodyPartSnake) {
        this.nextBodyPartSnake = nextBodyPartSnake;
    }

    @Override
    protected void initializeBody() {
        
        for (int i = 1; i <= CHEESE_START_LENGTH - 1; i++) {
            
            int posX = head.x - (i * 2);
            
            emptyBody.addLast(new Square(posX + 1, head.y, CellType.EMPTY));
            addLastBody(new Square(posX, head.y, CellType.SNAKE_BODY));
        }
        
        nextBodyPartSnake = true;
    }
    
    @Override
    public void move(Point newPos, boolean grow) {
        
        if (grow) growCount += 2;
        
        if (nextBodyPartSnake && growCount <= 0) {
            removeLastBody();
        }
        
        if (!nextBodyPartSnake && growCount <= 0) {
            emptyBody.removeLast();
        }
        
        Point previousHeadPos = head.getLocation();
        
        setLocationHead(previousHeadPos, newPos);
        
        if (nextBodyPartSnake) {
            addFirstBody(new Square(previousHeadPos, CellType.SNAKE_BODY));
        } else {
            emptyBody.addFirst(new Square(previousHeadPos, CellType.EMPTY));
        }
        
        if (growCount > 0) {
            growCount--;
        }
        
        nextBodyPartSnake = !nextBodyPartSnake;
    }
}
