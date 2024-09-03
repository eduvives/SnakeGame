/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.controller;

import com.mycompany.snake.model.BlenderSnake;
import com.mycompany.snake.model.SettingsParams;
import com.mycompany.snake.model.Snake;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Eduard
 */
public class BlenderGame extends ClassicGame {
    
    private List<String> modes;
    private WallGame wallGame;
    private CheeseGame cheeseGame;
    private BoundlessGame boundlessGame;
    private TwinGame twinGame;
    
    public BlenderGame(GameLogic game, List<String> modes) {
        super(game);
        this.modes = modes;
        initializeGameModes(modes);
    }
    
    private void initializeGameModes(List<String> modes) {
        for (String mode : modes) {
            switch (mode) {
                case "Wall" -> wallGame = new WallGame(game);
                case "Cheese" -> cheeseGame = new CheeseGame(game);
                case "Boundless" -> boundlessGame = new BoundlessGame(game);
                case "Twin" -> twinGame = new TwinGame(game);
            }
        }
    }
    
    // WallGame
    
    @Override
    protected boolean checkCollision() {
        
        if (wallGame != null) {
            return wallGame.checkCollision();
        } else {
            return super.checkCollision();
        }
    }
    
    @Override
    protected void prepareNewGame() {
        
        super.prepareNewGame();
        
        if (wallGame != null) {
            wallGame.postPrepareNewGameWallGame();
        }
    }

    @Override
    protected void snakeMove(Point currentDirection) { 
        
        if (wallGame != null) {
            wallGame.prevSnakeMoveWallGame(currentDirection);
        }
        
        super.snakeMove(currentDirection);
    }
    
    // CheeseGame
    
    @Override
    protected boolean checkFeast() {
        
        if (cheeseGame != null) {
            return cheeseGame.checkFeast();
        } else {
            return super.checkFeast();
        }
    }
    
    @Override
    protected Point getRandomFoodPosition() {
        
        if (cheeseGame != null) {
            return cheeseGame.getRandomFoodPosition();
        } else {
            return super.getRandomFoodPosition();
        }
    }
    
    // BoundlessGame
    
    @Override
    protected Point getNewPos(Point currentDirection) {
        
        if (boundlessGame != null) {
            return boundlessGame.getNewPos(currentDirection);
        } else {
            return super.getNewPos(currentDirection);
        }
    }
    
    // Combined Modes :
    
    // BlenderGame - CheeseGame - TwinGame
    
    @Override
    protected Snake createSnakeInstance(Point startPos) {
        
        List<String> blenderSnakeModeNames = new ArrayList<>(modes);
        blenderSnakeModeNames.retainAll(Arrays.asList(SettingsParams.BLENDER_SNAKE_INCLUDED_MODES));
        
        return new BlenderSnake(startPos, blenderSnakeModeNames);
    }
    
    // WallGame - TwinGame
    
    @Override
    protected void eatFood(Point newPos) {
        
        if (wallGame != null) {
            wallGame.prevEatFoodWallGame();
        }
        
        super.eatFood(newPos);
        
        if (twinGame != null) {
            twinGame.postEatFoodTwinGame();
        }
        
    }
}
