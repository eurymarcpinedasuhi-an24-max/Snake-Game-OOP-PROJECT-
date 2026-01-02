/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
    
    public double[] DIFFICULTY = {1.0, 2.0, 3.0, 4.0};
    protected Direction direction;
    protected int diff;
    protected int score = 0;
    
    public abstract void startGame();
    
    public void loadGame(SaveManager.SaveData save){
        this.diff = save.difficulty;
        this.score = save.score;
        this.direction = save.direction;
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
    
    public void addScore(){
        if (map.snake.addScore != 0){
            this.score += map.snake.addScore;
            map.snake.addScore = 0;
        }
    }
    
}
