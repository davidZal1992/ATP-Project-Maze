package Model;

import algorithms.mazeGenerators.Maze;
import java.io.Serializable;

/**
 * this class store the game corrent situation in saving
 */
public class MazeDeatils implements Serializable
{
   private Maze maze;
   private String chooslePlayer;
   private int currPositionRow;
   private  int currPositionCol;

    public MazeDeatils(Maze maze, String chooslePlayer, int currPositionRow, int currPositionCol) {
        this.maze = maze;
        this.chooslePlayer=chooslePlayer;
        this.currPositionCol=currPositionCol;
        this.currPositionRow=currPositionRow;
    }

    public void setMaze(Maze maze) {
        this.maze = maze;
    }

    public Maze getMaze() {
        return maze;
    }

    public String getChooslePlayer() {
        return this.chooslePlayer;
    }

    public int getCurrPositionRow() {
        return currPositionRow;
    }

    public int getCurrPositionCol() {
        return currPositionCol;
    }
}
