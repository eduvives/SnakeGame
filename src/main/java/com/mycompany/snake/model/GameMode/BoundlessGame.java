/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model.GameMode;

import com.mycompany.snake.model.GameModel;
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
        
        int posX = game.getSnake().getHead().x + newDirection.x;
        int posY = game.getSnake().getHead().y + newDirection.y;
        
        if (posX < 0) {
            posX = game.getNumBoardCols() - 1;
        } else if (posX >= game.getNumBoardCols()) {
            posX = 0;
        }

        if (posY < 0) {
            posY = game.getNumBoardRows() - 1;
        } else if (posY >= game.getNumBoardRows()) {
            posY = 0;
        }
        
        return new Point(posX, posY);
    }
}
