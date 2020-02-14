package ViewModel;

import Model.MyModel;
import javafx.scene.input.KeyCode;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * this class connect the view to the model
 */

public class MyViewModel extends  Observable implements Observer {


   private MyModel _model;
   public MyViewModel(MyModel model)
   {
       _model=model;
   }

    /**
     * check if details are goood and sent to model
     * @param rows
     * @param columns
     */
   public void generateMaze(String rows, String columns) {

       int newRows = Integer.parseInt(rows);
       int newColumns = Integer.parseInt(columns);
       if (newRows > 1000 || newColumns > 1000 || newRows <= 0 || newColumns <= 0) {
           throw new IllegalArgumentException();
       } else {
           _model.generateMaze(newRows, newColumns);
       }

   }

    /**
     * getters to game data from the model
     * @return
     */
    public void setMediaPlayer(MediaPlayer player){
        _model.setMediaPlayer(player);
    }
    public MediaPlayer getMediaPlayer(){
        return _model.getMediaPlayer();
    }

    public int[][] getMaze()
    {
           return _model.getMaze();
    }

    public int getGoalPositionRow()
    {
        return _model.getEndPositionRow();
    }

    public int getColPositionCol()
    {
        return _model.getEndPositionCol();
    }

    public void closeProgram()
    {
        _model.closeProgram();
    }

    public void solveMaze(){_model.solveMaze();}

    public void saveMaze(String path,String newPlayerChoose){_model.saveMaze(path,newPlayerChoose);}

    public int getCharacterPositionRow()
    {
        return _model.getCharacterPositionRow();
    }

    public int getCharacterPositionColumn()
    {
        return _model.getCharacterPositionColumn();
    }

    public void loadFile(String path) throws IOException, ClassNotFoundException  { _model.loadFile(path); }

    public void checkUpdate()
    {
        _model.checkUpdate();
    }

    /**
     * updare view
     * @param o
     * @param arg
     */
    public void update(Observable o, Object arg)
    {
        setChanged();
        notifyObservers(arg);
    }
    public void setPlayerChoose(String PlayerChoose)
    {
        _model.setPlayerChoose(PlayerChoose);
    }

    public void moveCharacter(KeyCode movement)
    {
        if(movement.equals(KeyCode.NUMPAD6) || movement.equals(KeyCode.NUMPAD8) || movement.equals(KeyCode.NUMPAD2) || movement.equals(KeyCode.NUMPAD4) || movement.equals(KeyCode.NUMPAD7)|| movement.equals(KeyCode.NUMPAD9) || movement.equals(KeyCode.NUMPAD1) || movement.equals(KeyCode.NUMPAD3))
        _model.moveCharacter(movement);
        else
            return;
    }

    public boolean isFinish()
    {
        return _model.isFinish();
    }

    public ArrayList<int[]> getSolution()
    {
        return _model.getSolution();
    }

    public String getPlayerChoose()
    {
       return _model.getPlayerChoose();
    }

    public void setGenerateAlgo(String algo)
    {
        _model.setGenerateAlgo(algo);
    }

    public void setSearchAlgo(String searchAlgo)
    {
        _model.setSearchAlgo(searchAlgo);
    }
}
