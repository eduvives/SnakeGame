/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.controller;

import java.awt.Point;

/**
 *
 * @author Eduard
 */
public class BoundlessGame extends ClassicGame {
    
    public BoundlessGame(GameLogic game) {
        super(game);        
    }
    
    @Override
    protected Point getNewPos(Point currentDirection) {
        
        int posX = game.snake.getHead().x + currentDirection.x;
        int posY = game.snake.getHead().y + currentDirection.y;
        
        if (posX < 0) {
            posX = game.numBoardCols - 1;
        } else if (posX >= game.numBoardCols) {
            posX = 0;
        }

        if (posY < 0) {
            posY = game.numBoardRows - 1;
        } else if (posY >= game.numBoardRows) {
            posY = 0;
        }
        
        return new Point(posX, posY);
    }
}
