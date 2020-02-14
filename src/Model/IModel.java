package Model;

import javafx.scene.input.KeyCode;

/**
 * each model must have this functions
 */
public interface IModel
{

    //Maze
    void generateMaze(int width, int height);
    int[][] getMaze();

    //Character
    void moveCharacter(KeyCode movement);
    int getCharacterPositionRow();
    int getCharacterPositionColumn();

    //
    void closeProgram();
}
