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
            body.addLast(createSnakeBodyPart(head.x - i, head.y));
        }
    }
    
    @Override
    protected void move(Point newPos, boolean grow) { // TODO deberia ser protected o no?
        
        if(!grow) body.removeLast();
        
        body.addFirst(createSnakeBodyPart(head));
        head.setLocation(newPos);
    }
    
    @Override
    protected Square createSnakeBodyPart(int col, int row) {
        return new DimensionSquare(col, row, CellType.SNAKE_BODY, false);
    }
    
    @Override
    protected Square createSnakeBodyPart(Point pos) {
        return new DimensionSquare(pos, CellType.SNAKE_BODY, false);
    }
}
