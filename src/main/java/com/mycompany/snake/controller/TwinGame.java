/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.controller;

import com.mycompany.snake.model.CellType;
import com.mycompany.snake.model.Square;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.LinkedList;
import javax.swing.Timer;

/**
 *
 * @author Eduard
 */
public class TwinGame extends ClassicGame {
    
    private Timer switchSidesTimer;
    
    public TwinGame(GameLogic game) {
        super(game);
        setSwitchSidesTimer();
    }
    
    @Override
    protected void snakeSimpleMove(Point newPos, boolean isFood) {
        
        super.snakeSimpleMove(newPos, isFood);
        
        if (isFood) {
            postSnakeSimpleMoveTwinGame(newPos, isFood);
        }
        
    }
    
    protected void postSnakeSimpleMoveTwinGame(Point newPos, boolean isFood) {
        switchSides(newPos);
        resetDirection(game.snake.getHead(), game.snake.getBody().getFirst());
    }
    
    protected void switchSides(Point newPos) {
        LinkedList<Square> snakeBody = game.snake.getBody();
        Point snakeHead = game.snake.getHead();

        snakeHead.setLocation(snakeBody.getLast());

        snakeBody.removeLast();
        snakeBody.addFirst(new Square(newPos, CellType.SNAKE_BODY));

        Collections.reverse(snakeBody);
    }
    
    protected void resetDirection(Point snakeHead, Point snakeFirstBodyPartPos) {
        game.snake.getDirection().setLocation(snakeHead.x - snakeFirstBodyPartPos.x, snakeHead.y - snakeFirstBodyPartPos.y);
    }
    
    @Override
    protected void eatFood(Point newPos) {
        
        super.eatFood(newPos);
        
        postEatFoodTwinGame();
    }
    
    protected void postEatFoodTwinGame() {
        game.inputQueue.clear();
        switchingSidesPause(); // Simular una pausa
    }
    
    // Métodos Auxiliares
    
    private void setSwitchSidesTimer() {
        switchSidesTimer = new Timer((int) Math.round(game.timerDelay * 1.5), (ActionEvent e) -> {
            if (!game.gameEnded) {
                game.timer.start();
            }
        });
        
        // Configurar el Timer para que se ejecute solo una vez
        switchSidesTimer.setRepeats(false);
    }
    
    private void switchingSidesPause() {
        game.timer.stop();
        switchSidesTimer.start();
    }
}