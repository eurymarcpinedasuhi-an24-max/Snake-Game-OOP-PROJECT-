
import javax.swing.JFrame;

public class GameWindow extends JFrame {
    public GameWindow() {
        setTitle("Snake in Java");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        MainMenuPanel menuPanel = new MainMenuPanel();
        add(menuPanel);

        pack();
        setLocationRelativeTo(null); // Center the window on the screen
    
    }
}