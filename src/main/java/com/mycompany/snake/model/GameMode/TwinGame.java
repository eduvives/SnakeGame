/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model.GameMode;

import com.mycompany.snake.model.GameModel;
import com.mycompany.snake.model.Snake.TwinSnake;
import com.mycompany.snake.model.Snake.Snake;
import java.awt.Point;

/**
 *
 * @author Eduard
 */
public class TwinGame extends ClassicGame {
    
    protected TwinSnake twinSnake;
    protected boolean switchSides;
    
    public TwinGame(GameModel game) {
        super(game);
    }
    
    @Override
    public void initializeSnake(){
        
        super.initializeSnake();
        
        initializeTwinSnake();
    }
    
    protected void initializeTwinSnake() {
        twinSnake = (TwinSnake) game.getSnake();
    }
    
    @Override
    protected Snake createSnakeInstance() {
        return new TwinSnake();
    }
    
    @Override
    protected void snakeMove(Point newHeadPos, boolean isFoodCollision) {
        
        super.snakeMove(newHeadPos, isFoodCollision);
        
        switchSides = isFoodCollision;
        
        if (isFoodCollision) {
            twinSnake.switchSides();
        }
        
    }
    
    @Override
    public void nextLoop() {
        
        super.nextLoop();
        
        if (switchSides) {
            switchSidesEffect();
        }
    }
    
    protected void switchSidesEffect() {
        game.getObserver().onSwitchSides();
    }
}