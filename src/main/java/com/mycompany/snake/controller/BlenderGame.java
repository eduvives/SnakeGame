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
        
        wallGame = new WallGame(game);
        cheeseGame = new CheeseGame(game);
        boundlessGame = new BoundlessGame(game);
        twinGame = new TwinGame(game);
        statueGame = new StatueGame(game);
        
        setBlenderModes(modes);
    }
    
    protected void setBlenderModes(List<String> modes) { // TODO reutilizar los modos y no ponerlos a null si ya estan creados? comprobar con contains?
        this.modes = modes;
    }
    
    // CheeseGame
    
    @Override
    protected boolean checkFeast() {
        
        if (modes.contains("Cheese")) {
            return cheeseGame.checkFeast();
        } else {
            return super.checkFeast();
        }
    }
    
    @Override
    protected Point getRandomFoodPosition() {
        
        if (modes.contains("Cheese")) {
            return cheeseGame.getRandomFoodPosition();
        } else {
            return super.getRandomFoodPosition();
        }
    }
    
    // BoundlessGame
    
    @Override
    protected Point getNewPos(Point newDirection) {
        
        if (modes.contains("Boundless")) {
            return boundlessGame.getNewPos(newDirection);
        } else {
            return super.getNewPos(newDirection);
        }
    }
    
    // Combined Modes :
    
    // BlenderGame - CheeseGame
    
    @Override
    protected void initializeSnake(){
        
        super.initializeSnake();
        
        if (modes.contains("Cheese")) {
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
            if (modes.contains("Twin")) {
                if (modes.contains("Cheese")) {
                    postSnakeSimpleMoveTwinCheeseGame(newPos);
                } else {
                    twinGame.switchSides(newPos); // TODO revisar que funcione
                    restoreDirectionBlenderGame(game.snake.getHead(), game.snake.getBody().getFirst()); 
                }

                if (modes.contains("Statue")) {
                    removeStatuesSpawnRadius();
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
        
        if (modes.contains("Boundless")) {
            
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
    
    private void removeStatuesSpawnRadius() {
        
        Set<Point> spawnRadiusStatues = new HashSet<>(statueGame.statues);
        spawnRadiusStatues.retainAll(getSpawnRadiusBlenderGame());

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
        
        if (modes.contains("Wall")) {
            if (game.score % 2 == 0) {
                wallGame.spawnRadius = getSpawnRadiusBlenderGame();
                wallGame.addWall();
            }
        }
        
        if (modes.contains("Statue")) {
            statueGame.prevEatFoodStatueGame();
        }
        
        super.eatFood(newPos);
        
        if (modes.contains("Twin")) {
            twinGame.postEatFoodTwinGame();
        }
    }
    
    private Set<Point> getSpawnRadiusBlenderGame() {
        
        if (modes.contains("Boundless")) {
            return getSpawnRadiusBoundlessGame();
        } else {
            return getSpawnRadius();
        }
    }
    
    private Set<Point> getSpawnRadiusBoundlessGame() {
        
        Set<Point> newSpawnRadius = new HashSet<>();
        
        int size = (SPAWN_RADIUS_WIDTH - 1) / 2;

        for (int x = -size; x <= size; x++) {
            int yLimit = size - Math.abs(x);

            for (int y = -yLimit; y <= yLimit; y++) {
                int newX = game.snake.getHead().x + x;
                int newY = game.snake.getHead().y + y;
                
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
        if (modes.contains("Wall") && modes.contains("Statue")){
            return checkCollisionWallStatueGame();
        } else if (modes.contains("Wall")) {
                return wallGame.checkCollision();
        } else if (modes.contains("Statue")) {
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
        
        if (modes.contains("Wall")) {
            wallGame.postPrepareNewGameWallGame();
        }
        
        if (modes.contains("Statue")) {
            statueGame.postPrepareNewGameStatueGame();
        }
    }
    
    // CheeseGame - StatueGame
    
    @Override
    protected void updateSnakeAvailablePositions(Point newPos, boolean isFood){
        
        if (modes.contains("Cheese")) {
            cheeseGame.updateSnakeAvailablePositions(newPos, isFood);
        } else {
            super.updateSnakeAvailablePositions(newPos, isFood);
        }
        
        if (modes.contains("Statue")) {
            statueGame.postUpdateSnakeAvailablePositionsStatueGame();
        }
    }
}
