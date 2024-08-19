/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.snake.view;

import com.mycompany.snake.controller.GameLogic;
import java.awt.Color;

/**
 *
 * @author Eduard
 */
public class SnakeView extends javax.swing.JFrame {

    private GameLogic controller;
    private int boardWidth = 600;
    private int boardHeight = 600;
    
    MenuPanel menu;
    
    /**
     * Creates new form SnakeView
     */
    public SnakeView() {
        initComponents();        
        this.setTitle("Snake Game");
        this.setSize(boardWidth, boardHeight);
        
        this.getContentPane().setBackground(Color.GRAY);
        pack();
        setLocationRelativeTo(null);
        this.setVisible(true);
        boardPanel.requestFocus();
        
        menu = new MenuPanel(this);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        boardPanel = new BoardPanel(boardWidth, boardHeight);
        topMenu = new javax.swing.JPanel();
        currentScorePic = new javax.swing.JPanel();
        currentScore = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        javax.swing.GroupLayout boardPanelLayout = new javax.swing.GroupLayout(boardPanel);
        boardPanel.setLayout(boardPanelLayout);
        boardPanelLayout.setHorizontalGroup(
            boardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 251, Short.MAX_VALUE)
        );
        boardPanelLayout.setVerticalGroup(
            boardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 185, Short.MAX_VALUE)
        );

        topMenu.setBackground(java.awt.Color.darkGray);

        currentScorePic.setBackground(java.awt.Color.red);

        javax.swing.GroupLayout currentScorePicLayout = new javax.swing.GroupLayout(currentScorePic);
        currentScorePic.setLayout(currentScorePicLayout);
        currentScorePicLayout.setHorizontalGroup(
            currentScorePicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );
        currentScorePicLayout.setVerticalGroup(
            currentScorePicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        currentScore.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        currentScore.setForeground(new java.awt.Color(255, 255, 255));
        currentScore.setText("0");

        javax.swing.GroupLayout topMenuLayout = new javax.swing.GroupLayout(topMenu);
        topMenu.setLayout(topMenuLayout);
        topMenuLayout.setHorizontalGroup(
            topMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topMenuLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(currentScorePic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(currentScore)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        topMenuLayout.setVerticalGroup(
            topMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topMenuLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(topMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(currentScorePic, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(currentScore, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(boardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
            .addComponent(topMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(topMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(boardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public void setController(GameLogic controller) {
        this.controller = controller;
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public int getBoardHeight() {
        return boardHeight;
    }
    
    public BoardPanel getPanel() {        
        return (BoardPanel) boardPanel;
    }
    
    public void setCurrentScore(int newScore) {
        currentScore.setText(String.valueOf(newScore));
    }
    
    public void openMenu() {     
        menu.setVisible(true);
    }
    
    public MenuPanel getMenu() {        
        return menu;
    }
    
    public SettingsPanel getSettings() {        
        return menu.getSettings();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel boardPanel;
    private javax.swing.JLabel currentScore;
    private javax.swing.JPanel currentScorePic;
    private javax.swing.JPanel topMenu;
    // End of variables declaration//GEN-END:variables
}
