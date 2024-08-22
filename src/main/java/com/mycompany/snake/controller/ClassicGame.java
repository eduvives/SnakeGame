/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.controller;

import com.mycompany.snake.model.Snake;
import java.awt.Point;
import java.util.Random;

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
    
    protected boolean checkCollision(Point pos) {
        
        boolean bodyCollision = game.snake.getBody().contains(pos);
        boolean boundariesCollision = pos.x < 0 || pos.x >= game.numBoardCols || pos.y < 0 || pos.y >= game.numBoardRows;
        
        return bodyCollision || boundariesCollision;
    }
    
    private boolean checkFeast() {
        return game.score == (game.numBoardRows * game.numBoardCols) - Snake.START_LENGTH;
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
        game.score = 0;
        game.updateScore();
        
        game.availablePositions.clear();
        game.specificModeLists.clear();
        game.food.clear();
        game.inputQueue.clear();
        game.direction.setLocation(1, 0);

        for (int i = 0; i < game.numBoardRows; i++) {
            for (int j = 0; j < game.numBoardCols; j++) {
                game.availablePositions.add(new Point(j,i));
            }
        }
    }
    
    protected void createSnake(){
        game.snake = new Snake(new Point(game.startPos));
        removeSnakeAvailablePositions();
    }
    
    protected void removeSnakeAvailablePositions(){
        for (Point bodyPart : game.snake.getBody()) {
            game.availablePositions.remove(bodyPart);
        }
        game.availablePositions.remove(game.snake.getHead());
    }
    
    protected void addSnakeAvailablePositions(){
        for (Point bodyPart : game.snake.getBody()) {
            game.availablePositions.add(new Point(bodyPart));
        }
        game.availablePositions.add(new Point(game.snake.getHead()));
    }
    
    // GAME LOOP
    
    protected void snakeMove(Point currentDirection) {
        
        Point newPos = new Point(game.snake.getHead().x + currentDirection.x, game.snake.getHead().y + currentDirection.y);

        boolean isFood = checkFood(newPos);
        
        addSnakeAvailablePositions();
        game.snake.move(newPos, isFood);
        removeSnakeAvailablePositions();
        
        if (isFood) {
            eatFood(newPos);
            increaseScore();
        }
        
        boolean isFeast = checkFeast();
        boolean isCollision = checkCollision(newPos);

        if (!isCollision) {
            game.updateView();
            game.view.getBoardPanel().repaint();
        } else {
            game.gameEnd(isFeast);
        }
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
                    if (!game.availablePositions.isEmpty()) {
                        game.food.add(getRandomAvailablePosition());
                    }
                }
            }
        } else {
            for (int i = 0; i < game.numFood - numPlacedFood; i++) {
                if (!game.availablePositions.isEmpty()) {
                    game.food.add(getRandomAvailablePosition());
                }
            }
        }
    }

    private Point getRandomAvailablePosition() {
        
        Random rand = new Random();   
        
        int index = rand.nextInt(game.availablePositions.size());
        return game.availablePositions.remove(index);

    }
}
