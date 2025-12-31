/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GameModes;

/**
 *
 * @author eurym
 */
public enum Direction {
    UP, DOWN, LEFT, RIGHT;

    public boolean isOpposite(Direction other) {
        return (this == UP && other == DOWN)
            || (this == DOWN && other == UP)
            || (this == LEFT && other == RIGHT)
            || (this == RIGHT && other == LEFT);
    }
}
