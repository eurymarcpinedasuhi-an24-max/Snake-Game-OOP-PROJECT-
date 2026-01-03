/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GameModes;
import GameObjects.GameMode;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.InputStream;
/**
 *
 * @author eurym
 */


// BUG: GAME OVER ON ANY MODE WHEN PRESSING UP + LEFT SIMULTANEOUSLY WHEN SNAKE IS TRAVELLING RIGHT.
public class GamePanel extends JPanel {

    private static final int MAXX = 20, MAXY = 20, TILE_SIZE = 20;
    private static final int GAME_WIDTH = MAXX * TILE_SIZE  ;  // 400
    private static final int GAME_HEIGHT = MAXY * TILE_SIZE ; // 400
    private static final int PANEL_WIDTH = 800;
    private static final int PANEL_HEIGHT = 600;

    final private GameMode game;
    private int gameMode;
    private int difficulty;
    private GameOverPanel gameOverPanel;
    private boolean gameOver = false;
    public static String reason = "None";

    // Background frame image
    private BufferedImage frameImage;
    private BufferedImage backgroundImage;
    
    private int offsetX;
    private int offsetY;
    private String playerName = "Player";

    // Start game with no name
    public GamePanel(int gameMode, int difficulty) {
        this(gameMode, difficulty, "Player");
    }
    
    // Start game with a name
    public GamePanel(int gameMode, int difficulty, String playerName) { 
        //handles if there's no save file
        if(gameMode < 0 || difficulty < 0){
            this.gameOver = true;
            this.game = null;
        }
        else {
            this.gameMode = gameMode;
            this.difficulty = difficulty;
            this.playerName = playerName;

            // Polymorphize GameMode to each subclasses
            this.game = switch(gameMode) {
                case 0 -> new ClassicGame(difficulty);
                case 1 -> new WallGame(difficulty);
                case 2 -> new PoisonGame(difficulty);
                case 3 -> new ObstacledGame(difficulty);
                default -> new ClassicGame(difficulty);
            };
            game.setPanel(this);

            setPreferredSize(new Dimension(800, 600));
            setBackground(Color.BLACK);
            setFocusable(true);
            setLayout(null); // Allow absolute positioning for overlay

            // Offsets to center the game. Change this if want to resizable feature
            offsetX = (PANEL_WIDTH - GAME_WIDTH) / 2;
            offsetY = (PANEL_HEIGHT - GAME_HEIGHT) / 2;

            loadImages();
            setupKeyBindings();
            game.startGame();
            System.out.println("Game started with Mode: " + gameMode + " Difficulty: " + difficulty + " Player: " + playerName);
        }
    }
    
    // Load game
    public GamePanel(){
        SaveManager.SaveData save;
        save = SaveManager.loadGame();
        
        //if there is no save
        if(save.isEmpty){
            save.mode = -1;
            save.difficulty = -1;
            reason = save.reason;
        }
        
        this(save.mode, save.difficulty, save.name);
        
        if(this.game != null)
            this.game.loadGame(save);        
    }

    // Load images from resources
    private void loadImages() {
         try {
            InputStream frameStream = getClass().getResourceAsStream("/resources/images/gamepanel.png");
            if (frameStream != null) {
                frameImage = ImageIO.read(frameStream);
            }
            InputStream bgStream = getClass().getResourceAsStream("/resources/images/background.png");
            if (bgStream != null) {
                backgroundImage = ImageIO.read(bgStream);
            }
        } catch (IOException e) {
            System.err.println("Error loading frame image");
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, PANEL_WIDTH, PANEL_HEIGHT, null);
        } else {
            g.setColor(new Color(139, 90, 43)); // Fallback brown color
            g.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
        }
        

        // Draw background frame image FIRST (behind everything)
        if (frameImage != null) {
            // Draw frame to cover the entire game area with some padding for the border
            int frameMargin = 85;
            g2d.drawImage(frameImage, 
                offsetX - frameMargin, 
                offsetY - frameMargin - 5, 
                GAME_WIDTH + frameMargin * 2, 
                GAME_HEIGHT + frameMargin * 2, 
                null);
        }
        // Score Counter
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        String scoreText = "Score: " + game.getScore() + " | Press ESC/SPACE to Pause";

        // Shadow for better visibility
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.drawString(scoreText, offsetX -40 + 2, offsetY - 15 + 2);
    
        // Draw main text
        g2d.setColor(new Color(255, 255, 255));
        g2d.drawString(scoreText, offsetX - 40, offsetY - 15);
        // Game Mode and Difficulty Display
        String modeText = switch (gameMode) {
            case 0 -> "Mode: Classic";
            case 1 -> "Mode: Walls";
            case 2 -> "Mode: Poison";
            case 3 -> "Mode: Obstacles";
            default -> "Mode: Classic";
        } + " | Difficulty: " + (difficulty) + " | Player: " + (playerName);
        // Shadow
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.drawString(modeText, offsetX -40 + 2, offsetY - 45 + 2);
        // Main text
        g2d.setColor(new Color(255, 255, 255));
        g2d.drawString(modeText, offsetX - 40, offsetY - 45);
        
        // 1️⃣ Draw map (translated to center)
        for (int y = 0; y < MAXY; y++) {
            for (int x = 0; x < MAXX; x++) {
                if (game.map.valueAtCoord(x, y) == 0) {
                    g.setColor(new Color(144, 238, 144)); // light green
                } else {
                    g.setColor(new Color(0, 100, 0));    // dark green (walls)
                }

                g.fillRect(
                    offsetX + x * TILE_SIZE,
                    offsetY + y * TILE_SIZE,
                    TILE_SIZE,
                    TILE_SIZE
                );
            }
        }
        
        // Draw grid lines
        g.setColor(new Color(0, 100, 0, 80)); // Semi-transparent dark green
        for (int x = 0; x <= MAXX; x++) {
            g.drawLine(
                offsetX + x * TILE_SIZE,
                offsetY,
                offsetX + x * TILE_SIZE,
                offsetY + GAME_HEIGHT
            );
        }
        for (int y = 0; y <= MAXY; y++) {
            g.drawLine(
                offsetX,
                offsetY + y * TILE_SIZE,
                offsetX + GAME_WIDTH,
                offsetY + y * TILE_SIZE
            );
        }

        // 2️⃣ Draw fruit
        Point fruit = game.map.fruitCoord();
        if (fruit != null) {
            g.setColor(Color.RED);
            g.fillRect(
                offsetX + fruit.x * TILE_SIZE + 2,
                offsetY + fruit.y * TILE_SIZE + 2,
                TILE_SIZE - 4,
                TILE_SIZE - 4
            );
        }
        
        if(game.map.isPoison()){
            Point poison = game.map.poisonCoord();
            if (poison != null) {
                g.setColor(Color.MAGENTA);
                g.fillRect(
                    offsetX + poison.x * TILE_SIZE + 2,
                    offsetY + poison.y * TILE_SIZE + 2,
                    TILE_SIZE - 4,
                    TILE_SIZE - 4
                );
            }
        }
        
        // 3️⃣ Draw snake
        Point[] snake = game.map.snake.coordinates();
        if (snake != null) {
            for (int i = 0; i < snake.length; i++) {
                Point p = snake[i];
                if (i == 0) {
                    // Make head darker for distinction
                    g.setColor(new Color(0, 180, 0));
                } else {
                    g.setColor(Color.GREEN);
                }
                g.fillRect(
                    offsetX + p.x * TILE_SIZE,
                    offsetY + p.y * TILE_SIZE,
                    TILE_SIZE,
                    TILE_SIZE
                );
            }
        }
        
        // Draw border around game area
        g2d.setColor(new Color(0, 80, 0));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(offsetX - 1, offsetY - 1, GAME_WIDTH + 2, GAME_HEIGHT + 2);
        
        
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

        // ESC/SPACE key for pause menu
        im.put(KeyStroke.getKeyStroke("pressed ESCAPE"), "escape");
        im.put(KeyStroke.getKeyStroke("pressed SPACE"), "escape");

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

        am.put("escape", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                togglePauseMenu();
            }
        });

        // Ensure panel has focus
        SwingUtilities.invokeLater(() -> requestFocusInWindow());
    }

    private boolean isPaused = false;

    private void togglePauseMenu() {
        if (gameOver) return; // Don't toggle if actual game over
        
        if (isPaused) {
            // Resume game
            if (gameOverPanel != null) {
                remove(gameOverPanel);
                gameOverPanel = null;
            }
            game.gameLoop.start();
            isPaused = false;
        } else {
            // Pause game
            game.gameLoop.stop();
            gameOverPanel = new GameOverPanel(game.getScore(), gameMode, difficulty, playerName, "PAUSED");
            gameOverPanel.panel = this;
            int panelWidth = 300;
            int panelHeight = 200;
            int x = (getWidth() - panelWidth) / 2;
            int y = (getHeight() - panelHeight) / 2;
            
            if (x <= 0) x = (800 - panelWidth) / 2;
            if (y <= 0) y = (600 - panelHeight) / 2;
            
            gameOverPanel.setBounds(x, y, panelWidth, panelHeight);
            add(gameOverPanel);
            isPaused = true;
        }
        revalidate();
        repaint();
    }

    public void showGameOver(int score) {
        if (gameOver) return;
        gameOver = true;
        
        // Create and position the game over panel with player name
        gameOverPanel = new GameOverPanel(score, gameMode, difficulty, playerName);
        int panelWidth = 300;
        int panelHeight = 200;
        int x = (getWidth() - panelWidth) / 2;
        int y = (getHeight() - panelHeight) / 2;
        
        // Fallback
        if (x <= 0) x = (800 - panelWidth) / 2;
        if (y <= 0) y = (600 - panelHeight) / 2;
        
        gameOverPanel.setBounds(x, y, panelWidth, panelHeight);
        add(gameOverPanel);
        revalidate();
        repaint();
    }

    public boolean saveGame(){
        SaveManager.SaveData save = new SaveManager.SaveData(
            gameMode, 
            difficulty, 
            game.getScore(), 
            game.getDir(), 
            game.map.snake.coordinates(),
            game.map.fruitCoord(),
            gameMode != 2? null: game.map.poisonCoord(),
            playerName);
        
        try{
            SaveManager.saveGame(save);
            return true;
        }
        catch(IOException e){
            return false;
        }
    }
    
    public boolean isGameOver(){
        return gameOver;
    }
}
