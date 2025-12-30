/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GameObjects;
import java.awt.Point;
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
    private Snake snake;
    public Point[] spawnPoint;
    public boolean success;
    
    public Map(String fileLink) {
        try {
            mapSource = loadMap(fileLink);
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load map!");

            // Fallback to empty map or stop game
            mapSource = new int[0][0];
            success = false;
        }
    }
    
    private int[][] loadMap(String filePath) throws IOException {
        List<int[]> rows = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(filePath));

        String line;
        while ((line = br.readLine()) != null) {
            int[] row = new int[line.length()];
            for (int i = 0; i < line.length(); i++) {
                row[i] = Character.getNumericValue(line.charAt(i));
            }
            rows.add(row);
        }
        br.close();

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
    
    public int valueAtCoord(int x, int y){
        return mapSource[y][x];
    }
    
    public Point fruitCoord(){
        return fruit;
    }
    
}
