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
public class BoundlessGame extends ClassicGame {
    
    public BoundlessGame(GameModel game) {
        super(game);
    }
    
    @Override
    protected Point getNewPos(Point newDirection) {
        
        int posX = game.snake.getHead().x + newDirection.x;
        int posY = game.snake.getHead().y + newDirection.y;
        
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
