/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.controller;

import com.mycompany.snake.model.CellType;
import com.mycompany.snake.model.SettingsParams;
import com.mycompany.snake.model.Snake;
import com.mycompany.snake.model.Square;
import com.mycompany.snake.view.SnakeView;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.Timer;

/**
 *
 * @author Eduard
 */
public class GameLogic {
    
    protected List<Square> testList = new ArrayList<>(); // Test Line
    
    protected SnakeView view;
    protected Point startPos;
    protected Snake snake;
    private ClassicGame gameMode;
    private List<String> blenderSelectedModes;
    
    private int boardWidth;
    private int boardHeight;
    private int squareSize;
    protected int numBoardRows;
    protected int numBoardCols;
    protected boolean isBoardUpdated;
    protected List<Point> availablePositions = new ArrayList<>();
    protected List<Collection<? extends Square>> specificModeLists = new ArrayList<>();
    
    protected int score;
    protected int numFood;
    protected List<Square> food = new ArrayList<>();
    
    protected Timer timer;
    protected int timerDelay;
    protected Queue<Point> inputQueue = new LinkedList<>();
    protected boolean gameStarted;
    protected boolean gameEnded;
    
    public GameLogic(SnakeView view) {
        this.view = view;
        setViewParams();
        setGameParams(
            SettingsParams.BOARD_VALUES[SettingsParams.DEFAULT_SELECTED_INDEX][0],
            SettingsParams.BOARD_VALUES[SettingsParams.DEFAULT_SELECTED_INDEX][1],
            SettingsParams.BOARD_VALUES[SettingsParams.DEFAULT_SELECTED_INDEX][2],
            SettingsParams.SPEED_VALUES[SettingsParams.DEFAULT_SELECTED_INDEX],
            SettingsParams.FOOD_VALUES[SettingsParams.DEFAULT_SELECTED_INDEX],
            SettingsParams.MODE_NAMES[SettingsParams.DEFAULT_SELECTED_INDEX]
        );
        updateBoardParams();
        setSettingsComboBoxesModels();
        setBlenderModeListModel();
        setViewListeners();
        configureKeyBindings();
    }        
    
    private void setViewParams(){
        view.getBoardPanel().setBackgroundColor(CellType.EMPTY.getColor());
    }
    
    private void setGameParams(int boardWidth, int boardHeight, int squareSize, int delay, int numFood, String mode) {
        
        if (boardWidth != this.boardWidth || boardHeight != this.boardHeight || squareSize != this.squareSize ) {
            this.boardWidth = boardWidth;
            this.boardHeight = boardHeight;
            this.squareSize = squareSize;
            isBoardUpdated = false;
            
            numBoardCols = boardWidth / squareSize;
            numBoardRows = boardHeight / squareSize;
        }
        
        this.timerDelay = delay;
        this.numFood = numFood;
        
        if (mode.equals("Classic")) {
            gameMode = new ClassicGame(this);
        } else if (mode.equals("Wall")) {
            gameMode = new WallGame(this);
        } else if (mode.equals("Cheese")) {
            gameMode = new CheeseGame(this);
        } else if (mode.equals("Boundless")) {
            gameMode = new BoundlessGame(this);
        } else if (mode.equals("Twin")) {
            gameMode = new TwinGame(this);
        } else if (mode.equals("Statue")) {
            gameMode = new StatueGame(this);
        } else if (mode.equals("Blender")) {
            blenderSelectedModes = view.getBlenderSettings().getModeListSelectedValues(); // TODO variable necesaria? o passar lista directamente como parametro a BlenderGame?
            gameMode = new BlenderGame(this, blenderSelectedModes);
        } else {
            gameMode = new ClassicGame(this);
        }
    }
    
    protected void updateBoardParams() {
        view.getBoardPanel().setBoardWidth(boardWidth);
        view.getBoardPanel().setBoardHeight(boardHeight);
        view.getBoardPanel().setSquareSize(squareSize);
        view.getBoardPanel().setPreferredSize(new Dimension(boardWidth + 1, boardHeight + 1));   
        
        view.getBoardPanel().revalidate();
        view.getBoardPanel().repaint();
        view.pack();
        
        isBoardUpdated = true;
    }
    
    private void setSettingsComboBoxesModels() {
        view.getSettings().setBoardCmbModel(SettingsParams.BOARD_NAMES, SettingsParams.DEFAULT_SELECTED_INDEX);
        view.getSettings().setSpeedCmbModel(SettingsParams.SPEED_NAMES, SettingsParams.DEFAULT_SELECTED_INDEX);
        view.getSettings().setFoodCmbModel(SettingsParams.FOOD_NAMES, SettingsParams.DEFAULT_SELECTED_INDEX);
        view.getSettings().setModeCmbModel(SettingsParams.MODE_NAMES, SettingsParams.DEFAULT_SELECTED_INDEX);
    }
    
    private void setBlenderModeListModel() {
        List<String> blenderModeNames = new ArrayList<>(Arrays.asList(SettingsParams.MODE_NAMES));
        blenderModeNames.removeAll(Arrays.asList(SettingsParams.BLENDER_MODE_EXCLUDED_MODES));
        view.getBlenderSettings().setModeListModel(blenderModeNames, SettingsParams.DEFAULT_SELECTED_INDEX);
    }
        
    private void setViewListeners() {
        
        // Al no abrir y cerrar las ventanas con mucha frecuencia, seguimos usando dispose() y así aprovechamos la animación de creación de ventana.
        
        view.getMenu().setPlayBtnListener(e -> {
            
            newGame();
            view.getMenu().dispose(); // view.getMenu().setVisible(false);
        });
        
        view.getSettings().setPlayBtnListener(e -> {
            
            updateGameParamsFromView();
            
            newGame();
            view.getSettings().dispose();
        });
        
        view.getSettings().setBackBtnListener(e -> {
            
            updateGameParamsFromView();
            
            view.getSettings().dispose();
            view.getMenu().setVisible(true);
        });
        
        view.getBlenderSettings().setPlayBtnListener(e -> {
            
            updateGameParamsFromView();
            
            newGame();
            view.getBlenderSettings().dispose();
        });                
        
        view.getBlenderSettings().setBackBtnListener(e -> {
            
            updateGameParamsFromView();
            
            view.getBlenderSettings().dispose();
            view.getSettings().setVisible(true);
        });
    }
    
    private void updateGameParamsFromView() {
        int[] boardValues = SettingsParams.BOARD_VALUES[view.getSettings().getBoardCmbSelectedIndex()];

        setGameParams(
            boardValues[0],
            boardValues[1],
            boardValues[2],
            SettingsParams.SPEED_VALUES[view.getSettings().getSpeedCmbSelectedIndex()],
            SettingsParams.FOOD_VALUES[view.getSettings().getFoodCmbSelectedIndex()],
            SettingsParams.MODE_NAMES[view.getSettings().getModeCmbSelectedIndex()]
        );
    }
    
    private void configureKeyBindings() {
        InputMap inputMap = view.getBoardPanel().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = view.getBoardPanel().getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("UP"), "moveUp");
        inputMap.put(KeyStroke.getKeyStroke("W"), "moveUp");
        inputMap.put(KeyStroke.getKeyStroke("DOWN"), "moveDown");
        inputMap.put(KeyStroke.getKeyStroke("S"), "moveDown");
        inputMap.put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
        inputMap.put(KeyStroke.getKeyStroke("A"), "moveLeft");
        inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");
        inputMap.put(KeyStroke.getKeyStroke("D"), "moveRight");
        
        actionMap.put("moveUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (snake.getDirection().y != 1 && inputQueue.size() < 2) {
                    if (!gameStarted) {
                        startGame();
                    }
                    snake.getDirection().setLocation(0, -1);
                    inputQueue.add(new Point(snake.getDirection()));
                }
            }
        });

        actionMap.put("moveDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (snake.getDirection().y != -1 && inputQueue.size() < 2) {
                    if (!gameStarted) {
                        startGame();
                    }
                    snake.getDirection().setLocation(0, 1);
                    inputQueue.add(new Point(snake.getDirection()));
                }                
            }
        });

        actionMap.put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (snake.getDirection().x != 1 && inputQueue.size() < 2) {
                    if (!gameStarted) {
                        startGame();
                    }
                    snake.getDirection().setLocation(-1, 0);
                    inputQueue.add(new Point(snake.getDirection()));
                }                
            }
        });

        actionMap.put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (snake.getDirection().x != -1 && inputQueue.size() < 2) {
                    if (!gameStarted) {
                        startGame();
                    }
                    snake.getDirection().setLocation(1, 0);
                    inputQueue.add(new Point(snake.getDirection()));
                }
            }
        });
        
        // Test Lines Start        
        inputMap.put(KeyStroke.getKeyStroke("SPACE"), "pauseGame");
        
        actionMap.put("pauseGame", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameStarted) {
                    if (timer.isRunning()) {
                        timer.stop();
                    } else {
                       timer.start();
                    }
                }
            }
        });
        // Test Lines End
    }          
    
    public void showGameBoard() {
        startPos = new Point(Snake.START_LENGTH + 1, numBoardRows / 2);
        snake = new Snake(new Point(startPos));
        
        updateView();
        view.getBoardPanel().repaint();
        
        this.view.setVisible(true);
    }
    
    public void openMenu() {
        view.getMenu().setScoreLabel(score);
        view.openMenu();
    }
    
    protected void updateScore() {
        view.setCurrentScore(score);
    } 
    
    protected void newGame() {
        
        gameMode.prepareNewGame();
        gameMode.initializeSnake();

        gameMode.placeFood();
        updateView();
        view.getBoardPanel().repaint();
    }
    
    private void startGame() {
        
        ActionListener gameLoopListener = getGameLoopListener();
        
        timer = new Timer(timerDelay, gameLoopListener);
        timer.start();
        gameStarted = true;
    }
    
    private ActionListener getGameLoopListener() {
        
        return (ActionEvent e) -> {
            
            Point currentDirection = inputQueue.isEmpty() ? snake.getDirection() : inputQueue.poll();
            
            gameMode.nextLoop(currentDirection);
        };
    }        
        
    protected void gameEnd(boolean isFeast) {
        System.out.println("Feast: " + isFeast);
        gameEnded = true;
        timer.stop();
        openMenu();
    }
    
    protected void updateView(){
        Map<Color, List<Point>> squaresColors = new HashMap<>();
        List<Square> allSquares = new ArrayList<>();
        
        // Snake Body
        allSquares.addAll(snake.getBody());
        
        // Snake Head
        allSquares.add(snake.getHead());
        
        // Food
        allSquares.addAll(food);
        
        // Specific Mode Lists (Wall...)
        for (Collection<? extends Square> modeList : specificModeLists) {
            allSquares.addAll(0,modeList);
        }
        
        // Test Lines Start 2
        
        List<Square> candidates_test = new ArrayList<>();
        for (Point pos : availablePositions) {
            candidates_test.add(new Square(pos, CellType.TEST));
        }
        testList.clear();
        testList.addAll(candidates_test);
        
        // Test Lines Start
        
        allSquares.addAll(0,testList);
        
        testList.clear();
        
        /*
        List<Square> candidates_test = new ArrayList<>();
        for (Point pos : candidates) {
            candidates_test.add(new Square(pos, CellType.TEST));
        }
        game.testList.clear();
        game.testList.addAll(candidates_test);
        */
        
        // Test Lines End
        
        for (Square square : allSquares) {
            Color color = square.getColor();
            Point position = square;

            squaresColors.computeIfAbsent(color, k -> new ArrayList<>()).add(position);
        }
        
        view.getBoardPanel().setSquaresColors(squaresColors);
    }
}