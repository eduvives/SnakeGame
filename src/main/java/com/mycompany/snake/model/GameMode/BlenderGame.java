/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model.GameMode;

import com.mycompany.snake.model.GameModel;
import com.mycompany.snake.model.Snake.BlenderSnake;
import com.mycompany.snake.model.Snake.Snake;
import com.mycompany.snake.model.Square.DimensionSquare;
import com.mycompany.snake.model.Square.StatueSquare;
import com.mycompany.snake.model.Square.DimensionSquareInterface;
import com.mycompany.snake.model.Square.CellType;
import com.mycompany.snake.model.Square.StatueDimensionSquare;
import com.mycompany.snake.model.Square.Square;
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
        
        wallGame = this.game.getWallGame();
        cheeseGame = this.game.getCheeseGame();
        boundlessGame = this.game.getBoundlessGame();
        twinGame = this.game.getTwinGame();
        statueGame = this.game.getStatueGame();
        dimensionGame = this.game.getDimensionGame();
    }
    
    public void setBlenderModes(List<String> modes) { // TODO reutilizar los modos y no ponerlos a null si ya estan creados? comprobar con contains?
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
    
    // TwinGame
    
    @Override
    public void nextLoop() {
        
        super.nextLoop();
        
        if (modes.contains("Twin") && twinGame.switchSides) {
            twinGame.switchSidesEffect();
        }
    }
    
    // DimensionGame
    
    @Override
    protected boolean checkSnakeListCollision(Collection<? extends Square> list, Point position) {

        if (modes.contains("Dimension")) {
            
            return list.stream()
            .filter(square -> square.equals(position))
            .anyMatch(square -> {
                if (square instanceof DimensionSquareInterface dimensionSquare) {
                    return !dimensionSquare.isOtherDimension();
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
    
    // BlenderGame - CheeseGame - DimensionGame - TwinGame
    
    @Override
    public void initializeSnake(){
        
        super.initializeSnake();
        
        if (modes.contains("Cheese")) postInitializeSnakeBlenderCheeseGame();
        if (modes.contains("Dimension")) postInitializeSnakeBlenderDimensionGame();
        if (modes.contains("Twin")) postInitializeSnakeBlenderTwinGame();
    }
    
    private void postInitializeSnakeBlenderCheeseGame() {
        BlenderSnake blenderSnake = (BlenderSnake) game.getSnake();
        cheeseGame.cheeseSnake = blenderSnake.getCheeseSnake();
    }
    
    private void postInitializeSnakeBlenderDimensionGame() {
        BlenderSnake blenderSnake = (BlenderSnake) game.getSnake();
        dimensionGame.dimensionSnake = blenderSnake.getDimensionSnake();
    }
    
    private void postInitializeSnakeBlenderTwinGame() {
        BlenderSnake blenderSnake = (BlenderSnake) game.getSnake();
        twinGame.twinSnake = blenderSnake.getTwinSnake();
    }
    
    @Override
    protected Snake createSnakeInstance() {
        return new BlenderSnake(modes);
    }
    
    // StatueGame - DimensionGame
    
    @Override
    protected boolean isSnakePositionAvailable(Point position) {
        
        boolean positionAvailable = super.isSnakePositionAvailable(position);
        
        if (modes.contains("Statue")) {
            positionAvailable &= !statueGame.statues.contains(position);
        }
        
        if (modes.contains("Dimension")) {
            positionAvailable &= !game.getFood().contains(position) 
            && !game.getSpecificModeLists().stream().anyMatch(modeList -> modeList.contains(position));
        }
        
        return positionAvailable;
    }
    
    // TwinGame - CheeseGame - StatueGame - WallGame - DimensionGame - BoundlessGame
    
    @Override
    protected void snakeMove(Point newPos, boolean isFoodCollision) {
        
        super.snakeMove(newPos, isFoodCollision);
        
        if (modes.contains("Twin")) {
            
            twinGame.switchSides = isFoodCollision;
            
            if (isFoodCollision) {
                
                BlenderSnake blenderSnake = (BlenderSnake) game.getSnake();

                if (modes.contains("Cheese")) {
                    blenderSnake.switchSidesTwinCheeseDimension();
                } else {
                    blenderSnake.switchSidesBlender(); // TODO revisar que funcione
                }

                if (modes.contains("Statue")) {
                    removeStatuesSpawnRadius();
                }
            }
        }
        
        if (isFoodCollision) {
            if (modes.contains("Statue")) {
                placeStatueBlender();
            }

            if (modes.contains("Dimension")) {

                dimensionGame.toggleGameDimension();
                toggleDimensionSpecificModeLists();
            }

            if (modes.contains("Wall")) {
                placeWallBlender();
            }
        }
    }
    
    // TwinGame - StatueGame - BoundlessGame
    
    private void removeStatuesSpawnRadius() { // TODO revisar si posicion disponible (dimension)
        
        Set<Point> spawnRadiusStatues = new HashSet<>(statueGame.statues);
        spawnRadiusStatues.retainAll(getSpawnRadius());

        for (Point statuePos : spawnRadiusStatues) {
            if (statuePos.equals(game.getSnake().getHead()) || !game.getSnake().getBody().contains(statuePos)) {
                statueGame.statues.remove(statuePos);
                if (!statuePos.equals(game.getSnake().getHead())) {
                    game.getAvailablePositions().add(new Point(statuePos));
                }
            }
        }
    }
    
    // WallGame - StatueGame - DimensionGame - BoundlessGame
    
    private Square createSimpleWall(Point pos) {
        
        if (modes.contains("Dimension")) {
            return new DimensionSquare(pos, CellType.WALL_SIMPLE, false);
        } else {
            return new Square(pos, CellType.WALL_SIMPLE);
        }
    }
    
    private StatueSquare createFilledWall(Point pos) {

        if (modes.contains("Dimension")) {
            return new StatueDimensionSquare(pos, CellType.WALL_FILLED, false);
        } else {
            return new StatueSquare(pos, CellType.WALL_FILLED);
        }
    }
    
    private void placeStatueBlender() {
        
        updateStatuesBlender();
        
        for (Point bodyPartPos : game.getSnake().getBody()) {
            statueGame.statues.add(createFilledWall(bodyPartPos));
        }
    }
    
    private void updateStatuesBlender() {
        
        Set<Square> snakeBodySet = new HashSet<>(game.getSnake().getBody());
        
        Set<StatueSquare> statuesNotFilled = new HashSet<>(statueGame.statues);
        statuesNotFilled.removeIf(statue -> snakeBodySet.contains(statue) && statue.getCellType() == CellType.WALL_FILLED);
        
        for (StatueSquare statueSquare : statuesNotFilled) {
            
            if (statueSquare.getCellType() == CellType.WALL_FILLED) {
                statueSquare.setFoodBeforeBreak(statueGame.generateNumFoodBeforeBreak());
            }
            
            statueSquare.decreaseFoodBeforeBreak();
            int foodBeforeBreak = statueSquare.getFoodBeforeBreak();
            
            if (foodBeforeBreak > 1) {
                
                statueSquare.setCellType(CellType.WALL_STATUE);
                
            } else if (foodBeforeBreak == 1) {
                
                statueSquare.setCellType(CellType.WALL_CRACKED);
                
            } else if (foodBeforeBreak == 0) {
                
                statueGame.statues.remove(statueSquare);
                if (!game.getSnake().getBody().contains(statueSquare)) { // TODO and check available position
                    game.getAvailablePositions().add(new Point(statueSquare));
                }
            }
        }
    }
    
    private void toggleDimensionSpecificModeLists () {
        
        for (Collection<? extends Square> modeList : game.getSpecificModeLists()) {
            for (Square square : modeList) {
                if (square instanceof DimensionSquareInterface dimensionSquare) {
                    dimensionSquare.toggleDimension();
                }
            }
        }
    }
    
    private void placeWallBlender() {
        if ((modes.contains("Dimension") || game.getScore() % 2 == 1)) {
            wallGame.spawnRadius = getSpawnRadius();
            createWallBlender();
        }
    }
    
    @Override
    protected void addSpawnRadiusPoint(int newX, int newY, Set<Point> newSpawnRadius) {
        
        if (modes.contains("Boundless")) {
            
            if (newX < 0) {
                newX += game.getNumBoardCols();
            } else if (newX >= game.getNumBoardCols()) {
                newX -= game.getNumBoardCols();
            }

            if (newY < 0) {
                newY += game.getNumBoardRows();
            } else if (newY >= game.getNumBoardRows()) {
                newY -= game.getNumBoardRows();
            }

            newSpawnRadius.add(new Point(newX, newY));
            
        } else {
            super.addSpawnRadiusPoint(newX, newY, newSpawnRadius);
        }
    }
    
    private void createWallBlender() {
        
        Point wallPos = wallGame.getRandomSpawnPosition(game.getAvailablePositions(), wallGame.spawnRadius, wallGame.spawnWalls);

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
    protected boolean checkCollision(Point snakeHeadPos) {
        
        boolean collision = super.checkCollision(snakeHeadPos);
        
        if (modes.contains("Wall")) {
            collision = collision || checkSnakeListCollision(wallGame.walls, snakeHeadPos);
        }
        
        if (modes.contains("Statue")) {
            collision = collision || checkSnakeListCollision(statueGame.statues, snakeHeadPos);
        }
        
        return collision;
    }
    
    @Override
    public void prepareNewGame() {
        
        super.prepareNewGame();
        
        if (modes.contains("Wall")) {
            wallGame.postPrepareNewGameWallGame();
        }
        
        if (modes.contains("Statue")) {
            statueGame.postPrepareNewGameStatueGame();
        }
    }
    
    // CheeseGame - DimensionGame
    
    @Override
    public void placeFood() {
        
        if (modes.contains("Dimension")) {
            dimensionGame.prevPlaceFoodDimensionGame();
        }
        if (modes.contains("Cheese")) {
            cheeseGame.prevPlaceFoodCheeseGame();
        }
        
        super.placeFood();
    }
}
