/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model.GameMode;

import com.mycompany.snake.model.GameModel;
import com.mycompany.snake.model.Snake.BoundlessSnake;
import com.mycompany.snake.model.Snake.Snake;
import java.awt.Point;

/**
 *
 * @author Eduard
 */
public class PeacefulGame extends ClassicGame {
    
    protected BoundlessSnake boundlessSnake;
    
    public PeacefulGame(GameModel game) {
        super(game);
    }
    
    @Override
    protected boolean checkCollision(Point newHeadPos) {
        return false;
    }
    
    @Override
    protected boolean checkFeast() {
        // Num Snake Body + Snake Head
        return game.getSnake().getBody().size() + 1 == game.getNumBoardRows() * game.getNumBoardCols();
    }
    
    @Override
    public void initializeSnake(){
        
        super.initializeSnake();
        
        initializeBoundlessSnake();
    }
    
    protected void initializeBoundlessSnake() {
        boundlessSnake = (BoundlessSnake) game.getSnake();
    }
    
    @Override
    protected Snake createSnakeInstance() {
        return new BoundlessSnake();
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
