/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GameObjects;
import GameModes.Direction;
import GameModes.GamePanel;
import javax.swing.Timer;

/**
 *
 * @author eurym
 */
public abstract class GameMode {
    
    public Timer gameLoop;
    protected GamePanel panel;
    public Map map;
    
    public double[] DIFFICULTY = {1.0, 2.0, 3.0, 4.0};
    protected Direction direction;
    protected int diff;
    protected int score;
    
    public abstract void startGame();
    public abstract void update();
    
    protected void render() {
        if (panel != null) {
            panel.repaint();
        }
    }
    
    public void setDirection(Direction newDir) {
    if (!newDir.isOpposite(direction)) {
        direction = newDir;
    }
}
    
    public void setPanel(GamePanel panel) {
        this.panel = panel;
    }
}
