package View;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.util.ArrayList;

public class MazeDisplayer extends Canvas
{
    private int[][] maze;
    private int characterPositionRow;
    private int characterPositionColumn;
    private int goalPositionRow;
    private int goalPositionCol;


    /**
     * set maze details
     * @param maze - maze represent array
     * @param characterPositionColumn - character position inital column
     * @param characterPositionRow - character position inital row
     * @param goalPositionCol - goal column
     * @param goalPositionRow -goal row
     * @param playChoose - the choise of player character
     *
     */
    public void setMaze(int[][] maze, int characterPositionRow,int characterPositionColumn,int goalPositionRow, int goalPositionCol ,String playChoose) {
        this.maze = maze;
        this.characterPositionRow=characterPositionRow;
        this.characterPositionColumn=characterPositionColumn;
        this.goalPositionRow=goalPositionRow;
        this.goalPositionCol=goalPositionCol;
        redraw(playChoose);
    }

    /**
     * getter of position row of character
     * @return  characterPositionRow - position of charachter
     */
    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    /**
     * getter of position column of character
     * @return  characterPositionColumn - position of charachter
     */
    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }

    /**
     * get all position of solution and draw on canvas
     * @param  solution - list of points
     */
    public void drawSolution(ArrayList<int[]> solution)
    {
        if (solution != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double cellHeight = canvasHeight / maze.length;
            double cellWidth = canvasWidth / maze[0].length;
            double cellsize = Math.min(cellHeight, cellWidth);
            Image SolutionImage = new Image(ClassLoader.getSystemResourceAsStream("images/Ball.png"));
            GraphicsContext gc = getGraphicsContext2D();
            //Draw Maze
            for (int i = 0; i < solution.size(); i++) {
                if (!(solution.get(i)[0]==getCharacterPositionRow() && solution.get(i)[1]==getCharacterPositionColumn()))
                {
                    gc.drawImage(SolutionImage,solution.get(i)[1] * cellsize, solution.get(i)[0] * cellsize, cellsize, cellsize);
                }
            }
        }
    }

    /**
     * draw the maze on screen
     * @param  playChoose - choose of player
     */
    public void redraw(String playChoose) {
        if (maze != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double cellHeight = canvasHeight / maze.length;
            double cellWidth = canvasWidth / maze[0].length;
            double cellsize = Math.min(cellHeight, cellWidth);
            Image wallImage=null;
            Image characterImage=null;
            Image goalImage= new Image(ClassLoader.getSystemResourceAsStream("images/cup.png"));
            if(playChoose.equals((String)"Liverpool Fan")) {
                wallImage=  new Image(ClassLoader.getSystemResourceAsStream("images/wall2.jpg"));
                characterImage=new Image(ClassLoader.getSystemResourceAsStream("images/character1.png"));
            }
            else {
                wallImage = new Image(ClassLoader.getSystemResourceAsStream("images/wall1.jpg"));
                characterImage=new Image(ClassLoader.getSystemResourceAsStream("images/character1.png"));
            }
            GraphicsContext gc = getGraphicsContext2D();
            gc.clearRect(0, 0, getWidth(), getHeight());

            //Draw Maze{
                for (int i = 0; i < maze.length; i++) {
                    for (int j = 0; j < maze[i].length; j++) {
                        if (maze[i][j] == 1) {
                            //gc.fillRect(i * cellHeight, j * cellWidth, cellHeight, cellWidth);
                            gc.drawImage(wallImage, j * cellsize, i * cellsize, cellsize, cellsize);
                        }
                    }
                }

            gc.drawImage(characterImage, characterPositionColumn * cellsize, characterPositionRow * cellsize, cellsize, cellsize);
            gc.drawImage(goalImage, goalPositionCol * cellsize, goalPositionRow* cellsize, cellsize, cellsize);
        }
    }

}