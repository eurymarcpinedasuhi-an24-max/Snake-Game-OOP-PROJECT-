package Main; 
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.io.IOException;
import java.io.InputStream;

// Custom JButton component for menu buttons that have images
public class MenuButton extends JButton {
    // The image displayed on the button
    private BufferedImage buttonImage;
    // Flag to track if the mouse is hovering over the button
    private boolean isHovered = false;
    
    /**
     * Constructor creates a custom image-based button.
     * Removes default Swing button styling and adds hover interaction.
     * 
     * @param imagePath The file path to the button image
     */
    public MenuButton(String imagePath) {
        loadImage(imagePath);
        
        // Remove default button appearance for custom rendering
        setContentAreaFilled(false);  
        setBorderPainted(false);       
        setFocusPainted(false);       
        setCursor(new Cursor(Cursor.HAND_CURSOR));  

        // Set button size based on loaded image dimensions
        if (buttonImage != null) {
            setPreferredSize(new Dimension(buttonImage.getWidth(), buttonImage.getHeight()));
        }

        // Add mouse listener to track hover state
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();  // Redraw button with hover effect
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();  // Redraw button without hover effect
            }
        });
    }

    /**
     * Loads the button image from the specified file path
     * Prints an error message if the image cannot be loaded
     * 
     * @param path The file path to the button image
     */
    private void loadImage(String path) {
        try { // Load image from resources
            String resourcePath = "/" + path;
            InputStream stream = getClass().getResourceAsStream(resourcePath);
            if (stream != null) {
                buttonImage = ImageIO.read(stream);
            } else {
                System.err.println("Resource not found: " + resourcePath);
            }
        } catch (IOException e) {
            System.err.println("Error loading button image: " + path);
            e.printStackTrace();
        }
    }

    /**
     * Custom painting method that renders the button image with optional hover effect
     * Applies transparency when hovered for visual feedback
     * 
     * @param g The Graphics object used for drawing
     */
    @Override
    protected void paintComponent(Graphics g) {
        if (buttonImage != null) {
            Graphics2D g2d = (Graphics2D) g;
            // Enable antialiasing for smooth edges
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // Enable smooth image scaling
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            // Apply transparency effect when hovered
            if (isHovered) {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
            }

            // Scale image to fit button bounds while maintaining aspect ratio
            g2d.drawImage(buttonImage, 0, 0, getWidth(), getHeight(), null);
        }
        super.paintComponent(g);
    }
}
