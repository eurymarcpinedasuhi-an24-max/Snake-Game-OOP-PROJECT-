
import javax.swing.SwingUtilities;

public class Main {
//Main method to start the app using swingutilities to ensure GUI components are created
    public static void main(String[] args) {
        // Schedule GUI creation on the Event Dispatch Thread for thread safety
        SwingUtilities.invokeLater(() -> {
            GameWindow window = new GameWindow();
            window.setVisible(true);
        });
    }
}