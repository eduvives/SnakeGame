/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model.Snake;

import com.mycompany.snake.model.GameMode.SnakeListener;
import com.mycompany.snake.model.Square.CellType;
import com.mycompany.snake.model.Square.Square;
import java.awt.Point;
import java.util.LinkedList;

/**
 *
 * @author Eduard
 */
public class Snake {
    
    protected SnakeListener listener;
    
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
    
    // Método para asignar el listener
    public void setListener(SnakeListener listener) {
        this.listener = listener;
    }
    
    protected void addLastBody(Square square) {
        body.addLast(square);
        listener.onPositionAdded(square);
    }
    
    protected void addFirstBody(Square square) {
        body.addFirst(square);
        listener.onPositionAdded(square);
    }
    
    protected Square removeLastBody() {
        Square lastBodyPart = body.removeLast();
        listener.onPositionRemoved(lastBodyPart.getLocation());
        return lastBodyPart;
    }
    
    protected Square removeFirstBody() {
        Square firstBodyPart = body.removeFirst();
        listener.onPositionRemoved(firstBodyPart.getLocation());
        return firstBodyPart;
    }
    
    protected void setLocationHead(Point previousPosition, Point newPosition) {
        head.setLocation(newPosition);
        listener.onPositionRemoved(previousPosition);
        listener.onPositionAdded(newPosition);
    }

    public void initializeSnake(Point startPos) {
        initializeHead(startPos);
        initializeBody();
    }
    
    protected void initializeHead(Point startPos) {
        
        head.setLocation(startPos);
        listener.onPositionAdded(startPos);
        
        head.setCellType(CellType.SNAKE_HEAD);
        direction.setLocation(START_DIRECTION);
    }
    
    protected void initializeBody() {
        
        for (int i = 1; i <= START_LENGTH - 1; i++) {
            addLastBody(new Square(head.x - i, head.y, CellType.SNAKE_BODY));
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
        
        if(!grow) removeLastBody();
        
        Point previousHeadPos = head.getLocation();
        
        setLocationHead(previousHeadPos, newPos);
        addFirstBody(new Square(previousHeadPos, CellType.SNAKE_BODY));
    }
    
    protected void restoreDirection(Point snakeHead, Point snakeFirstBodyPartPos) {
        direction.setLocation(getDefaultDirection(snakeHead, snakeFirstBodyPartPos));
    }
    
    protected Point getDefaultDirection(Point snakeHead, Point snakeFirstBodyPartPos) { // TODO delete params?
        return new Point(snakeHead.x - snakeFirstBodyPartPos.x, snakeHead.y - snakeFirstBodyPartPos.y);
    }
}
