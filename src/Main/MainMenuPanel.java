package Main;

import GameModes.GamePanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.io.IOException;
import java.io.InputStream;

// Displays title, background and menu buttons with custom assets 
public class MainMenuPanel extends JPanel {
    private BufferedImage backgroundImage;  // Images for the menu background and board overlay
    private BufferedImage boardImage;
    

    private static final int PANEL_WIDTH = 800;
    private static final int PANEL_HEIGHT = 600;


    //Constructor, sets panel size, layout, loads background and buttons
    public MainMenuPanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setLayout(null); // Absolute positioning for custom placement
        loadBackground();
        createButtons();
    }
    

    private void loadBackground() {
        try {
            // load images from resources folder
            InputStream bgStream = getClass().getResourceAsStream("/resources/images/background.png");
            InputStream boardStream = getClass().getResourceAsStream("/resources/images/board.png");
            
            if (bgStream != null) backgroundImage = ImageIO.read(bgStream);
            if (boardStream != null) boardImage = ImageIO.read(boardStream);
        } catch (IOException e) {
            System.err.println("Error loading background image");
            e.printStackTrace();
        }
    }
    

    // Creating and positioning menu buttons with action listeners
    private void createButtons() {
        // Button dimensions and positioning
        int buttonWidth = 200;
        int buttonHeight = 50;
        int buttonX = (PANEL_WIDTH - buttonWidth) / 2; // Center horizontally
        int startY = 220;  // Starting Y position for first button
        int spacing = 65;  // Vertical spacing between buttons
        
        // Play Button
        MenuButton playButton = new MenuButton("resources/images/newgame.png");
        playButton.setBounds(buttonX, startY, buttonWidth, buttonHeight);
        playButton.addActionListener(this::onPlayClicked);
        add(playButton);
        
        // Options Button
        MenuButton continueButton = new MenuButton("resources/images/continue.png");
        continueButton.setBounds(buttonX, startY + spacing, buttonWidth, buttonHeight);
        continueButton.addActionListener(this::onOptionsClicked);
        add(continueButton);
        
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
    
    // Handler for Play button click event.
    private void onPlayClicked(ActionEvent e) {
        System.out.println("Play button clicked!");

        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        parentFrame.getContentPane().removeAll();
        
        // Navigate to configuration panel
        ConfigurationPanel configPanel = new ConfigurationPanel();
        parentFrame.add(configPanel);

        parentFrame.revalidate();
        parentFrame.repaint();
        
        /** System.out.println("Play button clicked!");

        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this); // Parent JFrame
        parentFrame.getContentPane().removeAll(); // Clearing current panel
        GamePanel gamePanel = new GamePanel(); // Create new game panel — see GameModes/GamePanel.java
        parentFrame.add(gamePanel); 

        parentFrame.revalidate(); // Refresh frame 
        parentFrame.repaint();

        gamePanel.requestFocusInWindow(); */

    }
    
    // Handler for Options button click event.
    private void onOptionsClicked(ActionEvent e) {
        System.out.println("Continue button clicked!");
        
        
        
        /**
         * Please help fix this 
         */
        System.out.println("Continue game!");
        
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.getContentPane().removeAll();
        
        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);
        
        frame.revalidate();
        frame.repaint();
        gamePanel.requestFocusInWindow();
    }
    
    // Handler for Option button click event.
    private void onScoreClicked(ActionEvent e) {
        System.out.println("Score button clicked!");

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.getContentPane().removeAll();

        // TODO: Migrate scoreboard panel to package Main
        frame.add(new GameModes.ScoreBoardPanel());
        frame.revalidate();
        frame.repaint();
    }
    
    // Handler for Exit button click event.
    private void onExitClicked(ActionEvent e) {
        System.out.println("Exit button clicked!");
        System.exit(0);  // Exit the application
    }
    
    // Custom painting for bg and title. Looks cool
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // Enable antialiasing for smoother graphics, no noticeable difference(?)
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw background image or fallback color
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, PANEL_WIDTH, PANEL_HEIGHT, null);
        } else {
            // Fallback brown color if image doesn't load
            g.setColor(new Color(139, 90, 43));
            g.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
        }
        
        // Draw board panel as backdrop for the menu
        if (boardImage != null) {
            int boardWidth = 650;
            int boardHeight = 550;
            int boardX = (PANEL_WIDTH - boardWidth) / 2;  // Center horizontally
            int boardY = 25;  // Position from top
            g2d.drawImage(boardImage, boardX, boardY, boardWidth, boardHeight, null);
        }
        
        // Draw game title with shadow effect (TODO:  BE REPLACED WITH IMAGE LATER)
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setFont(new Font("Arial", Font.BOLD, 49));
        g2d.setColor(new Color(255, 215, 100));
        String title = "SNAKE JAVA";
        FontMetrics fm = g2d.getFontMetrics();
        int titleX = (PANEL_WIDTH - fm.stringWidth(title)) / 2; 
        int titleY = 125;

        
        // Draw shadow for 3D effect
        g2d.setColor(new Color(80, 40, 20));
        g2d.drawString(title, titleX + 3, titleY + 3);
        
        // Draw main title text
        g2d.setColor(new Color(255, 215, 100));
        g2d.drawString(title, titleX, titleY);

        String subtitle = "for OOP:)";
        FontMetrics fm2 = g2d.getFontMetrics();
        int subtitleX = ((PANEL_WIDTH - fm2.stringWidth(subtitle)) / 2) + 60;
        int subtitleY = 155;
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.setColor(new Color(80, 40, 20));
        g2d.drawString(subtitle, subtitleX + 3, subtitleY + 3);
        g2d.setColor(new Color(255, 215, 100));
        g2d.drawString(subtitle, subtitleX, subtitleY);
    }
}
