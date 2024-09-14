/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model;

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
    protected boolean checkCollision() {
        
        Point snakeHeadPos = game.snake.getHead().getLocation();
        
        boolean collision = super.checkCollision();
        boolean statueCollision = checkSnakeListCollision(statues, snakeHeadPos);
        
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
    protected boolean isSnakePositionAvailable(Point position) {
        
        return super.isSnakePositionAvailable(position) && !statues.contains(position);
    }
    
    @Override
    protected void eatFood(Point newPos) {
        
        placeStatue();
        
        super.eatFood(newPos);
    }
    
    protected void placeStatue() {
        updateStatues();
        sculptStatue();
    }
    
    // Métodos Auxiliares
    
    protected void sculptStatue() {
        
        for (Point bodyPartPos : game.snake.getBody()) {
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
        
        Set<Square> snakeBodySet = new HashSet<>(game.snake.getBody());
        
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
                game.availablePositions.add(new Point(statueSquare));
            }
        }
    }
    
    protected int generateNumFoodBeforeBreak() { // TODO (tal vez así esta bien :) falta buscar una mejor ecuación que cuanto mayor sea el número menor sea la probabilidad de obtenerlo
        
        Random random = new Random();
        return random.nextInt(MAX_FOOD_BEFORE_BREAK - MIN_FOOD_BEFORE_BREAK + 1) + MIN_FOOD_BEFORE_BREAK;
    }
}