/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GameModes;

import java.io.*;
import java.util.*;
import java.awt.Point;

/**
 *
 * @author eurym
 */
public class SaveManager {
    
    public static class SaveData {
        public String name;
        public int mode;
        public int difficulty;
        public int score;
        public Direction direction;
        public Point[] snake;
        public Point fruit;
        public Point poison; // nullable

        public boolean isEmpty = false;
        
        SaveData(int mode, int difficulty, int score, Direction direction, Point[] point, Point fruit, Point poison, String name){
            this.name = name;
            this.mode = mode;
            this.difficulty = difficulty;
            this.score = score;
            this.direction = direction;
            this.snake = point;
            this.fruit = fruit;
            this.poison = poison;
        }
        
        // Empty constructor
        SaveData(){
            isEmpty = true;
        }
    }
    
    // Save game from a SaveData object
    public static void saveGame(SaveData save) throws IOException {
        File file = new File(System.getProperty("user.home"), "save.txt");

        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            pw.println("name=" + save.name);
            pw.println("mode=" + save.mode);
            pw.println("difficulty=" + save.difficulty);
            pw.println("score=" + save.score);
            pw.println("direction=" + save.direction); // save as enum text

            // Snake
            if (save.snake != null) {
                StringBuilder sb = new StringBuilder();
                for (Point p : save.snake) {
                    sb.append(p.x).append(",").append(p.y).append(";");
                }
                pw.println("snake=" + sb.toString());
            }

            // Fruit
            if (save.fruit != null) {
                pw.println("fruit=" + save.fruit.x + "," + save.fruit.y);
            }

            // Optional poison
            if (save.poison != null) {
                pw.println("poison=" + save.poison.x + "," + save.poison.y);
            }
        }
    }
    
    // Load game and returns a SaveData object
    public static SaveData loadGame() throws IOException {
        SaveData data = new SaveData();

        InputStream is = SaveManager.class.getResourceAsStream("/GameModes/save.txt");

        if (is == null) {
            System.out.println("save.txt not found in resources!");
            return data;
        }

        if (is.available() == 0) {
            System.out.println("save.txt is empty!");
            return data;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) {

                if (line.startsWith("name=")) {
                    data.name = line.substring(5);
                }

                else if (line.startsWith("mode=")) {
                    data.mode = Integer.parseInt(line.substring(5));
                }

                else if (line.startsWith("difficulty=")) {
                    data.difficulty = Integer.parseInt(line.substring(11));
                }

                else if (line.startsWith("score=")) {
                    data.score = Integer.parseInt(line.substring(6));
                }
                
                else if (line.startsWith("direction=")) {
                    data.direction = Direction.valueOf(line.substring(10));
                }

                else if (line.startsWith("snake=")) {
                    String[] parts = line.substring(6).split(";");
                    List<Point> points = new ArrayList<>();

                    for (String p : parts) {
                        if (p.isEmpty()) continue;
                        String[] xy = p.split(",");
                        points.add(new Point(
                                Integer.parseInt(xy[0]),
                                Integer.parseInt(xy[1])
                        ));
                    }
                    data.snake = points.toArray(new Point[0]);
                }

                else if (line.startsWith("fruit=")) {
                    String[] xy = line.substring(6).split(",");
                    data.fruit = new Point(
                            Integer.parseInt(xy[0]),
                            Integer.parseInt(xy[1])
                    );
                }

                else if (line.startsWith("poison=")) {
                    String[] xy = line.substring(7).split(",");
                    data.poison = new Point(
                            Integer.parseInt(xy[0]),
                            Integer.parseInt(xy[1])
                    );
                }
            }
        }

        return data;
    }
}
