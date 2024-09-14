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
        
        Point previousHeadPos = head.getLocation();
        
        setLocationHead(previousHeadPos, removeLastBody());
        addFirstBody(new Square(previousHeadPos, CellType.SNAKE_BODY));
        
        Collections.reverse(body);
        
        restoreDirection(head, body.getFirst());
    }
    
}
