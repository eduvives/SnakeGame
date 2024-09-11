/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model;

import java.awt.Point;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Eduard
 */
public class BlenderSnake extends Snake {
    
    private List<String> modes;
    private CheeseSnake cheeseSnake;
    private DimensionSnake dimensionSnake;
    private TwinSnake twinSnake;
    
    public BlenderSnake(List<String> modes) {
        super();
        
        cheeseSnake = new CheeseSnake(this);
        dimensionSnake = new DimensionSnake(this);
        twinSnake = new TwinSnake(this);
        
        setBlenderSnakeModes(modes);
    }
    
    protected void setBlenderSnakeModes(List<String> modes) {
        this.modes = modes;
    }

    public CheeseSnake getCheeseSnake() {
        return cheeseSnake;
    }
    
    public DimensionSnake getDimensionSnake() {
        return dimensionSnake;
    }
    
    public TwinSnake getTwinSnake() {
        return twinSnake;
    }
    
    // BlenderSnake - DimensionSnake
    
    @Override
    protected Square createSnakeBodyPart(int col, int row) {
        
        if (modes.contains("Dimension")) {
            return dimensionSnake.createSnakeBodyPart(col, row);
        } else {
            return super.createSnakeBodyPart(col, row);
        }   
    }
    
    @Override
    protected Square createSnakeBodyPart(Point pos) {
        
        if (modes.contains("Dimension")) {
            return dimensionSnake.createSnakeBodyPart(pos);
        } else {
            return super.createSnakeBodyPart(pos);
        }   
    }
    
    // CheeseSnake - DimensionSnake - TwinSnake
    
    @Override
    public void initializeBody() {
        
        if (modes.contains("Cheese")) {
            initializeBodyCheeseDimensionSnake();
        } else if (modes.contains("Dimension")) {
            dimensionSnake.initializeBody();
        } else {
            super.initializeBody();
        }
    }
    
    private void initializeBodyCheeseDimensionSnake() {
        
        for (int i = 1; i <= CheeseSnake.CHEESE_START_LENGTH - 1; i++) {
            
            int posX = cheeseSnake.head.x - (i * 2);
            
            cheeseSnake.emptyBody.addLast(new Square(posX + 1, cheeseSnake.head.y, CellType.EMPTY));
            cheeseSnake.body.addLast(createSnakeBodyPart(posX, cheeseSnake.head.y));
        }
        
        cheeseSnake.nextBodyPartSnake = true;
    }
    
    @Override
    public void move(Point newPos, boolean isFoodCollision) {
        
        if (modes.contains("Cheese")) {
            moveCheeseDimensionSnake(newPos, isFoodCollision);
        } else if (modes.contains("Dimension")) {
            dimensionSnake.move(newPos, isFoodCollision);
        } else {
            super.move(newPos, isFoodCollision);
        }
        
        if (isFoodCollision) {
            if (modes.contains("Twin")) {
                if (modes.contains("Cheese")) {
                    postSnakeSimpleMoveTwinCheeseDimensionGame(newPos);
                } else {
                    switchSidesBlender(newPos); // TODO revisar que funcione
                    restoreDirectionBlender(head, body.getFirst());
                }
            }
        }
    }
    
    private void moveCheeseDimensionSnake(Point newPos, boolean grow) {
        
        if (grow) cheeseSnake.growCount += 2;
        
        if (cheeseSnake.nextBodyPartSnake) {
            cheeseSnake.body.addFirst(createSnakeBodyPart(cheeseSnake.head));

            if (cheeseSnake.growCount <= 0) {
                cheeseSnake.body.removeLast();
            } 
        } else {
            cheeseSnake.emptyBody.addFirst(new Square(cheeseSnake.head, CellType.EMPTY));
            
            if (cheeseSnake.growCount <= 0) {
                cheeseSnake.emptyBody.removeLast();
            }
        }
        
        if (cheeseSnake.growCount > 0) {
            cheeseSnake.growCount--;
        }
        
        cheeseSnake.head.setLocation(newPos);
        
        cheeseSnake.nextBodyPartSnake = !cheeseSnake.nextBodyPartSnake;
    }
    
    private void postSnakeSimpleMoveTwinCheeseDimensionGame(Point newPos) {

        LinkedList<Square> snakeBody = cheeseSnake.getBody();
        Point snakeHead = cheeseSnake.getHead();
        LinkedList<Square> emptyBody = cheeseSnake.getEmptyBody();

        boolean isFirstBodyPartSnake = !cheeseSnake.isNextBodyPartSnake();
        boolean isLastBodyPartSnake = (!isFirstBodyPartSnake & cheeseSnake.getGrowCount() % 2 == 0) || (isFirstBodyPartSnake & cheeseSnake.getGrowCount() % 2 == 1);

        if (isFirstBodyPartSnake) {
            emptyBody.addFirst(new Square(newPos, CellType.EMPTY));

            // Si el método move de la combinación Twin Cheese Snake genera una celda vacía, 
            // esta debe ser agregada a la lista de posiciones disponibles.
            //game.availablePositions.add(new Point(newPos));

        } else {
            snakeBody.addFirst(createSnakeBodyPart(newPos));
        }

        Collections.reverse(snakeBody);
        Collections.reverse(emptyBody);

        if (isLastBodyPartSnake) {
            snakeHead.setLocation(snakeBody.removeFirst());
            restoreDirectionBlender(snakeHead, emptyBody.getFirst());
        } else {
            snakeHead.setLocation(emptyBody.removeFirst());

            // Si el método move de la combinación Twin Cheese Snake elimina una celda vacía,
            // esta debe ser también eliminada de la lista de posiciones disponibles.
            //game.availablePositions.remove(snakeHead);

            restoreDirectionBlender(snakeHead, snakeBody.getFirst());
        }

        cheeseSnake.setNextBodyPartSnake(isLastBodyPartSnake);
    }
    
    private void switchSidesBlender(Point newPos) {
        
        head.setLocation(body.getLast());

        body.removeLast();
        body.addFirst(createSnakeBodyPart(newPos));
        
        Collections.reverse(body);
    }
    
    private void restoreDirectionBlender(Point snakeHead, Point snakeFirstBodyPartPos) { // TODO eliminar y trasladar a Snake?
        
        Point defaultDirection = getDefaultDirection(snakeHead, snakeFirstBodyPartPos);
        
        if (modes.contains("Boundless")) {
            
            // Ajustar por teletransporte en el eje X
            if (defaultDirection.x > 1) {
                defaultDirection.x = -1;
            } else if (defaultDirection.x < -1) {
                defaultDirection.x = 1;
            }

            // Ajustar por teletransporte en el eje Y
            if (defaultDirection.y > 1) {
                defaultDirection.y = -1;
            } else if (defaultDirection.y < -1) {
                defaultDirection.y = 1;
            }
        }
        
        direction.setLocation(defaultDirection.x, defaultDirection.y);
    }
}
