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
public class TwinGame extends ClassicGame {
    
    protected TwinSnake twinSnake;
    protected boolean switchSides;
    
    public TwinGame(GameModel game) {
        super(game);
    }
    
    @Override
    protected void initializeSnake(){
        
        super.initializeSnake();
        
        postInitializeSnakeTwinGame();
    }
    
    protected void postInitializeSnakeTwinGame() {
        twinSnake = (TwinSnake) game.snake;
    }
    
    @Override
    protected Snake createSnakeInstance() {
        return new TwinSnake();
    }
    
    @Override
    protected void snakeMove(Point newPos, boolean isFoodCollision) {
        
        super.snakeMove(newPos, isFoodCollision);
        
        switchSides = isFoodCollision;
        
        if (switchSides) {
            twinSnake.switchSides();
        }
        
    }
    
    @Override
    protected void nextLoop() {
        
        super.nextLoop();
        
        if (switchSides) {
            switchSidesEffect();
        }
    }
    
    protected void switchSidesEffect() {
        game.observer.onSwitchSides();
    }
}