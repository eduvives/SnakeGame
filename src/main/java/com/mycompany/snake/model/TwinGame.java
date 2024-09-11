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
    
    public TwinGame(GameModel game) {
        super(game);
    }
    
    @Override
    protected void initializeGameSnake(){
        
        super.initializeGameSnake();
        
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
    protected void eatFood(Point newPos) {
        
        super.eatFood(newPos);
        
        postEatFoodTwinGame();
    }
    
    protected void postEatFoodTwinGame() {
        game.observer.onSwitchSides();
    }
}