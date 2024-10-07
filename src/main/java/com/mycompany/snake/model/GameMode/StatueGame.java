/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model.GameMode;

import com.mycompany.snake.model.GameModel;
import com.mycompany.snake.model.Square.StatueSquare;
import com.mycompany.snake.model.Square.CellConfiguration.CellType;
import com.mycompany.snake.model.Square.CellConfiguration.SpecificCellType;
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
    private static final int MAX_FOOD_BEFORE_BREAK = 6;

    public StatueGame(GameModel game) {
        super(game);        
    }
    
    @Override
    protected boolean checkCollision(Point newHeadPos) {
        
        boolean collision = super.checkCollision(newHeadPos);
        boolean statueCollision = checkSnakeListCollision(statues, newHeadPos);
        
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
    protected void snakeMove(Point newHeadPos, boolean isFoodCollision) {
        
        super.snakeMove(newHeadPos, isFoodCollision);
        
        if (isFoodCollision) placeStatue();
    }
    
    protected void placeStatue() {
        updateStatues();
        sculptStatue();
    }
    
    // Métodos Auxiliares
    
    protected void sculptStatue() {
        
        for (Point bodyPartPos : game.getSnake().getBody()) {
            statues.add(new StatueSquare(bodyPartPos));
        }
    }
    
    protected void updateStatues() {
        
        Set<Square> snakeBodySet = new HashSet<>(game.getSnake().getBody());
        
        Set<StatueSquare> statuesNotFilled = new HashSet<>(statues);
        statuesNotFilled.removeAll(snakeBodySet);
        
        for (StatueSquare statueSquare : statuesNotFilled) {
            
            if (statueSquare.getSpecificCellType() == SpecificCellType.FILLED_STATUE) {
                statueSquare.setFoodBeforeBreak(generateNumFoodBeforeBreak());
            }
            
            statueSquare.decreaseFoodBeforeBreak();
            int foodBeforeBreak = statueSquare.getFoodBeforeBreak();
            
            if (foodBeforeBreak > 1) {
                
                statueSquare.setSpecificCellType(SpecificCellType.STATUE);
                
            } else if (foodBeforeBreak == 1) {
                
                statueSquare.setSpecificCellType(SpecificCellType.CRACKED_STATUE);
                
            } else if (foodBeforeBreak == 0) {
                
                statues.remove(statueSquare);
                game.getAvailablePositions().add(new Point(statueSquare));
            }
        }
    }
    // Devuelve un número aleatorio entre los valores de las constantes MIN_FOOD_BEFORE_BREAK y MAX_FOOD_BEFORE_BREAK (incluidos).
    // El valor devuelto indicará el número de frutas que debe comer la serpiente para que se rompa la celda de estatua.
    protected int generateNumFoodBeforeBreak() {
        
        Random random = new Random();
        return random.nextInt(MAX_FOOD_BEFORE_BREAK - MIN_FOOD_BEFORE_BREAK + 1) + MIN_FOOD_BEFORE_BREAK;
    }
}