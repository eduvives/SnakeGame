/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model;

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
    protected ModelObserver observer;
    
    protected Point startPos;
    protected Snake snake;
    private ClassicGame gameMode;
    private String gameModeName;
    private List<String> blenderSelectedModes;
    
    protected int numBoardRows;
    protected int numBoardCols;
    protected List<Point> availablePositions = new ArrayList<>();
    protected List<Collection<? extends Square>> specificModeLists = new ArrayList<>();
    
    protected int score;
    protected int numFood;
    protected List<Square> food = new ArrayList<>();
    
    protected boolean gameStarted;
    protected boolean gameEnded;
    
    // Método para asignar el observador
    public void setObserver(ModelObserver observer) {
        this.observer = observer;
    }
    
    protected void setScore(int score) {
        this.score = score;
        observer.onScoreChanged();
    }

    public String getGameModeName() {
        return gameModeName;
    }

    public int getScore() {
        return score;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public boolean isGameEnded() {
        return gameEnded;
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

    public List<Point> getAvailablePositions() { // TODO eliminar getter al terminar test code
        return availablePositions;
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
            if (mode.equals("Blender") && gameMode instanceof BlenderGame) {
                BlenderGame blenderGame = (BlenderGame) gameMode;
                blenderGame.setBlenderModes(blenderSelectedModes); // TODO revisar
            }
        }
    }
    
    public void updateGameMode(String mode) {
        
        switch (mode) {
            case "Classic" -> gameMode = new ClassicGame(this);
            case "Wall" -> gameMode = new WallGame(this);
            case "Cheese" -> gameMode = new CheeseGame(this);
            case "Boundless" -> gameMode = new BoundlessGame(this);
            case "Twin" -> gameMode = new TwinGame(this);
            case "Statue" -> gameMode = new StatueGame(this);
            case "Blender" -> gameMode = new BlenderGame(this, blenderSelectedModes);
            case "Dimension" -> gameMode = new DimensionGame(this);
            default -> gameMode = new ClassicGame(this);
        }

        gameModeName = mode;
    }
    
    // Game Logic
    
    public void newGame() {
        
        gameMode.prepareNewGame();
        gameMode.initializeGameSnake();

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
