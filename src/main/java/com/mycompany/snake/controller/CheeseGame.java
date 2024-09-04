/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.controller;

import com.mycompany.snake.model.CheeseSnake;
import com.mycompany.snake.model.Snake;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Eduard
 */
public class CheeseGame extends ClassicGame {
    
    protected CheeseSnake cheeseSnake;
    
    private int[][] SIDES_DIRECTIONS = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
    private int numAvailableFooodPositions;
    
    public CheeseGame(GameLogic game) {
        super(game);
    }
    
    @Override
    protected boolean checkFeast() {
        return numAvailableFooodPositions == 0 && game.food.isEmpty();
    }
    
    @Override
    protected void initializeSnake(){
        
        super.initializeSnake();
        
        postInitializeSnakeCheeseGame();
    }
    
    protected void postInitializeSnakeCheeseGame() {
        cheeseSnake = (CheeseSnake) game.snake;
    }
    
    @Override
    protected Snake createSnakeInstance(Point startPos) {
        return new CheeseSnake(startPos);
    }
    
    @Override
    protected void updateSnakeAvailablePositions(Point newPos, boolean isFood){
        
        Point lastBodyPartPos = cheeseSnake.getBody().getLast().getLocation();
        Point headPos = cheeseSnake.getHead().getLocation();
        int growCount = cheeseSnake.getGrowCount();
        boolean nextBodyPartSnake = cheeseSnake.isNextBodyPartSnake();
        
        if (isFood) growCount += 2;
        
        if (nextBodyPartSnake && growCount <= 0) {
            game.availablePositions.add(lastBodyPartPos);
        } else if (!nextBodyPartSnake) {
            game.availablePositions.add(headPos);
        }
        
        game.availablePositions.remove(newPos);
    }
    
    @Override
    protected Point getRandomFoodPosition() {
                
        List<Point> candidates = new ArrayList<>();
        
        for (Point pos : game.availablePositions) {
            
            int freeSides = 0;
            for (int[] dir : SIDES_DIRECTIONS) {
                Point sidePos = new Point(pos.x + dir[0], pos.y + dir[1]);
                if (game.availablePositions.contains(sidePos) || game.food.contains(sidePos)) freeSides++;
            }

            if (freeSides >= 2) {
                candidates.add(pos);
            }
        }        
        
        numAvailableFooodPositions = candidates.size();
        
        if (candidates.isEmpty()) {
            return null;
        }

        Random rand = new Random();
        
        Point candidate = candidates.get(rand.nextInt(candidates.size()));
        game.availablePositions.remove(candidate);
        
        return candidate;
    }
}
