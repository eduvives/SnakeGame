/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.controller;

import com.mycompany.snake.model.CellType;
import com.mycompany.snake.model.Snake;
import com.mycompany.snake.model.Square;
import java.awt.Point;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Eduard
 */
public class ClassicGame {

    protected final GameLogic game;
    
    public ClassicGame(GameLogic game) {
        this.game = game;
    }
    
    // CHECKS
    
    protected boolean checkCollision() {
        
        Point snakeHeadPos = game.snake.getHead().getLocation();
        
        boolean bodyCollision = game.snake.getBody().contains(snakeHeadPos);
        boolean boundariesCollision = snakeHeadPos.x < 0 || snakeHeadPos.x >= game.numBoardCols || snakeHeadPos.y < 0 || snakeHeadPos.y >= game.numBoardRows;
        
        return bodyCollision || boundariesCollision;
    }
    
    protected boolean checkFeast() {
        return game.availablePositions.isEmpty() && game.food.isEmpty();
        // return game.score == (game.numBoardRows * game.numBoardCols) - Snake.START_LENGTH;
    }
    
    private boolean checkFood(Point newPos) {        
        return game.food.contains(newPos);
    }
    
    // NEW GAME
    
    protected void prepareNewGame() {
        if (!game.isBoardUpdated) {
            game.updateBoardParams();
            game.startPos = new Point(Snake.START_LENGTH + 1, game.numBoardRows / 2);
        }
        
        game.gameStarted = false;
        game.gameEnded = false;
        game.score = 0;
        game.updateScore();
        
        game.availablePositions.clear();
        game.specificModeLists.clear();
        game.food.clear();
        game.inputQueue.clear();

        for (int i = 0; i < game.numBoardRows; i++) {
            for (int j = 0; j < game.numBoardCols; j++) {
                game.availablePositions.add(new Point(j,i));
            }
        }
    }
    
    protected void initializeSnake(){
        game.snake = createSnakeInstance(new Point(game.startPos));
        removeAllSnakeAvailablePositions();
    }
    
    protected Snake createSnakeInstance(Point startPos) {
        return new Snake(startPos);
    }
    
    protected void removeAllSnakeAvailablePositions(){
        for (Point bodyPart : game.snake.getBody()) {
            game.availablePositions.remove(bodyPart);
        }
        game.availablePositions.remove(game.snake.getHead());
    }
    
    protected void updateSnakeAvailablePositions(Point newPos, boolean isFood){
        
        if(!isFood) {
            game.availablePositions.add(game.snake.getBody().getLast().getLocation());
        }
        
        game.availablePositions.remove(newPos);
    }
    
    // GAME LOOP
    
    protected void nextLoop() {
        
        Point newPos = getNewPos(game.snake.getDirection().getLocation());

        boolean isFood = checkFood(newPos);
        boolean isFeast = false;
        boolean isCollision = false;
        
        snakeMove(newPos, isFood);
        
        if (isFood) {
            eatFood(newPos);
            isFeast = checkFeast();
            increaseScore();
        } else {
            isCollision = checkCollision();
        }
        
        if (!isCollision) {
            game.updateView();
            game.view.getBoardPanel().repaint();
        }
        
        if (isCollision || isFeast) {
            game.gameEnd(isFeast);
        }
    }
    
    protected void snakeMove(Point newPos, boolean isFood) {
        updateSnakeAvailablePositions(newPos, isFood);
        snakeSimpleMove(newPos, isFood);
    }
    
    protected void snakeSimpleMove(Point newPos, boolean isFood) {
        game.snake.move(newPos, isFood);
    }
    
    protected Point getNewPos(Point newDirection) {
        return new Point(game.snake.getHead().x + newDirection.x, game.snake.getHead().y + newDirection.y);
    }
    
    private void increaseScore() {
        game.score += 1;
        game.updateScore();
    }
    
    protected void eatFood(Point newPos) {
        game.food.remove(newPos);
        placeFood();
    }
    
    // OTHERS
    
    protected void placeFood() {
        
        Random rand = new Random(); 
        
        int numPlacedFood = game.food.size();

        if (game.numFood == -1) { // Random Food Num
            if (numPlacedFood == 0) {
                int randNumFood = rand.nextInt(6) + 1;

                for (int i = 0; i < randNumFood; i++) {
                    Point foodPos = getRandomFoodPosition();
                    if (foodPos != null) game.food.add(new Square(foodPos, CellType.FOOD));
                    // TODO else break?
                }
            }
        } else {
            for (int i = 0; i < game.numFood - numPlacedFood; i++) {
                Point foodPos = getRandomFoodPosition();
                if (foodPos != null) game.food.add(new Square(foodPos, CellType.FOOD));
            }
        }
    }

    protected Point getRandomFoodPosition() {
        
        if (game.availablePositions.isEmpty()) {
            return null;
        }
        
        Random rand = new Random();
        
        int index = rand.nextInt(game.availablePositions.size());
        return game.availablePositions.remove(index);

    }
    
    // TODO not used
    private Point getRandomAvailablePosition() {
        
        if (game.availablePositions.isEmpty()) {
            return null;
        }
        
        Random rand = new Random();   
        
        int index = rand.nextInt(game.availablePositions.size());
        return game.availablePositions.remove(index);

    }
    
    // Métodos Auxiliares Subclases
    
    protected void restoreDirection(Point snakeHead, Point snakeFirstBodyPartPos) {
        game.snake.getDirection().setLocation(getDefaultDirection(snakeHead, snakeFirstBodyPartPos));
    }
    
    protected Point getDefaultDirection(Point snakeHead, Point snakeFirstBodyPartPos) {
        return new Point(snakeHead.x - snakeFirstBodyPartPos.x, snakeHead.y - snakeFirstBodyPartPos.y);
    }
    
    public static final int SPAWN_RADIUS_WIDTH = 7;
    
    protected Set<Point> getSpawnRadius() {
        
        Set<Point> newSpawnRadius = new HashSet<>();
        
        int size = (SPAWN_RADIUS_WIDTH - 1) / 2;

        for (int x = -size; x <= size; x++) {
            int yLimit = size - Math.abs(x);

            for (int y = -yLimit; y <= yLimit; y++) {
                int newX = game.snake.getHead().x + x;
                int newY = game.snake.getHead().y + y;
                
                if (newX >= 0 && newX < game.numBoardCols && newY >= 0 && newY < game.numBoardRows) {
                    newSpawnRadius.add(new Point(newX, newY));
                }
            }
        }
        
        return newSpawnRadius;
    }
}
