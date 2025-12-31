/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GameModes;
import javax.swing.*;

/**
 *
 * @author eurym
 */


public class GameLoop extends JFrame {

    private GamePanel gamePanel;

    public GameLoop() {
        initGame();
    }

    private void initGame() {

        gamePanel = new GamePanel();

        setTitle("Snake Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        add(gamePanel);   // BorderLayout.CENTER by default
        pack();           // Uses GamePanel preferred size
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameLoop::new);
    }
}

