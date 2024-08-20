/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.snake.main;

import com.mycompany.snake.controller.GameLogic;
import com.mycompany.snake.view.SnakeView;

/**
 *
 * @author Eduard
 */
public class SnakeGame {

    private static final SnakeView view = new SnakeView();
    private static final GameLogic controller = new GameLogic(view);
    
    public static void main(String[] args) {
        controller.showGameBoard();
        controller.openMenu();
    }
}
