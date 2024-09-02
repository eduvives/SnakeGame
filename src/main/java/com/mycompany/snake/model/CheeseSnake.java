/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model;

import java.awt.Point;
import java.util.LinkedList;

/**
 *
 * @author Eduard
 */
public class CheeseSnake extends Snake {
    
    protected LinkedList<Square> emptyBody;
    
    private static final int CHEESE_START_LENGTH = 3;
    protected int growCount;
    protected boolean isNextBodyPartSnake;
    
    public CheeseSnake(Point startPos) {
        super(startPos);
        emptyBody = new LinkedList<>();
        
        growCount = 0;
        postInitializeBody();
    }
    
    public CheeseSnake(Snake snake) {
        super(snake);
        emptyBody = new LinkedList<>();
        
        growCount = 0;
        postInitializeBody();
    }
    
    @Override
    public void initializeBody() {
    }
    
    protected void postInitializeBody() {
        
        for (int i = 1; i <= CHEESE_START_LENGTH - 1; i++) {
            
            int posX = head.x - (i * 2);
            
            emptyBody.addLast(new Square(posX + 1, head.y, CellType.EMPTY));
            body.addLast(new Square(posX, head.y, CellType.SNAKE_BODY));
        }
        
        isNextBodyPartSnake = true;
    }
    
    @Override
    public void move(Point newPos, boolean grow) {
        
        if (grow) growCount += 2;
        
        if (isNextBodyPartSnake) {
            body.addFirst(new Square(head, CellType.SNAKE_BODY));

            if (growCount <= 0) {
                body.removeLast();
            } 
        } else {
            emptyBody.addFirst(new Square(head, CellType.EMPTY));
            
            if (growCount <= 0) {
                emptyBody.removeLast();
            }
        }
        
        if (growCount > 0) {
            growCount--;
        }
        
        head.setLocation(newPos);
        
        isNextBodyPartSnake = !isNextBodyPartSnake;
    }
}
