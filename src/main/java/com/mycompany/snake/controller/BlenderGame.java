/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.controller;

import com.mycompany.snake.model.BlenderSnake;
import com.mycompany.snake.model.CellType;
import com.mycompany.snake.model.CheeseSnake;
import com.mycompany.snake.model.SettingsParams;
import com.mycompany.snake.model.Snake;
import com.mycompany.snake.model.Square;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
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
    private StatueGame statueGame;
    
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
                case "Statue" -> statueGame = new StatueGame(game);
            }
        }
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
    
    // BlenderGame - CheeseGame
    
    @Override
    protected void initializeSnake(){
        
        super.initializeSnake();
        
        if (cheeseGame != null) {
            postInitializeSnakeBlenderCheeseGame();
        }
    }
    
    private void postInitializeSnakeBlenderCheeseGame() {
        BlenderSnake blenderSnake = (BlenderSnake) game.snake;
        cheeseGame.cheeseSnake = blenderSnake.getCheeseSnake();
    }
    
    @Override
    protected Snake createSnakeInstance(Point startPos) {
        
        List<String> blenderSnakeModeNames = new ArrayList<>(modes);
        blenderSnakeModeNames.retainAll(Arrays.asList(SettingsParams.BLENDER_SNAKE_INCLUDED_MODES));
        
        return new BlenderSnake(startPos, blenderSnakeModeNames);
    }
    
    // CheeseGame - TwinGame
    
    @Override
    protected void snakeSimpleMove(Point newPos, boolean isFood) {
        
        super.snakeSimpleMove(newPos, isFood);
        
        if (twinGame != null) {
            if (cheeseGame != null) {
                postSnakeSimpleMoveTwinCheeseGame(newPos, isFood);
            } else {
                twinGame.postSnakeSimpleMoveTwinGame(newPos, isFood); // TODO revisar que funcione
            }
        }
    }
    
    private void postSnakeSimpleMoveTwinCheeseGame(Point newPos, boolean isFood) {
        
        if (isFood) {
            
            CheeseSnake cheeseSnake = cheeseGame.cheeseSnake;
            
            LinkedList<Square> snakeBody = cheeseSnake.getBody();
            Square snakeHead = cheeseSnake.getHead();
            LinkedList<Square> emptyBody = cheeseSnake.getEmptyBody();
            
            boolean isFirstBodyPartSnake = !cheeseSnake.isNextBodyPartSnake();
            boolean isLastBodyPartSnake = (!isFirstBodyPartSnake & cheeseSnake.getGrowCount() % 2 == 0) || (isFirstBodyPartSnake & cheeseSnake.getGrowCount() % 2 == 1);
            
            if (isFirstBodyPartSnake) {
                emptyBody.addFirst(new Square(newPos, CellType.EMPTY));
                
                // Si el método move de la combinación Twin Cheese Snake genera una celda vacía, 
                // esta debe ser agregada a la lista de posiciones disponibles.
                game.availablePositions.add(new Point(newPos));
                
            } else {
                snakeBody.addFirst(new Square(newPos, CellType.SNAKE_BODY));
            }
            
            Collections.reverse(snakeBody);
            Collections.reverse(emptyBody);
            
            if (isLastBodyPartSnake) {
                snakeHead.setLocation(snakeBody.removeFirst());
                cheeseSnake.getDirection().setLocation(snakeHead.x - emptyBody.getFirst().x, snakeHead.y - emptyBody.getFirst().y);
            } else {
                snakeHead.setLocation(emptyBody.removeFirst());
                
                // Si el método move de la combinación Twin Cheese Snake elimina una celda vacía,
                // esta debe ser también eliminada de la lista de posiciones disponibles.
                game.availablePositions.remove(snakeHead);
                
                cheeseSnake.getDirection().setLocation(snakeHead.x - snakeBody.getFirst().x, snakeHead.y - snakeBody.getFirst().y);
            }
            
            cheeseSnake.setNextBodyPartSnake(isLastBodyPartSnake);
        }
    }
    
    // WallGame - TwinGame - StatueGame
    
    @Override
    protected void eatFood(Point newPos) {
        
        if (wallGame != null) {
            wallGame.prevEatFoodWallGame(newPos);
        }
        
        if (statueGame != null) {
            statueGame.prevEatFoodStatueGame();
        }
        
        super.eatFood(newPos);
        
        if (twinGame != null) {
            twinGame.postEatFoodTwinGame();
        }
        
    }
    
    // WallGame - StatueGame
    
    @Override
    protected boolean checkCollision() {
        if (wallGame != null && statueGame != null){
            return checkCollisionWallStatueGame();
        } else if (wallGame != null) {
                return wallGame.checkCollision();
        } else if (statueGame != null) {
            return statueGame.checkCollision();
        } else {
            return super.checkCollision();
        }
    }
    
    private boolean checkCollisionWallStatueGame() {
        
        Point snakeHeadPos = game.snake.getHead().getLocation();
        
        boolean collision = super.checkCollision();
        boolean wallCollision = wallGame.walls.contains(snakeHeadPos);
        boolean statueCollision = statueGame.statues.contains(snakeHeadPos);
        
        return collision || wallCollision || statueCollision;
    }
    
    @Override
    protected void prepareNewGame() {
        
        super.prepareNewGame();
        
        if (wallGame != null) {
            wallGame.postPrepareNewGameWallGame();
        }
        
        if (statueGame != null) {
            statueGame.postPrepareNewGameStatueGame();
        }
    }
    
    // CheeseGame - StatueGame
    
    @Override
    protected void updateSnakeAvailablePositions(Point newPos, boolean isFood){
        
        if (cheeseGame != null) {
            cheeseGame.updateSnakeAvailablePositions(newPos, isFood);
        } else {
            super.updateSnakeAvailablePositions(newPos, isFood);
        }
        
        if (statueGame != null) {
            statueGame.postUpdateSnakeAvailablePositionsStatueGame();
        }
    }
}
