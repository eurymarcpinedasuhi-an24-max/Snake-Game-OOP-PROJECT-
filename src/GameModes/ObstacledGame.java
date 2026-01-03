/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GameModes;

import GameObjects.*;
import java.awt.Point;
import javax.swing.Timer;

/**
 *
 * @author eurym
 */
public class ObstacledGame extends GameMode{
    
    final public Point[] spawnPoint = {
        new Point(10, 11),
        new Point(9, 11),
        new Point(8, 11),
        new Point(7, 11),
        new Point(6, 11)
    };
    
    public ObstacledGame(int diff){
        this.map = new Map("Maze.txt", spawnPoint);
        if (!this.map.success)
            throw new IllegalStateException("Map failed to load");
        
        //set GameMode variables
        this.direction = Direction.RIGHT;
        this.score = 0;
        this.diff = diff;
        this.gameMode = 3; 
    }
    
    @Override
    public void startGame(){
        int delay = (int)(500 / diff); // milliseconds

        gameLoop = new Timer(delay, e -> {
            update();
            render();
        });
        gameLoop.start();
    }
    
    @Override
    public void update(){
        map.snake.moveSnake(direction);
        addScore();
        
        if (map.snake.defeat) {          // check defeat flag
            System.out.println("Game Over!");
            gameLoop.stop();                 // stop the Timer/game loop
            if (panel != null) {
                panel.showGameOver(score);
            }
        }
    }
    
}
