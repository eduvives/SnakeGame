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
        head = new Square();
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
        head.setLocation(startPos);
        head.cellType = CellType.SNAKE_HEAD;
        direction.setLocation(START_DIRECTION);
    }
    
    protected void initializeBody() {
        
        for (int i = 1; i <= START_LENGTH - 1; i++) {
            body.addLast(createSnakeBodyPart(head.x - i, head.y));
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
        
        if(!grow) body.removeLast();
        
        body.addFirst(createSnakeBodyPart(head));
        head.setLocation(newPos);
    }
    
    protected void restoreDirection(Point snakeHead, Point snakeFirstBodyPartPos) {
        direction.setLocation(getDefaultDirection(snakeHead, snakeFirstBodyPartPos));
    }
    
    protected Point getDefaultDirection(Point snakeHead, Point snakeFirstBodyPartPos) { // TODO delete params?
        return new Point(snakeHead.x - snakeFirstBodyPartPos.x, snakeHead.y - snakeFirstBodyPartPos.y);
    }
    
    protected Square createSnakeBodyPart(int col, int row) {
        return new Square(col, row, CellType.SNAKE_BODY);
    }
    
    protected Square createSnakeBodyPart(Point pos) {
        return new Square(pos, CellType.SNAKE_BODY);
    }
}
