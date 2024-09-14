/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model;

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
    protected boolean checkSnakeListCollision(Collection<? extends Square> list, Point position) {
        
        return list.stream()
        .filter(square -> square.equals(position))
        .map(square -> (DimensionSquare) square) // Convierte el Square a DimensionSquare
        .anyMatch(dimensionSquare -> !dimensionSquare.isOtherDimension());
    }
    
    @Override
    protected void initializeSnake(){
        
        super.initializeSnake();
        
        postInitializeSnakeDimensionGame();
    }
    
    protected void postInitializeSnakeDimensionGame() {
        dimensionSnake = (DimensionSnake) game.snake;
    }
    
    @Override
    protected Snake createSnakeInstance() {
        return new DimensionSnake();
    }
    
    // Establece si, después de moverse la serpiente, la posición previa de cola (última posición del cuerpo de la serpiente) 
    // debe ser marcado como posición disponible, comprobando si hay algún elemento en esa posición
    @Override
    protected boolean isSnakePositionAvailable(Point position) {
        
        return super.isSnakePositionAvailable(position) && !game.food.contains(position);
     }
    
    @Override
    protected void eatFood(Point newPos) {
        
        toggleGameDimension(newPos);
        
        super.eatFood(newPos);
    }
    
    protected void toggleGameDimension(Point currentPos) {
        
        for (Square bodyPart : game.snake.getBody()) {
            DimensionSquare bodyPartDim = (DimensionSquare) bodyPart;
            bodyPartDim.toggleDimension();
        }
        
        for (Square food : game.food) {
            DimensionSquare foodDim = (DimensionSquare) food;
            foodDim.toggleDimension();
        }
    }
    
    @Override
    protected void placeFood() {

        prevPlaceFoodDimensionGame();

        super.placeFood();
    }
    
    // Set Other Dimension Food Start Value
    protected void prevPlaceFoodDimensionGame() {
        otherDimensionFood = !game.food.isEmpty();
    }
    
    @Override
    protected int getNumTotalFoodToPlace() {
        return game.numFood * 2;
    }
    
    @Override
    protected void addNewFoodSquare(Point foodPos) {
        game.food.add(new DimensionSquare(foodPos, CellType.FOOD, otherDimensionFood));
        otherDimensionFood = !otherDimensionFood;
    }
    
    @Override
    protected boolean noFoodPositions() { // TODO aun no funciona
        //System.out.println(otherDimensionFood);
        int numFoodInotherDimension = game.food.size() / 2 + (game.food.size() % 2 != 0 && otherDimensionFood ? 1 : 0);
        //System.out.println(numFoodInotherDimension);
        return game.availablePositions.size() - numFoodInotherDimension <= 0;
    }
}
