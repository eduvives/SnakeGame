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
    
    @Override
    public void move(Point newPos, boolean isFood) {
        
        super.move(newPos, isFood);
        
        if(isFood){
            Point newHeadPos = new Point(body.getLast());
            
            super.move(newPos, false);
            Collections.reverse(body);
            head.setLocation(newHeadPos.x, newHeadPos.y);
            
            direction.setLocation(head.x - body.getFirst().x, head.y - body.getFirst().y);
        }
        
    }
}
