/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GameObjects;
import java.io.*;
import java.util.*;
import java.awt.Point;


/**
 *
 * @author eurym
 */
public class Map {
    final private int MAXX = 20, MAXY = 20;
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

            // Fallback to empty map or stop game
            this.mapSource = new int[0][0];
            this.success = false;
        }
        this.snake = new Snake(5, spawnPoint, this);
        generateFruit();
    }
    
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
 
    public void generateFruit(){
        while(true){
            int newX = Util.RANDOM.nextInt(0, MAXX);
            int newY = Util.RANDOM.nextInt(0, MAXY);
            
            if (mapSource[newY][newX] == 0 && !snake.pointCheck(new Point(newX, newY))) {
                fruit = new Point(newX, newY);
                return;
            }
        }
    }

    public void generatePoison(){
        while(true){
            int newX = Util.RANDOM.nextInt(0, MAXX);
            int newY = Util.RANDOM.nextInt(0, MAXY);
            
            if (mapSource[newY][newX] == 0 && !snake.pointCheck(new Point(newX, newY)) && !fruit.equals(new Point(newX, newY))) {
                poison = new Point(newX, newY);
                return;
            }
        }
    }
    
    public int valueAtCoord(int x, int y){
        return mapSource[y][x];
    }
    
    public Point fruitCoord(){
        return fruit;
    }

    public Point poisonCoord() {
        return poison;
    }
    
    public boolean isPoison(){
        return poison != null;
    }
}
