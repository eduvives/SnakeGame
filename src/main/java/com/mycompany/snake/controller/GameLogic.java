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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
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
    private String gameModeName;
    private List<String> blenderSelectedModes;
    private Map<Color, List<Point>> viewSquaresColors = new HashMap<>();
    private List<Square> viewAllSquares = new ArrayList<>();
    
    private int boardWidth;
    private int boardHeight;
    private int squareSize;
    protected int numBoardRows;
    protected int numBoardCols;
    protected boolean isBoardUpdated;
    protected List<Point> availablePositions = new ArrayList<>();
    protected List<List<Point>> notAvailablePositionElements = new ArrayList<>();
    protected List<Collection<? extends Square>> specificModeLists = new ArrayList<>();
    
    protected String boardName;
    protected int score;
    protected int numFood;
    protected List<Square> food = new ArrayList<>();
    
    protected Timer timer;
    protected int timerDelay;
    protected List<Point> inputQueue = new ArrayList<>();
    private static final int MAX_INPUT_QUEUE_SIZE = 2;
    protected boolean gameStarted;
    protected boolean gameEnded;
    
    public GameLogic(SnakeView view) {
        this.view = view;
        setViewParams();
        setSettingsComboBoxesModels();
        setBlenderModeListModel();
        setGameParams(
            SettingsParams.BOARD_VALUES[SettingsParams.DEFAULT_SELECTED_INDEX],
            SettingsParams.SPEED_VALUES[SettingsParams.DEFAULT_SELECTED_INDEX],
            SettingsParams.FOOD_VALUES[SettingsParams.DEFAULT_SELECTED_INDEX],
            SettingsParams.MODE_NAMES[SettingsParams.DEFAULT_SELECTED_INDEX]
        );
        updateBoardParams();
        setViewListeners();
        configureKeyBindings();
    }        
    
    private void setViewParams(){
        view.getBoardPanel().setBackgroundColor(CellType.EMPTY.getColor());
    }
    
    private void setGameParams(int[] boardSize, int delay, int numFood, String mode) {
        
        // Board Size Changed
        if (boardWidth != boardSize[0] || boardHeight != boardSize[1] || squareSize != boardSize[2] ) {
            
            boardWidth = boardSize[0];
            boardHeight = boardSize[1];
            squareSize = boardSize[2];
            isBoardUpdated = false;
            
            numBoardCols = boardWidth / squareSize;
            numBoardRows = boardHeight / squareSize;
        }

        this.timerDelay = delay;

        this.numFood = numFood;
        
        boolean selectBlindly = view.getBlenderSettings().isSelectBlindlyModes();
        
        if (!selectBlindly || (selectBlindly && mode.equals("Blender"))) {
            
            List<String> newBlenderSelectedModes = view.getBlenderSettings().getModeListSelectedValues();
            
            // Blender Selected Modes Changed
            if (!Objects.equals(blenderSelectedModes, newBlenderSelectedModes)) {
                blenderSelectedModes = newBlenderSelectedModes; // TODO variable necesaria? o passar lista directamente como parametro a BlenderGame?
                if (mode.equals("Blender") && gameMode instanceof BlenderGame) {
                    BlenderGame blenderGame = (BlenderGame) gameMode;
                    blenderGame.setBlenderModes(blenderSelectedModes); // TODO revisar
                }
            }
        }
        
        // Game Mode Changed
        if (!Objects.equals(gameModeName, mode)) {
            
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
            playBtnAction(view.getMenu());
        });
        
        view.getSettings().setPlayBtnListener(e -> {
            playBtnAction(view.getSettings());
        });
        
        view.getSettings().setBackBtnListener(e -> {
            backBtnAction(view.getSettings(), view.getMenu());
        });
        
        view.getBlenderSettings().setPlayBtnListener(e -> {
            playBtnAction(view.getBlenderSettings());
        });                
        
        view.getBlenderSettings().setBackBtnListener(e -> {
            backBtnAction(view.getBlenderSettings(), view.getSettings());
        });
        
        view.getSettings().setResetBtnListener(e -> {
            for (JComboBox<String> comboBox : view.getSettings().getComboBoxes()) {
                comboBox.setSelectedIndex(SettingsParams.DEFAULT_SELECTED_INDEX);
            }
        });
        
        view.getBlenderSettings().setResetBtnListener(e -> {
            view.getBlenderSettings().getModeList().setSelectedIndex(SettingsParams.DEFAULT_SELECTED_INDEX);
        });
    }
    
    private void playBtnAction(JDialog fromDialog) {
        updateGameParamsFromView();
            
        newGame();
        fromDialog.dispose();
    }
    
    private void backBtnAction(JDialog fromDialog, JDialog toDialog) {
        fromDialog.dispose();
        toDialog.setVisible(true);
    }
    
    private void updateGameParamsFromView() {

        setGameParams(
            SettingsParams.BOARD_VALUES[view.getSettings().getBoardCmbSelectedIndex()],
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
                handleDirectionChange(new Point(0, -1));
            }
        });

        actionMap.put("moveDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDirectionChange(new Point(0, 1));
            }
        });

        actionMap.put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDirectionChange(new Point(-1, 0));
            }
        });

        actionMap.put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleDirectionChange(new Point(1, 0));
            }
        });
        
        inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), "endGame");
        
        actionMap.put("endGame", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameEnded) {
                    gameEnd(false);
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
    
    private void handleDirectionChange(Point newDirection) {
        
        Point previousDirection;
        int numInputs = inputQueue.size();
        
        if (numInputs == 0) {
            previousDirection = snake.getDirection();
        } else {
            previousDirection = (numInputs < MAX_INPUT_QUEUE_SIZE) ? inputQueue.get(numInputs - 1) : inputQueue.get(numInputs - 2);
        }
        
        // Comprobar que no sea la dirección opuesta
        if (previousDirection.x != -newDirection.x || previousDirection.y != -newDirection.y) {
            
            if (!gameStarted) {
                startGame();
            }
            
            if (!previousDirection.equals(newDirection)) {
                if (numInputs >= MAX_INPUT_QUEUE_SIZE) {
                    inputQueue.remove(numInputs - 1);
                }
                inputQueue.add(newDirection);
            }
        }
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
            
            if (!inputQueue.isEmpty()) {
                snake.getDirection().setLocation(inputQueue.remove(0));
            }
            
            gameMode.nextLoop();
        };
    }        
        
    protected void gameEnd(boolean isFeast) {
        System.out.println("Feast: " + isFeast);
        gameEnded = true;
        
        if (timer != null) {
            timer.stop();
        }
        
        openMenu();
    }
    
    protected void updateView(){
        viewSquaresColors.clear();
        viewAllSquares.clear();
        
        // Snake Body
        viewAllSquares.addAll(snake.getBody());
        
        // Snake Head
        viewAllSquares.add(snake.getHead());
        
        // Food
        viewAllSquares.addAll(food);
        
        // Specific Mode Lists (Wall...)
        for (Collection<? extends Square> modeList : specificModeLists) {
            viewAllSquares.addAll(0,modeList);
        }
        
        // Test Lines Start 2
        
        List<Square> candidates_test = new ArrayList<>();
        for (Point pos : availablePositions) {
            candidates_test.add(new Square(pos, CellType.TEST));
        }
        testList.clear();
        testList.addAll(candidates_test);

        // Test Lines Start
        
        viewAllSquares.addAll(0,testList);
        
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
        
        for (Square square : viewAllSquares) {
            Color color = square.getColor();
            Point position = square;

            viewSquaresColors.computeIfAbsent(color, k -> new ArrayList<>()).add(position);
        }
        
        view.getBoardPanel().setSquaresColors(viewSquaresColors);
    }
}