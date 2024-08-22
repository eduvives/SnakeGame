/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model;

import java.awt.Point;

/**
 *
 * @author Eduard
 */
public class CheeseSnake extends Snake {
    
    private static final int CHEESE_START_LENGTH = 3;
    private int growCount;
    private boolean isNextBodyPartSnake;
    private boolean lastBodyPartRemoved;
    
    public CheeseSnake(Point startPos) {
        super(startPos);
    }
    
    @Override
    public void setBody() {
        growCount = 0;
        for (int i = 1; i <= CHEESE_START_LENGTH - 1; i++) {
            body.addLast(new Point(head.x - (i * 2), head.y));
        }
        
        isNextBodyPartSnake = true;
    }

    public boolean isLastBodyPartRemoved() {
        return lastBodyPartRemoved;
    }        
    
    @Override
    public void move(Point newPos, boolean grow) {
        
        if (grow) growCount++;
        
        if (isNextBodyPartSnake) {
            body.addFirst(new Point(head.x, head.y));

            if(growCount > 0) {
                growCount--;
                lastBodyPartRemoved = false;
            } else {
                body.removeLast();
                lastBodyPartRemoved = true;
            }
        }
        
        head.setLocation(newPos.x, newPos.y);
        
        isNextBodyPartSnake = !isNextBodyPartSnake;
    }
    
}
