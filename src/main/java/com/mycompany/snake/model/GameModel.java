/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model;

import com.mycompany.snake.model.GameMode.BoundlessGame;
import com.mycompany.snake.model.GameMode.BlenderGame;
import com.mycompany.snake.model.GameMode.ShrinkGame;
import com.mycompany.snake.model.GameMode.CheeseGame;
import com.mycompany.snake.model.GameMode.StatueGame;
import com.mycompany.snake.model.GameMode.TwinGame;
import com.mycompany.snake.model.GameMode.WallGame;
import com.mycompany.snake.model.GameMode.DimensionGame;
import com.mycompany.snake.model.GameMode.ClassicGame;
import com.mycompany.snake.model.GameMode.PeacefulGame;
import com.mycompany.snake.model.Snake.Snake;
import com.mycompany.snake.model.Square.ColorPaletteManager;
import com.mycompany.snake.model.Square.Square;
import java.awt.Color;
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
    
    private HighScoreManager highScoreManager;
    private ColorPaletteManager colorPaletteManager;
    
    private ClassicGame classicGame;
    private WallGame wallGame;
    private CheeseGame cheeseGame;
    private BoundlessGame boundlessGame;
    private ShrinkGame shrinkGame;
    private TwinGame twinGame;
    private StatueGame statueGame;
    private DimensionGame dimensionGame;
    private PeacefulGame peacefulGame;
    private BlenderGame blenderGame;
    
    private String boardName;
    private String speedName;
    private String foodName;
    private String modeName;
    
    private ClassicGame gameMode;
    private List<String> blenderSelectedModes;
    
    private int numBoardRows;
    private int numBoardCols;
    private List<Point> availablePositions = new ArrayList<>();
    private List<Collection<? extends Square>> specificModeLists = new ArrayList<>();
    
    private int score;
    private int currentGameHighScore;
    private boolean newHighScore;
    private int numFood;
    private List<Square> food = new ArrayList<>();
    
    private boolean gameActive;
    private boolean gameStarted;

    public GameModel() {
        
        highScoreManager = new HighScoreManager(this);
        colorPaletteManager = new ColorPaletteManager();
        
        classicGame = new ClassicGame(this);
        wallGame = new WallGame(this);
        cheeseGame = new CheeseGame(this);
        boundlessGame = new BoundlessGame(this);
        shrinkGame = new ShrinkGame(this);
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
        
        if (score > currentGameHighScore) {
            newHighScore = true;
            setCurrentGameHighScore(score);
        }
        observer.onScoreChanged();
    }
    
    public int getCachedHighScore (String board, String speed, String food, String mode) {
        return highScoreManager.getCachedHighScore(board, speed, food, mode);
    }
    
    public void initializeCurrentGameHighScore() {
        this.currentGameHighScore = getCachedHighScore(boardName, speedName, foodName, modeName);
        observer.onHighScoreInitialized();
    }
    
    private void setCurrentGameHighScore(int score) {
        this.currentGameHighScore = score;
        observer.onHighScoreChanged();
    }

    public void setNewHighScore(boolean newHighScore) {
        this.newHighScore = newHighScore;
    }

    protected boolean isNewHighScore() {
        return newHighScore;
    }

    public String getBoardName() {
        return boardName;
    }

    public String getSpeedName() {
        return speedName;
    }

    public String getFoodName() {
        return foodName;
    }
    
    public String getModeName() {
        return modeName;
    }

    public int getNumFood() {
        return numFood;
    }
    
    public int getScore() {
        return score;
    }
    
    public int getCurrentGameHighScore() {
        return currentGameHighScore;
    }
    
    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameActive(boolean gameActive) {
        this.gameActive = gameActive;
    }

    public boolean isGameActive() {
        return gameActive;
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
    
    public ShrinkGame getShrinkGame() {
        return shrinkGame;
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
    
    public void updateBoardParams(String boardName, int numBoardCols, int numBoardRows) {
        
        this.boardName = boardName;
        this.numBoardCols = numBoardCols;
        this.numBoardRows = numBoardRows;

        startPos = new Point(Snake.START_LENGTH + 1, numBoardRows / 2);
    }
    
    public void updateSpeedParam(String speedName) {
        this.speedName = speedName;
    }
    
    public void updateFoodParam(String foodName) {
        this.foodName = foodName;
        this.numFood = SettingsParams.FOODS.get(foodName);
    }
    
    public void updateBlenderSelectedModes(List<String> newBlenderSelectedModes) {
        // Blender Selected Modes Changed
        blenderSelectedModes = newBlenderSelectedModes;
        blenderGame.setBlenderModes(blenderSelectedModes);
    }
    
    public void updateGameMode(String modeName) {
        
        switch (modeName) {
            case "Classic" -> gameMode = classicGame;
            case "Wall" -> gameMode = wallGame;
            case "Cheese" -> gameMode = cheeseGame;
            case "Boundless" -> gameMode = boundlessGame;
            case "Shrink" -> gameMode = shrinkGame;
            case "Twin" -> gameMode = twinGame;
            case "Statue" -> gameMode = statueGame;
            case "Dimension" -> gameMode = dimensionGame;
            case "Peaceful" -> gameMode = peacefulGame;
            case "Blender" -> gameMode = blenderGame;
            default -> gameMode = classicGame;
        }

        this.modeName = modeName;
    }
    
    public boolean isModeActive(String modeName) {
        return Objects.equals(getModeName(), modeName) || (Objects.equals(getModeName(), "Blender") && getBlenderSelectedModes().contains(modeName));
    }
    
    // Game Logic
    
    public void newGame() {
        
        gameMode.prepareNewGame();
        gameMode.initializeSnake();

        gameMode.placeFood();
        observer.onNewGame();
        observer.onViewChanged();
    }
    
    public void startGame() {
        gameStarted = true;
    }
    
    public void gameEnd() {
        
        if (newHighScore) {
            highScoreManager.updateCachedHighScore();
        }
        
        gameStarted = false;
        gameActive = false;
    }
    
    public void nextLoop() {
        gameMode.nextLoop();
    }
    
    public void initializeSnake() {
        gameMode.initializeSnake();
    }
    
    public void boardColorChanged(Color newColor) {
        colorPaletteManager.boardColorChanged(newColor);
    }
    
    public void foodColorChanged(Color newColor) {
        colorPaletteManager.foodColorChanged(newColor);
    }
    
    public void snakeColorChanged(Color newColor) {
        colorPaletteManager.snakeColorChanged(newColor);
    }
}
