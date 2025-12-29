/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GameObjects;

/**
 *
 * @author eurym
 */
public abstract class GameMode {
    
    public double[] DIFFICULTY = {1.0, 2.0, 3.0, 4.0, 5.0};
    public Map map;
    
    public abstract void startGame();
}

class ClassicGame extends GameMode{
    ClassicGame(){
        
    }
    
    public void startGame(){
        
    }
}