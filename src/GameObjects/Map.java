/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GameObjects;
import java.io.*;
import java.util.*;
import java.awt.Point;
import java.security.SecureRandom;


/**
 *
 * @author eurym
 */
public class Map {
    final private int MAXX = 20, MAXY = 20;
    final public SecureRandom RANDOM = new SecureRandom();
    private int[][] mapSource;
    private Point fruit;
    private Point poison;
    public Snake snake;
    public boolean success;
    
    public Map(String fileLink, Point[] spawnPoint) {
        try {
            this.mapSource = loadMap(fileLink);
            this.success = true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load map!");

            // Fallback to empty map and stop game
            this.mapSource = new int[0][0];
            this.success = false;
        }
        this.snake = new Snake(5, spawnPoint, this);
        generateFruit();
    }
    
    // To load Map from the source txt file
    private int[][] loadMap(String fileName) throws IOException {
        List<int[]> rows = new ArrayList<>();

        InputStream is = getClass().getResourceAsStream("/GameObjects/" + fileName);
        if (is == null) throw new IOException("Map file not found: " + fileName);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) {
                int[] row = new int[line.length()];
                for (int i = 0; i < line.length(); i++) {
                    char c = line.charAt(i);
                    row[i] = Character.isDigit(c) ? c - '0' : 0;
                }
                rows.add(row);
            }
        }

        // Convert List to 2D array
        int[][] map = new int[rows.size()][rows.get(0).length];
        for (int i = 0; i < rows.size(); i++) {
            map[i] = rows.get(i);
        }

        return map;
    }
 
    // To generate a fruit in a random valid location (not in a wall nor in the snake nor in an existing poison)
    public void generateFruit(){
        while(true){
            int newX = RANDOM.nextInt(0, MAXX);
            int newY = RANDOM.nextInt(0, MAXY);
            Point newPoint = new Point(newX, newY);
            
            if (mapSource[newY][newX] == 0 && !snake.pointCheck(newPoint)) {
                if(isPoison() && poison.equals(newPoint))
                    return;

                fruit = newPoint;
                return;
            }
        }
    }

    // To generate a poison fruit in a random valid location(not in a wall nor in the snake nor  in a generated fruit)
    public void generatePoison(){
        while(true){
            int newX = RANDOM.nextInt(0, MAXX);
            int newY = RANDOM.nextInt(0, MAXY);
            Point newPoint = new Point(newX, newY);
            
            if (mapSource[newY][newX] == 0 && !snake.pointCheck(newPoint) && !fruit.equals(newPoint)) {
                poison = newPoint;
                return;
            }
        }
    }
    
    // Getter for a value in the mapSource
    public int valueAtCoord(int x, int y){
        return mapSource[y][x];
    }
    
    // Getter for the fruit Point
    public Point fruitCoord(){
        return fruit;
    }

    //Getter for the Poison point
    public Point poisonCoord() {
        return poison;
    }
    
    // Checks if game has poison
    public boolean isPoison(){
        return poison != null;
    }
    
    // Setter for Fruit
    public void setFruit(Point point){
        this.fruit = point;
    }
    
    // Setter for Fruit
    public void setPoison(Point point){
        this.poison = point;
    }
}
