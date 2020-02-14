package Model;

import Client.Client;
import Client.IClientStrategy;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import algorithms.mazeGenerators.Maze;
import algorithms.search.*;
import javafx.scene.input.KeyCode;
import Server.Configurations;
import javafx.scene.media.MediaPlayer;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;

/**
 * this class represent the model class in the game
 * the game logic set in this class and connect to partB
 */

public class MyModel extends Observable implements  IModel
{
    private Server mazeGeneratingServer;
    private Server solveSearchProblemServer;
    private Maze maze;
    private int characterPositionRow;
    private int characterPositionColumn;
    private String playerChoose;
    Solution mazeSolution;
    protected MediaPlayer player;
    ArrayList<int[]> integerSolution;


    /**
     * raise servers
     */
    public MyModel()
    {
        mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        solveSearchProblemServer.start();
        mazeGeneratingServer.start();
    }

    /**
     * shut down servers and close the program
     */
    public void closeProgram()
    {
            mazeGeneratingServer.stop();
            solveSearchProblemServer.stop();
            Configurations.setGeneratorMaze("MyMazeGenerator");
            Configurations.setSearchingAlgorithm("Best");
    }

    /**
     * connect to server and update the maze solution
     */
    public void solveMaze()
    {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy()
            {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        toServer.writeObject(maze); //send maze to server
                        toServer.flush();
                        mazeSolution = (Solution) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        setChanged();
        notifyObservers("drawSolution");
    }

    /**
     * connect to server and generate maze
     * @param rows
     * @param columns
     */

    public void generateMaze(int rows, int columns) {
        Client client = null;
        try {
            client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{rows, columns};
                        toServer.writeObject(mazeDimensions); //send maze dimensions to server
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[rows*columns+15]; //allocating byte[] for the decompressed maze -
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        maze = new Maze(decompressedMaze);
                        characterPositionRow=maze.getStartPosition().getRowIndex();
                        characterPositionColumn=maze.getStartPosition().getColumnIndex();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        setChanged();
        notifyObservers("maze");
    }

    /**
     * update all ovservers
     */
    public void checkUpdate(){
        setChanged();
        notifyObservers();
    }

    /**
     * load old game from player path
     * @param path
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void loadFile(String path) throws IOException,ClassNotFoundException
    {
        File checkingFile;
        checkingFile = new File(path);
        if(checkingFile.exists())
        {
            FileInputStream fileIn = new FileInputStream(checkingFile);
            ObjectInputStream oin=new ObjectInputStream(fileIn);
            MazeDeatils loadedMaze = (MazeDeatils) oin.readObject();
            if(loadedMaze!=null)
            {
                maze=loadedMaze.getMaze();
                playerChoose=loadedMaze.getChooslePlayer();
                characterPositionRow=loadedMaze.getCurrPositionRow();
                characterPositionColumn=loadedMaze.getCurrPositionCol();
                setChanged();
                notifyObservers("loadmaze");
            }
        }
    }

    /**
     * check if game is finish
     * @return
     */
    public boolean isFinish()
    {
        if(getCharacterPositionColumn()==getEndPositionCol() && getEndPositionRow()==characterPositionRow)
        {
            return true;
        }
        return false;
    }

    /**
     * save the maze in player computer by path
     * @param path
     * @param playerChoose
     */

    public void saveMaze(String path,String playerChoose)
    {
        File file=new File(path);
        ObjectOutputStream outToFile ;
        try {
            this.playerChoose=playerChoose;
            FileOutputStream myFile=new FileOutputStream(file);
            outToFile = new ObjectOutputStream(myFile);
            MazeDeatils mazeDeatils=new MazeDeatils(maze,playerChoose,getCharacterPositionRow(),getCharacterPositionColumn());
            outToFile.writeObject(mazeDeatils);
            outToFile.flush();
            outToFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * move the character by player decision
     * @param movement
     */

    public void moveCharacter(KeyCode movement) {
        switch (movement) {
            case NUMPAD8:
                if(characterPositionRow-1>=0 && getMaze()[characterPositionRow-1][characterPositionColumn]!=1)
                    characterPositionRow--;
                break;
            case NUMPAD2:
                if(characterPositionRow+1<getMaze().length && getMaze()[characterPositionRow+1][characterPositionColumn]!=1)
                    characterPositionRow++;
                break;
            case NUMPAD6:
                if(characterPositionColumn+1<=maze.getColumns()-1 && getMaze()[characterPositionRow][characterPositionColumn+1]!=1)
                    characterPositionColumn++;
                break;
            case NUMPAD4:
                if(characterPositionColumn-1>=0 && getMaze()[characterPositionRow][characterPositionColumn-1]!=1)
                    characterPositionColumn--;
                break;
            case NUMPAD1:
                if((characterPositionColumn-1>=0 && characterPositionRow+1<maze.getRows())&& maze.getValue(characterPositionRow+1,characterPositionColumn-1)!=1)
                {
                    if(maze.getValue(characterPositionRow+1,characterPositionColumn)!=1||maze.getValue(characterPositionRow,characterPositionColumn-1)!=1)
                    {
                        characterPositionRow++;
                        characterPositionColumn--;
                    }
                }
                break;
            case NUMPAD3:
                if((characterPositionColumn+1<maze.getColumns() && characterPositionRow+1<maze.getRows())&& maze.getValue(characterPositionRow+1,characterPositionColumn+1)!=1)
                {
                    if(maze.getValue(characterPositionRow,characterPositionColumn+1)!=1||maze.getValue(characterPositionRow+1,characterPositionColumn)!=1)
                    {
                        characterPositionRow++;
                        characterPositionColumn++;
                    }
                }
                break;
            case NUMPAD9:
                if((characterPositionColumn+1<maze.getColumns() && characterPositionRow-1>=0)&& maze.getValue(characterPositionRow-1,characterPositionColumn+1)!=1)
                {
                    if(maze.getValue(characterPositionRow,characterPositionColumn+1)!=1||maze.getValue(characterPositionRow-1,characterPositionColumn)!=1)
                    {
                        characterPositionRow--;
                        characterPositionColumn++;
                    }
                }
                break;
            case NUMPAD7:
                if((characterPositionColumn-1>=0 && characterPositionRow-1>=0)&& maze.getValue(characterPositionRow-1,characterPositionColumn-1)!=1)
                {
                    if(maze.getValue(characterPositionRow,characterPositionColumn-1)!=1||maze.getValue(characterPositionRow-1,characterPositionColumn)!=1)
                    {
                        characterPositionRow--;
                        characterPositionColumn--;
                    }
                }
                break;
        }
        setChanged();
        notifyObservers();
    }

    /**
     * getters to game data
     *
     */
    public int getStartPosition()
    {
        return maze.getStartPosition().getRowIndex();
    }
    public int getStartPositionCol()
    {
        return maze.getStartPosition().getColumnIndex();
    }
    public int getEndPositionRow()
    {
        return maze.getGoalPosition().getRowIndex();
    }
    public int getEndPositionCol()
    {
        return this.maze.getGoalPosition().getColumnIndex();
    }
    public int[][] getMaze()
    {
           return maze.getMaze();
    }
    public int getCharacterPositionRow() {
        return characterPositionRow;
    }
    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }

    /**
     * make ArrayList<int[]> for print solution on game view
     * @return
     */
    public ArrayList<int[]> getSolution()
    {
        if(mazeSolution!=null)
        {
            ArrayList<AState> sol =mazeSolution.getSolutionPath();
            integerSolution = new ArrayList<>();
            for(int i=0; i<sol.size();i++)
            {
                MazeState position = (MazeState)sol.get(i);
                if(!(position.getCurrenetPosition().getRowIndex()==maze.getGoalPosition().getRowIndex()&& position.getCurrenetPosition().getColumnIndex()==maze.getGoalPosition().getColumnIndex()))
                {
                    int [] integerPosition = new int [2];
                    integerPosition[0]=position.getCurrenetPosition().getRowIndex();
                    integerPosition[1]=position.getCurrenetPosition().getColumnIndex();
                    integerSolution.add(integerPosition);
                }

            }
        }
        return integerSolution;
    }
    public String getPlayerChoose()
    {
        return playerChoose;
    }
    public void setPlayerChoose(String newPlayerChoose)
    {
        playerChoose=newPlayerChoose;
    }
    public void setMediaPlayer(MediaPlayer player){
        this.player=player;
    }
    public MediaPlayer getMediaPlayer(){
        return player;
    }

    /**
     * write the player choose generate algorithm to the properties file
     * @param algo
     */
    public void setGenerateAlgo(String algo)
    {
        Configurations.setGeneratorMaze(algo);
    }
    /**
     * write the player choose searching algorithm to the properties file
     * @param searchAlgo
     */
    public void setSearchAlgo(String searchAlgo)
    {
        Configurations.setSearchingAlgorithm(searchAlgo);
    }
}
