package GameModes;

import Main.MainMenuPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;

public class GameOverPanel extends JPanel {
    
    private BufferedImage labelImage, homeButtonImage, retryButtonImage;
    private int score, gameMode, difficulty;
    private String playerName;
    
    public GameOverPanel(int score, int gameMode, int difficulty, String playerName) {
        this.score = score;
        this.gameMode = gameMode;
        this.difficulty = difficulty;
        this.playerName = playerName;
        
        setOpaque(false); 
        setLayout(null);
        loadImages();
        createButtons();
        
        // Save score automatically when game over
        if (score > 0) {
            ScoreManager.addScore(playerName, score, gameMode);
        }
    }
    
    // Keep old constructor for compatibility
    public GameOverPanel(int score, int gameMode, int difficulty) {
        this(score, gameMode, difficulty, "Player");
    }
    
    private void loadImages() {
        try {
            InputStream labelStream = getClass().getResourceAsStream("/resources/images/label.png");
            InputStream homeStream = getClass().getResourceAsStream("/resources/images/homebutton.png");
            InputStream retryStream = getClass().getResourceAsStream("/resources/images/retry.png");
            
            if (labelStream != null) labelImage = ImageIO.read(labelStream);
            if (homeStream != null) homeButtonImage = ImageIO.read(homeStream);
            if (retryStream != null) retryButtonImage = ImageIO.read(retryStream);
        } catch (IOException e) {
            System.err.println("Error loading images");
        }
    }
    
    private void createButtons() {
        int panelWidth = 300, buttonSize = 50, buttonY = 130, gap = 40;
        
        JButton homeBtn = createImageButton(homeButtonImage, "HOME");
        homeBtn.setBounds((panelWidth / 2) - buttonSize - (gap / 2), buttonY, buttonSize, buttonSize);
        homeBtn.addActionListener(e -> goToMainMenu());
        add(homeBtn);
        
        JButton retryBtn = createImageButton(retryButtonImage, "RETRY");
        retryBtn.setBounds((panelWidth / 2) + (gap / 2), buttonY, buttonSize, buttonSize);
        retryBtn.addActionListener(e -> retryGame());
        add(retryBtn);
    }
    
    private JButton createImageButton(BufferedImage image, String fallbackText) {
        JButton button = new JButton();
        if (image != null) {
            button.setIcon(new ImageIcon(image.getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        } else {
            button.setText(fallbackText);
            button.setFont(new Font("Arial", Font.BOLD, 12));
        }
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private void goToMainMenu() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.getContentPane().removeAll();
        frame.add(new MainMenuPanel());
        frame.revalidate();
        frame.repaint();
    }
    
    private void retryGame() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.getContentPane().removeAll();
        GamePanel gamePanel = new GamePanel(gameMode, difficulty, playerName);
        frame.add(gamePanel);
        frame.revalidate();
        frame.repaint();
        gamePanel.requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int panelWidth = getWidth(), panelHeight = getHeight();
        
        if (labelImage != null) {
            g2d.drawImage(labelImage, 0, 0, panelWidth, panelHeight, null);
        } else {
            g2d.setColor(new Color(50, 50, 50, 230));
            g2d.fillRoundRect(0, 0, panelWidth, panelHeight, 20, 20);
        }
        
        g2d.setFont(new Font("Arial", Font.BOLD, 28));
        g2d.setColor(Color.WHITE);
        String gameOverText = "GAME OVER";
        int textX = (panelWidth - g2d.getFontMetrics().stringWidth(gameOverText)) / 2;
        g2d.drawString(gameOverText, textX, 45);
        
        g2d.setFont(new Font("Arial", Font.PLAIN, 16));
        String playerText = "Player: " + playerName; 
        textX = (panelWidth - g2d.getFontMetrics().stringWidth(playerText)) / 2;
        g2d.drawString(playerText, textX, 75);
        
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        String scoreText = "Score: " + score;
        textX = (panelWidth - g2d.getFontMetrics().stringWidth(scoreText)) / 2;
        g2d.drawString(scoreText, textX, 105);
    }
}
