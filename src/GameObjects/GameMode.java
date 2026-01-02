/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

//CHANGES: ADD GAME MODE TRACKING FOR SCORE MULTIPLIER
package GameObjects;
import GameModes.*;
import javax.swing.Timer;

/**
 *
 * @author eurym
 */
public abstract class GameMode {
    
    public Timer gameLoop;
    protected GamePanel panel;
    public Map map;
    
    public double[] MODE_MULTIPLIERS = {1.0, 1.25, 1.5, 2.0};
    protected Direction direction;
    protected int diff;
    protected int score = 0;
    protected int gameMode; 
    
    public abstract void startGame();
    
    public void loadGame(SaveManager.SaveData save){
        this.diff = save.difficulty;
        this.score = save.score;
        this.direction = save.direction;
        this.gameMode = save.mode;
        this.map.snake = new Snake(save.snake.length, save.snake, this.map);
        this.map.setFruit(save.fruit);
        if(save.poison != null)
            this.map.setPoison(save.poison);
    }
    
    public void update(){
        map.snake.moveSnake(direction);
        addScore();
        
        if (map.snake.defeat) {          // check defeat flag
            System.out.println("Game Over! Final Score: " + score);
            gameLoop.stop();                 // stop the Timer/game loop
        }
    }
    
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
    
    public void addScore() {
        if (map.snake.addScore != 0) {
            int baseScore = map.snake.addScore;
            int multipliedScore = (int)(baseScore * MODE_MULTIPLIERS[gameMode]);
            this.score += multipliedScore;
            map.snake.addScore = 0;
        }
    }
}
