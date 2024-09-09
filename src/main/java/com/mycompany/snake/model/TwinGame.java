/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model;

import java.awt.Point;
import java.util.Collections;
import java.util.LinkedList;

/**
 *
 * @author Eduard
 */
public class TwinGame extends ClassicGame {
    
    public TwinGame(GameModel game) {
        super(game);
        this.game.observer.onNewTwinGame();
    }
    
    @Override
    protected void snakeSimpleMove(Point newPos, boolean isFoodCollision) {
        
        super.snakeSimpleMove(newPos, isFoodCollision);
        
        if (isFoodCollision) {
            postSnakeSimpleMoveTwinGame(newPos);
        }
        
    }
    
    protected void postSnakeSimpleMoveTwinGame(Point newPos) {
        switchSides(newPos);
        restoreDirection(game.snake.getHead(), game.snake.getBody().getFirst());
    }
    
    protected void switchSides(Point newPos) {
        LinkedList<Square> snakeBody = game.snake.getBody();
        Point snakeHead = game.snake.getHead();

        snakeHead.setLocation(snakeBody.getLast());

        snakeBody.removeLast();
        snakeBody.addFirst(new Square(newPos, CellType.SNAKE_BODY));

        Collections.reverse(snakeBody);
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