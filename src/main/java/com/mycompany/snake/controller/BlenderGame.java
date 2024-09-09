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
    private DimensionGame dimensionGame;
    
    public BlenderGame(GameLogic game, List<String> modes) {
        super(game);
        
        wallGame = new WallGame(game);
        cheeseGame = new CheeseGame(game);
        boundlessGame = new BoundlessGame(game);
        twinGame = new TwinGame(game);
        statueGame = new StatueGame(game);
        dimensionGame = new DimensionGame(game);
        
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
    
    // DimensionGame
    
    @Override
    protected boolean checkSnakeListCollision(List<Square> list, Point position) {
        
        if (modes.contains("Dimension")) {
            return dimensionGame.checkSnakeListCollision(list, position);
        } else {
            return super.checkSnakeListCollision(list, position);
        }
    }
    
    @Override
    protected int getNumTotalFoodToPlace() {
        if (modes.contains("Dimension")) {
            return dimensionGame.getNumTotalFoodToPlace();
        } else {
            return super.getNumTotalFoodToPlace();
        }
    }
    
    @Override
    protected void addNewFoodSquare(Point foodPos) {
        if (modes.contains("Dimension")) {
            dimensionGame.addNewFoodSquare(foodPos);
        } else {
            super.addNewFoodSquare(foodPos);
        }
    }
    
    @Override
    protected boolean noFoodPositions() {
        if (modes.contains("Dimension")) {
            return dimensionGame.noFoodPositions();
        } else {
            return super.noFoodPositions();
        }
    }
    
    // Combined Modes :
    
    // BlenderGame - CheeseGame - DimensionGame
    
    @Override
    protected void initializeSnake(){
        
        super.initializeSnake();
        
        if (modes.contains("Cheese")) postInitializeSnakeBlenderCheeseGame();
        
        if (modes.contains("Dimension")) postInitializeSnakeBlenderDimensionGame();
    }
    
    private void postInitializeSnakeBlenderCheeseGame() {
        BlenderSnake blenderSnake = (BlenderSnake) game.snake;
        cheeseGame.cheeseSnake = blenderSnake.getCheeseSnake();
    }
    
    private void postInitializeSnakeBlenderDimensionGame() {
        BlenderSnake blenderSnake = (BlenderSnake) game.snake;
        dimensionGame.dimensionSnake = blenderSnake.getDimensionSnake();
    }
    
    @Override
    protected Snake createSnakeInstance() {
        
        List<String> blenderSnakeModeNames = new ArrayList<>(modes);
        blenderSnakeModeNames.retainAll(Arrays.asList(SettingsParams.BLENDER_SNAKE_INCLUDED_MODES));
        
        return new BlenderSnake(blenderSnakeModeNames);
    }
    
    // TwinGame - CheeseGame - BoundlessGame - StatueGame
    
    @Override
    protected void snakeSimpleMove(Point newPos, boolean isFoodCollision) {
        
        super.snakeSimpleMove(newPos, isFoodCollision);
        
        if (isFoodCollision) {
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
        Point snakeHead = cheeseSnake.getHead();
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
    
    // WallGame - BoundlessGame - TwinGame - StatueGame - DimensionGame
    
    @Override
    protected void eatFood(Point newPos) {
        
        if (modes.contains("Dimension")) {
            dimensionGame.prevEatFoodDimensionGame(newPos);
        }
        
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
    protected void updateSnakeAvailablePositions(boolean isFoodCollision, Point previousLastBodyPartPos){
        
        if (modes.contains("Cheese") && (modes.contains("Statue") || modes.contains("Dimension"))) {
            updateSnakeAvailablePositionsCheeseStatueDimensionGame(isFoodCollision, previousLastBodyPartPos);
        } else if (modes.contains("Cheese")) {
            cheeseGame.updateSnakeAvailablePositions(isFoodCollision, previousLastBodyPartPos);
        } else {
            super.updateSnakeAvailablePositions(isFoodCollision, previousLastBodyPartPos);
        }
    }
    
    private void updateSnakeAvailablePositionsCheeseStatueDimensionGame(boolean isFoodCollision, Point previousLastBodyPartPos) {

        int growCount = cheeseGame.cheeseSnake.getGrowCount();
        boolean nextBodyPartSnake = cheeseGame.cheeseSnake.isNextBodyPartSnake();
        
        if (isFoodCollision) growCount += 2;
        
        if (nextBodyPartSnake && growCount <= 0) {
            if (positionAvailableAfterSnakeSimpleMove(isFoodCollision, previousLastBodyPartPos)) {
                game.availablePositions.add(previousLastBodyPartPos);
            }
        } else if (!nextBodyPartSnake) {
            game.availablePositions.add(cheeseGame.cheeseSnake.getEmptyBody().getFirst().getLocation()); // Previous Snake Head Position
        }
        
        game.availablePositions.remove(cheeseGame.cheeseSnake.getHead().getLocation());
    }
    
    // StatueGame - DimensionGame
    
    @Override
    protected boolean positionAvailableAfterSnakeSimpleMove(boolean isFoodCollision, Point previousLastBodyPartPos) {
        if (modes.contains("Statue") && modes.contains("Dimension")) {
            return positionAvailableAfterSnakeSimpleMoveStatueDimensionGame(isFoodCollision, previousLastBodyPartPos);
        } else if (modes.contains("Statue")) {
            return statueGame.positionAvailableAfterSnakeSimpleMove(isFoodCollision, previousLastBodyPartPos);
        } else if (modes.contains("Dimension")) {
            return dimensionGame.positionAvailableAfterSnakeSimpleMove(isFoodCollision, previousLastBodyPartPos);
        } else {
            return super.positionAvailableAfterSnakeSimpleMove(isFoodCollision, previousLastBodyPartPos);
        }
    }
    
    private boolean positionAvailableAfterSnakeSimpleMoveStatueDimensionGame(boolean isFoodCollision, Point previousLastBodyPartPos) {
        
        boolean positionAvailable = super.positionAvailableAfterSnakeSimpleMove(isFoodCollision, previousLastBodyPartPos);
        
        return positionAvailable && !statueGame.statues.contains(previousLastBodyPartPos) 
                && !game.snake.getBody().contains(previousLastBodyPartPos) && !game.food.contains(previousLastBodyPartPos) 
                && !game.specificModeLists.stream().anyMatch(modeList -> modeList.contains(previousLastBodyPartPos));
    }
    
    // CheeseGame - DimensionGame
    
    @Override
    protected void placeFood() {
        
        if (modes.contains("Dimension")) {
            dimensionGame.prevPlaceFoodDimensionGame();
        }
        if (modes.contains("Cheese")) {
            cheeseGame.prevPlaceFoodCheeseGame();
        }
        
        super.placeFood();
    }
}
