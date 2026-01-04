package Main; 
import javax.swing.SwingUtilities;

public class Main {
// Main method to start the app using swingutilities to ensure GUI components are created
    public static void main(String[] args) {
        // Schedule on EDT for thread safety
        SwingUtilities.invokeLater(() -> {
            GameWindow window = new GameWindow();
            window.setVisible(true);
        });
    }
}