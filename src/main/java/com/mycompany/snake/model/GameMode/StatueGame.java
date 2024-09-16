/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model.GameMode;

import com.mycompany.snake.model.GameModel;
import com.mycompany.snake.model.Square.StatueSquare;
import com.mycompany.snake.model.Square.CellType;
import com.mycompany.snake.model.Square.Square;
import java.awt.Point;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Eduard
 */
public class StatueGame extends ClassicGame {
    
    protected Set<StatueSquare> statues = new HashSet<>();
    private static final int MIN_FOOD_BEFORE_BREAK = 2;
    private static final int MAX_FOOD_BEFORE_BREAK = 8;

    public StatueGame(GameModel game) {
        super(game);        
    }
    
    @Override
    protected boolean checkCollision(Point snakeHeadPos) {
        
        boolean collision = super.checkCollision(snakeHeadPos);
        boolean statueCollision = checkSnakeListCollision(statues, snakeHeadPos);
        
        return collision || statueCollision;
    }
    
    @Override
    public void prepareNewGame() {
        
        super.prepareNewGame();

        postPrepareNewGameStatueGame();
    }
    
    protected void postPrepareNewGameStatueGame() {
        
        game.getSpecificModeLists().add(statues);

        statues.clear();
    }
    
    @Override
    protected boolean isPositionAvailable(Point position) {
        
        return super.isPositionAvailable(position) && !statues.contains(position);
    }
    
    @Override
    protected void snakeMove(Point newPos, boolean isFoodCollision) {
        
        super.snakeMove(newPos, isFoodCollision);
        
        if (isFoodCollision) placeStatue();
    }
    
    protected void placeStatue() {
        updateStatues();
        sculptStatue();
    }
    
    // Métodos Auxiliares
    
    protected void sculptStatue() {
        
        for (Point bodyPartPos : game.getSnake().getBody()) {
            statues.add(new StatueSquare(bodyPartPos, CellType.WALL_FILLED));
        }
        
        /* TODO revisar si este código puede ser útil
        for (Point bodyPartPos : game.snake.getBody()) {
            if (statues.add(new StatueSquare(bodyPartPos, CellType.WALL_FILLED))) {
                game.availablePositions.remove(bodyPartPos);
            }
        }
        */
    }
    
    protected void updateStatues() {
        
        Set<Square> snakeBodySet = new HashSet<>(game.getSnake().getBody());
        
        Set<StatueSquare> statuesNotFilled = new HashSet<>(statues);
        statuesNotFilled.removeAll(snakeBodySet);
        
        for (StatueSquare statueSquare : statuesNotFilled) {
            
            if (statueSquare.getCellType() == CellType.WALL_FILLED) {
                statueSquare.setFoodBeforeBreak(generateNumFoodBeforeBreak());
            }
            
            statueSquare.decreaseFoodBeforeBreak();
            int foodBeforeBreak = statueSquare.getFoodBeforeBreak();
            
            if (foodBeforeBreak > 1) {
                
                statueSquare.setCellType(CellType.WALL_STATUE);
                
            } else if (foodBeforeBreak == 1) {
                
                statueSquare.setCellType(CellType.WALL_CRACKED);
                
            } else if (foodBeforeBreak == 0) {
                
                statues.remove(statueSquare);
                game.getAvailablePositions().add(new Point(statueSquare));
            }
        }
    }
    
    protected int generateNumFoodBeforeBreak() { // TODO (tal vez así esta bien :) o tal vez falta buscar una mejor ecuación que cuanto mayor sea el número menor sea la probabilidad de obtenerlo
        
        Random random = new Random();
        return random.nextInt(MAX_FOOD_BEFORE_BREAK - MIN_FOOD_BEFORE_BREAK + 1) + MIN_FOOD_BEFORE_BREAK;
    }
}