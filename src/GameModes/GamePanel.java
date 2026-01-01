/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GameModes;
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author eurym
 */


public class GamePanel extends JPanel {

    private static final int MAXX = 20, MAXY = 20, TILE_SIZE = 20;

    final private ClassicGame game;
    private int gameMode; //TODO implement different game modes
    private int difficulty;
    

    public GamePanel() {
        this(0, 1); // Default constructor for now, default to Classic mode, easy difficulty as default
    }

    public GamePanel(int gameMode, int difficulty) {
        this.gameMode = gameMode;
        this.difficulty = difficulty;
        

        // TODO Gamemodes on 0 = Classic, 1 = Walls, 2 = Objects, 3 = Impostor. All options use Classic 4 now
        this.game = new ClassicGame(1);
        game.setPanel(this);
        
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.BLACK);
        setFocusable(true);
        
        setupKeyBindings();
        game.startGame();
        System.out.println("Game started with Mode: " + gameMode + " Difficulty: " + difficulty);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 1️⃣ Draw map
        for (int y = 0; y < MAXY; y++) {
            for (int x = 0; x < MAXX; x++) {

                if (game.map.valueAtCoord(x, y) == 0) {
                    g.setColor(new Color(144, 238, 144)); // light green
                } else {
                    g.setColor(new Color(0, 100, 0));    // dark green
                }

                g.fillRect(
                        x * TILE_SIZE,
                        y * TILE_SIZE,
                        TILE_SIZE,
                        TILE_SIZE
                );
            }
        }

        // 2️⃣ Draw fruit
        Point fruit = game.map.fruitCoord();
        g.setColor(Color.RED);
        g.fillRect(
                fruit.x * TILE_SIZE,
                fruit.y * TILE_SIZE,
                TILE_SIZE,
                TILE_SIZE
        );
        

        // 3️⃣ Draw snake (overrides map & fruit if overlapping)
        Point[] snake = game.map.snake.coordinates();
        g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(
                    p.x * TILE_SIZE,
                    p.y * TILE_SIZE,
                    TILE_SIZE,
                    TILE_SIZE
            );
        }
    }
    
    private void setupKeyBindings() {
        InputMap im = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        // Arrow keys
        im.put(KeyStroke.getKeyStroke("pressed UP"), "up");
        im.put(KeyStroke.getKeyStroke("pressed DOWN"), "down");
        im.put(KeyStroke.getKeyStroke("pressed LEFT"), "left");
        im.put(KeyStroke.getKeyStroke("pressed RIGHT"), "right");

        // WASD
        im.put(KeyStroke.getKeyStroke("pressed W"), "up");
        im.put(KeyStroke.getKeyStroke("pressed S"), "down");
        im.put(KeyStroke.getKeyStroke("pressed A"), "left");
        im.put(KeyStroke.getKeyStroke("pressed D"), "right");

        am.put("up", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                game.setDirection(Direction.UP);
            }
        });
        am.put("down", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                game.setDirection(Direction.DOWN);
            }
        });
        am.put("left", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                game.setDirection(Direction.LEFT);
            }
        });
        am.put("right", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                game.setDirection(Direction.RIGHT);
            }
        });

        // Ensure panel has focus
        SwingUtilities.invokeLater(() -> requestFocusInWindow());
    }

}
