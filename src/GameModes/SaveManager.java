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
        public int mode;
        public int difficulty;
        public int score;
        public Direction direction;
        public Point[] snake;
        public Point fruit;
        public Point poison; // nullable
        
        SaveData(int mode, int difficulty, int score, Direction direction, Point[] point, Point fruit, Point poison){
            this.mode = mode;
            this.difficulty = difficulty;
            this.score = score;
            this.direction = direction;
            this.snake = point;
            this.fruit = fruit;
            this.poison = poison;
        }
        
        SaveData(){
            
        }
    }
    
    public static void saveGame(SaveData save) throws IOException {
        File file = new File(System.getProperty("user.home"), "save.txt");

        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
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
    
    public static SaveData loadGame() throws IOException {
        SaveData data = new SaveData();

        InputStream is = SaveManager.class.getResourceAsStream("/GameModes/save.txt");

        if (is == null) {
            System.out.println("save.txt not found in resources!");
            return data;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) {

                if (line.startsWith("mode=")) {
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
    
    public static enum Mode{
        classic, wall, poison, maze
    };

    public static class ScoresSheets {
        public int[] classic = new int[5];
        public int[] wall = new int[5];
        public int[] poison = new int[5];
        public int[] maze = new int[5];
        
        ScoresSheets(int[] classic, int[] wall, int[] poison, int[] maze){
            for (int i = 0; i < 5; i++){
                this.classic[i] = classic[i];
                this.wall[i] = wall[i];
                this.poison[i] = poison[i];
                this.maze[i] = maze[i];
            }
        }
        
        ScoresSheets() {
            int[] zero = {0, 0, 0, 0, 0};
            this(zero, zero, zero, zero);
        }
        
        public void save(Mode mode, int score){
            switch(mode){
                case Mode.classic: addScore(this.classic, score);
                case Mode.wall: addScore(this.wall, score);
                case Mode.poison: addScore(this.poison, score);
                case Mode.maze: addScore(this.maze, score);
            }
        }
        
        private static void addScore(int[] scores, int newScore) {
            int minIndex = 0;
            for (int i = 1; i < scores.length; i++) {
                if (scores[i] < scores[minIndex]) {
                    minIndex = i;
                }
            }
            
            if (newScore < scores[minIndex])
                return;

            if (newScore > scores[minIndex]) {
                scores[minIndex] = newScore;
                Arrays.sort(scores);      // ascending
                reverse(scores);          // make it descending
            }
        }

        private static void reverse(int[] arr) {
            for (int i = 0; i < arr.length / 2; i++) {
                int temp = arr[i];
                arr[i] = arr[arr.length - 1 - i];
                arr[arr.length - 1 - i] = temp;
            }
        }
    }
    
    public void saveScores(ScoresSheets scores) throws IOException{
        File file = new File(System.getProperty("user.home"), "save.txt");

        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            for (int i = 0; i < 5; i++){
                StringBuilder sb = new StringBuilder();
                sb.append(scores.classic[i]).append(",")
                  .append(scores.wall[i]).append(",")
                  .append(scores.poison[i]).append(",")
                  .append(scores.maze[i]).append(",");
                pw.println(sb.toString());
            }
        }
    }
    
    public ScoresSheets loadScores() throws IOException {
        File file = new File(System.getProperty("user.home"), "save.txt");

        ScoresSheets scores = new ScoresSheets();

        // If file does not exist, return empty scores (all zeros)
        if (!file.exists()) {
            return scores;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int row = 0;

            while ((line = br.readLine()) != null && row < 5) {
                String[] parts = line.split(",");

                if (parts.length >= 4) {
                    scores.classic[row] = Integer.parseInt(parts[0]);
                    scores.wall[row]    = Integer.parseInt(parts[1]);
                    scores.poison[row]  = Integer.parseInt(parts[2]);
                    scores.maze[row]    = Integer.parseInt(parts[3]);
                }

                row++;
            }
        }

        return scores;
    }
}
