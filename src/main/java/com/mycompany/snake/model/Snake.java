/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model;

import java.awt.Point;
import java.util.LinkedList;

/**
 *
 * @author Eduard
 */
public class Snake {
    
    protected LinkedList<Square> body;
    protected Square head;
    protected Point direction;
    
    public static final int START_LENGTH = 4;
    public static final Point START_DIRECTION = new Point(1, 0);   
    
    public Snake(Point startPos) {
        head = new Square(startPos, CellType.SNAKE_HEAD);
        body = new LinkedList<>();
        direction = new Point(START_DIRECTION);
        initializeBody();
    }
    
    public Snake(Snake snake) {
        head = snake.head;
        body = snake.body;
        direction = snake.direction;
    }
    
    protected void initializeBody() {
        for (int i = 1; i <= START_LENGTH - 1; i++) {
            body.addLast(new Square(head.x - i, head.y, CellType.SNAKE_BODY));
        }
    }

    public LinkedList<Square> getBody() {
        return body;
    }

    public Square getHead() {
        return head;
    }

    public Point getDirection() {
        return direction;
    }
    
    public void move(Point newPos, boolean grow) {
        
        if(!grow) {
            body.removeLast();
        }
        
        body.addFirst(new Square(head, CellType.SNAKE_BODY));
        head.setLocation(newPos);
    }
}
