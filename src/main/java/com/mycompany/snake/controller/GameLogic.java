/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.controller;

import com.mycompany.snake.model.Board;
import com.mycompany.snake.model.Snake;
import com.mycompany.snake.view.SnakeView;
import com.sun.java.accessibility.util.AWTEventMonitor;
import java.awt.Color;
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
    private int SNAKE_START_LENGTH = 5;
    private Color SNAKE_HEAD_COLOR = new Color(0,128,0);
    private int numBoardRows;
    private int numBardCols;
    private int numFood = 3;
    private List<Point> availablePositions = new ArrayList<>();
    private List<Point> food = new ArrayList<>();
    
    private Timer timer;
    private int TIMER_DELAY = 100;
    private Point direction;
    private Queue<Point> inputQueue = new LinkedList<>();
    
    boolean gameStarted = false;
    boolean gameOver = false;
    
    int score;
    
    public GameLogic(SnakeView view) {
        this.view = view;
        numBoardRows = view.getBoardHeight() / view.getPanel().getSquareSize();
        numBardCols = view.getBoardWidth()/ view.getPanel().getSquareSize();
        startPos = new Point(SNAKE_START_LENGTH + 2, numBoardRows / 2);
        direction = new Point(1, 0);
        configureKeyBindings();
    }
    
    private void configureKeyBindings() {
        InputMap inputMap = view.getPanel().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = view.getPanel().getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("UP"), "moveUp");
        inputMap.put(KeyStroke.getKeyStroke("DOWN"), "moveDown");
        inputMap.put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
        inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");

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
    
    public void newGame() {
        
        availablePositions.clear();
        score = 0;
        
        for (int i = 0; i < numBoardRows; i++) {
            final int row = i;
            for (int j = 0; j < numBardCols; j++) {
                availablePositions.add(new Point(j,i));
            }
        }      
        
        snake = new Snake(startPos, SNAKE_START_LENGTH);
        
        for (Point bodyPart : snake.getBody()) {
            availablePositions.remove(bodyPart);
        }
        availablePositions.remove(snake.getHead());
        
        placeFood();
        updateView();        
    }

    public void startGame() {
        
        ActionListener gameListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Point currentDirection = inputQueue.isEmpty() ? direction : inputQueue.poll();
                Point newPos = new Point(snake.getHead().x + currentDirection.x, snake.getHead().y + currentDirection.y);
                boolean isFood = checkFood(newPos);
                move(newPos, isFood);
                gameOver = checkCollision(newPos);
                       
                if (!gameOver) {
                    updateView();
                    view.getPanel().actionPerformed(e);
                } else {
                    gameOver();
                }
            }
        };
        
        timer = new Timer(TIMER_DELAY, gameListener);
        timer.start();
        gameStarted = true;
    }
    
    public void move(Point newPos, boolean isFood){
        snake.getBody().addFirst(new Point(snake.getHead().x, snake.getHead().y));        
        snake.getHead().x = newPos.x;
        snake.getHead().y = newPos.y;
                
        if (isFood) {
            score += 1;
            food.remove(newPos);                        
        } else {
            availablePositions.add(snake.getBody().removeLast());
            availablePositions.remove(snake.getHead());
        }
        
        placeFood();        
    }
    
    private void gameOver() {
        gameOver = true;
        timer.stop();        
    }
    
    private boolean checkCollision(Point pos) {
        
        boolean bodyCollision = snake.getBody().contains(pos);
        boolean boundariesCollision = pos.x < 0 || pos.x >= numBardCols || pos.y < 0 || pos.y >= numBoardRows;

        return bodyCollision || boundariesCollision;
    }
    
    private boolean checkFood(Point newPos) {        
        return food.contains(newPos);
    }
    
    private void updateView(){
        Map<Color, List<Point>> squaresColors = new HashMap<>();
        
        squaresColors.put(SNAKE_HEAD_COLOR, new ArrayList<>());
        
        squaresColors.get(SNAKE_HEAD_COLOR).add(new Point(snake.getHead().x, snake.getHead().y));
        
        squaresColors.put(Color.GREEN, new ArrayList<>());
        
        for (Point snakePos : snake.getBody()) {
            squaresColors.get(Color.GREEN).add(new Point(snakePos.x, snakePos.y));
        }
        
        // Food
        squaresColors.put(Color.RED, new ArrayList<>());
        
        for (Point foodPos : food) {
            squaresColors.get(Color.RED).add(foodPos);
        }
        
        view.getPanel().setSquaresColors(squaresColors);
    }
    
    private void placeFood() {                
        int numPlacedFood = food.size();
        for (int i = 0; i < numFood - numPlacedFood; i++) {
            food.add(getRandomAvailablePosition());
        }
    }

    private Point getRandomAvailablePosition() {
        
        Random rand = new Random();   
        
        int index = rand.nextInt(availablePositions.size());
        return availablePositions.remove(index);

    }
}
