/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model;

import java.awt.Point;
import java.util.Collections;

/**
 *
 * @author Eduard
 */
public class TwinSnake extends Snake {

    public TwinSnake(Point startPos) {
        super(startPos);
    }
    
    public TwinSnake(Snake snake) {
        super(snake);
    }
    
    @Override
    public void move(Point newPos, boolean isFood) {
        
        super.move(newPos, isFood);
        
        postMoveTwinSnake(newPos, isFood);
    }
    
    protected void postMoveTwinSnake(Point newPos, boolean isFood) {
        if (isFood) {
            head.setLocation(body.getLast());
            
            body.removeLast();
            body.addFirst(new Square(newPos, CellType.SNAKE_BODY));
            
            Collections.reverse(body);
            
            direction.setLocation(head.x - body.getFirst().x, head.y - body.getFirst().y);
        }
    }
}
