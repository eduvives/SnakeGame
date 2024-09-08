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
    private List<Point> foodPositionCandidates = new ArrayList<>();;
    
    public CheeseGame(GameLogic game) {
        super(game);
    }
    
    @Override
    protected boolean checkFeast() {
        return foodPositionCandidates.isEmpty() && game.food.isEmpty();
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
    protected void updateSnakeAvailablePositions(boolean isFoodCollision, Point previousLastBodyPartPos){
        
        int growCount = cheeseSnake.getGrowCount();
        int growCountPrev = growCount + (growCount > 0 ? 1 : 0) - (isFoodCollision ? 2 : 0); // ???
        boolean nextBodyPartSnake = !cheeseSnake.isNextBodyPartSnake();
        
        if (nextBodyPartSnake && growCountPrev <= 0) {
            game.availablePositions.add(previousLastBodyPartPos);
        } else if (!nextBodyPartSnake) {
            game.availablePositions.add(cheeseSnake.getEmptyBody().getFirst().getLocation()); // Previous Snake Head Position
        }
        
        game.availablePositions.remove(cheeseSnake.getHead().getLocation());
    }
    
    @Override
    protected void placeFood() {

        prevPlaceFoodCheeseGame();

        super.placeFood();
    }
    
    // Update Food Position Candidates
    protected void prevPlaceFoodCheeseGame() {
        
        foodPositionCandidates.clear();
        
        for (Point pos : game.availablePositions) {
            
            int freeSides = 0;
            for (int[] dir : SIDES_DIRECTIONS) {
                Point sidePos = new Point(pos.x + dir[0], pos.y + dir[1]);
                if (game.availablePositions.contains(sidePos) || game.food.contains(sidePos)) freeSides++; // TODO revisar amb Blender Dimension mode (checkSnakeListCollision)?
            }

            if (freeSides >= 2) {
                foodPositionCandidates.add(pos);
            }
        }
    }
    
    @Override
    protected Point getRandomFoodPosition() {
        
        if (foodPositionCandidates.isEmpty()) {
            return null;
        }

        Random rand = new Random();
        
        Point candidate = foodPositionCandidates.remove(rand.nextInt(foodPositionCandidates.size()));
        game.availablePositions.remove(candidate);
        
        return candidate;
    }
}
