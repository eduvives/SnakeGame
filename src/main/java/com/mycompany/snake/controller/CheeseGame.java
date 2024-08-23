/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.controller;

import com.mycompany.snake.model.CheeseSnake;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Eduard
 */
public class CheeseGame extends ClassicGame {
    
    private CheeseSnake cheeseSnake;
    private int[][] SIDES_DIRECTIONS = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
    
    public CheeseGame(GameLogic game) {
        super(game);
    }
    
    @Override
    protected void createSnake(){
        game.snake = new CheeseSnake(new Point(game.startPos));
        cheeseSnake = (CheeseSnake) game.snake;
        super.removeSnakeAvailablePositions();
    }
    
    @Override
    protected Point getRandomFoodPosition() {
                
        List<Point> candidates = new ArrayList<>();
        
        for (Point pos : game.availablePositions) {
            
            int freeSides = 0;
            for (int[] dir : SIDES_DIRECTIONS) {
                if (game.availablePositions.contains(new Point(pos.x + dir[0], pos.y + dir[1]))) freeSides++;
            }

            if (freeSides >= 2) {
                candidates.add(pos);
            }
        }
        
        game.testList.clear();
        game.testList.addAll(candidates);

        if (candidates.isEmpty()) {
            return null;
        }

        Random rand = new Random();
        
        Point candidate = candidates.get(rand.nextInt(candidates.size()));
        game.availablePositions.remove(candidate);
        
        return candidate;
    }
}
