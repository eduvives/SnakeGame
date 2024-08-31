/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.controller;

import java.awt.Color;
import java.awt.Point;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Eduard
 */
public class WallGame extends ClassicGame {
    
    private List<Point> walls = new ArrayList<>();
    private List<Point> spawnWalls = new ArrayList<>();
    private List<Point> spawnRadius = new ArrayList<>();
    
    public static final int SPAWN_RADIUS_WIDTH = 7;
    public static final Color WALL_COLOR = Color.GRAY;    
    
    public WallGame(GameLogic game) {
        super(game);        
    }
    
    @Override
    protected boolean checkCollision(Point pos) {
        
        boolean collision = super.checkCollision(pos);
        boolean wallCollision = walls.contains(pos);
        
        return collision || wallCollision;
    }
    
    @Override
    protected void prepareNewGame() {
        
        super.prepareNewGame();

        postPrepareNewGameWallGame();
    }
    
    protected void postPrepareNewGameWallGame() {
        game.specificModeLists.add(new AbstractMap.SimpleEntry<>(WALL_COLOR, walls));
        
        spawnRadius.clear();
        generateSpawnRadius(game.startPos);

        walls.clear();
        spawnWalls.clear();
    }

    @Override
    protected void snakeMove(Point currentDirection) {
        
        prevSnakeMoveWallGame(currentDirection);
        
        super.snakeMove(currentDirection);
    }
    
    protected void prevSnakeMoveWallGame(Point currentDirection) {
        moveSpawnRadius(currentDirection);
    }
    
    @Override
    protected void eatFood(Point newPos) {
        
        prevEatFoodWallGame();
        
        super.eatFood(newPos);
    }
    
    protected void prevEatFoodWallGame() {
        if (game.score % 2 == 0) addWall();
    }
    
    // Métodos Auxiliares
    
    private void generateSpawnRadius(Point startPos) {
        int size = (SPAWN_RADIUS_WIDTH - 1) / 2;

        for (int x = -size; x <= size; x++) {
            int yLimit = size - Math.abs(x);

            for (int y = -yLimit; y <= yLimit; y++) {
                spawnRadius.add(new Point(startPos.x + x, startPos.y + y));
            }
        }
    }
    
    private void moveSpawnRadius(Point currentDirection) {
        
        for(Point pos : spawnRadius){
            pos.setLocation(pos.x + currentDirection.x, pos.y + currentDirection.y);
        }
    }
    
    private void addWall() {
        Point wallPos = getRandomSpawnPosition(game.availablePositions, spawnRadius, spawnWalls);

        if (wallPos != null) {

            walls.add(wallPos);

            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    spawnWalls.add(new Point(wallPos.x + x, wallPos.y + y));
                }
            }
        }
    }
    
    private Point getRandomSpawnPosition(List<Point> availablePositions, List<Point>... excludedLists) {
        // Crear un conjunto para almacenar los puntos presentes en otras listas
        Set<Point> excludedPoints = new HashSet<>();
        for (List<Point> list : excludedLists) {
            excludedPoints.addAll(list);
        }

        // Filtrar la lista de puntos para excluir los que ya están en otras listas
        List<Point> candidates = new ArrayList<>();
        for (Point p : availablePositions) {
            if (!excludedPoints.contains(p)) {
                candidates.add(p);
            }
        }

        // Si no hay puntos disponibles, regresar null
        if (candidates.isEmpty()) {
            return null;
        }

        // Seleccionar un punto aleatorio de los candidatos disponibles
        Random rand = new Random();
        
        Point candidate = candidates.get(rand.nextInt(candidates.size()));
        availablePositions.remove(candidate);
        
        return candidate;
    }    
}