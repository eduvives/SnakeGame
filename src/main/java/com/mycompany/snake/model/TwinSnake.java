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
    
    public TwinSnake() {
        super();
    }
    
    public TwinSnake(Snake snake) {
        super(snake);
    }
    
    @Override
    protected void move(Point newPos, boolean grow) { // TODO deberia ser protected?
        
        super.move(newPos, grow);
        
        if (grow) {
            switchSides(newPos);
            restoreDirection(head, body.getFirst());
        }
    }
    
    protected void switchSides(Point newPos) {

        head.setLocation(body.removeLast());
        body.addFirst(new Square(newPos, CellType.SNAKE_BODY));

        Collections.reverse(body);
    }
    
}
