package GameModes;

import Main.MainMenuPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;

public class ScoreBoardPanel extends JPanel {
    
    private static final int PANEL_WIDTH = 800;
    private static final int PANEL_HEIGHT = 600;
    
    private BufferedImage backgroundImage, boardImage, labelImage;
    
    private Color goldColor = new Color(255, 215, 0);
    private Color silverColor = new Color(192, 192, 192);
    private Color bronzeColor = new Color(205, 127, 50);
    
    // Constructor: setup panel dimensions and assets
    public ScoreBoardPanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setLayout(null);
        loadImages();
        createButtons();
    }
    
    private void loadImages() {
        try { //Classpath loading 
            InputStream bgStream = getClass().getResourceAsStream("/resources/images/background.png");  
            InputStream boardStream = getClass().getResourceAsStream("/resources/images/board.png");
            InputStream labelStream = getClass().getResourceAsStream("/resources/images/label.png");
            
            if (bgStream != null) backgroundImage = ImageIO.read(bgStream);
            if (boardStream != null) boardImage = ImageIO.read(boardStream);
            if (labelStream != null) labelImage = ImageIO.read(labelStream);
        } catch (IOException e) {
            System.err.println("Error loading scoreboard images");
        }
    }

    private void createButtons() { // ENCAPSULATION: button creation details hidden
        JButton homeButton = new JButton("Back to Home"); 
        homeButton.setBounds(250, 501, 130, 40);
        homeButton.setFont(new Font("Arial", Font.BOLD, 12));
        homeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        homeButton.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
        homeButton.setBackground(new Color (0, 200, 0));
        homeButton.setForeground(Color.WHITE);
        homeButton.addActionListener(e -> goBack());

        add(homeButton) ;


        JButton clearButton = new JButton("Clear Scores"); 
        clearButton.setBounds(420, 501, 130, 40);
        clearButton.setFont(new Font("Arial", Font.BOLD, 12));
        clearButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        clearButton.setBackground(new Color (200, 0, 0));
        clearButton.setForeground(Color.WHITE);
        clearButton.addActionListener(e -> clearScores()); 
        add(clearButton);
    }
    
    // Switch back to MainMenuPanel
    private void goBack() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.getContentPane().removeAll();
        frame.add(new MainMenuPanel());
        frame.revalidate();
        frame.repaint();
    }
    // Clear persisting scores from highscores.txt
    private void clearScores() {
        int confirm = JOptionPane.showConfirmDialog(this, "Clear all scores?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                ScoreManager.clearAllScores();
                repaint();
                JOptionPane.showMessageDialog(this, "Scores cleared!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // again, Inheritance and Runtime Polymorphism overriding painting behaviour
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, PANEL_WIDTH, PANEL_HEIGHT, null);
        } else {
            g2d.setColor(new Color(139, 90, 43));
            g2d.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
        }
        
        if (boardImage != null) {
            g2d.drawImage(boardImage, 75, 25, 650, 550, null);
        }
        
        g2d.setColor(new Color(80, 40, 20));
        g2d.setFont(new Font("Arial", Font.BOLD, 36));
        g2d.drawString("HIGH SCORES", 283, 116);
        g2d.setColor(new Color(255, 215, 100));
        g2d.drawString("HIGH SCORES", 280, 113);
        
        ScoreEntry[] scores = ScoreManager.loadScores();
        int entryY = 130, entryHeight = 65, entryWidth = 300;
        int entryX = (PANEL_WIDTH - entryWidth) / 2;
        
        for (int i = 0; i < 5; i++) {
            int y = entryY + i * (entryHeight + 10);
            
            if (labelImage != null) {
                g2d.drawImage(labelImage, entryX, y, entryWidth, entryHeight, null);
            } else {
                g2d.setColor(new Color(50, 50, 50));
                g2d.fillRoundRect(entryX, y, entryWidth, entryHeight, 10, 10);
            }
            
            Color textColor = (i == 0) ? goldColor : (i == 1) ? silverColor : (i == 2) ? bronzeColor : Color.WHITE;
            String rank = (i == 0) ? "1st" : (i == 1) ? "2nd" : (i == 2) ? "3rd" : "#" + (i + 1);
            
            // Draw rank with outline
            g2d.setFont(new Font("Arial", Font.BOLD, 24));
            drawOutlinedText(g2d, rank, entryX + 20, y + 42, textColor);
            
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            String name = scores[i].name.length() > 15 ? scores[i].name.substring(0, 15) + "..." : scores[i].name;
            
            // Draw name with outline
            drawOutlinedText(g2d, name, entryX + 80, y + 42, textColor);
            
            // Draw score with outline
            String scoreText = String.valueOf(scores[i].score);
            int scoreWidth = g2d.getFontMetrics().stringWidth(scoreText);
            drawOutlinedText(g2d, scoreText, entryX + entryWidth - scoreWidth - 30, y + 42, textColor);
        }
    }
    private void drawOutlinedText(Graphics2D g2d, String text, int x, int y, Color fillColor) {
        // Draw outline by drawing text in all 8 directions
        g2d.setColor(new Color(0, 0, 0, 80));
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx != 0 || dy != 0) {
                    g2d.drawString(text, x + dx, y + dy);
                }
            }
        }
        // Draw main text on top
        g2d.setColor(fillColor);
        g2d.drawString(text, x, y);
    }
}
