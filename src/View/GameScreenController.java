package View;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import java.util.Observable;

/**
 * controller of the gameplay screen.
 * here you can find all functionality as "solve" "hide" and more
 */
public class GameScreenController extends MyViewController {
    @FXML
    public MazeDisplayer mazeDisplayer;
    @FXML
    private BorderPane stage;
    @FXML
    private AnchorPane pane;
    private boolean showSolution = false;
    @FXML
    private Button solve;
    @FXML
    private Button hide;

    /**
     * set the all maze details
     * @param maze - maze that represent in array
     */

    public void displayMaze(int[][] maze) {
        mazeDisplayer.setMaze(maze, _viewModel.getCharacterPositionRow(), _viewModel.getCharacterPositionColumn(), _viewModel.getGoalPositionRow(), _viewModel.getColPositionCol(), getPlayerChoose());
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == _viewModel && _viewModel.isFinish() == false && arg == null) {
            displayMaze(_viewModel.getMaze());
            if (showSolution == true) {
                mazeDisplayer.drawSolution(_viewModel.getSolution());
            }
        } else if (arg == null) {
            finishGame();
        } else {
            if (arg.equals("drawSolution")) {
                mazeDisplayer.drawSolution(_viewModel.getSolution());
            }
        }
    }

    /**
     * Key pressed event
     * press key to move left down right up or or up right up left down right down left and the function will handle
     * @param keyEvent - the key that user pressed
     */
    public void KeyPressed(KeyEvent keyEvent) {
        _viewModel.moveCharacter(keyEvent.getCode());
        keyEvent.consume();
    }

    /**
     * check if finish game
     * if game finish call to new controller and switch scene
     */
    public void finishGame() {
        if (!showEndGame) {
            showEndGame = true;
            if(onOffStartSong)
            {
                player.stop();
                Media finishSong = new Media(getClass().getClassLoader().getResource("songs/Merged_Goal-_Queen.mp3").toExternalForm());
                _viewModel.setMediaPlayer(new MediaPlayer(finishSong));
                player=_viewModel.getMediaPlayer();
                player.play();
            }
            newPopScene("EndGame.fxml");
            Stage sg = (Stage) stage.getScene().getWindow();
            sg.close();
        }
    }

    /**
     * solve maze and show the solution on screen
     * @param  actionEvent - button "solve" press event
     */

    public void solve(ActionEvent actionEvent)
    {   actionEvent.consume();
        showSolution = true;
        hide.setDisable(false);
        solve.setDisable(true);
        _viewModel.solveMaze();
    }

    /**
     * event that switch to main menu scene
     * @param actionEvent - pressing on button event
     */
    public void backMain(ActionEvent actionEvent)
    {
        actionEvent.consume();
        switchScene("MyView.fxml", pane);
    }

    /**
     * hide the solution that had shown on screen
     * @param  actionEvent - button "hide" press event
     */
    public void hide(ActionEvent actionEvent)
    {
        actionEvent.consume();
        showSolution = false;
        hide.setDisable(true);
        solve.setDisable(false);
        _viewModel.checkUpdate();
    }

    /**
     * resize lisetner that make sure if user resize the screen so all objects will resize automaticly
     * @param  scene - the scene that listen
     */
    public void setResizeEvent(Scene scene) {
        long width = 0;
        long height = 0;
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                mazeDisplayer.setWidth( mazeDisplayer.getWidth() + ( newSceneWidth.doubleValue() - oldSceneWidth.doubleValue() ));
                mazeDisplayer.redraw(_viewModel.getPlayerChoose());

            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                mazeDisplayer.setHeight( mazeDisplayer.getHeight() + ( newSceneHeight.doubleValue() - oldSceneHeight.doubleValue() ));
                mazeDisplayer.redraw(_viewModel.getPlayerChoose());
            }
        });
    }

    /**
     * scroll event zoom in\out
     * @param  scroll - scroll event that respone on zoom in out work only whhle ctrl button press
     */
    public void scrollEvent(ScrollEvent scroll) {
        if(!scroll.isControlDown()) {
            return;
        }
        double zoom_fac = 1.05;
        double delta_y = scroll.getDeltaY();
        if (delta_y < 0) {
            zoom_fac = 2.0 - zoom_fac;
        }
        Scale myScale = new Scale();
        myScale.setPivotX(scroll.getSceneX());
        myScale.setPivotY(scroll.getSceneY());
        myScale.setX(mazeDisplayer.getScaleX() * zoom_fac);
        myScale.setY(mazeDisplayer.getScaleY() * zoom_fac);
        mazeDisplayer.getTransforms().add(myScale);
        scroll.consume();
    }

}

