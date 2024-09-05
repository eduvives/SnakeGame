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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
    
    // TwinGame - CheeseGame - BoundlessGame - StatueGame
    
    @Override
    protected void snakeSimpleMove(Point newPos, boolean isFood) {
        
        super.snakeSimpleMove(newPos, isFood);
        
        if (isFood) {
            if (twinGame != null) {
                if (cheeseGame != null) {
                    postSnakeSimpleMoveTwinCheeseGame(newPos);
                } else {
                    twinGame.switchSides(newPos); // TODO revisar que funcione
                    restoreDirectionBlenderGame(game.snake.getHead(), game.snake.getBody().getFirst()); 
                }

                if (statueGame != null) {
                    removeStatuesSpawnRadius(game.snake.getHead());
                }
            }
        }
    }
    
    private void postSnakeSimpleMoveTwinCheeseGame(Point newPos) {

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
            restoreDirectionBlenderGame(snakeHead, emptyBody.getFirst());
        } else {
            snakeHead.setLocation(emptyBody.removeFirst());

            // Si el método move de la combinación Twin Cheese Snake elimina una celda vacía,
            // esta debe ser también eliminada de la lista de posiciones disponibles.
            game.availablePositions.remove(snakeHead);

            restoreDirectionBlenderGame(snakeHead, snakeBody.getFirst());
        }

        cheeseSnake.setNextBodyPartSnake(isLastBodyPartSnake);
    }
    
    private void restoreDirectionBlenderGame(Point snakeHead, Point snakeFirstBodyPartPos) {
        
        Point direction = getDefaultDirection(snakeHead, snakeFirstBodyPartPos);
        
        if (boundlessGame != null) {
            
            // Ajustar por teletransporte en el eje X
            if (direction.x > 1) {
                direction.x = -1;
            } else if (direction.x < -1) {
                direction.x = 1;
            }

            // Ajustar por teletransporte en el eje Y
            if (direction.y > 1) {
                direction.y = -1;
            } else if (direction.y < -1) {
                direction.y = 1;
            }
        }
        
        game.snake.getDirection().setLocation(direction.x, direction.y);
    }
    
    private void removeStatuesSpawnRadius(Point newPos) {
        
        Set<Point> spawnRadiusStatues = new HashSet<>(statueGame.statues);
        spawnRadiusStatues.retainAll(getSpawnRadiusBlenderGame(newPos));

        for (Point statuePos : spawnRadiusStatues) {
            if (statuePos.equals(game.snake.getHead()) || !game.snake.getBody().contains(statuePos)) {
                statueGame.statues.remove(statuePos);
                if (!statuePos.equals(game.snake.getHead())) {
                    game.availablePositions.add(new Point(statuePos));
                }
            }
        }
    }
    
    // WallGame - BoundlessGame - TwinGame - StatueGame
    
    @Override
    protected void eatFood(Point newPos) {
        
        if (wallGame != null) {
            if (game.score % 2 == 0) {
                wallGame.spawnRadius = getSpawnRadiusBlenderGame(newPos);
                wallGame.addWall();
            }
        }
        
        if (statueGame != null) {
            statueGame.prevEatFoodStatueGame();
        }
        
        super.eatFood(newPos);
        
        if (twinGame != null) {
            twinGame.postEatFoodTwinGame();
        }
    }
    
    private Set<Point> getSpawnRadiusBlenderGame(Point currentPos) {
        
        if (boundlessGame != null) {
            return getSpawnRadiusBoundlessGame(currentPos);
        } else {
            return getSpawnRadius(currentPos);
        }
    }
    
    private Set<Point> getSpawnRadiusBoundlessGame(Point currentPos) {
        
        Set<Point> newSpawnRadius = new HashSet<>();
        
        int size = (SPAWN_RADIUS_WIDTH - 1) / 2;

        for (int x = -size; x <= size; x++) {
            int yLimit = size - Math.abs(x);

            for (int y = -yLimit; y <= yLimit; y++) {
                int newX = currentPos.x + x;
                int newY = currentPos.y + y;
                
                if (newX < 0) {
                    newX += game.numBoardCols;
                } else if (newX >= game.numBoardCols) {
                    newX -= game.numBoardCols;
                }

                if (newY < 0) {
                    newY += game.numBoardRows;
                } else if (newY >= game.numBoardRows) {
                    newY -= game.numBoardRows;
                }

                newSpawnRadius.add(new Point(newX, newY));
            }
        }
        
        return newSpawnRadius;
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
