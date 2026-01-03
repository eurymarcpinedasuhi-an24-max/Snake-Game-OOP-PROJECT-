/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GameObjects;
import GameModes.Direction;
import java.awt.Point;
import java.util.Deque;
import java.util.LinkedList;

/**
 *
 * @author eurym
 */
public class Snake {

    private Deque<Point> snake = new LinkedList<>();
    private int length;
    private Map map;
    final private int MAXX = 20, MAXY = 20;
    public boolean defeat = false;
    public int addScore = 0;
    
    Snake(int length, Point[] spawnPoint, Map map){
        this.length = length;
        this.map = map;
        
        summon(spawnPoint);
    }
    
    // Checks if snake will collide wall with next move
    private boolean collideWall(Point head, Map map){
        //if snake hits a wall
        return map.valueAtCoord(head.x, head.y) == 1;
    }
    
    // Checks if snake will colide with itself with the next move
    private boolean collideSelf(Point head){
        Point[] copy = coordinates();
        
        if(copy.length <= 4)    // 4 length snake gets in a cycle than getting out
            return false;
        
        for(int i = 0; i < copy.length - 1; i++)
            if(copy[i].equals(head))
                return true;
        
        return false;
    }
    
    // Checks if snake will collide with a fruit at next move
    private boolean collideFruit(Point head, Map map){
        Point fruit = map.fruitCoord();
        
        return (fruit.x == head.x) && (fruit.y == head.y);
    }
    
    // Checks if snake will collide with a poison at next move
    public boolean collidePoison(Point head, Map map){
        if(!map.isPoison())
            return false;
        
        Point poison = map.poisonCoord();
        
        return (poison.x == head.x) && (poison.y == head.y);
    }
    
    // Moves snake and perform actions based on that move
    public void moveSnake(final Direction direction){
        
        int dx = direction == Direction.LEFT? -1: (direction == Direction.RIGHT? 1: 0);
        int dy = direction == Direction.UP? -1: (direction == Direction.DOWN? 1: 0);
        Point head = snake.peekFirst();
        int newx = (head.x + dx + MAXX) % MAXX;
        int newy = (head.y + dy + MAXY) % MAXY;
        Point newHead = new Point(newx, newy);
        
        if(collideSelf(newHead) || collideWall(newHead, map)){
            defeat = true;
            return;
        }
        
        if (collidePoison(newHead, map)){
            getPoisoned(newHead);
            return;
        }
        
        if (collideFruit(newHead, map)){
            eatFruit(newHead);
            return;
        }
        
        snake.addFirst(newHead);   // move forward
        snake.removeLast();        // remove tail
    }
    
    // Events in eating fruit: increase size and gain points, map generates a new fruit
    public void eatFruit(Point newHead){
        snake.addFirst(newHead);
        map.generateFruit();
        addScore = 10;
        length++;
    }
    
    // Events in eating a Poison: decrease size and reduce points by 20, map generates another poison, also if snake's length is 1 and eats a poison then lose the game
    public void getPoisoned(Point newHead){
        if(!map.isPoison())
            return;
        
        if(length <= 1){
            defeat = true;
            return;
        }
        
        snake.addFirst(newHead);
        snake.removeLast();     //override move
        
        snake.removeLast();     //get poisoned
        length--;
                
        map.generatePoison();
        addScore = -20;
    }
    
    // Getter for snake length
    public int getLength(){
        return this.length;
    }
    
    // Checks if a snake is in a certain point
    public boolean pointCheck(Point point){
        return snake.contains(point);
    }
    
    // Adds Snake body using a list of points for its body
    private void summon(Point[] coord){
        int lengthConstrict = 0;
        
        for(Point body: coord){
            snake.add(body);
            lengthConstrict++;
            if(lengthConstrict == length)
                break;
        }
    }
    
    // Convert snake deque/linked list into a Point array
    public Point[] coordinates() {
        return snake.toArray(new Point[0]);
    }

    
}
