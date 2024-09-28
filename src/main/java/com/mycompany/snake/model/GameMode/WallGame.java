/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model.GameMode;

import com.mycompany.snake.model.GameModel;
import com.mycompany.snake.model.Square.CellType;
import com.mycompany.snake.model.Square.Square;
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
    protected Set<Point> spawnWalls = new HashSet<>();
    protected Set<Point> spawnRadius;
    
    public WallGame(GameModel game) {
        super(game);        
    }
    
    @Override
    protected boolean checkCollision(Point snakeHeadPos) {
        
        boolean collision = super.checkCollision(snakeHeadPos);
        boolean wallCollision = checkSnakeListCollision(walls, snakeHeadPos);
        
        return collision || wallCollision;
    }
    
    @Override
    public void prepareNewGame() {
        
        super.prepareNewGame();

        postPrepareNewGameWallGame();
    }
    
    protected void postPrepareNewGameWallGame() {
        
        game.getSpecificModeLists().add(walls);
        
        walls.clear();
        spawnWalls.clear();
    }
    
    @Override
    protected void snakeMove(Point newHeadPos, boolean isFoodCollision) {
        
        super.snakeMove(newHeadPos, isFoodCollision);
        
        if (isFoodCollision) placeWall();
    }
    
    protected void placeWall() {
        if (game.getScore() % 2 != 0) {
            spawnRadius = getSpawnRadius();
            createWall();
        }
    }
    
    // Métodos Auxiliares

    protected void createWall() {
        
        Point wallPos = getRandomSpawnPosition(game.getAvailablePositions(), spawnRadius, spawnWalls);

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
    
    protected Point getRandomSpawnPosition(List<Point> availablePositions, Collection<Point>... excludedLists) {
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