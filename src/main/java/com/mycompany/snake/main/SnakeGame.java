/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.snake.main;

import com.mycompany.snake.controller.GameController;
import com.mycompany.snake.model.GameModel;
import com.mycompany.snake.view.SnakeView;

/**
 *
 * @author Eduard
 */
public class SnakeGame {
    
    public static void main(String[] args) {
        
        SnakeView view = new SnakeView();
        GameModel model = new GameModel();
        GameController controller = new GameController(view, model);
    
        controller.registerObserver();
        controller.showGamePreview();
        controller.openMenu();
    }
}
