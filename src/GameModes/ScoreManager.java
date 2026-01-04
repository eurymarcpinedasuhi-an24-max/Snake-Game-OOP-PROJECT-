package GameModes;

import java.io.*;


// Handles scores persistence
public class ScoreManager {
    private static final String SCORES_FILE = "highscores.txt"; // File name constant
    private static final int MAX_SCORES = 5; // Top 5 scores only
    

    public static ScoreEntry[] loadScores() {
        ScoreEntry[] scores = new ScoreEntry[MAX_SCORES];
        
        // Initialize with empty entries 
        for (int i = 0; i < MAX_SCORES; i++) {
            scores[i] = new ScoreEntry();
        }
        
        File file = new File("src/GameModes/" + SCORES_FILE);
        if (!file.exists()) {
            return scores; // Return defaults if no file exists
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int index = 0;
            while ((line = reader.readLine()) != null && index < MAX_SCORES) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String name = parts[0].trim();
                    int score = Integer.parseInt(parts[1].trim());
                    scores[index] = new ScoreEntry(name, score);
                    index++;
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading scores: " + e.getMessage());
        }
        
        return scores;
    }
    
    // Save scores array to src/GameModes/highscores.txt
    public static void saveScores(ScoreEntry[] scores) throws IOException {
        File file = new File("src/GameModes/" + SCORES_FILE);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs(); // Create directory if it doesn't exist
        }
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (ScoreEntry entry : scores) {
                if (entry != null && entry.score > 0) {
                    writer.println(entry.name + "," + entry.score); // CSV format: name,score
                }
            }
        }
    }
    
    // Add a new score and maintain sorted order (highest first)
    public static boolean addScore(String name, int score, int gameMode) {
        ScoreEntry[] scores = loadScores();
        
        // Find position to insert new score
        int insertPos = -1; // -1 means not high enough
        for (int i = 0; i < MAX_SCORES; i++) {
            if (score > scores[i].score) { // Higher score found
                insertPos = i; // Found position
                break;
            }
        }
        
        if (insertPos == -1) {
            return false; // Score not high enough to make top 5
        }
        
        // Shift lower scores down
        for (int i = MAX_SCORES - 1; i > insertPos; i--) {
            scores[i] = scores[i - 1];
        }
        
        // Insert new score at correct position
        scores[insertPos] = new ScoreEntry(name, score);
        
        try {
            saveScores(scores);
            return true;
        } catch (IOException e) {
            System.err.println("Error saving score: " + e.getMessage());
            return false;
        }
    }
    
    // Clear all scores (reset to defaults)
    public static void clearAllScores() throws IOException {
        ScoreEntry[] emptyScores = new ScoreEntry[MAX_SCORES];
        for (int i = 0; i < MAX_SCORES; i++) {
            emptyScores[i] = new ScoreEntry(); // Reset to "---", 0
        }
        saveScores(emptyScores);
    }
}