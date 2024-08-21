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
    private int cheeseCount;
    
    public CheeseSnake(Point startPos) {
        super(startPos);
    }
    
    @Override
    public void setBody() {
        cheeseCount = 0;
        for (int i = 1; i <= CHEESE_START_LENGTH - 1; i++) {
            getBody().addLast(new Point(getHead().x - (i * 2), getHead().y));
        }
    }
    
    public int getCheeseCount() {
        return cheeseCount;
    }

    public void setCheeseCount(int cheeseCount) {
        this.cheeseCount = cheeseCount;
    }
    
}
