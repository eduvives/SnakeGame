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
import java.util.Random;
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
    private PeacefulGame peacefulGame;
    
    private List<String> modes;
    
    private boolean invertNextBodyPartSnake;
    
    public BlenderGame(GameModel game) {
        super(game);
        
        wallGame = this.game.getWallGame();
        cheeseGame = this.game.getCheeseGame();
        boundlessGame = this.game.getBoundlessGame();
        twinGame = this.game.getTwinGame();
        statueGame = this.game.getStatueGame();
        dimensionGame = this.game.getDimensionGame();
        peacefulGame = this.game.getPeacefulGame();
    }
    
    public void setBlenderModes(List<String> modes) {
        this.modes = modes;
    }
    
    // CheeseGame
    
    @Override
    protected boolean checkFeast() {
        
        if (modes.contains("Peaceful") && modes.contains("Cheese")) {
            return ((game.getSnake().getBody().size() + 1 >= game.getNumBoardRows() * game.getNumBoardCols() / 2) && game.getFood().isEmpty()) || cheeseGame.checkFeast();
        } else if (modes.contains("Peaceful")) {
            return peacefulGame.checkFeast() || super.checkFeast();
        } else if (modes.contains("Cheese")) {
            return cheeseGame.checkFeast();
        } else {
            return super.checkFeast();
        }
    }
    
    // BoundlessGame
    
    @Override
    protected Point getNewPos(Point newDirection) {
        
        invertNextBodyPartSnake = false;
        
        if (modes.contains("Boundless") || modes.contains("Peaceful")) {
            if (modes.contains("Cheese")) {
                
                int posX = game.getSnake().getHead().x + newDirection.x;
                int posY = game.getSnake().getHead().y + newDirection.y;
                
                boolean isOutOfBoundsX = posX < 0 || posX >= game.getNumBoardCols();
                boolean isOddNumCols = game.getNumBoardCols() % 2 != 0;

                boolean isOutOfBoundsY = posY < 0 || posY >= game.getNumBoardRows();
                boolean isOddNumRows = game.getNumBoardRows() % 2 != 0;
                
                invertNextBodyPartSnake = (isOutOfBoundsX && isOddNumCols) || (isOutOfBoundsY && isOddNumRows);
            }
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
    protected boolean isPositionAvailable(Point position) {
        
        boolean positionAvailable = super.isPositionAvailable(position);
        
        // No hace falta comprobar si el modo Statue está activo cuando el modo Dimenson este activo, 
        // ya que las comprobaciones del modo Dimensión incluyen las del modo Statue. 
        // Al revisar los specificModeLists se accederá a la lista de statues.
        if (modes.contains("Dimension")) {
            positionAvailable &= !game.getFood().contains(position) 
            && !game.getSpecificModeLists().stream().anyMatch(modeList -> modeList.contains(position));
        } else if (modes.contains("Statue")) {
            positionAvailable &= !statueGame.statues.contains(position);
        }

        return positionAvailable;
    }
    
    // TwinGame - CheeseGame - StatueGame - WallGame - DimensionGame - BoundlessGame
    
    @Override
    protected void snakeMove(Point newPos, boolean isFoodCollision) {
        
        super.snakeMove(newPos, isFoodCollision);
        
        if (invertNextBodyPartSnake) {
            cheeseGame.cheeseSnake.invertNextBodyPartSnake();
        }
        
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
    
    // TwinGame - StatueGame - DimensionGame - BoundlessGame
    
    private void removeStatuesSpawnRadius() {
        
        Set<Point> spawnRadiusStatues = new HashSet<>(statueGame.statues);
        spawnRadiusStatues.retainAll(getSpawnRadius());

        for (Point statuePos : spawnRadiusStatues) {
            if (statuePos.equals(game.getSnake().getHead()) || !game.getSnake().getBody().contains(statuePos)) {
                statueGame.statues.remove(statuePos);
                if (isPositionAvailable(statuePos)) {
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
                if (isPositionAvailable(statueSquare)) {
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
        if ((modes.contains("Dimension") || game.getScore() % 2 != 0)) {
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
        
        if (modes.contains("Peaceful")) {
            return peacefulGame.checkCollision(snakeHeadPos);
        }
        
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
    
    @Override
    protected Point getRandomFoodPosition() {
        
        if (modes.contains("Cheese")) {
            return getRandomFoodPositionCheeseDimension();
        } else {
            return super.getRandomFoodPosition();
        }
    }
    
    private Point getRandomFoodPositionCheeseDimension() {
        
        if (noFoodPositions()) {
            return null;
        }

        Random rand = new Random();
        
        Point candidate = cheeseGame.getFoodPositionCandidates().remove(rand.nextInt(cheeseGame.getFoodPositionCandidates().size()));
        game.getAvailablePositions().remove(candidate);
        
        return candidate;
    }
    
    @Override
    protected boolean noFoodPositions() {
        
        if (modes.contains("Peaceful") && modes.contains("Cheese")) {
            return (game.getSnake().getBody().size() + 1 + game.getFood().size() >= game.getNumBoardRows() * game.getNumBoardCols() / 2) || noFoodPositionsCheeseDimension();
        } else if (modes.contains("Peaceful")) {
            return peacefulGame.noFoodPositions() || super.noFoodPositions();
        } else if (modes.contains("Cheese") && modes.contains("Dimension")) {
            return noFoodPositionsCheeseDimension();
        } else if (modes.contains("Cheese")) {
            return cheeseGame.noFoodPositions();
        } else if (modes.contains("Dimension")) {
            return dimensionGame.noFoodPositions();
        } else {
            return super.noFoodPositions();
        }
    }
    
    private boolean noFoodPositionsCheeseDimension() {
        // Cantidad total de posiciones disponibles
        int numFoodPositionCandidates = cheeseGame.getFoodPositionCandidates().size();

        // Filtramos las posiciones de "food" que coincidan con alguna posición de "body"
        long numFoodInsideSnakeBody = game.getFood().stream()
                .filter(f -> game.getSnake().getBody().stream().anyMatch(b -> b.equals(f)))
                .count();

        // Restamos la cantidad de coincidencias al total de availablePositions
        return (numFoodPositionCandidates - (int) numFoodInsideSnakeBody) <= 0;
    }
}
