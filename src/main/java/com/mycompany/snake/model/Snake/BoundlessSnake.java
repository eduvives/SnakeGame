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
public class BoundlessSnake extends Snake {
    
    public BoundlessSnake() {
        super();
    }
    
    public BoundlessSnake(Snake snake) {
        super(snake);
    }
    
    @Override
    public Point getDefaultDirection() {
        return getDefaultDirectionBoundless(super.getDefaultDirection());
    }
    
    protected Point getDefaultDirectionBoundless (Point defaultDirection) {
        
        // Ajustar por teletransporte en el eje X
        if (defaultDirection.x > 1) {
            defaultDirection.x = -1;
        } else if (defaultDirection.x < -1) {
            defaultDirection.x = 1;
        }

        // Ajustar por teletransporte en el eje Y
        if (defaultDirection.y > 1) {
            defaultDirection.y = -1;
        } else if (defaultDirection.y < -1) {
            defaultDirection.y = 1;
        }
        
        return defaultDirection;
    }
    
    public void reduce() {
        setLocationHead(head.getLocation(), removeFirstBody());
    }
}
