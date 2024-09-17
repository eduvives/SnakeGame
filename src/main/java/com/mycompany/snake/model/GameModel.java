/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model;

import com.mycompany.snake.model.GameMode.BoundlessGame;
import com.mycompany.snake.model.GameMode.BlenderGame;
import com.mycompany.snake.model.GameMode.CheeseGame;
import com.mycompany.snake.model.GameMode.StatueGame;
import com.mycompany.snake.model.GameMode.TwinGame;
import com.mycompany.snake.model.GameMode.WallGame;
import com.mycompany.snake.model.GameMode.DimensionGame;
import com.mycompany.snake.model.GameMode.ClassicGame;
import com.mycompany.snake.model.GameMode.PeacefulGame;
import com.mycompany.snake.model.Snake.Snake;
import com.mycompany.snake.model.Square.Square;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Eduard
 */
public class GameModel {
    
    // Observador que será notificado cuando el modelo cambie
    private ModelObserver observer;
    
    private Point startPos;
    private Snake snake;
    
    private ClassicGame classicGame;
    private WallGame wallGame;
    private CheeseGame cheeseGame;
    private BoundlessGame boundlessGame;
    private TwinGame twinGame;
    private StatueGame statueGame;
    private DimensionGame dimensionGame;
    private PeacefulGame peacefulGame;
    private BlenderGame blenderGame;
    
    private ClassicGame gameMode;
    private String gameModeName;
    private List<String> blenderSelectedModes;
    
    private int numBoardRows;
    private int numBoardCols;
    private List<Point> availablePositions = new ArrayList<>();
    private List<Collection<? extends Square>> specificModeLists = new ArrayList<>();
    
    private int score;
    private int numFood;
    private List<Square> food = new ArrayList<>();
    
    private boolean gameStarted;
    private boolean gameEnded;

    public GameModel() {
        classicGame = new ClassicGame(this);
        wallGame = new WallGame(this);
        cheeseGame = new CheeseGame(this);
        boundlessGame = new BoundlessGame(this);
        twinGame = new TwinGame(this);
        statueGame = new StatueGame(this);
        dimensionGame = new DimensionGame(this);
        peacefulGame = new PeacefulGame(this);
        blenderGame = new BlenderGame(this);
    }
    
    
    // Método para asignar el observador
    public void setObserver(ModelObserver observer) {
        this.observer = observer;
    }

    public ModelObserver getObserver() {
        return observer;
    }
    
    public void setScore(int score) {
        this.score = score;
        observer.onScoreChanged();
    }

    public int getNumFood() {
        return numFood;
    }
    
    public String getGameModeName() {
        return gameModeName;
    }

    public int getScore() {
        return score;
    }
    
    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }
    
    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameEnded(boolean gameEnded) {
        this.gameEnded = gameEnded;
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    public void setSnake(Snake snake) {
        this.snake = snake;
    }

    public Snake getSnake() {
        return snake;
    }

    public List<Collection<? extends Square>> getSpecificModeLists() {
        return specificModeLists;
    }

    public List<Square> getFood() {
        return food;
    }

    public List<Point> getAvailablePositions() {
        return availablePositions;
    }

    public List<String> getBlenderSelectedModes() {
        return blenderSelectedModes;
    }

    public int getNumBoardRows() {
        return numBoardRows;
    }

    public int getNumBoardCols() {
        return numBoardCols;
    }

    public Point getStartPos() {
        return startPos;
    }

    public WallGame getWallGame() {
        return wallGame;
    }

    public CheeseGame getCheeseGame() {
        return cheeseGame;
    }

    public BoundlessGame getBoundlessGame() {
        return boundlessGame;
    }

    public TwinGame getTwinGame() {
        return twinGame;
    }

    public StatueGame getStatueGame() {
        return statueGame;
    }

    public DimensionGame getDimensionGame() {
        return dimensionGame;
    }
    
    public PeacefulGame getPeacefulGame() {
        return peacefulGame;
    }
    
    // Update Game Params
    
    public void updateBoardParams(int boardWidth, int boardHeight, int squareSize) {
        numBoardCols = boardWidth / squareSize;
        numBoardRows = boardHeight / squareSize;

        startPos = new Point(Snake.START_LENGTH + 1, numBoardRows / 2);
    }
    
    public void updateNumFoodParam(int numFood) {
        this.numFood = numFood;
    }
    
    public void updateBlenderSelectedModes(String mode, List<String> newBlenderSelectedModes) {
        // Blender Selected Modes Changed
        if (!Objects.equals(blenderSelectedModes, newBlenderSelectedModes)) {
            blenderSelectedModes = newBlenderSelectedModes; // TODO variable necesaria? o passar lista directamente como parametro a BlenderGame?
            blenderGame.setBlenderModes(blenderSelectedModes); // TODO revisar
        }
    }
    
    public void updateGameMode(String mode) {
        
        switch (mode) {
            case "Classic" -> gameMode = classicGame;
            case "Wall" -> gameMode = wallGame;
            case "Cheese" -> gameMode = cheeseGame;
            case "Boundless" -> gameMode = boundlessGame;
            case "Twin" -> gameMode = twinGame;
            case "Statue" -> gameMode = statueGame;
            case "Dimension" -> gameMode = dimensionGame;
            case "Peaceful" -> gameMode = peacefulGame;
            case "Blender" -> gameMode = blenderGame;
            default -> gameMode = classicGame;
        }

        gameModeName = mode;
    }
    
    // Game Logic
    
    public void newGame() {
        
        gameMode.prepareNewGame();
        gameMode.initializeSnake();

        gameMode.placeFood();
        observer.onViewChanged();
    }
    
    public void startGame() {
        gameStarted = true;
    }
    
    public void gameEnd() {
        gameEnded = true;
    }
    
    public void nextLoop() {
        gameMode.nextLoop();
    }
    
    public void initializeSnake() {
        gameMode.initializeSnake();
    }
}
