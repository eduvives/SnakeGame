/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model;

import java.awt.Point;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Eduard
 */
public class BlenderGame extends ClassicGame {
    
    private WallGame wallGame;
    private CheeseGame cheeseGame;
    private BoundlessGame boundlessGame;
    private TwinGame twinGame;
    private StatueGame statueGame;
    private DimensionGame dimensionGame;
    
    private List<String> modes;
    
    public BlenderGame(GameModel game) {
        super(game);
        
        wallGame = this.game.wallGame;
        cheeseGame = this.game.cheeseGame;
        boundlessGame = this.game.boundlessGame;
        twinGame = this.game.twinGame;
        statueGame = this.game.statueGame;
        dimensionGame = this.game.dimensionGame;
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
    protected boolean checkSnakeListCollision(Collection<? extends Square> list, Point position) {

        if (modes.contains("Dimension")) {
            
            return list.stream()
            .filter(square -> square.equals(position))
            .anyMatch(square -> {
                if (square instanceof DimensionSquare dimensionSquare) {
                    return !dimensionSquare.isOtherDimension();
                } else if (square instanceof StatueDimensionSquare statueSquare) {
                    return !statueSquare.isOtherDimension();
                }
                return false;
            });
            
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
    protected void initializeGameSnake(){
        
        super.initializeGameSnake();
        
        if (modes.contains("Cheese")) postInitializeSnakeBlenderCheeseGame();
        if (modes.contains("Dimension")) postInitializeSnakeBlenderDimensionGame();
        if (modes.contains("Twin")) postInitializeSnakeBlenderTwinGame();
    }
    
    private void postInitializeSnakeBlenderCheeseGame() {
        BlenderSnake blenderSnake = (BlenderSnake) game.snake;
        cheeseGame.cheeseSnake = blenderSnake.getCheeseSnake();
    }
    
    private void postInitializeSnakeBlenderDimensionGame() {
        BlenderSnake blenderSnake = (BlenderSnake) game.snake;
        dimensionGame.dimensionSnake = blenderSnake.getDimensionSnake();
    }
    
    private void postInitializeSnakeBlenderTwinGame() {
        BlenderSnake blenderSnake = (BlenderSnake) game.snake;
        twinGame.twinSnake = blenderSnake.getTwinSnake();
    }
    
    @Override
    protected Snake createSnakeInstance() {
        return new BlenderSnake(modes);
    }
    
    // TwinGame - CheeseGame - BoundlessGame - StatueGame - DimensionGame
    
    @Override
    protected void snakeSimpleMove(Point newPos, boolean isFoodCollision) {
        
        super.snakeSimpleMove(newPos, isFoodCollision);
        
        if (isFoodCollision) {
            if (modes.contains("Twin") && modes.contains("Statue")) {
                removeStatuesSpawnRadius();
            }
        }
    }
    
    private void removeStatuesSpawnRadius() { // TODO revisar si posicion disponible (dimension)
        
        Set<Point> spawnRadiusStatues = new HashSet<>(statueGame.statues);
        spawnRadiusStatues.retainAll(getSpawnRadius());

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
    
    protected Square createSimpleWall(Point pos) {
        
        if (modes.contains("Dimension")) {
            return new DimensionSquare(pos, CellType.WALL_SIMPLE, false);
        } else {
            return new Square(pos, CellType.WALL_SIMPLE);
        }
    }
    
    protected StatueSquare createFilledWall(Point pos) {

        if (modes.contains("Dimension")) {
            return new StatueDimensionSquare(pos, CellType.WALL_FILLED, false);
        } else {
            return new StatueSquare(pos, CellType.WALL_FILLED);
        }
    }
    
    @Override
    protected void eatFood(Point newPos) {
        
        if (modes.contains("Statue")) {
            placeStatueBlender();
        }
        
        if (modes.contains("Dimension")) {
            
            dimensionGame.toggleGameDimension(newPos);
            
            if (!game.specificModeLists.isEmpty()) {
                toggleDimensionSpecificModeLists();
            }
        }
        
        if (modes.contains("Wall")) {
            placeWallBlender();
        }
        
        super.eatFood(newPos);
        
        if (modes.contains("Twin")) {
            twinGame.postEatFoodTwinGame();
        }
    }
    
    private void placeStatueBlender() {
        
        updateStatuesBlender();
        
        for (Point bodyPartPos : game.snake.getBody()) {
            statueGame.statues.add(createFilledWall(bodyPartPos));
        }
    }
    
    private void updateStatuesBlender() {
        
        Set<Square> snakeBodySet = new HashSet<>(game.snake.getBody());
        
        Set<StatueSquare> statuesNotFilled = new HashSet<>(statueGame.statues);
        statuesNotFilled.removeIf(statue -> snakeBodySet.contains(statue) && statue.getCellType() == CellType.WALL_FILLED);
        
        for (StatueSquare statueSquare : statuesNotFilled) {
            if (statueSquare.getCellType() == CellType.WALL_FILLED) {

                statueSquare.setCellType(CellType.WALL_STATUE);
                statueSquare.setFoodBeforeBreak(statueGame.generateNumFoodBeforeBreak());

            } else if (statueSquare.getCellType() == CellType.WALL_STATUE) {

                int foodBeforeBreak = statueSquare.getFoodBeforeBreak() - 1;

                if (foodBeforeBreak == 1) statueSquare.setCellType(CellType.WALL_CRACKED);

                statueSquare.setFoodBeforeBreak(foodBeforeBreak);
                
            } else if (statueSquare.getCellType() == CellType.WALL_CRACKED) {
                
                statueGame.statues.remove(statueSquare);
                if (!game.snake.getBody().contains(statueSquare)) {
                    game.availablePositions.add(new Point(statueSquare));
                }
            }
        }
    }
    
    private void toggleDimensionSpecificModeLists () {
        
        for (Collection<? extends Square> modeList : game.specificModeLists) {
            for (Square square : modeList) {
                if (square instanceof DimensionSquare dimensionSquare) {
                    dimensionSquare.toggleDimension();
                } else if (square instanceof StatueDimensionSquare statueSquare) {
                    statueSquare.toggleDimension();
                }
            }
        }
    }
    
    private void placeWallBlender() {
        if ((!modes.contains("Dimension") && game.score % 2 == 0) || modes.contains("Dimension")) {
            wallGame.spawnRadius = getSpawnRadius();
            createWallBlender();
        }
    }
    
    @Override
    protected void addSpawnRadiusPoint(int newX, int newY, Set<Point> newSpawnRadius) {
        
        if (modes.contains("Boundless")) {
            
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
            
        } else {
            super.addSpawnRadiusPoint(newX, newY, newSpawnRadius);
        }
    }
    
    protected void createWallBlender() {
        
        Point wallPos = wallGame.getRandomSpawnPosition(game.availablePositions, wallGame.spawnRadius, wallGame.spawnWalls);

        if (wallPos != null) {

            // Create New Wall
            wallGame.walls.add(createSimpleWall(wallPos));
            
            // Update Spawn Walls List
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    wallGame.spawnWalls.add(new Point(wallPos.x + x, wallPos.y + y));
                }
            }
        }
    }
    
    // WallGame - StatueGame
    
    @Override
    protected boolean checkCollision() {
        
        boolean collision = super.checkCollision();
        
        Point snakeHeadPos = game.snake.getHead().getLocation();
        
        if (modes.contains("Wall")) {
            collision = collision || checkSnakeListCollision(wallGame.walls, snakeHeadPos);
        }
        
        if (modes.contains("Statue")) {
            collision = collision || checkSnakeListCollision(statueGame.statues, snakeHeadPos);
        }
        
        return collision;
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
    
    // CheeseGame - StatueGame - DimensionGame
    
    @Override
    protected void snakeMove(Point newPos, boolean isFoodCollision) {
        
        if (modes.contains("Cheese")) {
            updateSnakeAvailablePositionsCheeseStatueDimension(newPos, isFoodCollision);
        } else {
            updateSnakeAvailablePositionsBlenderStatueDimension(newPos, isFoodCollision);
        }
        
        snakeSimpleMove(newPos, isFoodCollision);
    }
    
    private void updateSnakeAvailablePositionsCheeseStatueDimension(Point newHeadPos, boolean isFoodCollision) {

        CheeseSnake cheeseSnake = cheeseGame.cheeseSnake;
        
        int growCount = cheeseSnake.growCount;
        
        if (isFoodCollision) growCount += 2;
        
        if (cheeseSnake.nextBodyPartSnake && growCount <= 0) {
            if (positionAvailableAfterCheeseSnakeMove(cheeseSnake.body.getLast(), newHeadPos)) {
                game.availablePositions.add(cheeseSnake.body.getLast().getLocation());
            }
        } else if (!cheeseSnake.nextBodyPartSnake) {
            if (positionAvailable(cheeseSnake.head)) {
                game.availablePositions.add(cheeseSnake.head.getLocation());
            }
        }
        
        if (growCount > 0) {
            growCount--;
        }
        
        game.availablePositions.remove(newHeadPos);
        
        if (isFoodCollision && modes.contains("Twin")) {
            updateSwitchSidesAvailablePositionsCheese(newHeadPos, growCount);
        }
    }
    
    private void updateSnakeAvailablePositionsBlenderStatueDimension(Point newHeadPos, boolean isFoodCollision) {

        if(positionAvailableAfterSnakeMove(newHeadPos, isFoodCollision)) {
            game.availablePositions.add(game.snake.getBody().getLast().getLocation());
        }
        
        game.availablePositions.remove(newHeadPos);
    }
    
    @Override
    protected boolean positionAvailableAfterSnakeMove(Point newHeadPos, boolean isFoodCollision) {
        
        return super.positionAvailableAfterSnakeMove(newHeadPos, isFoodCollision) && positionAvailable(game.snake.getBody().getLast());
    }
    
    private boolean positionAvailable(Point position) {
        
        boolean positionAvailable = true;
        
        if (modes.contains("Statue")) {
            positionAvailable = positionAvailable && !statueGame.statues.contains(position);
        }
        
        if (modes.contains("Dimension")) {
            positionAvailable = positionAvailable && !game.snake.getBody().contains(position) && !game.food.contains(position) 
            && !game.specificModeLists.stream().anyMatch(modeList -> modeList.contains(position));
        }
        
        return positionAvailable;
    }
    
    private boolean positionAvailableAfterCheeseSnakeMove(Point position, Point newHeadPos) {
        
        return !newHeadPos.equals(position) && positionAvailable(position);
    }
    
    private void updateSwitchSidesAvailablePositionsCheese(Point headPos, int growCount) {
        
        CheeseSnake cheeseSnake = cheeseGame.cheeseSnake;

        boolean isFirstBodyPartSnake = cheeseSnake.nextBodyPartSnake;
        boolean isLastBodyPartSnake = (!isFirstBodyPartSnake && growCount % 2 == 0) || (isFirstBodyPartSnake && growCount % 2 == 1);

        if (isFirstBodyPartSnake) {
            
            // Si el método move de la combinación Twin Cheese Snake genera una celda vacía, 
            // esta debe ser agregada a la lista de posiciones disponibles.
            //if (positionAvailableAfterSnakeMove(headPos, previousHeadPos)) {
                game.availablePositions.add(new Point(headPos));
            //}
        }
        
        if (!isLastBodyPartSnake) {

            // Si el método move de la combinación Twin Cheese Snake elimina una celda vacía,
            // esta debe ser también eliminada de la lista de posiciones disponibles.
            game.availablePositions.remove(cheeseSnake.emptyBody.getLast().getLocation());
        }
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
