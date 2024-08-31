/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.controller;

import com.mycompany.snake.model.Snake;
import com.mycompany.snake.model.TwinSnake;
import java.awt.Point;
import java.awt.event.ActionEvent;
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
    protected Snake createSnakeInstance(Point startPos) {
        return new TwinSnake(startPos);
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