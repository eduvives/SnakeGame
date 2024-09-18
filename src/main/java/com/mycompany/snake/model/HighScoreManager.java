/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.snake.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Eduard
 */
public class HighScoreManager {
    
    private GameModel gameModel;
    private static final String FILE_NAME = "snake_highscores.txt";
    private Map<String, Integer> cachedHighScores;
    private boolean dataChanged = false;
    
    public HighScoreManager(GameModel gameModel) {
        
        this.gameModel = gameModel;
        
        cachedHighScores = new HashMap<>();
        loadStoredScores();
        
        // Agrega un shutdown hook para guardar los puntajes al cerrar la aplicación
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            saveGameProgress();
        }));
    }
    
    // Genera una clave única para la configuración dada
    private String generateKey(String board, String speed, String food, String mode) {
        
        if (mode.equals("Blender")) {
            String combinedModes = "Blender(" + String.join(",", gameModel.getBlenderSelectedModes()) + ")";
            return board + ":" + speed + ":" + food + ":" + combinedModes;
        } else {
            return board + ":" + speed + ":" + food + ":" + mode;
        }
    }

    // Carga los puntajes desde el archivo al mapa
    private void loadStoredScores() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length >= 5) {
                    // Último elemento es el puntaje, los demás son la clave
                    String key = String.join(":", parts[0], parts[1], parts[2], parts[3]);
                    int score = Integer.parseInt(parts[4]);
                    cachedHighScores.put(key, score);
                }
            }
        } catch (IOException e) {
            System.out.println("No se pudo cargar el archivo de puntajes: " + e.getMessage());
        }
    }

    // Guarda el mapa de puntajes en el archivo
    private void saveScores() {
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Map.Entry<String, Integer> entry : cachedHighScores.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("No se pudo guardar el archivo de puntajes: " + e.getMessage());
        }
    }

    // Devuelve el puntaje más alto para la configuración dada
    protected int getCachedHighScore(String board, String speed, String food, String mode) {
        String key = generateKey(board, speed, food, mode);
        return cachedHighScores.getOrDefault(key, 0); // Si no existe, devuelve 0
    }

    // Actualiza el puntaje más alto
    protected void updateCachedHighScore() {
        String key = generateKey(gameModel.getBoardName(), gameModel.getSpeedName(), gameModel.getFoodName(), gameModel.getModeName());
        cachedHighScores.put(key, gameModel.getCurrentGameHighScore());
        dataChanged = true;
    }
    
    private void saveGameProgress() {
        
        if (gameModel.isGameStarted() && gameModel.isNewHighScore()) {
            // Si la partida no ha terminado y se ha superado el puntaje más alto, actualiza el puntaje
            updateCachedHighScore();
        }
        // Guarda los puntajes en el archivo
        if (dataChanged) {
            saveScores();
            dataChanged = false;
        }
    }
}
