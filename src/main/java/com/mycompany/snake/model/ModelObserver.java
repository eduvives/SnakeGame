/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.snake.model;

/**
 *
 * @author Eduard
 */
public interface ModelObserver {
    
    // Global (Classic Game)
    void onViewChanged();
    void onGameEnded(boolean isFeast);
    void onScoreChanged();
    void onHighScoreChanged();
    void onNewGame();
    
    // Specific (Twin Game)
    void onSwitchSides();
}
