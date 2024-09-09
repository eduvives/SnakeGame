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
    
    public Snake() {
        body = new LinkedList<>();
        direction = new Point();
    }
    
    public Snake(Snake snake) {
        head = snake.head;
        body = snake.body;
        direction = snake.direction;
    }

    protected void initializeSnake(Point startPos) { // TODO deberia ser protected?
        initializeHead(startPos);
        initializeBody();
    }
    
    protected void initializeHead(Point startPos) {
        head = createSquare(startPos, CellType.SNAKE_HEAD);
        direction.setLocation(START_DIRECTION);
    }
    
    protected void initializeBody() {
        for (int i = 1; i <= START_LENGTH - 1; i++) {
            body.addLast(createSquare(head.x - i, head.y, CellType.SNAKE_BODY));
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
    
    protected void move(Point newPos, boolean grow) { // TODO deberia ser protected?
        
        if(!grow) {
            body.removeLast();
        }
        
        body.addFirst(createSquare(head, CellType.SNAKE_BODY));
        head.setLocation(newPos);
    }
    
    protected Square createSquare(int col, int row, CellType cellType){
        return new Square(col, row, cellType);
    }
    
    protected Square createSquare(Point pos, CellType cellType) {
        return new Square(pos, cellType);
    }
}
