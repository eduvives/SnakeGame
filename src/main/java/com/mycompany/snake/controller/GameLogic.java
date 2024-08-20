/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.controller;

import com.mycompany.snake.model.SettingsParams;
import com.mycompany.snake.model.Snake;
import com.mycompany.snake.view.SnakeView;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
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
    
    private SnakeView view;
    private Point startPos;
    private Snake snake;

    private int boardWidth;
    private int boardHeight;
    private int squareSize;
    private int numBoardRows;
    private int numBoardCols;
    private int numFood;
    private List<Point> availablePositions = new ArrayList<>();
    private List<Point> food = new ArrayList<>();
    private boolean isBoardUpdated;
    
    private List<Point> spawnRadius = new ArrayList<>();
    
    private Timer timer;
    private int timerDelay;
    private Point direction = new Point();
    private Queue<Point> inputQueue = new LinkedList<>();
    
    boolean gameStarted;
    
    int score;
    
    public GameLogic(SnakeView view) {
        this.view = view;
        setGameParams(
            SettingsParams.BOARD_VALUES[SettingsParams.DEFAULT_SELECTED_INDEX][0],
            SettingsParams.BOARD_VALUES[SettingsParams.DEFAULT_SELECTED_INDEX][1],
            SettingsParams.BOARD_VALUES[SettingsParams.DEFAULT_SELECTED_INDEX][2],
            SettingsParams.SPEED_VALUES[SettingsParams.DEFAULT_SELECTED_INDEX],
            SettingsParams.FOOD_VALUES[SettingsParams.DEFAULT_SELECTED_INDEX]
        );
        updateBoardParams();
        setSettingsComboBoxesModels();
        setViewListeners();
        configureKeyBindings();
        this.view.setVisible(true);
    }        
    
    private void setGameParams(int boardWidth, int boardHeight, int squareSize, int delay, int numFood) {
        
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
    }
    
    private void updateBoardParams() {
        view.getBoardPanel().setBoardWidth(boardWidth);
        view.getBoardPanel().setBoardHeight(boardHeight);
        view.getBoardPanel().setSquareSize(squareSize);
        view.getBoardPanel().setPreferredSize(new Dimension(boardWidth, boardHeight));   
        
        view.getBoardPanel().revalidate();
        view.getBoardPanel().repaint();
        view.pack();
        
        isBoardUpdated = true;
    }
    
    private void setSettingsComboBoxesModels() {
        view.getSettings().setBoardCmbModel(SettingsParams.BOARD_NAMES, SettingsParams.DEFAULT_SELECTED_INDEX);
        view.getSettings().setSpeedCmbModel(SettingsParams.SPEED_NAMES, SettingsParams.DEFAULT_SELECTED_INDEX);
        view.getSettings().setFoodCmbModel(SettingsParams.FOOD_NAMES, SettingsParams.DEFAULT_SELECTED_INDEX);
        view.getSettings().setEffectCmbModel(SettingsParams.EFFECT_NAMES, SettingsParams.DEFAULT_SELECTED_INDEX);
    }
        
    private void setViewListeners() {
        
        view.getMenu().setPlayButtonListener(e -> {
            
            newGame();
            view.getMenu().dispose();
        });
        
        view.getSettings().setPlaySettingsListener(e -> {
            
            int[] boardValues = SettingsParams.BOARD_VALUES[view.getSettings().getBoardCmbSelectedIndex()];
            
            setGameParams(
                boardValues[0],
                boardValues[1],
                boardValues[2],
                SettingsParams.SPEED_VALUES[view.getSettings().getSpeedCmbSelectedIndex()],
                SettingsParams.FOOD_VALUES[view.getSettings().getFoodCmbSelectedIndex()]
            );
            
            newGame();
            view.getSettings().dispose();
        });
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
                if (direction.y != 1 && inputQueue.size() < 2) {
                    if (!gameStarted) {
                        startGame();
                    }
                    direction.setLocation(0, -1);
                    inputQueue.add(new Point(direction));
                }
            }
        });

        actionMap.put("moveDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (direction.y != -1 && inputQueue.size() < 2) {
                    if (!gameStarted) {
                        startGame();
                    }
                    direction.setLocation(0, 1);
                    inputQueue.add(new Point(direction));
                }                
            }
        });

        actionMap.put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (direction.x != 1 && inputQueue.size() < 2) {
                    if (!gameStarted) {
                        startGame();
                    }
                    direction.setLocation(-1, 0);
                    inputQueue.add(new Point(direction));
                }                
            }
        });

        actionMap.put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (direction.x != -1 && inputQueue.size() < 2) {
                    if (!gameStarted) {
                        startGame();
                    }
                    direction.setLocation(1, 0);
                    inputQueue.add(new Point(direction));
                }
            }
        });
    }
    
    public void openMenu() {
        view.getMenu().setScoreLabel(score);
        view.openMenu();
    }
    
    public void showGameBoard() {
        startPos = new Point(Snake.START_LENGTH + 1, numBoardRows / 2);
        snake = new Snake(new Point(startPos), Snake.START_LENGTH);
        
        updateView();
        view.getBoardPanel().repaint();
    }
    
    private void newGame() {
        
        if (!isBoardUpdated) {
            updateBoardParams();
            startPos = new Point(Snake.START_LENGTH + 1, numBoardRows / 2);
        }
        
        gameStarted = false;
        score = 0;
        updateScore();
        
        availablePositions.clear();
        food.clear();
        inputQueue.clear();
        direction.setLocation(1, 0);

        for (int i = 0; i < numBoardRows; i++) {
            for (int j = 0; j < numBoardCols; j++) {
                availablePositions.add(new Point(j,i));
            }
        }      
        
        snake = new Snake(new Point(startPos), Snake.START_LENGTH);
        
        generateSpawnRadius(new Point(startPos));
        
        for (Point bodyPart : snake.getBody()) {
            availablePositions.remove(bodyPart);
        }
        availablePositions.remove(snake.getHead());
        
        placeFood();
        updateView();
        view.getBoardPanel().repaint();
    }

    private void startGame() {
        
        ActionListener gameListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Point currentDirection = inputQueue.isEmpty() ? direction : inputQueue.poll();
                Point newPos = new Point(snake.getHead().x + currentDirection.x, snake.getHead().y + currentDirection.y);
                boolean isFood = checkFood(newPos);
                move(newPos, isFood);
                
                boolean isFeast = checkFeast();
                boolean isCollision = checkCollision(newPos);
                
                if (!isCollision) {
                    updateView();
                    view.getBoardPanel().actionPerformed(e);
                } else {
                    gameEnd(isFeast);
                }
            }
        };
        
        timer = new Timer(timerDelay, gameListener);
        timer.start();
        gameStarted = true;
    }
    
    private void move(Point newPos, boolean isFood){
        snake.getBody().addFirst(new Point(snake.getHead().x, snake.getHead().y));        
        snake.getHead().x = newPos.x;
        snake.getHead().y = newPos.y;
                
        if (isFood) {
            score += 1;
            updateScore();
            food.remove(newPos);
        } else {
            availablePositions.add(snake.getBody().removeLast());
            availablePositions.remove(snake.getHead());
        }
        
        placeFood();        
    }
    
    private void gameEnd(boolean isFeast) {
        timer.stop();
        openMenu();
    }
    
    private boolean checkCollision(Point pos) {
        
        boolean bodyCollision = snake.getBody().contains(pos);
        boolean boundariesCollision = pos.x < 0 || pos.x >= numBoardCols || pos.y < 0 || pos.y >= numBoardRows;

        return bodyCollision || boundariesCollision;
    }
    
    private boolean checkFeast() {
        return score == (numBoardRows * numBoardCols) - Snake.START_LENGTH;
    }
    
    private boolean checkFood(Point newPos) {        
        return food.contains(newPos);
    }
    
    private void updateScore() {
        view.setCurrentScore(score);
    }
    
    private void updateView(){
        Map<Color, List<Point>> squaresColors = new HashMap<>();       
        
        // Snake Body
        squaresColors.put(Snake.BODY_COLOR, new ArrayList<>());
        for (Point snakePos : snake.getBody()) {
            squaresColors.get(Snake.BODY_COLOR).add(new Point(snakePos.x, snakePos.y));
        }
        
        // Snake Head
        squaresColors.put(Snake.HEAD_COLOR, new ArrayList<>());
        squaresColors.get(Snake.HEAD_COLOR).add(new Point(snake.getHead().x, snake.getHead().y));
        
        // Food
        squaresColors.put(Color.RED, new ArrayList<>());
        
        for (Point foodPos : food) {
            squaresColors.get(Color.RED).add(foodPos);
        }
        
        view.getBoardPanel().setSquaresColors(squaresColors);
    }
    
    private void placeFood() {
        
        Random rand = new Random(); 
        
        int numPlacedFood = food.size();

        if (numFood == -1) { // Random Food Num
            if (numPlacedFood == 0) {
                int randNumFood = rand.nextInt(6) + 1;

                for (int i = 0; i < randNumFood; i++) {
                    if (!availablePositions.isEmpty()) {
                        food.add(getRandomAvailablePosition());
                    }
                }
            }
        } else {
            for (int i = 0; i < numFood - numPlacedFood; i++) {
                if (!availablePositions.isEmpty()) {
                    food.add(getRandomAvailablePosition());
                }
            }
        }
    }

    private Point getRandomAvailablePosition() {
        
        Random rand = new Random();   
        
        int index = rand.nextInt(availablePositions.size());
        return availablePositions.remove(index);

    }
    
    private void generateSpawnRadius(Point startPos) {
        int size = 3;

        for (int x = -size; x <= size; x++) {
            int yLimit = size - Math.abs(x);

            for (int y = -yLimit; y <= yLimit; y++) {
                spawnRadius.add(new Point(startPos.x + x, startPos.y + y));
            }
        }
    }
}
