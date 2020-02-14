package View;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

/**
 * controller of the exit window
 */

public class EndGameController extends MyViewController
{
    @FXML
    private AnchorPane pane;

    /**
     * event that switch to main menu scene
     * @param actionEvent - pressing on button event
     */
    public void backMain(ActionEvent actionEvent) {
        this.player.stop();
        showEndGame=false;
        if(onOffStartSong)
        {
            player.dispose();
            ControlStartMusic();
        }
        switchScene("MyView.fxml",pane);


    }

    /**
     * event that exit from game
     * @param actionEvent - pressing on button event
     */
    public void Exit(ActionEvent actionEvent)
    {
        player.stop();
        exitPopWindow("Quit","Are you sure you want to exit?");
    }
}
