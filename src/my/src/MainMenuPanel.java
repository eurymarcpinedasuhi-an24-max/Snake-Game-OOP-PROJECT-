import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class MainMenuPanel extends JPanel {
    private BufferedImage backgroundImage;
    private BufferedImage boardImage;
    private static final int PANEL_WIDTH = 800;
    private static final int PANEL_HEIGHT = 600;
    
    public MainMenuPanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setLayout(null); // Absolute positioning for custom placement
        loadBackground();
        createButtons();
    }
    
    private void loadBackground() {
        try {
            backgroundImage = ImageIO.read(new File("resources/images/background.png"));
            boardImage = ImageIO.read(new File("resources/images/board.png"));
        } catch (IOException e) {
            System.err.println("Error loading background image");
            e.printStackTrace();
        }
    }
    
    private void createButtons() {
        int buttonWidth = 200;
        int buttonHeight = 50;
        int buttonX = (PANEL_WIDTH - buttonWidth) / 2; // Center horizontally
        int startY = 240;
        int spacing = 65;
        
        // Play Button
        MenuButton playButton = new MenuButton("resources/images/newgame.png");
        playButton.setBounds(buttonX, startY, buttonWidth, buttonHeight);
        playButton.addActionListener(this::onPlayClicked);
        add(playButton);
        
        // Options Button
        MenuButton optionsButton = new MenuButton("resources/images/options.png");
        optionsButton.setBounds(buttonX, startY + spacing, buttonWidth, buttonHeight);
        optionsButton.addActionListener(this::onOptionsClicked);
        add(optionsButton);
        
        // Score Button
        MenuButton scoreButton = new MenuButton("resources/images/score.png");
        scoreButton.setBounds(buttonX, startY + spacing * 2, buttonWidth, buttonHeight);
        scoreButton.addActionListener(this::onScoreClicked);
        add(scoreButton);
        
        // Exit Button
        MenuButton exitButton = new MenuButton("resources/images/exit.png");
        exitButton.setBounds(buttonX, startY + spacing * 3, buttonWidth, buttonHeight);
        exitButton.addActionListener(this::onExitClicked);
        add(exitButton);
    }
    
    private void onPlayClicked(ActionEvent e) {
        System.out.println("Play button clicked!");
        // TODO: Start game
    }
    
    private void onOptionsClicked(ActionEvent e) {
        System.out.println("Options button clicked!");
        // TODO: Show options menu
    }
    
    private void onScoreClicked(ActionEvent e) {
        System.out.println("Score button clicked!");
        // TODO: Score menu
    }
    
    private void onExitClicked(ActionEvent e) {
        System.out.println("Exit button clicked!");
        System.exit(0);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw background
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, PANEL_WIDTH, PANEL_HEIGHT, null);
        } else {
            // Fallback brown color if image doesn't load
            g.setColor(new Color(139, 90, 43));
            g.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
        }
        
        // Draw board panel as backdrop
        if (boardImage != null) {
            int boardWidth = 650;
            int boardHeight = 550;
            int boardX = (PANEL_WIDTH - boardWidth) / 2;
            int boardY = 50;
            g2d.drawImage(boardImage, boardX, boardY, boardWidth, boardHeight, null);
        }
        
        // Draw title
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setFont(new Font("Arial", Font.BOLD, 50));
        g2d.setColor(new Color(255, 215, 100));
        String title = "SNAKE JAVA";
        FontMetrics fm = g2d.getFontMetrics();
        int titleX = (PANEL_WIDTH - fm.stringWidth(title)) / 2;
        int titleY = 180;
        
        // Draw shadow
        g2d.setColor(new Color(80, 40, 20));
        g2d.drawString(title, titleX + 3, titleY + 3);
        
        // Draw title
        g2d.setColor(new Color(255, 215, 100));
        g2d.drawString(title, titleX, titleY);
    }
}
