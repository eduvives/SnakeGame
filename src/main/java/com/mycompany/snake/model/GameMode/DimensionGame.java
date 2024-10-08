/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model.GameMode;

import com.mycompany.snake.model.GameModel;
import com.mycompany.snake.model.Snake.DimensionSnake;
import com.mycompany.snake.model.Snake.Snake;
import com.mycompany.snake.model.Square.DimensionSquare;
import com.mycompany.snake.model.Square.CellConfiguration.CellType;
import com.mycompany.snake.model.Square.DimensionSquareInterface;
import com.mycompany.snake.model.Square.Square;
import java.awt.Point;
import java.util.Collection;

/**
 *
 * @author Eduard
 */
public class DimensionGame extends ClassicGame {
    
    protected DimensionSnake dimensionSnake;
    
    private boolean otherDimensionFood;
    
    public DimensionGame(GameModel game) {
        super(game);
    }
    
    @Override
    protected boolean checkSnakePositionCollision(Square square, Point newHeadPos) {
        
        return super.checkSnakePositionCollision(square, newHeadPos) && 
                square instanceof DimensionSquareInterface dimensionSquare &&
                !dimensionSquare.isOtherDimension();
    }
    
    @Override
    protected boolean checkSnakeListCollision(Collection<? extends Square> list, Point newHeadPos) {
        
        return list.stream()
        .filter(square -> square.equals(newHeadPos))
        .anyMatch(square -> square instanceof DimensionSquareInterface dimensionSquare
            && !dimensionSquare.isOtherDimension());
    }
    
    @Override
    public void initializeSnake(){
        
        super.initializeSnake();
        
        initializeDimensionSnake();
    }
    
    protected void initializeDimensionSnake() {
        dimensionSnake = (DimensionSnake) game.getSnake();
    }
    
    @Override
    protected Snake createSnakeInstance() {
        return new DimensionSnake();
    }
    
    // En la versión normal (no Blender) de este modo, sirve para indicar si, después de moverse la serpiente, 
    // la posición previa de cola (última posición del cuerpo de la serpiente) debe ser marcado como posición disponible, 
    // comprobando si hay algún elemento en esa posición
    @Override
    protected boolean isPositionAvailable(Point position) {
        
        return super.isPositionAvailable(position) && !game.getFood().contains(position);
     }
    
    @Override
    protected void snakeMove(Point newHeadPos, boolean isFoodCollision) {
        
        super.snakeMove(newHeadPos, isFoodCollision);
        
        if (isFoodCollision) toggleGameDimension();
    }
    
    protected void toggleGameDimension() {
        
        for (Square bodyPart : game.getSnake().getBody()) {
            if (bodyPart instanceof DimensionSquareInterface bodyPartDim) {
                bodyPartDim.toggleDimension();
            }
        }
        
        for (Square food : game.getFood()) {
            if (food instanceof DimensionSquareInterface foodDim) {
                foodDim.toggleDimension();
            }
        }
    }
    
    @Override
    public void placeFood() {

        prevPlaceFoodDimensionGame();

        super.placeFood();
    }
    
    // Set Other Dimension Food Start Value
    protected void prevPlaceFoodDimensionGame() {
        otherDimensionFood = !game.getFood().isEmpty();
    }
    
    @Override
    protected int getNumTotalFoodToPlace() {
        return game.getNumFood() * 2;
    }
    
    @Override
    protected void addNewFoodSquare(Point foodPos) {
        game.getFood().add(new DimensionSquare(foodPos, CellType.FOOD, otherDimensionFood));
        otherDimensionFood = !otherDimensionFood;
    }
    
    @Override
    protected boolean noFoodPositions() {
        
        // Cantidad total de posiciones disponibles
        int numAvailablePositions = game.getAvailablePositions().size();

        // Filtramos las posiciones de "food" que coincidan con alguna posición de "body"
        long numFoodInsideSnakeBody = game.getFood().stream()
                .filter(f -> game.getSnake().getBody().stream().anyMatch(b -> b.equals(f)))
                .count();
        
        // Restamos la cantidad de coincidencias al total de availablePositions
        return (numAvailablePositions - (int) numFoodInsideSnakeBody) <= 0;
    }
}
