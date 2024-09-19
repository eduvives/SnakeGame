/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.controller;

import com.mycompany.snake.model.GameModel;
import com.mycompany.snake.model.ModelObserver;
import com.mycompany.snake.model.SettingsParams;
import com.mycompany.snake.model.Square.ColorPaletteManager;
import com.mycompany.snake.model.Square.Square;
import com.mycompany.snake.view.SnakeView;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
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
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 *
 * @author Eduard
 */
public class GameController implements ModelObserver {
    
    private List<Square> testList = new ArrayList<>(); // Test Line
    
    private SnakeView view;
    private Map<Color, List<Point>> viewSquaresColors = new LinkedHashMap<>();
    
    private GameModel model;
    
    // View
    private int boardWidth;
    private int boardHeight;
    private int squareSize;
    private boolean isBoardUpdated;
    
    // Game Logic
    private Timer timer;
    private List<Point> inputQueue = new ArrayList<>();
    private static final int MAX_INPUT_QUEUE_SIZE = 2;
    
    public GameController(SnakeView view, GameModel model) {
        
        this.view = view;
        this.model = model;
        
        setSettingsComboBoxesModels();
        setBlenderModeListModel();
        initializeGameTimer();
        initializeSwitchSidesTimer();
        initializeGameParams();
        updateViewBoardParams();
        initializeGamePaletteColor();
        
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
    
    private void refreshBoard() {
        updateView();
        view.getBoardPanel().repaint();
    }
    
    @Override
    public void onNewGame() {

        if (!isBoardUpdated) {
            updateViewBoardParams();
        }
        clearUserInputs();
        toggleKeyBindings(true);
    }
    
    private void clearUserInputs() {
        inputQueue.clear();
    }
    
    @Override
    public void onScoreChanged() {
        updateScoreView();
    }
    
    @Override
    public void onHighScoreInitialized() {
        initializeHighScoreView();
    }
    
    @Override
    public void onHighScoreChanged() {
        updateHighScoreView();
    }
    
    @Override
    public void onGameEnded(boolean isFeast) {
        endGameLoop(isFeast);
    }
    
    // Métodos View
    
    private void initializeGameParams() {
        updateGameParams(
            SettingsParams.getBoardNames()[SettingsParams.DEFAULT_SELECTED_INDEX],
            SettingsParams.getSpeedNames()[SettingsParams.DEFAULT_SELECTED_INDEX],
            SettingsParams.getFoodNames()[SettingsParams.DEFAULT_SELECTED_INDEX],
            SettingsParams.MODE_NAMES[SettingsParams.DEFAULT_SELECTED_INDEX]
        );
    }
    
    private void updateGameParams(String board, String speed, String food, String mode) {
        
        // Update Board Size
        if (!Objects.equals(model.getBoardName(), board)) {
            
            int [] boardSize = SettingsParams.BOARDS.get(board);
            
            boardWidth = boardSize[0];
            boardHeight = boardSize[1];
            squareSize = boardSize[2];
            isBoardUpdated = false;
            
            int numBoardCols = boardWidth / squareSize;
            int numBoardRows = boardHeight / squareSize;
        
            model.updateBoardParams(board, numBoardCols, numBoardRows);
        }
        
        // Update Num Food
        if (!Objects.equals(model.getFoodName(), food)) {
            model.updateFoodParam(food);
        }
        
        // Update Blender Selected Modes
        checkBlenderSelectedModes(mode);
        
        // Update Game Timer Delay
        if (!Objects.equals(model.getSpeedName(), speed)) {
            
            updateGameTimerDelay(SettingsParams.SPEEDS.get(speed));
            model.updateSpeedParam(speed);
            
            // Update Switch Sides Timer Delay (Initial Delay)
            if (mode.equals("Twin") || (mode.equals("Blender") && model.getBlenderSelectedModes().contains("Twin"))) {
                int newSwitchSidesDelay = (int) Math.round(SettingsParams.SPEEDS.get(speed) * 2.5);
                updateSwitchSidesTimerDelay(newSwitchSidesDelay);
            }
        }
        
        // Update Game Mode
        if (!Objects.equals(model.getModeName(), mode)) {
            model.updateGameMode(mode);
        }
    }
    
    private void checkBlenderSelectedModes(String selectedMode) {
        if (selectedMode.equals("Blender")) {
            List<String> newBlenderSelectedModes = view.getBlenderSettings().getModeListSelectedValues();
            if (!Objects.equals(model.getBlenderSelectedModes(), newBlenderSelectedModes)) {
                model.updateBlenderSelectedModes(newBlenderSelectedModes);
            }
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
    
    private void initializeGamePaletteColor() {
        boardColorChangedView(ColorPaletteManager.BOARD_COLOR_PALETTE[ColorPaletteManager.DEFAULT_SELECTED_INDEX]);
        foodColorChangedView(ColorPaletteManager.FOOD_COLOR_PALETTE[ColorPaletteManager.DEFAULT_SELECTED_INDEX]);
        snakeColorChangedView(ColorPaletteManager.SNAKE_COLOR_PALETTE[ColorPaletteManager.DEFAULT_SELECTED_INDEX]);
    }
    
    private void setSettingsComboBoxesModels() {
        view.getSettings().setBoardCmbModel(SettingsParams.getBoardNames(), SettingsParams.DEFAULT_SELECTED_INDEX);
        view.getSettings().setSpeedCmbModel(SettingsParams.getSpeedNames(), SettingsParams.DEFAULT_SELECTED_INDEX);
        view.getSettings().setFoodCmbModel(SettingsParams.getFoodNames(), SettingsParams.DEFAULT_SELECTED_INDEX);
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
            
            String modeName = view.getSettings().getModeCmbSelectedItem();
            
            checkBlenderSelectedModes(modeName);
                
            view.getMenu().setHighScoreLabel(model.getCachedHighScore(
                view.getSettings().getBoardCmbSelectedItem(),
                view.getSettings().getSpeedCmbSelectedItem(),
                view.getSettings().getFoodCmbSelectedItem(),
                modeName)
            );
            
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
        
        view.setBoardColorBtnListener(e -> {
            view.colorSelection(ColorPaletteManager.BOARD_COLOR_PALETTE, "Board Color Selection", colorChangedListener -> {
                boardColorChangedView(view.getColorSelectionPanel().getSelectedColor());
                refreshBoard();
                view.getContentPane().repaint();
            });
        });
        
        view.setFoodColorBtnListener(e -> {
            view.colorSelection(ColorPaletteManager.FOOD_COLOR_PALETTE, "Food Color Selection", colorChangedListener -> {
                foodColorChangedView(view.getColorSelectionPanel().getSelectedColor());
                refreshBoard();
            });
        });
        
        view.setSnakeColorBtnListener(e -> {
            view.colorSelection(ColorPaletteManager.SNAKE_COLOR_PALETTE, "Snake Color Selection", colorChangedListener -> {
                snakeColorChangedView(view.getColorSelectionPanel().getSelectedColor());
                refreshBoard();
            });
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
    
    private void boardColorChangedView(Color newColor) {
        model.boardColorChanged(newColor);
        //view.setBoardColorBtnForeground(newColor); TODO poner?
        
        view.setBackgroundColor(newColor);
    }
    
    private void foodColorChangedView(Color newColor) {
        model.foodColorChanged(newColor);
        //view.setFoodColorBtnForeground(newColor);
        
        view.setScoreColor(newColor);
    }
    
    private void snakeColorChangedView(Color newColor) {
        model.snakeColorChanged(newColor);
        //view.setSnakeColorBtnForeground(newColor);
    }
    
    private void updateGameParamsFromView() {

        updateGameParams(
            view.getSettings().getBoardCmbSelectedItem(),
            view.getSettings().getSpeedCmbSelectedItem(),
            view.getSettings().getFoodCmbSelectedItem(),
            view.getSettings().getModeCmbSelectedItem()
        );
    }
    
    public void toggleKeyBindings(boolean enable) {
        
        InputMap inputMap = view.getBoardPanel().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

        if (enable) {
            // Activar los key bindings
            inputMap.put(KeyStroke.getKeyStroke("UP"), "moveUp");
            inputMap.put(KeyStroke.getKeyStroke("W"), "moveUp");
            inputMap.put(KeyStroke.getKeyStroke("DOWN"), "moveDown");
            inputMap.put(KeyStroke.getKeyStroke("S"), "moveDown");
            inputMap.put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
            inputMap.put(KeyStroke.getKeyStroke("A"), "moveLeft");
            inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");
            inputMap.put(KeyStroke.getKeyStroke("D"), "moveRight");
            
            inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), "endGame");

            inputMap.put(KeyStroke.getKeyStroke("SPACE"), "pauseGame"); // Test Line
            
        } else {
            // Desactivar los key bindings
            inputMap.clear();
        }
    }
    
    private void configureKeyBindings() {
        
        ActionMap actionMap = view.getBoardPanel().getActionMap();
        
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
        
        actionMap.put("endGame", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (model.isGameActive()) {
                    endGameLoop(false);
                }
            }
        });
        
        // Test Lines Start
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
        timer = new Timer(0, gameLoopListener); // initialDelay se establece también a zero (No haya retraso inicial)
    }
    
    private ActionListener getGameLoopListener() {
        
        return (ActionEvent e) -> {
            
            if (!inputQueue.isEmpty()) {
                model.getSnake().getDirection().setLocation(inputQueue.remove(0));
            }
            
            model.nextLoop();
        };
    }
    
    private void updateGameTimerDelay(int newDelay) {
        timer.setDelay(newDelay);
    }
    
    public void showGamePreview() {
        
        model.initializeCurrentGameHighScore();
        model.initializeSnake();
        
        refreshBoard();
        
        this.view.setVisible(true);
    }
    
    public void openMenu() {
        view.getMenu().setScoreLabel(model.getScore());
        view.getMenu().setHighScoreLabel(model.getCurrentGameHighScore());
        view.openMenu();
    }
    
    private void updateScoreView() {
        view.setCurrentScore(model.getScore());
    }
    
    private void initializeHighScoreView() {
        
        view.getHighScorePanel().setVisible(model.getCurrentGameHighScore() > 0);
        
        updateHighScoreView();
    }
    
    private void updateHighScoreView() {
        if (view.getHighScorePanel().isVisible()) {
            view.setCurrentHighScore(model.getCurrentGameHighScore());
        }
    }
        
    private void startGameLoop() {
        view.toggleStyleButtons(false);
        timer.start();
        model.startGame();
    }
    
    private void endGameLoop(boolean isFeast) {
        
        System.out.println("Feast: " + isFeast);
        
        toggleKeyBindings(false);
        view.toggleStyleButtons(true);
        
        timer.stop();
        model.gameEnd();
        
        SwingUtilities.invokeLater(() -> openMenu());
    }
    
    private void updateView(){
        
        viewSquaresColors.clear();
        
        // Snake Body
        addSquareColorListView(model.getSnake().getBody());
        
        // Snake Head
        addSquareColorView(model.getSnake().getHead());
        
        // Food
        addSquareColorListView(model.getFood());
        
        // Specific Mode Lists (Wall...)
        for (Collection<? extends Square> modeList : model.getSpecificModeLists()) {
            addSquareColorListView(modeList);
        }
        
        // Test Lines Start 2
        
        /*
        List<Square> candidates_test = new ArrayList<>();
        for (Point pos : model.getAvailablePositions()) {
            candidates_test.add(new Square(pos, CellType.TEST));
        }
        testList.clear();
        testList.addAll(candidates_test);
        */
        
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
    public void onSwitchSides() {
        inputQueue.clear();
        switchingSidesPause(); // Simular una pausa
    }
    
    private void initializeSwitchSidesTimer() {
        
        switchSidesTimer = new Timer(0, (ActionEvent e) -> {
            if (model.isGameActive()) { // TODO || !isGamePaused?
                timer.start();
            }
        });

        // Configurar el Timer para que se ejecute solo una vez
        switchSidesTimer.setRepeats(false);
    }
    
    private void updateSwitchSidesTimerDelay(int newDelay) {
        switchSidesTimer.setInitialDelay(newDelay);
    }
    
    private void switchingSidesPause() {
        timer.stop();
        switchSidesTimer.start();
    }
}