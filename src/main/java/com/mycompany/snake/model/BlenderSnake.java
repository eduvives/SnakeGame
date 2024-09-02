/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model;

import java.awt.Point;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Eduard
 */
public class BlenderSnake extends Snake {
    
    private List<String> modes;
    private CheeseSnake cheeseSnake;
    private TwinSnake twinSnake;
    
    public BlenderSnake(Point startPos, List<String> modes) {
        super(startPos);
        this.modes = modes;
        initializeGameModes(modes);
        postInitializeBody();
    }
    
    private void initializeGameModes(List<String> modes) {
        for (String mode : modes) {
            switch (mode) {
                case "Cheese" -> cheeseSnake = new CheeseSnake(this);
                case "Twin" -> twinSnake = new TwinSnake(this);
            }
        }                
    }
    
    // CheeseSnake
    
    @Override
    public void initializeBody() {
    }
    
    private void postInitializeBody() { // TODO revisar
        if (cheeseSnake == null) {
            super.initializeBody();
        }
    }
    
    // CheeseSnake - TwinSnake
    
    @Override
    public void move(Point newPos, boolean isFood) {
        
        if (cheeseSnake != null) {
            cheeseSnake.move(newPos, isFood);
        } else {
            super.move(newPos, isFood);
        }
        
        if (twinSnake != null) {
            if (cheeseSnake != null) {
                postMoveTwinCheeseSnake(newPos, isFood);
            } else {
                twinSnake.postMoveTwinSnake(newPos, isFood); // TODO revisar que funcione
            }
        }
    }
    
    protected void postMoveTwinCheeseSnake(Point newPos, boolean isFood) {
        if (isFood) {
            
            boolean isFirstBodyPartSnake = !cheeseSnake.isNextBodyPartSnake;
            
            if (isFirstBodyPartSnake) {
                head.setLocation(body.getLast());
                
                body.removeLast();
                body.addFirst(new Square(newPos, CellType.SNAKE_BODY));
            } else {
                head.setLocation(cheeseSnake.emptyBody.getLast());
                
                cheeseSnake.emptyBody.removeLast();
                cheeseSnake.emptyBody.addFirst(new Square(newPos, CellType.EMPTY));
            }
            
            Collections.reverse(body);
            Collections.reverse(cheeseSnake.emptyBody);
            
            if (isFirstBodyPartSnake) {
                direction.setLocation(head.x - cheeseSnake.emptyBody.getFirst().x, head.y - cheeseSnake.emptyBody.getFirst().y);
            } else {
                direction.setLocation(head.x - body.getFirst().x, head.y - body.getFirst().y);
            }
            
            cheeseSnake.isNextBodyPartSnake = !cheeseSnake.isNextBodyPartSnake;
        }
    }
}
