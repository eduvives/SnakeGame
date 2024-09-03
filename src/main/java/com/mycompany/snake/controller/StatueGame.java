/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.controller;

import com.mycompany.snake.model.CellType;
import com.mycompany.snake.model.Square;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Eduard
 */
public class StatueGame extends ClassicGame {
    
    private Set<Square> statues = new HashSet<>();
    
    public StatueGame(GameLogic game) {
        super(game);        
    }
    
    @Override
    protected boolean checkCollision() {
        
        Point snakeHeadPos = game.snake.getHead().getLocation();
        
        boolean collision = super.checkCollision();
        boolean statueCollision = statues.contains(snakeHeadPos);
        
        return collision || statueCollision;
    }
    
    @Override
    protected void prepareNewGame() {
        
        super.prepareNewGame();

        postPrepareNewGameStatueGame();
    }
    
    protected void postPrepareNewGameStatueGame() {
        game.specificModeLists.add(statues);

        statues.clear();
    }
    
    @Override
    protected void addSnakeAvailablePositions(){
        
        super.addSnakeAvailablePositions();
        
        postAddSnakeAvailablePositionsStatueGame();
    }
    
    protected void postAddSnakeAvailablePositionsStatueGame() {
        for (Point statuePos : statues) {
            game.availablePositions.remove(statuePos);
        }
    }
    
    @Override
    protected void eatFood(Point newPos) {
        
        prevEatFoodStatueGame();
        
        super.eatFood(newPos);
    }
    
    protected void prevEatFoodStatueGame() {
        sculptStatue();
    }
    
    // Métodos Auxiliares
    
    private void sculptStatue() {
        
        for (Square bodyPart : game.snake.getBody()) {
            if (statues.add(new Square(bodyPart, CellType.WALL_FILLED))) {
                game.availablePositions.remove(bodyPart);
            }
        }
    }
}
