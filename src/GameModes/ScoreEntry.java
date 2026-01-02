package GameModes;

public class ScoreEntry {
    public String name;
    public int score;
    
    // Default for empty entry
    public ScoreEntry() {
        this.name = "---";
        this.score = 0;
    }
    
    // Polymorphism Constructor Overload
    public ScoreEntry(String name, int score) {
        this.name = name;
        this.score = score;
    }
}
