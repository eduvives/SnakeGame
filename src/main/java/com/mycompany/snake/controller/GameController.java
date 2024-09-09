/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.controller;

import com.mycompany.snake.model.CellType;
import com.mycompany.snake.model.GameModel;
import com.mycompany.snake.model.ModelObserver;
import com.mycompany.snake.model.SettingsParams;
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
public class GameController implements ModelObserver {
    
    private List<Square> testList = new ArrayList<>(); // Test Line
    
    private SnakeView view;
    private Map<Color, List<Point>> viewSquaresColors = new HashMap<>();
    
    private GameModel model;
    
    // View
    private int boardWidth;
    private int boardHeight;
    private int squareSize;
    private boolean isBoardUpdated;
    
    // Game Logic
    private Timer timer;
    private int timerDelay;
    private List<Point> inputQueue = new ArrayList<>();
    private static final int MAX_INPUT_QUEUE_SIZE = 2;
    
    public GameController(SnakeView view, GameModel model) {
        
        this.view = view;
        this.model = model;
        
        setViewParams();
        setSettingsComboBoxesModels();
        setBlenderModeListModel();
        initializeGameTimer();
        updateGameViewParams(
            SettingsParams.BOARD_VALUES[SettingsParams.DEFAULT_SELECTED_INDEX],
            SettingsParams.SPEED_VALUES[SettingsParams.DEFAULT_SELECTED_INDEX],
            SettingsParams.FOOD_VALUES[SettingsParams.DEFAULT_SELECTED_INDEX],
            SettingsParams.MODE_NAMES[SettingsParams.DEFAULT_SELECTED_INDEX]
        );
        updateViewBoardParams();
        setViewListeners();
        configureKeyBindings();
    }
    
    // Métodos Model Observer
    
    public void registerObserver() {
        model.setObserver(this);
    }
    
    @Override
    public void onViewChanged() {
        refreshBoard();
    }
    
    @Override
    public void onScoreChanged() {
        updateScore();
    }
    
    private void refreshBoard() {
        updateView();
        view.getBoardPanel().repaint();
    }
    
    @Override
    public void onGameEnded(boolean isFeast) {
        endGameLoop(isFeast);
    }
    
    @Override
    public void onNewGame() {

        if (!isBoardUpdated) {
            updateViewBoardParams();
        }
        clearUserInputs();
    }
    
    private void clearUserInputs() {
        inputQueue.clear();
    }
    
    // Métodos View
    
    private void setViewParams(){
        view.getBoardPanel().setBackgroundColor(CellType.EMPTY.getColor());
    }
    
    private void updateGameViewParams(int[] boardSize, int delay, int numFood, String mode) {
        
        // Board Size Changed
        if (boardWidth != boardSize[0] || boardHeight != boardSize[1] || squareSize != boardSize[2] ) {
            
            boardWidth = boardSize[0];
            boardHeight = boardSize[1];
            squareSize = boardSize[2];
            isBoardUpdated = false;
            
            model.updateBoardParams(boardWidth, boardHeight, squareSize);
        }

        if (timerDelay != delay) {
            this.timerDelay = delay;
            updateGameTimerDelay();
            if (Objects.equals(model.getGameModeName(), "Twin")) {
                updateSwitchSidesTimerDelay();
            }
        }
        
        if (model.getNumFood() != numFood) {
            model.updateNumFoodParam(numFood);
        }
        
        boolean selectBlindly = view.getBlenderSettings().isSelectBlindlyModes();
        
        if (!selectBlindly || (selectBlindly && mode.equals("Blender"))) {
            List<String> newBlenderSelectedModes = view.getBlenderSettings().getModeListSelectedValues();
            model.updateBlenderSelectedModes(mode, newBlenderSelectedModes);
        }
        
        // Game Mode Changed
        if (!Objects.equals(model.getGameModeName(), mode)) {
            model.updateGameMode(mode);
        }
    }
    
    private void updateViewBoardParams() {
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
        
        model.newGame();
        // Al no abrir y cerrar las ventanas con mucha frecuencia, seguimos usando dispose() y así aprovechamos la animación de creación de ventana.
        fromDialog.dispose();
    }
    
    private void backBtnAction(JDialog fromDialog, JDialog toDialog) {
        fromDialog.dispose();
        toDialog.setVisible(true);
    }
    
    private void updateGameParamsFromView() {

        updateGameViewParams(
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
                if (!model.isGameEnded()) {
                    endGameLoop(false);
                }
            }
        });
        
        // Test Lines Start
        inputMap.put(KeyStroke.getKeyStroke("SPACE"), "pauseGame");
        
        actionMap.put("pauseGame", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.isGameStarted()) {
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
            previousDirection = model.getSnake().getDirection();
        } else {
            previousDirection = (numInputs < MAX_INPUT_QUEUE_SIZE) ? inputQueue.get(numInputs - 1) : inputQueue.get(numInputs - 2);
        }
        
        // Comprobar que no sea la dirección opuesta
        if (previousDirection.x != -newDirection.x || previousDirection.y != -newDirection.y) {
            
            if (!model.isGameStarted()) {
                startGameLoop();
            }
            
            if (!previousDirection.equals(newDirection)) {
                if (numInputs >= MAX_INPUT_QUEUE_SIZE) {
                    inputQueue.remove(numInputs - 1);
                }
                inputQueue.add(newDirection);
            }
        }
    }
    
    // Métodos Timer
    
    private void initializeGameTimer() {
        ActionListener gameLoopListener = getGameLoopListener();
        timer = new Timer(timerDelay, gameLoopListener);
        timer.setInitialDelay(0); // No haya retraso inicial
    }
    
    private ActionListener getGameLoopListener() {
        
        return (ActionEvent e) -> {
            
            if (!inputQueue.isEmpty()) {
                model.getSnake().getDirection().setLocation(inputQueue.remove(0));
            }
            
            model.nextLoop();
        };
    }
    
    private void updateGameTimerDelay() {
        timer.setDelay(timerDelay);
    }
    
    public void showGameBoard() {
        model.initializeSnake();
        
        refreshBoard();
        
        this.view.setVisible(true);
    }
    
    public void openMenu() {
        view.getMenu().setScoreLabel(model.getScore());
        view.openMenu();
    }
    
    private void updateScore() {
        view.setCurrentScore(model.getScore());
    }
    
    private void startGameLoop() {
        timer.start();
        model.startGame();
    }
    
    private void endGameLoop(boolean isFeast) {
        System.out.println("Feast: " + isFeast);
        model.gameEnd();
        
        timer.stop();
        
        openMenu();
    }
    
    private void updateView(){
        viewSquaresColors.clear();
        
        // Test Lines Start 2
        
        List<Square> candidates_test = new ArrayList<>();
        for (Point pos : model.getAvailablePositions()) {
            candidates_test.add(new Square(pos, CellType.TEST));
        }
        testList.clear();
        testList.addAll(candidates_test);

        // Test Lines Start
        
        addSquareColorListView(testList);
        
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
        
        // Specific Mode Lists (Wall...)
        for (Collection<? extends Square> modeList : model.getSpecificModeLists()) {
            addSquareColorListView(modeList);
        }
        
        // Food
        addSquareColorListView(model.getFood());
        
        // Snake Head
        addSquareColorView(model.getSnake().getHead());
        
        // Snake Body
        addSquareColorListView(model.getSnake().getBody());
        
        view.getBoardPanel().setSquaresColors(viewSquaresColors);
    }
    
    private void addSquareColorListView(Collection<? extends Square> squaresList) {
        for (Square square : squaresList) {
            addSquareColorView(square);
        }
    }
    
    private void addSquareColorView(Square square) {
        Color color = square.getColor();
        Point position = square;

        viewSquaresColors.computeIfAbsent(color, k -> new ArrayList<>()).add(position);
    }
    
    // Métodos Auxiliares Subclases ClassicGame
    
    private Timer switchSidesTimer;
    
    @Override
    public void onNewTwinGame() {
        initializeSwitchSidesTimer();
    }
    
    @Override
    public void onSwitchSides() {
        inputQueue.clear();
        switchingSidesPause(); // Simular una pausa
    }
    
    private void initializeSwitchSidesTimer() {
        
        switchSidesTimer = new Timer((int) Math.round(timerDelay * 1.5), (ActionEvent e) -> {
            if (!model.isGameEnded()) {
                timer.start();
            }
        });

        // Configurar el Timer para que se ejecute solo una vez
        switchSidesTimer.setRepeats(false);
    }
    
    private void updateSwitchSidesTimerDelay() {
        switchSidesTimer.setInitialDelay((int) Math.round(timerDelay * 1.5));
    }
    
    private void switchingSidesPause() {
        timer.stop();
        switchSidesTimer.start();
    }
}