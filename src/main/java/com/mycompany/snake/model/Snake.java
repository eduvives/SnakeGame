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
    
    private LinkedList<Point> body = new LinkedList<>();
    private Point head;
    
    public Snake(Point startPos, int startLength) {
        for (int i = 1; i <= startLength; i++) {
            body.addLast(new Point(startPos.x - i, startPos.y));
        }
        head = startPos;
    }

    public LinkedList<Point> getBody() {
        return body;
    }

    public Point getHead() {
        return head;
    }
}
