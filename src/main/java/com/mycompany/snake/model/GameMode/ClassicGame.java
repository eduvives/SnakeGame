/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model.GameMode;

import com.mycompany.snake.model.GameModel;
import com.mycompany.snake.model.Snake.Snake;
import com.mycompany.snake.model.Square.CellType;
import com.mycompany.snake.model.Square.Square;
import java.awt.Point;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Eduard
 */
public class ClassicGame implements SnakeListener {

    protected final GameModel game;
    
    public ClassicGame(GameModel game) {
        this.game = game;
    }
    
    // CHECKS
    
    protected boolean checkCollision(Point snakeHeadPos) {
        
        boolean bodyCollision = checkSnakeListCollision(game.getSnake().getBody(), snakeHeadPos);
        boolean boundariesCollision = snakeHeadPos.x < 0 || snakeHeadPos.x >= game.getNumBoardCols() || snakeHeadPos.y < 0 || snakeHeadPos.y >= game.getNumBoardRows();
        
        return bodyCollision || boundariesCollision;
    }
    
    protected boolean checkFeast() {
        return game.getAvailablePositions().isEmpty() && game.getFood().isEmpty();
        // return game.getScore() == (game.getNumBoardRows() * game.getNumBoardCols()) - Snake.START_LENGTH;
    }
    
    // Comprueba si hay alguna colisión entre la posición relacionada con la cabeza de la serpiente y la nueva posición proporcionada
    protected boolean checkSnakeListCollision(Collection<? extends Square> list, Point position) {
        return list.contains(position);
    }
    
    // NEW GAME
    
    public void prepareNewGame() {
        
        game.getObserver().onNewGame();

        game.setGameEnded(false);
        game.setScore(0);
        game.initializeCurrentGameHighScore();
        game.setNewHighScore(false);
        
        game.getAvailablePositions().clear();
        game.getSpecificModeLists().clear();
        game.getFood().clear();
        
        // Inicializar posiciones disponibles
        for (int i = 0; i < game.getNumBoardRows(); i++) {
            for (int j = 0; j < game.getNumBoardCols(); j++) {
                game.getAvailablePositions().add(new Point(j,i));
            }
        }
    }
    
    protected Snake createSnakeInstance() {
        return new Snake();
    }
    
    public void initializeSnake(){
        game.setSnake(createSnakeInstance());
        addListener();
        game.getSnake().initializeSnake(new Point(game.getStartPos()));
    }
    
    protected boolean isPositionAvailable(Point position) {
        return !game.getSnake().getBody().contains(position)  && !game.getSnake().getHead().equals(position);
    }
    
    // Métodos Snake Listener
    
    private void addListener() {
        game.getSnake().setListener(this);
    }

    @Override
    public void onPositionRemoved(Point position) {
        if (isPositionAvailable(position)) game.getAvailablePositions().add(position);
    }

    @Override
    public void onPositionAdded(Point position) {
        game.getAvailablePositions().remove(position);
    }
    
    // GAME LOOP
    
    public void nextLoop() {
        
        Point newPos = getNewPos(game.getSnake().getDirection());

        boolean isFoodCollision = checkSnakeListCollision(game.getFood(), newPos);
        boolean isFeast = false;
        boolean isCollision = false;
        
        if (isFoodCollision) {
            game.getFood().remove(newPos);
            increaseScore();
        }

        snakeMove(newPos, isFoodCollision);

        if (isFoodCollision) {
            placeFood();
            isFeast = checkFeast();
        } else {
            isCollision = checkCollision(newPos);
        }
        
        if (isCollision) {
            game.getObserver().onGameEnded(false);
        } else {
            game.getObserver().onViewChanged();
        }
        
        if (isFeast) {
            game.getObserver().onGameEnded(true);
        }
    }
    
    protected void snakeMove(Point newPos, boolean isFoodCollision) {
        game.getSnake().move(newPos, isFoodCollision);
    }
    
    protected Point getNewPos(Point newDirection) {
        return new Point(game.getSnake().getHead().x + newDirection.x, game.getSnake().getHead().y + newDirection.y);
    }
    
    private void increaseScore() {
        game.setScore(game.getScore() + 1);
    }
    
    // PLACE NEW FOOD
    
    public void placeFood() {
        
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
        
        int numPlacedFood = game.getFood().size();
        int numTotalFoodToPlace = getNumTotalFoodToPlace();
        
        if (game.getNumFood() != -1) {
            return numTotalFoodToPlace - numPlacedFood;
        } else if (game.getNumFood() == -1 && numPlacedFood == 0) { // Random Food Num
            return rand.nextInt(6) + 1; // Rand Num Food To Place
        } else {
            return 0;
        }
    }
    
    protected int getNumTotalFoodToPlace() {
        return game.getNumFood();
    }
    
    protected void addNewFoodSquare(Point foodPos) {
        game.getFood().add(new Square(foodPos, CellType.FOOD));
    }

    protected Point getRandomFoodPosition() {
        
        if (noFoodPositions()) {
            return null;
        }
        
        Random rand = new Random();
        
        int index = rand.nextInt(game.getAvailablePositions().size());
        return game.getAvailablePositions().remove(index);

    }
    
    protected boolean noFoodPositions() {
        return game.getAvailablePositions().isEmpty();
    }
    
    // TODO not used
    private Point getRandomAvailablePosition() {
        
        if (game.getAvailablePositions().isEmpty()) {
            return null;
        }
        
        Random rand = new Random();   
        
        int index = rand.nextInt(game.getAvailablePositions().size());
        return game.getAvailablePositions().remove(index);

    }
    
    // Métodos Auxiliares Subclases
    
    public static final int SPAWN_RADIUS_WIDTH = 7;
    
    protected Set<Point> getSpawnRadius() {
        
        Set<Point> newSpawnRadius = new HashSet<>();
        
        int size = (SPAWN_RADIUS_WIDTH - 1) / 2;

        for (int x = -size; x <= size; x++) {
            int yLimit = size - Math.abs(x);

            for (int y = -yLimit; y <= yLimit; y++) {
                int newX = game.getSnake().getHead().x + x;
                int newY = game.getSnake().getHead().y + y;
                
                addSpawnRadiusPoint(newX, newY, newSpawnRadius);
            }
        }
        
        return newSpawnRadius;
    }
    
    protected void addSpawnRadiusPoint(int newX, int newY, Set<Point> newSpawnRadius) {
        if (newX >= 0 && newX < game.getNumBoardCols() && newY >= 0 && newY < game.getNumBoardRows()) {
            newSpawnRadius.add(new Point(newX, newY));
        }
    }
}
