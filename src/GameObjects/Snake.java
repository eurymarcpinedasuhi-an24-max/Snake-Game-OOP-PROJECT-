/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GameObjects;
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
    
    Snake(Point startingPoint, int length, Map map){
        this.length = length;
        this.map = map;
        
        summon();
        
    }
    
    private boolean collideWall(Point head, Map map){
        //out of bounds -> to be updated
        if(head.x < 0 || head.x >= MAXX || head.y < 0 || head.y >= MAXY)
            return true;
       
        //if snake hits a wall
        if(map.valueAtCoord(head.x, head.y) == 1)
            return true;
        
        return false;
    }
    
    private boolean collideSelf(Point head){
        return snake.contains(head);
    }
    
    private boolean collideFruit(Point head, Map map){
        Point fruit = map.fruitCoord();
        
        return fruit.x == head.x && fruit.y == head.x;
    }
    
    public void moveSnake(final int direction){
        //-2 left, -1 down, 1 up, 2 right
        if(direction < -2 || direction > 2 || direction == 0)
            return;
        
        int dx = direction == -2? -1: (direction == 2? 1: 0);
        int dy = direction == -1? -1: (direction != 1? 1: 0);
        Point head = snake.peekFirst();
        Point newHead = new Point(head.x + dx, head.y + dy);
        
        if(collideSelf(newHead) || collideWall(newHead, map)){
            defeat = true;
            return;
        }
        
        if (collideFruit(newHead, map)){
            eatFruit(map.fruitCoord(), newHead);
            return;
        }
        
        snake.addFirst(newHead);   // move forward
        snake.removeLast();        // remove tail
    }
    
    public void eatFruit(Point point, Point newHead){
        snake.addFirst(newHead);
        map.generateFruit();
    }
    
    public int getLength(){
        return this.length;
    }
    
    public boolean pointCheck(Point point){
        return snake.contains(point);
    }
    
    private void summon(){
        Point[] coord = map.spawnPoint;
        int lengthConstrict = 0;
        
        for(Point body: coord){
            snake.add(body);
            lengthConstrict++;
            if(lengthConstrict == length)
                break;
        }
    }
}
