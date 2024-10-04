/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model.Snake;

import java.awt.Point;

/**
 *
 * @author Eduard
 */
public class ShrinkSnake extends Snake {
    
    protected boolean collision;
    
    public ShrinkSnake() {
        super();
    }
    
    public ShrinkSnake(Snake snake) {
        super(snake);
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }
    
    @Override
    public void move(Point newHeadPos, boolean grow) {
        
        if(!grow) removeLastBody();
        
        if (!collision){
            Point previousHeadPos = head.getLocation();

            setLocationHead(previousHeadPos, newHeadPos);
            addFirstBody(createSnakeBodyPart(previousHeadPos));
        } else {
            listener.onShrink();
        }
    }
}
