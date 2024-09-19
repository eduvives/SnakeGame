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
    void onNewGame();
    void onScoreChanged();
    void onHighScoreInitialized();
    void onHighScoreChanged();
    void onGameEnded(boolean isFeast);
    
    // Specific (Twin Game)
    void onSwitchSides();
}
