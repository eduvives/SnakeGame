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
    
    protected void switchSides() {
        
        body.addFirst(new Square(head, CellType.SNAKE_BODY));
        head.setLocation(body.removeLast());

        Collections.reverse(body);
        
        restoreDirection(head, body.getFirst());
    }
    
}
