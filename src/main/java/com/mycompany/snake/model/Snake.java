/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model;

import java.awt.Color;
import java.awt.Point;
import java.util.LinkedList;

/**
 *
 * @author Eduard
 */
public class Snake {
    
    protected LinkedList<Point> body = new LinkedList<>();
    protected Point head;
    protected Point direction = new Point();
    
    public static final int START_LENGTH = 4;
    public static final Point START_DIRECTION = new Point(1, 0);
    public static final Color BODY_COLOR = Color.GREEN;
    public static final Color HEAD_COLOR = new Color(0,128,0);    
    
    public Snake(Point startPos) {
        head = startPos;
        setBody();
        direction.setLocation(START_DIRECTION);
    }
    
    public void setBody() {
        for (int i = 1; i <= START_LENGTH - 1; i++) {
            body.addLast(new Point(head.x - i, head.y));
        }
    }

    public LinkedList<Point> getBody() {
        return body;
    }

    public Point getHead() {
        return head;
    }

    public Point getDirection() {
        return direction;
    }
    
    public void move(Point newPos, boolean grow) {
        body.addFirst(new Point(head.x, head.y));
        head.setLocation(newPos.x, newPos.y);

        if(!grow) {
            body.removeLast();
        }
    }
}
