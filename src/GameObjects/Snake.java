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
    
    private boolean collideWall(Point head, Map map){
        //if snake hits a wall
        return map.valueAtCoord(head.x, head.y) == 1;
    }
    
    private boolean collideSelf(Point head){
        Point[] copy = coordinates();
        
        if(copy.length <= 1)
            return false;
        
        for(int i = 0; i < copy.length - 1; i++)
            if(copy[i].equals(head))
                return true;
        
        return false;
    }
    
    private boolean collideFruit(Point head, Map map){
        Point fruit = map.fruitCoord();
        
        return (fruit.x == head.x) && (fruit.y == head.y);
    }
    
    public boolean collidePoison(Point head, Map map){
        if(!map.isPoison())
            return false;
        
        Point poison = map.poisonCoord();
        
        return (poison.x == head.x) && (poison.y == head.y);
    }
    
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
    
    public void eatFruit(Point newHead){
        snake.addFirst(newHead);
        map.generateFruit();
        addScore = 10;
        length++;
    }
    
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
        addScore = -5;
    }
    
    public int getLength(){
        return this.length;
    }
    
    public boolean pointCheck(Point point){
        return snake.contains(point);
    }
    
    private void summon(Point[] coord){
        int lengthConstrict = 0;
        
        for(Point body: coord){
            snake.add(body);
            lengthConstrict++;
            if(lengthConstrict == length)
                break;
        }
    }
    
    public Point[] coordinates() {
        return snake.toArray(new Point[0]);
    }

    
}
