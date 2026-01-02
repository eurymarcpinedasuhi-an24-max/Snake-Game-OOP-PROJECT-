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
public class WallGame extends GameMode{
    
    final public Point[] spawnPoint = {
        new Point(10, 10),
        new Point(9, 10),
        new Point(8, 10),
        new Point(7, 10),
        new Point(6, 10)
    };
    
    public WallGame(int diff){
        this.map = new Map("Classic-with-Walls.txt", spawnPoint);
        if (!this.map.success)
            throw new IllegalStateException("Map failed to load");
        
        //set GameMode variables
        this.direction = Direction.RIGHT;
        this.score = 0;
        this.diff = diff;
        this.gameMode = 1;
    }
    
    @Override
    public void startGame(){
        int delay = (int)(1000 / (Math.pow(diff, 1.5))); // milliseconds

        gameLoop = new Timer(delay, e -> {
            update();
            render();
        });
        gameLoop.start();
    }
<<<<<<< HEAD
    
    @Override
    public void update(){
        map.snake.moveSnake(direction);
        score += map.snake.addScore;
        map.snake.addScore = 0;
        
        if (map.snake.defeat) {          // check defeat flag
            System.out.println("Game Over!");
            gameLoop.stop();                 // stop the Timer/game loop
            if (panel != null) {
                panel.showGameOver(score);
            }
        }
    }
    
=======
>>>>>>> 6dbe9b722c18630bad67229529aee9d6b7bb7f05
}