package View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import java.io.IOException;
import java.util.Observable;
import static View.Main.myStage;

/**
 * Controller of the Character selector maze
 * here we choose the character
 */

public class ChooseCharacterController extends MyViewController
{

    public   AnchorPane container;
    public Pane display;

    /**
     * event that switch to character select window
     * @param actionEvent - pressing on button event
     */

    public void insertDetailsOfMeaze(javafx.event.ActionEvent actionEvent)  throws IOException {
        String playerChoose =((Button)actionEvent.getSource()).getText();
        setSettings(playerChoose,menuBar,player);
        _viewModel.setPlayerChoose(playerChoose);
        newPopScene("inputDetailsOfMaze.fxml");
    }
    /**
     * event that switch to main menu scene
     * @param actionEvent - pressing on button event
     */
    public void backToMain(javafx.event.ActionEvent actionEvent)  throws IOException
    {
       switchScene("MyView.fxml",container);
    }

    /**
     * function that call to game screen controller
     * open the screen that the maze will shown
     */
    public void letsStart() throws IOException {
        FXMLLoader fxm = new  FXMLLoader(getClass().getResource("GameScreen.fxml"));
        Parent root = (Parent) fxm.load();
        //Stage sg=new Stage();
       // sg.setResizable(true);
        Scene scene=new Scene(root);
        ((BorderPane)scene.getRoot()).setTop(getMenu());
        getMenu().getMenus().get(0).getItems().get(1).setDisable(false);
        getMenu().getMenus().get(0).getItems().get(0).setDisable(true);
        getMenu().getMenus().get(0).getItems().get(2).setDisable(true);
        getMenu().getMenus().get(1).getItems().get(0).setDisable(true);
        getMenu().getMenus().get(0).getItems().get(1).setDisable(false);
        MyViewController view = fxm.getController();
        view.setSettings(getPlayerChoose(),getMenu(),_viewModel.getMediaPlayer());
       //sg.setResizable(true);
       myStage.setScene(scene);
        view.setViewModel(_viewModel);
        ((GameScreenController)view).setResizeEvent(scene);
        _viewModel.addObserver(view);
        myStage.show();
        scene.getRoot().requestFocus();
       // myStage.close();0
        _viewModel.checkUpdate();
        myStage.setOnCloseRequest(e->{
            e.consume();
            view.exitPopWindow("Quit","Are you sure you want to exit?");
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        try {
            if(arg==("maze")||arg==("loadmaze")) {
                if (getPlayerChoose() == null)
                    PlayerChoose = _viewModel.getPlayerChoose();
                letsStart();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}