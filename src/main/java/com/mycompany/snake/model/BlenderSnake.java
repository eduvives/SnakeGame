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

    protected Square createSnakeBodyPart(int col, int row) {
        
        if (modes.contains("Dimension")) {
            return new DimensionSquare(col, row, CellType.SNAKE_BODY, false);
        } else {
            return new Square(col, row, CellType.SNAKE_BODY);
        }   
    }

    protected Square createSnakeBodyPart(Point pos) {
        
        if (modes.contains("Dimension")) {
            return new DimensionSquare(pos, CellType.SNAKE_BODY, false);
        } else {
            return new Square(pos, CellType.SNAKE_BODY);
        }   
    }
    
    // CheeseSnake - DimensionSnake - TwinSnake
    
    @Override
    public void initializeBody() {
        
        if (modes.contains("Cheese")) {
            initializeBodyCheeseDimension();
        } else {
            initializeBodyBlender();
        }
    }
    
    private void initializeBodyCheeseDimension() {
        
        for (int i = 1; i <= CheeseSnake.CHEESE_START_LENGTH - 1; i++) {
            
            int posX = cheeseSnake.head.x - (i * 2);
            
            cheeseSnake.emptyBody.addLast(new Square(posX + 1, cheeseSnake.head.y, CellType.EMPTY));
            cheeseSnake.body.addLast(createSnakeBodyPart(posX, cheeseSnake.head.y));
        }
        
        cheeseSnake.nextBodyPartSnake = true;
    }
    
    private void initializeBodyBlender() {
        for (int i = 1; i <= START_LENGTH - 1; i++) {
            body.addLast(createSnakeBodyPart(head.x - i, head.y));
        }
    }
    
    @Override
    public void move(Point newPos, boolean isFoodCollision) {
        
        if (modes.contains("Cheese")) {
            moveCheeseDimension(newPos, isFoodCollision);
        } else {
            moveBlender(newPos, isFoodCollision);
        }
        
        if (isFoodCollision) {
            if (modes.contains("Twin")) {
                if (modes.contains("Cheese")) {
                    switchSidesTwinCheeseDimension(newPos);
                } else {
                    switchSidesBlender(newPos); // TODO revisar que funcione
                    restoreDirectionBlender(head, body.getFirst());
                }
            }
        }
    }
    
    private void moveCheeseDimension(Point newPos, boolean grow) {
        
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
    
    private void moveBlender(Point newPos, boolean grow) {
        if(!grow) body.removeLast();
        
        body.addFirst(createSnakeBodyPart(head));
        head.setLocation(newPos);
    }
    
    private void switchSidesTwinCheeseDimension(Point newPos) {

        LinkedList<Square> snakeBody = cheeseSnake.getBody();
        Point snakeHead = cheeseSnake.getHead();
        LinkedList<Square> emptyBody = cheeseSnake.getEmptyBody();

        boolean isFirstBodyPartSnake = !cheeseSnake.nextBodyPartSnake;
        boolean isLastBodyPartSnake = (!isFirstBodyPartSnake && cheeseSnake.getGrowCount() % 2 == 0) || (isFirstBodyPartSnake && cheeseSnake.getGrowCount() % 2 == 1);

        if (isFirstBodyPartSnake) {
            emptyBody.addFirst(new Square(newPos, CellType.EMPTY));

            // Si el método move de la combinación Twin Cheese Snake genera una celda vacía, 
            // esta debe ser agregada a la lista de posiciones disponibles.
            //game.availablePositions.add(new Point(newPos));

        } else {
            snakeBody.addFirst(createSnakeBodyPart(newPos));
        }
        
        if (isLastBodyPartSnake) {
            snakeHead.setLocation(snakeBody.removeLast());
            restoreDirectionBlender(snakeHead, emptyBody.getLast());
        } else {
            snakeHead.setLocation(emptyBody.removeLast());

            // Si el método move de la combinación Twin Cheese Snake elimina una celda vacía,
            // esta debe ser también eliminada de la lista de posiciones disponibles.
            //game.availablePositions.remove(snakeHead);

            restoreDirectionBlender(snakeHead, snakeBody.getLast());
        }

        cheeseSnake.setNextBodyPartSnake(isLastBodyPartSnake);

        Collections.reverse(snakeBody);
        Collections.reverse(emptyBody);
    }
    
    private void switchSidesBlender(Point newPos) {
        
        head.setLocation(body.removeLast());
        body.addFirst(createSnakeBodyPart(newPos));
        
        Collections.reverse(body);
    }
    
    private void restoreDirectionBlender(Point snakeHead, Point snakeFirstBodyPartPos) {
        
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
