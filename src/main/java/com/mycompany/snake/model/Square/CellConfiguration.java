/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model.Square;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Eduard
 */
public class CellConfiguration {
    
    // Tipo general de celda
    public enum CellType {
        SNAKE,
        FOOD,
        WALL,
        STATUE,
        TEST; // TODO TEST DELETE
    }

    // Tipo específico de celda
    public enum SpecificCellType {
        SNAKE_HEAD,
        SNAKE_BODY,
        EMPTY_BODY,
        FOOD,
        WALL,
        FILLED_STATUE,
        STATUE,
        CRACKED_STATUE,
        TEST; // TODO TEST DELETE
    }
    
    // Asocia cada tipo general de celda con sus tipos específicos
    protected static final Map<CellType, Set<SpecificCellType>> specificTypes = Map.of(
        CellType.SNAKE, Set.of(SpecificCellType.SNAKE_HEAD, SpecificCellType.SNAKE_BODY, SpecificCellType.EMPTY_BODY),
        CellType.FOOD, Set.of(SpecificCellType.FOOD),
        CellType.WALL, Set.of(SpecificCellType.WALL),
        CellType.STATUE, Set.of(SpecificCellType.FILLED_STATUE, SpecificCellType.STATUE, SpecificCellType.CRACKED_STATUE),
        CellType.TEST, Set.of(SpecificCellType.TEST) // TODO TEST DELETE
    );
    
    // Asocia cada tipo general de celda con su tipo específico por defecto
    protected static final Map<CellType, SpecificCellType> defaultSpecificTypes = Map.of(
        CellType.SNAKE, SpecificCellType.SNAKE_BODY,
        CellType.FOOD, SpecificCellType.FOOD,
        CellType.WALL, SpecificCellType.WALL,
        CellType.STATUE, SpecificCellType.FILLED_STATUE,
        CellType.TEST, SpecificCellType.TEST // TODO TEST DELETE
    );
    
    // Verifica si un tipo específico de celda es válido, es decir, si el tipo específico de celda pertenece al tipo general de celda indicado
    protected static void validateSpecificCellType(CellType cellType, SpecificCellType newSpecificCellType) {
        
        Set<SpecificCellType> specificCellTypes = specificTypes.get(cellType);
        
        if (specificCellTypes == null || !specificCellTypes.contains(newSpecificCellType)) {
            throw new IllegalArgumentException("El tipo específico " + newSpecificCellType + " no pertenece el tipo de celda " + cellType);
        }
    }
    
    // Asocia cada tipo específico de celda a un color
    protected static final Map<SpecificCellType, Color> specificTypeColors = new HashMap<>();
    
    // Establece el color de un tipo específico de celda
    protected static void setSpecificCellTypeColor(SpecificCellType cellType, Color color) {
        specificTypeColors.put(cellType, color);
    }
}
