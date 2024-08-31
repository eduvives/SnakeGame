/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model;

import java.awt.Point;
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
    
    private void postInitializeBody() {
        if (cheeseSnake != null) {
            cheeseSnake.initializeBody();
        } else {
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
            twinSnake.postMoveTwinSnake(newPos, isFood); // TODO revisar que funcione
        }
    }
}
