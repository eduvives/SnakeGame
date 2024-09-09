/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model;

import java.awt.Point;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Eduard
 */
public class ClassicGame {

    protected final GameModel game;
    
    public ClassicGame(GameModel game) {
        this.game = game;
    }
    
    // CHECKS
    
    protected boolean checkCollision() {
        
        Point snakeHeadPos = game.snake.getHead().getLocation();
        
        boolean bodyCollision = checkSnakeListCollision(game.snake.getBody(), snakeHeadPos);
        boolean boundariesCollision = snakeHeadPos.x < 0 || snakeHeadPos.x >= game.numBoardCols || snakeHeadPos.y < 0 || snakeHeadPos.y >= game.numBoardRows;
        
        return bodyCollision || boundariesCollision;
    }
    
    protected boolean checkFeast() {
        return game.availablePositions.isEmpty() && game.food.isEmpty();
        // return game.score == (game.numBoardRows * game.numBoardCols) - Snake.START_LENGTH;
    }
    
    // Comprueba si hay alguna colisión entre la posición relacionada con la cabeza de la serpiente y la nueva posición proporcionada
    protected boolean checkSnakeListCollision(List<Square> list, Point position) {
        return list.contains(position);
    }
    
    // NEW GAME
    
    protected void prepareNewGame() {
        
        game.observer.onNewGame();
        
        game.gameStarted = false;
        game.gameEnded = false;
        game.setScore(0);
        
        game.availablePositions.clear();
        game.specificModeLists.clear();
        game.food.clear();
        
        // Inicializar posiciones disponibles
        for (int i = 0; i < game.numBoardRows; i++) {
            for (int j = 0; j < game.numBoardCols; j++) {
                game.availablePositions.add(new Point(j,i));
            }
        }
    }
    
    protected void initializeSnake() {
        game.snake = createSnakeInstance();
        game.snake.initializeSnake(new Point(game.startPos));
    }
    
    protected Snake createSnakeInstance() {
        return new Snake();
    }
    
    protected void initializeGameSnake(){
        initializeSnake();
        removeAllSnakeAvailablePositions();
    }
    
    protected void removeAllSnakeAvailablePositions(){
        for (Point bodyPartPos : game.snake.getBody()) {
            game.availablePositions.remove(bodyPartPos);
        }
        game.availablePositions.remove(game.snake.getHead());
    }
    
    protected void updateSnakeAvailablePositions(boolean isFoodCollision, Point previousLastBodyPartPos){
        
        game.availablePositions.remove(game.snake.getHead());
        
        if(positionAvailableAfterSnakeSimpleMove(isFoodCollision, previousLastBodyPartPos)) {
            game.availablePositions.add(previousLastBodyPartPos);
        }
        
    }
    
    protected boolean positionAvailableAfterSnakeSimpleMove(boolean isFoodCollision, Point previousLastBodyPartPos) {
        return !isFoodCollision && !game.snake.getHead().equals(previousLastBodyPartPos);
    }
    
    // GAME LOOP
    
    protected void nextLoop() {
        
        Point newPos = getNewPos(game.snake.getDirection().getLocation());

        boolean isFoodCollision = checkSnakeListCollision(game.food, newPos);
        boolean isFeast = false;
        boolean isCollision = false;
        
        snakeMove(newPos, isFoodCollision);
        
        if (isFoodCollision) {
            eatFood(newPos);
            isFeast = checkFeast();
            increaseScore();
        } else {
            isCollision = checkCollision();
        }
        
        if (!isCollision) {
            game.observer.onViewChanged();
        }
        
        if (isCollision || isFeast) {
            game.observer.onGameEnded(isFeast);
        }
    }
    
    protected void snakeMove(Point newPos, boolean isFoodCollision) {
        
        Point previousLastBodyPartPos = game.snake.getBody().getLast().getLocation();
        
        snakeSimpleMove(newPos, isFoodCollision);
        updateSnakeAvailablePositions(isFoodCollision, previousLastBodyPartPos);
    }
    
    protected void snakeSimpleMove(Point newPos, boolean isFoodCollision) {
        game.snake.move(newPos, isFoodCollision);
    }
    
    protected Point getNewPos(Point newDirection) {
        return new Point(game.snake.getHead().x + newDirection.x, game.snake.getHead().y + newDirection.y);
    }
    
    private void increaseScore() {
        game.setScore(game.score + 1);
    }
    
    protected void eatFood(Point newPos) {
        game.food.remove(newPos);
        placeFood();
    }
    
    // PLACE NEW FOOD
    
    protected void placeFood() {
        
        int numFoodToPlace = getNumFoodToPlace();
        
        for (int i = 0; i < numFoodToPlace; i++) {
            
            Point foodPos = getRandomFoodPosition();
            
            if (foodPos != null) {
                addNewFoodSquare(foodPos);
            } else {
                break;
            }
        }
    }
    
    protected int getNumFoodToPlace() {
        
        Random rand = new Random();
        
        int numPlacedFood = game.food.size();
        int numTotalFoodToPlace = getNumTotalFoodToPlace();
        
        if (game.numFood != -1) {
            return numTotalFoodToPlace - numPlacedFood;
        } else if (game.numFood == -1 && numPlacedFood == 0) { // Random Food Num
            return rand.nextInt(6) + 1; // Rand Num Food To Place
        } else {
            return 0;
        }
    }
    
    protected int getNumTotalFoodToPlace() {
        return game.numFood;
    }
    
    protected void addNewFoodSquare(Point foodPos) {
        game.food.add(new Square(foodPos, CellType.FOOD));
    }

    protected Point getRandomFoodPosition() {
        
        if (noFoodPositions()) {
            return null;
        }
        
        Random rand = new Random();
        
        int index = rand.nextInt(game.availablePositions.size());
        return game.availablePositions.remove(index);

    }
    
    protected boolean noFoodPositions() {
        return game.availablePositions.isEmpty();
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
