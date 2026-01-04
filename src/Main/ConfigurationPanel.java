package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;

import GameModes.GamePanel;

public class ConfigurationPanel extends JPanel {
    
    // Panel size
    private int panelWidth = 800;
    private int panelHeight = 600;
    
    // Images to be used
    private BufferedImage backgroundImage;
    private BufferedImage boardImage;
    private BufferedImage labelImage;
    private BufferedImage leftArrowImage;
    private BufferedImage rightArrowImage;
    
    // Game mode stuff
    private String[] gameModes = {"CLASSIC", "WALLS", "OBJECTS", "MAZE"};
    private String[] modeDescriptions = {
        "Standard Snake",
        "Extra walls and obstacles!",
        "Poison Apples (purple) = -1 length",
        "Super Difficult Tight Maze!"
    };
    private Color[] modeColors = {
        new Color(76, 175, 80),   // Green
        new Color(33, 150, 243),  // Blue
        new Color(255, 152, 0),   // Orange
        new Color(244, 67, 54)    // Red
    };
    private double[] modeMultipliers = {1.0, 1.25, 1.5, 2.0};
    
    // Difficulty stuff
    private String[] difficulties = {"Easy", "Normal", "Hard", "Extreme"};
    private String[] diffSpeeds = {"Slow Speed", "Medium Speed", "Fast Speed", "Very Fast Speed"};
    private double[] diffMultipliers = {1.0, 2.0, 3.0, 4.0};
    
    // Current selections
    private int selectedMode = 0;
    private int selectedDifficulty = 0;
    
    // Buttons and labels
    private JButton[] modeButtons = new JButton[4];
    private JLabel difficultyNameLabel;
    private JLabel difficultyDescLabel;
    
    // Add player name field
    private String playerName = "Player";
    
    public ConfigurationPanel() {
        setPreferredSize(new Dimension(panelWidth, panelHeight));
        setLayout(null);
        loadImages();
        createButtons();
    }
    
    private void loadImages() {
        try { //TODO: REHAUL WITH CLASS PATH RESOURCES  LIKE IN MAINMENUPANEL

            InputStream backgroundImage = getClass().getResourceAsStream("/resources/images/background.png");
            InputStream boardImage = getClass().getResourceAsStream("/resources/images/board.png");
            InputStream labelImage = getClass().getResourceAsStream("/resources/images/label.png");
            InputStream leftArrowImage = getClass().getResourceAsStream("/resources/images/leftarrow.png");
            InputStream rightArrowImage = getClass().getResourceAsStream("/resources/images/rightarrow.png");


            if (backgroundImage != null) this.backgroundImage = ImageIO.read(backgroundImage);
            if (boardImage != null) this.boardImage = ImageIO.read(boardImage);
            if (labelImage != null) this.labelImage = ImageIO.read(labelImage);
            if (leftArrowImage != null) this.leftArrowImage = ImageIO.read(leftArrowImage);
            if (rightArrowImage != null) this.rightArrowImage = ImageIO.read(rightArrowImage);
        } catch (IOException e) {
            System.out.println("Could not load images!");
        }
    }
    
    private void createButtons() {
        // Position values
        int startX = 270;
        int startY = 125;
        int buttonWidth = 260;
        int buttonHeight = 55;
        int gap = 8;
        
        // Create 4 mode buttons
        for (int i = 0; i < 4; i++) {
            int y = startY + i * (buttonHeight + gap);
            modeButtons[i] = createModeButton(i);
            modeButtons[i].setBounds(startX, y, buttonWidth, buttonHeight);
            add(modeButtons[i]);
        }
        
        // Difficulty section Y position
        int diffY = startY + 4 * (buttonHeight + gap) + 40;
        
        // Left arrow - position to the left of the label
        JButton leftBtn = createArrowButton(leftArrowImage, -1);
        leftBtn.setBounds(startX - 10, diffY + 2, 30, 30);
        add(leftBtn);
        
        // Right arrow - position to the right of the label
        JButton rightBtn = createArrowButton(rightArrowImage, 1);
        rightBtn.setBounds(startX + buttonWidth -20, diffY + 2, 30, 30);
        add(rightBtn);
        

        // Difficulty name label - centered in the label background
        difficultyNameLabel = new JLabel(difficulties[0], SwingConstants.CENTER);
        difficultyNameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        difficultyNameLabel.setForeground(Color.WHITE);
        difficultyNameLabel.setBounds(startX, diffY - 5, buttonWidth, 25);
        add(difficultyNameLabel);

        // Difficulty description label - centered below the name
        difficultyDescLabel = new JLabel(diffSpeeds[0] + " - " + diffMultipliers[0] + "x Multiplier", SwingConstants.CENTER);
        difficultyDescLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        difficultyDescLabel.setForeground(Color.LIGHT_GRAY);
        difficultyDescLabel.setBounds(startX, diffY + 15, buttonWidth, 20);
        add(difficultyDescLabel);

        // Start button
        MenuButton startBtn = new MenuButton("resources/images/continue.png");
        startBtn.setBounds(300, diffY + 130, 200, 45);
        startBtn.addActionListener(e -> startGame());
        add(startBtn);
    }
    
    private JButton createModeButton(int index) {
        JButton button = new JButton() {  // Polymorphism / method override for custom painting 4 board. TODO: add separate images for each mode
            @Override
            protected void paintComponent(Graphics g) {
                if (labelImage != null) {
                    g.drawImage(labelImage, 0, 0, getWidth(), getHeight(), null);
                } else {
                    g.setColor(modeColors[index]);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
                super.paintComponent(g);
            }
        };
        button.setLayout(new BorderLayout());
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add text panel
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        
        JLabel nameLabel = new JLabel(gameModes[index]);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel descLabel = new JLabel(modeDescriptions[index]);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        descLabel.setForeground(new Color(255, 255, 255, 200));
        descLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel multLabel = new JLabel("Score Multiplier: " + modeMultipliers[index] + "x");
        multLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        multLabel.setForeground(new Color(255, 255, 255, 180));
        multLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        textPanel.add(Box.createVerticalGlue());
        textPanel.add(nameLabel);
        textPanel.add(descLabel);
        textPanel.add(multLabel);
        textPanel.add(Box.createVerticalGlue());
        
        button.add(textPanel, BorderLayout.CENTER);
        
        // Click action
        button.addActionListener(e -> {
            selectedMode = index;
            updateModeButtons();
        });
        
        return button;
}
    
    private void updateModeButtons() {
        for (int i = 0; i < 4; i++) {
            if (i == selectedMode) {
                modeButtons[i].setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
            } else {
                modeButtons[i].setBorder(null);
            }
        }
        repaint();
    }
    
    private JButton createArrowButton(BufferedImage image, int direction) {
        JButton button = new JButton();
        
        if (image != null) {
            button.setIcon(new ImageIcon(image.getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        } else {
            button.setText(direction < 0 ? "<" : ">");
            button.setFont(new Font("Arial", Font.BOLD, 20));
        }
        
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addActionListener(e -> {
            selectedDifficulty = selectedDifficulty + direction;
            
            // Wrap around basically
            if (selectedDifficulty < 0) {
                selectedDifficulty = 3;
            }
            if (selectedDifficulty > 3) {
                selectedDifficulty = 0;
            }
            
            updateDifficultyLabels();
        });
        
        return button;
    }
    
    // Update difficutlty based on current selection
    private void updateDifficultyLabels() {
        difficultyNameLabel.setText(difficulties[selectedDifficulty]);
        difficultyDescLabel.setText(diffSpeeds[selectedDifficulty] + " - " + diffMultipliers[selectedDifficulty] + "x Multiplier");
    }

    // Start game itself. Nothing else to modify here.
    private void startGame() {
        // Ask for player name before starting
        String name = JOptionPane.showInputDialog(
            SwingUtilities.getWindowAncestor(this),
            "Enter your name:",
            "Player Name",
            JOptionPane.PLAIN_MESSAGE
        );
        
        if (name == null || name.trim().isEmpty()) { // Default name
            name = "Player";
        }
        
        name = name.trim();
        if (name.length() > 20) {
            name = name.substring(0, 20); // Limit
        }
        playerName = name;
        
        System.out.println("Starting game!");
        System.out.println("Player: " + playerName);
        System.out.println("Mode: " + gameModes[selectedMode]);
        System.out.println("Difficulty: " + (difficulties[selectedDifficulty] + 1));
        
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.getContentPane().removeAll();
        
        GamePanel gamePanel = new GamePanel(selectedMode, selectedDifficulty + 1, playerName);
        frame.add(gamePanel);
        
        frame.revalidate();
        frame.repaint();
        gamePanel.requestFocusInWindow();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        
        // Enable anti-aliasing for smoother text and shapes. Not working(?)
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Draw background
        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, panelWidth, panelHeight, null);
        } else {
            g2d.setColor(new Color(139, 90, 43));
            g2d.fillRect(0, 0, panelWidth, panelHeight);
        }
        
        // Draw board
        int boardX = 75;
        int boardY = 25;
        int boardW = 650;
        int boardH = 550;
        
        if (boardImage != null) {
            g2d.drawImage(boardImage, boardX, boardY, boardW, boardH, null);
        } else {
            g2d.setColor(new Color(30, 30, 30));
            g2d.fillRoundRect(boardX, boardY, boardW, boardH, 20, 20);
        }
        
        // Draw title with Graphics2D
        g2d.setColor(new Color(80, 40, 20));
        g2d.setFont(new Font("Arial", Font.BOLD, 28));
        g2d.drawString("CONFIGURE YOUR GAME", 228, 72);
         // Draw shadow for 3D effect
        g2d.setColor(new Color(255, 215, 100));
        g2d.setFont(new Font("Arial", Font.BOLD, 28));
        g2d.drawString("CONFIGURE YOUR GAME", 225, 69);
        
        
        // Draw "Game Mode:" text
        g2d.setColor(new Color(80, 40, 20));
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.drawString("Game Mode: " + gameModes[selectedMode], 240 + 2 , 115 + 2);
        g2d.setColor(new Color(255, 215, 100));
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.drawString("Game Mode: " + gameModes[selectedMode], 240, 115);
        
        // Draw "Difficulty:" text
        int diffY = 125 + 4 * (55 + 8) + 30;
        g2d.setColor(new Color(80, 40, 20));
        g2d.drawString("Difficulty:", 250, diffY - 10);
        g2d.setColor(new Color(255, 215, 100));
        g2d.drawString("Difficulty:", 247, diffY - 12);
        
        // Draw label background for difficulty
        int labelX = 270;
        int labelW = 260;
        
        if (labelImage != null) {
            g2d.drawImage(labelImage, labelX, diffY, labelW, 55, null);
        } else {
            g2d.setColor(new Color(50, 50, 50));
            g2d.fillRoundRect(labelX, diffY, labelW, 55, 10, 10);
        }
    }
    
    // Getters for selected mode and difficulty. To use in GamePanel
    public int getSelectedMode() {
        return selectedMode;
    }
    
    public int getSelectedDifficulty() {
        return selectedDifficulty;
    }
}