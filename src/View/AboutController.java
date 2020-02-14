package View;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * About controller
 */

public class AboutController extends MyViewController
{
    @FXML
    public Button closeButton;
    /**
     * event that close the about window
     * @param actionEvent - pressing on button event
     */
    public void closeButton(javafx.event.ActionEvent actionEvent)
    {
        Stage stage=(Stage)closeButton.getScene().getWindow();
        stage.close();
    }
}
