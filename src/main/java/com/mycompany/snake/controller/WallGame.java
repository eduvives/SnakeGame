/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.controller;

import com.mycompany.snake.model.CellType;
import com.mycompany.snake.model.Square;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Eduard
 */
public class WallGame extends ClassicGame {
    
    protected List<Square> walls = new ArrayList<>();
    private Set<Point> spawnWalls = new HashSet<>();
    protected Set<Point> spawnRadius;
    
    public WallGame(GameLogic game) {
        super(game);        
    }
    
    @Override
    protected boolean checkCollision() {
        
        Point snakeHeadPos = game.snake.getHead().getLocation();
        
        boolean collision = super.checkCollision();
        boolean wallCollision = walls.contains(snakeHeadPos);
        
        return collision || wallCollision;
    }
    
    @Override
    protected void prepareNewGame() {
        
        super.prepareNewGame();

        postPrepareNewGameWallGame();
    }
    
    protected void postPrepareNewGameWallGame() {
        
        game.specificModeLists.add(walls);
        
        walls.clear();
        spawnWalls.clear();
    }
    
    @Override
    protected void eatFood(Point newPos) {
        
        prevEatFoodWallGame(newPos);
        
        super.eatFood(newPos);
    }
    
    protected void prevEatFoodWallGame(Point currentPos) {
        if (game.score % 2 == 0) {
            spawnRadius = getSpawnRadius();
            addWall();
        }
    }
    
    // Métodos Auxiliares

    protected void addWall() {
        
        Point wallPos = getRandomSpawnPosition(game.availablePositions, spawnRadius, spawnWalls);

        if (wallPos != null) {

            // Create New Wall
            walls.add(new Square(wallPos, CellType.WALL_SIMPLE));

            // Update Spawn Walls List
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    spawnWalls.add(new Point(wallPos.x + x, wallPos.y + y));
                }
            }
        }
    }
    
    private Point getRandomSpawnPosition(List<Point> availablePositions, Collection<Point>... excludedLists) {
        // Crear un conjunto para almacenar los puntos presentes en otras listas
        Set<Point> excludedPoints = new HashSet<>();
        for (Collection<Point> list : excludedLists) {
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