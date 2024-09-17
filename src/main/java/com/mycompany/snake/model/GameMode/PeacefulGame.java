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
public class PeacefulGame extends ClassicGame {
    
    public PeacefulGame(GameModel game) {
        super(game);
    }
    
    @Override
    protected boolean checkCollision(Point snakeHeadPos) {
        return false;
    }
    
    @Override
    protected boolean checkFeast() {
        // Num Snake Body + Snake Head
        return game.getSnake().getBody().size() + 1 == game.getNumBoardRows() * game.getNumBoardCols();
    }
    
    @Override
    protected Point getNewPos(Point newDirection) {
        return game.getBoundlessGame().getNewPos(newDirection);
    }
    
    @Override
    protected boolean noFoodPositions() {
        return game.getSnake().getBody().size() + 1 + game.getFood().size() == game.getNumBoardRows() * game.getNumBoardCols();
    }
    
}
