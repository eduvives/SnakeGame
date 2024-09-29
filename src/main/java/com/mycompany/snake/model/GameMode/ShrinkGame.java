/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model.GameMode;

import com.mycompany.snake.model.GameModel;
import com.mycompany.snake.model.Snake.ShrinkSnake;
import com.mycompany.snake.model.Snake.Snake;
import static com.mycompany.snake.model.Snake.Snake.START_LENGTH;
import java.awt.Point;

/**
 *
 * @author Eduard
 */
public class ShrinkGame extends ClassicGame {
    
    protected ShrinkSnake shrinkSnake;
    
    public ShrinkGame(GameModel game) {
        super(game);
    }
    
    @Override
    public void initializeSnake(){
        
        super.initializeSnake();
        
        initializeShrinkSnake();
    }
    
    protected void initializeShrinkSnake() {
        shrinkSnake = (ShrinkSnake) game.getSnake();
    }
    
    @Override
    protected Snake createSnakeInstance() {
        return new ShrinkSnake();
    }
    
    @Override
    protected boolean checkCollision(Point newHeadPos) {
        
        boolean isCollision = super.checkCollision(newHeadPos);
        boolean isFinalCollision = false;
        
        if (isCollision) {
            shrinkSnake.reduce();
            isFinalCollision = game.getSnake().getBody().size() < START_LENGTH - 1;
            
            if (!isFinalCollision) decreaseScore();
        }
        
        return isFinalCollision;
    }
    
    @Override
    protected boolean isPositionAvailable(Point position) {
        boolean isOutOfBounds = position.y < 0 || position.y >= game.getNumBoardRows() || position.x < 0 || position.x >= game.getNumBoardCols();
        return super.isPositionAvailable(position) && !isOutOfBounds;
    }
}
