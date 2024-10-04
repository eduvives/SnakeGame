/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.controller;

import com.mycompany.snake.model.GameModel;
import com.mycompany.snake.model.ModelObserver;
import com.mycompany.snake.model.SettingsParams;
import com.mycompany.snake.model.Square.CellType;
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
import java.util.LinkedList;
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
    private Map<Color, List<Point>> positionsColorsView = new LinkedHashMap<>();
    
    private GameModel model;
    
    // View
    private int boardWidth;
    private int boardHeight;
    private int squareSize;
    private boolean isBoardUpdated;
    
    // Game Loop Timers
    private Timer timer;
    private Timer switchSidesTimer;
    
    // User Inputs
    private static final int MAX_INPUT_QUEUE_SIZE = 2;
    private LinkedList<Point> inputQueue = new LinkedList<>();
    
    // User Inputs Shrink Mode
    // Atributos para ampliar el procesamiento de inputs del usuario cuando el modo Shrink está activo, 
    // almacenando direcciones que podrían no ser válidas al recibirlas, pero sí tras una colisión.
    // En caso de no estar activo el modo Shrink, podemos saber con total certeza la dirección en la que se dirigirá 
    // la serpiente en todo momento (al finalizar el juego en colisionar) y, por tanto, podemos ahorrarnos 
    // comprobaciones y gestiones innecesarias.
    private boolean shrinkModeActive;
    private LinkedList<Point> reserveInputQueue = new LinkedList<>();
    
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
        System.out.println("shrinkModeActive: "+shrinkModeActive);
        if (!isBoardUpdated) {
            updateViewBoardParams();
        }
        clearUserInputs();
        toggleKeyBindings(true);
    }
    
    private void clearUserInputs() {
        inputQueue.clear();
        if (shrinkModeActive) reserveInputQueue.clear();
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
        
        // Update Game Mode
        if (!Objects.equals(model.getModeName(), mode)) {
            model.updateGameMode(mode);
        }
        
        // Update Blender Selected Modes
        checkBlenderSelectedModes(mode);
        
        // Update Game Timer Delay
        if (!Objects.equals(model.getSpeedName(), speed)) {
            updateGameTimerDelay(SettingsParams.SPEEDS.get(speed));
            model.updateSpeedParam(speed);
        }
        
        // Update Switch Sides Timer Delay (Initial Delay)
        if (model.isModeActive("Twin")) {
            int newSwitchSidesDelay = (int) Math.round(SettingsParams.SPEEDS.get(speed) * 2.5);
            if (switchSidesTimer.getInitialDelay() != newSwitchSidesDelay) {
                updateSwitchSidesTimerDelay(newSwitchSidesDelay);
            }
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
        view.getBoardPanel().setCellSize(squareSize);
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
        
        Color borderColor;
        Color topMenuColor;
        
        if (newColor.equals(Color.BLACK)) {
            borderColor = Color.GRAY;
            topMenuColor = Color.DARK_GRAY;
        } else {
            borderColor = ColorPaletteManager.darkenColor(newColor, ColorPaletteManager.BORDER_FACTOR);
            topMenuColor = ColorPaletteManager.darkenColor(newColor, ColorPaletteManager.TOP_MENU_FACTOR);
        }
        
        view.setBackgroundColor(newColor, borderColor, topMenuColor);
    }
    
    private void foodColorChangedView(Color newColor) {
        model.foodColorChanged(newColor);
        
        view.setScoreColor(newColor);
    }
    
    private void snakeColorChangedView(Color newColor) {
        model.snakeColorChanged(newColor);
    }
    
    private void updateGameParamsFromView() {
        
        updateGameParams(
            view.getSettings().getBoardCmbSelectedItem(),
            view.getSettings().getSpeedCmbSelectedItem(),
            view.getSettings().getFoodCmbSelectedItem(),
            view.getSettings().getModeCmbSelectedItem()
        );
        
        shrinkModeActive = model.isModeActive("Shrink");
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
    
    // El valor de retorno indica si el input era válido y, por tanto, si ha sido añadido a la cola de inputs del usuario
    private boolean handleDirectionChange(Point newDirection) {
        
        if (!model.isGameStarted() && !oppositeDirection(model.getSnake().getDefaultDirection(), newDirection)) {
            startGameLoop();
        }
        
        boolean validInput = checkValidInput(newDirection);
        
        int numInputs = inputQueue.size();
        
        if (validInput) {
            
            if (numInputs < MAX_INPUT_QUEUE_SIZE) {
                inputQueue.addLast(newDirection);
            } else {
                
                int lastInputIndex = MAX_INPUT_QUEUE_SIZE - 1;
                
                if (!Objects.equals(inputQueue.get(lastInputIndex), newDirection)) {
                    inputQueue.set(lastInputIndex, newDirection);
                }
            }
        }
        
        if (shrinkModeActive && (!validInput || numInputs >= 1)) {
            addReserveInput(newDirection);
        }
        
        return validInput;
    }
    
    private boolean checkValidInput (Point newDirection) {
        
        Point previousPointingDirection;
        Point previousHeadingDirection;
        
        if (inputQueue.isEmpty()) {
            // Obtener la dirección en que apunta la serpiente en este momento
            previousPointingDirection = model.getSnake().getDefaultDirection();
            previousHeadingDirection = shrinkModeActive ? model.getSnake().getDirection() : previousPointingDirection;
        } else {
            // Obtener la dirección en que apuntará la serpiente tras el próximo movimiento
            previousPointingDirection = previousHeadingDirection = inputQueue.getFirst();
        }
        
        // Comprobar que la nueva dirección no sea opuesta a la dirección en que apunta la serpiente
        boolean oppositeDirection = oppositeDirection(previousPointingDirection, newDirection);
        
        // Comprobar que la nueva dirección no sea igual a la dirección en que se dirige la serpiente
        boolean sameDirection = sameDirection(previousHeadingDirection, newDirection);
        
        return !oppositeDirection && !sameDirection;
    }
    
    private boolean oppositeDirection(Point previousDirection, Point newDirection) {
        return previousDirection.x == -newDirection.x && previousDirection.y == -newDirection.y;
    }
    
    private boolean sameDirection(Point previousDirection, Point newDirection) {
        return previousDirection.equals(newDirection);
    }
    
    private void addReserveInput(Point newPoint) {
        
        reserveInputQueue.remove(newPoint);
        reserveInputQueue.addLast(newPoint);
    }
    
    @Override
    public void onShrink() {
        revalidateInputQueue();
    }
    
    // La llamada a este método implica que el primer input ha provocado una colisión en modo Shrink.
    // Al permitir un máximo de dos inputs seguidos, debemos comprobar si hay más inputs en cola y, 
    // en tal caso, obtener el último input válido introducido por el usuario de entre los inputs de reserva, 
    // ya que el input de la cola podría no ser válido tras la colisión.
    private synchronized void revalidateInputQueue() { // TODO funciona? // TODO más de un input al llamar revalidateInputQueue?
        
        System.out.println("input  :"+inputQueue);
        System.out.println("shrink :"+reserveInputQueue);
        System.out.println("*****");
        
        inputQueue.clear();
        
        for (int j = reserveInputQueue.size() - 1; j >= 0; j--) {
            Point reserveInput = reserveInputQueue.get(j);
            if (checkValidInput(reserveInput)) {
                inputQueue.addLast(reserveInput);
                break; // Salimos al encontrar el último válido
            }
        }
        
        /*
        int firstInputindex = -1;
        
        // Buscar el primer Point válido
        for (int i = 0; i < reserveInputQueue.size(); i++) {
            Point reserveInput = reserveInputQueue.get(i);
            if (checkValidInput(reserveInput)) {
                inputQueue.addLast(reserveInput);
                firstInputindex = i;
                break; // Salimos cuando encontramos el primer válido
            }
        }
        
        // Si encontramos el primer Point válido, buscamos el último desde el final
        if (firstInputindex != -1) {
            for (int j = reserveInputQueue.size() - 1; j > firstInputindex; j--) {
                Point reserveInput = reserveInputQueue.get(j);
                if (checkValidInput(reserveInput)) {
                    inputQueue.addLast(reserveInput);
                    break; // Salimos al encontrar el último válido
                }
            }
        }
        */
    }
    
    // Métodos Timer
    
    private void initializeGameTimer() {
        ActionListener gameLoopListener = getGameLoopListener();
        timer = new Timer(0, gameLoopListener); // initialDelay se establece también a zero (No haya retraso inicial)
    }
    
    private ActionListener getGameLoopListener() {
        
        return (ActionEvent e) -> {
            
            System.out.println("input  :"+inputQueue);
            System.out.println("shrink :"+reserveInputQueue);
            System.out.println("-----");
            
            if (!inputQueue.isEmpty()) {
                model.getSnake().getDirection().setLocation(inputQueue.pollFirst());
            }
            
            model.nextLoop();
            
            if (shrinkModeActive) reserveInputQueue.clear();
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
        
        view.getHighScorePanel().setVisible(model.getCurrentGameHighScore() > 0 || !Objects.equals(model.getModeName(), "Classic"));
        
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
        
        positionsColorsView.clear();
        
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
        
        view.getBoardPanel().setCellColors(positionsColorsView);
    }
    
    private void addSquareColorListView(Collection<? extends Square> squaresList) {
        for (Square square : squaresList) {
            addSquareColorView(square);
        }
    }
    
    private void addSquareColorView(Square square) {
        Color color = square.getColor();
        Point position = square;

        positionsColorsView.computeIfAbsent(color, k -> new ArrayList<>()).add(position);
    }
    
    // Métodos Auxiliares Subclases ClassicGame
    
    @Override
    public void onSwitchSides() {
        clearUserInputs();
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