package View;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.util.Observable;

/**
 * controller of generating maze window
 * here we insert the rows and columns of maze
 */

public class inputDeatilsOfMazeController  extends MyViewController
{
    @FXML
    private TextField rows;
    @FXML
    private TextField col;
    @FXML
    public javafx.scene.control.Button closeButton;

    /**
     * event that close the about window
     */
    public void closeButtonAction()
    {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    /**
     * generate new maze array after press on button
     * generate after insert rows and columns
     * @param e-after pressed "Generate Maze" button
     */
    public void getnerateMaze(ActionEvent e) throws IOException {
        setSettings(_viewModel.getPlayerChoose(),menuBar,player);
        String realRow = rows.getText();
        String realCol = col.getText();
        Stage stage = (Stage) closeButton.getScene().getWindow();
        try {
            _viewModel.generateMaze(realRow, realCol);
           // myStage.setScene(scene);
            stage.close();
        } catch (IllegalArgumentException exeption) {
            Stage window = new Stage();
            window.setResizable(false);
            window.centerOnScreen();
            //Block events to other windows
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Error");
            window.setMinWidth(314);
            window.setMinHeight(142);
            Label label = new Label();
            Text text = new Text();
            text.setText("                        Wrong input. \n  Please enter numbers between 2 - 1000");
            text.setLayoutX(44);
            text.setLayoutY(40);
            text.setFont(Font.font("Champions", 14));
            text.setFill(Color.WHITE);
            ;
            Button yesButton = new Button("Ok");
            yesButton.setStyle("-fx-background-image: url('images/Webp.net-resizeimage.jpg'); -fx-border-color:white; -fx-text-fill:white;\n" +
                    "    -fx-font: Champions;\n" +
                    "    -fx-font-style: Bold;");
            yesButton.setLayoutX(150);
            yesButton.setLayoutY(60);
            yesButton.setOnAction(event -> window.close());

            AnchorPane layout = new AnchorPane();
            layout.setStyle("-fx-background-image: url('images/Webp.net-resizeimage.jpg');");
            layout.getChildren().addAll(yesButton, text);
            // layout.setAlignment(Pos.CENTER);

            //Display window and wait for it to be closed before returning
            Scene scene = new Scene(layout);
            window.setScene(scene);
            window.showAndWait();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
}

}