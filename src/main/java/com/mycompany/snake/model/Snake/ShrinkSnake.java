/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model.Snake;

/**
 *
 * @author Eduard
 */
public class ShrinkSnake extends Snake {
    
    public ShrinkSnake() {
        super();
    }
    
    public ShrinkSnake(Snake snake) {
        super(snake);
    }
    
    public void reduce() {
        setLocationHead(head.getLocation(), removeFirstBody());
    }
}
