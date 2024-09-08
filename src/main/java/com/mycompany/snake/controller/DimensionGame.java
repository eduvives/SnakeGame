/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.controller;

import com.mycompany.snake.model.CellType;
import com.mycompany.snake.model.DimensionSnake;
import com.mycompany.snake.model.DimensionSquare;
import com.mycompany.snake.model.Snake;
import com.mycompany.snake.model.Square;
import java.awt.Point;
import java.util.List;

/**
 *
 * @author Eduard
 */
public class DimensionGame extends ClassicGame {
    
    protected DimensionSnake dimensionSnake;
    
    private boolean otherDimensionFood;
    
    public DimensionGame(GameLogic game) {
        super(game);
    }
    
    @Override
    protected boolean checkSnakeListCollision(List<Square> list, Point position) {
        
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
    protected Snake createSnakeInstance(Point startPos) {
        return new DimensionSnake(startPos);
    }
    
    @Override
    protected void eatFood(Point newPos) {
        
        prevEatFoodDimensionGame(newPos);
        
        super.eatFood(newPos);
    }
    
    protected void prevEatFoodDimensionGame(Point currentPos) {
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
    private void prevPlaceFoodDimensionGame() {
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
    protected boolean noFoodPositions() {
        System.out.println(otherDimensionFood);
        int numFoodInotherDimension = game.food.size() / 2 + (game.food.size() % 2 != 0 && otherDimensionFood ? 1 : 0);
        System.out.println(numFoodInotherDimension);
        return game.availablePositions.size() - numFoodInotherDimension <= 0;
    }
}
