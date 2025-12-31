import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class MenuButton extends JButton {
    private BufferedImage buttonImage;
    private boolean isHovered = false;
    
    public MenuButton(String imagePath) {
        loadImage(imagePath);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (buttonImage != null) {
            setPreferredSize(new Dimension(buttonImage.getWidth(), buttonImage.getHeight()));
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                isHovered = false;
                repaint();
            }
        });
    }

    private void loadImage(String path) {
        try {
            buttonImage = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.err.println("Error loading button image: " + path);
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (buttonImage != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            if (isHovered) {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
            }

            // Scale image to fit button bounds while maintaining aspect ratio
            g2d.drawImage(buttonImage, 0, 0, getWidth(), getHeight(), null);
        }
        super.paintComponent(g);
    }
}
