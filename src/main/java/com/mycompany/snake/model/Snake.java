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
    
    private LinkedList<Point> body = new LinkedList<>();
    private Point head;
    
    public static final int START_LENGTH = 4;
    public static final Color BODY_COLOR = Color.GREEN;
    public static final Color HEAD_COLOR = new Color(0,128,0);
    
    public Snake(Point startPos, int startLength) {
        // Set Body
        for (int i = 1; i <= startLength - 1; i++) {
            body.addLast(new Point(startPos.x - i, startPos.y));
        }
        // Set Head
        head = startPos;
    }

    public LinkedList<Point> getBody() {
        return body;
    }

    public Point getHead() {
        return head;
    }
}
