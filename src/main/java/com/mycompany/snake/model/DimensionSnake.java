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
public class DimensionSnake extends Snake {
    
    public DimensionSnake() {
        super();
    }
    
    public DimensionSnake(Snake snake) {
        super(snake);
    }
    
    @Override
    protected void initializeBody() {
        
        for (int i = 1; i <= START_LENGTH - 1; i++) {
            body.addLast(new DimensionSquare(head.x - i, head.y, CellType.SNAKE_BODY, false));
        }
    }
    
    @Override
    protected void move(Point newPos, boolean grow) { // TODO deberia ser protected?
        
        if(!grow) body.removeLast();
        
        body.addFirst(new DimensionSquare(head, CellType.SNAKE_BODY, false));
        head.setLocation(newPos);
    }
}
