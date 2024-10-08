/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model.GameMode;

import com.mycompany.snake.model.GameModel;
import com.mycompany.snake.model.Snake.CheeseSnake;
import com.mycompany.snake.model.Snake.Snake;
import com.mycompany.snake.model.Square.CellConfiguration.SpecificCellType;
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
    
    public CheeseGame(GameModel game) {
        super(game);
    }

    public List<Point> getFoodPositionCandidates() {
        return foodPositionCandidates;
    }
    
    @Override
    protected boolean checkFeast() {
        return foodPositionCandidates.isEmpty() && game.getFood().isEmpty();
    }
    
    // Sobrescritura del método auxiliar para facilitar la adaptación simple del método 
    // checkSnakeBodyCollision en combinación con el modo Cheese
    @Override
    protected int getNumPositionsBodyCollision() {
        
        boolean ignoreLastPosition = cheeseSnake.getCheeseBody().getLast().getSpecificCellType()== SpecificCellType.SNAKE_BODY && cheeseSnake.getGrowCount() <= 0;
                
        return ignoreLastPosition ? cheeseSnake.getBody().size() - 1 : cheeseSnake.getBody().size();
    }
    
    @Override
    public void initializeSnake(){
        
        super.initializeSnake();
        
        initializeCheeseSnake();
    }
    
    protected void initializeCheeseSnake() {
        cheeseSnake = (CheeseSnake) game.getSnake();
    }
    
    @Override
    protected Snake createSnakeInstance() {
        return new CheeseSnake();
    }

    @Override
    public void placeFood() {

        prevPlaceFoodCheeseGame();

        super.placeFood();
    }
    
    // Update Food Position Candidates
    protected void prevPlaceFoodCheeseGame() {
        
        foodPositionCandidates.clear();
        
        for (Point pos : game.getAvailablePositions()) {
            
            int freeSides = 0;
            for (int[] dir : SIDES_DIRECTIONS) {
                Point sidePos = new Point(pos.x + dir[0], pos.y + dir[1]);
                if (game.getAvailablePositions().contains(sidePos) || game.getFood().contains(sidePos)) freeSides++;
            }

            if (freeSides >= 2) {
                foodPositionCandidates.add(pos.getLocation());
            }
        }
    }
    
    @Override
    protected Point getRandomFoodPosition() {
        
        if (noFoodPositions()) {
            return null;
        }

        Random rand = new Random();
        
        Point candidate = foodPositionCandidates.remove(rand.nextInt(foodPositionCandidates.size()));
        game.getAvailablePositions().remove(candidate);
        
        return candidate;
    }
    
    @Override
    protected boolean noFoodPositions() {
        return foodPositionCandidates.isEmpty();
    }
}
