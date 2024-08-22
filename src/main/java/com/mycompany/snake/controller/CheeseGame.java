/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.controller;

import com.mycompany.snake.model.CheeseSnake;
import java.awt.Point;

/**
 *
 * @author Eduard
 */
public class CheeseGame extends ClassicGame {
    
    private CheeseSnake cheeseSnake;
    
    public CheeseGame(GameLogic game) {
        super(game);
    }
    
    @Override
    protected void createSnake(){
        game.snake = new CheeseSnake(new Point(game.startPos));
        cheeseSnake = (CheeseSnake) game.snake;
        super.updateSnakeAvailablePositions();
    }
    
    @Override
    protected void updateMoveAvailablePositions(Point newPos){
        
        if(cheeseSnake.isLastBodyPartRemoved()) {
            game.availablePositions.add(cheeseSnake.getBody().getLast());                
        }
        game.availablePositions.remove(newPos);
    }
}
