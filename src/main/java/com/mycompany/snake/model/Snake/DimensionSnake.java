/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model.Snake;

import com.mycompany.snake.model.Square.DimensionSquare;
import com.mycompany.snake.model.Square.CellConfiguration.CellType;
import com.mycompany.snake.model.Square.CellConfiguration.SpecificCellType;
import com.mycompany.snake.model.Square.Square;
import java.awt.Point;

/**
 *
 * @author Eduard
 */
public class DimensionSnake extends Snake {
    
    public DimensionSnake() {
        super();
    }
    
    public DimensionSnake(Snake snake) {
        super(snake);
    }
    
    @Override
    protected Square createSnakeBodyPart(int col, int row) {
        return new DimensionSquare(col, row, CellType.SNAKE, SpecificCellType.SNAKE_BODY, false);
    }

    @Override
    protected Square createSnakeBodyPart(Point pos) {
        return new DimensionSquare(pos, CellType.SNAKE, SpecificCellType.SNAKE_BODY, false);
    }
    
    @Override
    protected void initializeBody() {
        
        for (int i = 1; i <= START_LENGTH - 1; i++) {
            addLastBody(createSnakeBodyPart(head.x - i, head.y));
        }
    }
    
    @Override
    public void move(Point newHeadPos, boolean grow) {
        
        if(!grow) removeLastBody();
        
        Point previousHeadPos = head.getLocation();
        
        setLocationHead(previousHeadPos, newHeadPos);
        addFirstBody(createSnakeBodyPart(previousHeadPos));
    }
}
