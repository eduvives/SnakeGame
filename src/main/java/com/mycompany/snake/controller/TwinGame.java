/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.controller;

import com.mycompany.snake.model.TwinSnake;
import java.awt.Point;

/**
 *
 * @author Eduard
 */
public class TwinGame extends ClassicGame {
    
    public TwinGame(GameLogic game) {
        super(game);
    }
    
    @Override
    protected void createSnake(){
        game.snake = new TwinSnake(new Point(game.startPos));
        super.removeSnakeAvailablePositions();
    }
}
