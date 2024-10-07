/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model.GameMode;

import com.mycompany.snake.model.GameModel;
import com.mycompany.snake.model.Snake.Snake;
import com.mycompany.snake.model.Square.CellConfiguration.CellType;
import com.mycompany.snake.model.Square.Square;
import java.awt.Point;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
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
    
    protected boolean checkCollision(Point newHeadPos) {
        
        boolean bodyCollision = checkSnakeBodyCollision(newHeadPos);
        boolean boundariesCollision = newHeadPos.x < 0 || newHeadPos.x >= game.getNumBoardCols() || newHeadPos.y < 0 || newHeadPos.y >= game.getNumBoardRows();
        
        return bodyCollision || boundariesCollision;
    }
    
    protected boolean checkFeast() {
        return game.getAvailablePositions().isEmpty() && game.getFood().isEmpty();
    }
    
    private boolean checkSnakeBodyCollision(Point newHeadPos) {
        
        LinkedList<Square> body = game.getSnake().getBody();

        boolean isBodyCollision = false;
        int numPositions = getNumPositionsBodyCollision();
        
        for (int i = 0; i < numPositions; i++) {
            if (checkSnakePositionCollision(body.get(i), newHeadPos)) {
                isBodyCollision = true;
                break; // Salir del bucle al encontrar una colisión
            }
        }

        return isBodyCollision;
    }
    
    // Método auxiliar para facilitar la adaptación simple del método checkSnakeBodyCollision en 
    // combinación con el modo Cheese (clase CheeseGame)
    protected int getNumPositionsBodyCollision() {
        
        // Tal como está implementada la lógica no es necesario contemplar el caso en que haya una colisión 
        // al mismo tiempo que estamos en una posición de comida, ya se da prioridad a la comida y no se comprueba 
        // la colisión con otro elemento
    
        return game.getSnake().getBody().size() - 1;
    }
    
    
    // Método auxiliar para facilitar la adaptación del método checkSnakeBodyCollision en la 
    // clase BlenderGame al estar activo el modo Dimension
    protected boolean checkSnakePositionCollision(Square square, Point newHeadPos) {
        return square.equals(newHeadPos);
    }
    
    // Comprueba si hay alguna colisión entre la posición relacionada con la cabeza de la serpiente y la nueva posición proporcionada
    protected boolean checkSnakeListCollision(Collection<? extends Square> list, Point newHeadPos) {
        return list.contains(newHeadPos);
    }
    
    // NEW GAME
    
    public void prepareNewGame() {

        game.setGameActive(true);
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
        return !game.getSnake().getBody().contains(position) && !game.getSnake().getHead().equals(position);
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
    
    @Override
    public void onShrink() {
        game.getObserver().onShrink();
    }
    
    // GAME LOOP
    
    public void nextLoop() {
        
        Point newPos = getNewPos(game.getSnake().getDirection());

        boolean isCollision = checkCollision(newPos);
        
        // La colisión es lo primero en comprobarse para evitar ejecutar código innecesario en caso de ser detectada
        if (isCollision) {
            game.getObserver().onGameEnded(false);
            return;
        }
        
        boolean isFoodCollision = checkSnakeListCollision(game.getFood(), newPos);
        boolean isFeast = false;
        
        if (isFoodCollision) {
            // La posición de comida se elimina antes del movimiento de la serpiente para que no 
            // se detecte incorrectamente como una posición ocupada cuando ha dejado de serlo
            game.getFood().remove(newPos);
            // El incremento de la puntuación (score) se realiza antes del movimiento de la serpiente 
            // para ser utilizado al generar las paredes en el modo Wall
            increaseScore();
        }

        snakeMove(newPos, isFoodCollision);

        if (isFoodCollision) {
            // El método placeFood se llama despues de mover la serpiente para tratar con las posiciones 
            // disponibles correctas, ya que el movimiento de la serpiente puede cambiarlas
            placeFood();
            // La comprobación checkFeast debe realizarse después de eliminar la posición actual de comida, 
            // mover la serpiente y establecer la nueva posición de comida llamando al método placeFood
            isFeast = checkFeast();
        }

        if (isFeast) {
            game.getObserver().onGameEnded(true);
        }

        game.getObserver().onViewChanged();
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
    
    protected void decreaseScore() {
        game.setScore(game.getScore() - 1);
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
    
    // Not used
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
