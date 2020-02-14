package View;

import ViewModel.MyViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import static View.Main.letsPlay;
import static View.Main.myStage;

public class MyViewController implements IView , Observer {

    @FXML
    protected MenuItem loadFromMenu;
    @FXML
    protected Button load;
    @FXML
    protected MenuItem saveMenuIteam;
    @FXML
    protected Button start;
    @FXML
    public MenuBar menuBar;
    public AnchorPane rootPane;
    protected MyViewModel _viewModel;
    protected MediaPlayer player;
    protected String PlayerChoose;
    protected static boolean showEndGame=false;
    protected Media startSong;
    protected static boolean onOffStartSong = false;

    /**
     * set the view model to the view
     * start the music if its new game
     * @param viewModel
     */
    public void setViewModel(MyViewModel viewModel)
    {
        _viewModel = viewModel;
        if (letsPlay == false)
        {
            ControlStartMusic();
            letsPlay=true;
        }
    }

    /**
     * exit pop window
     * @param title
     * @param message
     */
    public void exitPopWindow(String title, String message)
    {
        Stage window = new Stage();
        window.centerOnScreen();
        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(314);
        window.setMinHeight(142);
        Label label = new Label();
        label.setText(message);
        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");
        Text text = new Text();
        text.setText("Are you sure you want to exit?");
        text.setLayoutX(66);
        text.setLayoutY(40);
        text.setFont(Font.font("Champions", 14));
        text.setFill(Color.WHITE);
        yesButton.setLayoutX(93);
        yesButton.setLayoutY(60);
        noButton.setLayoutX(192);
        noButton.setLayoutY(60);
        yesButton.setStyle("-fx-background-image: url('images/Webp.net-resizeimage.jpg'); -fx-border-color:white; -fx-text-fill:white;\n" +
                "    -fx-font: Champions;\n" +
                "    -fx-font-style: Bold;");
        noButton.setStyle("-fx-background-image: url('images/Webp.net-resizeimage.jpg'); -fx-border-color:white; -fx-text-fill:white;\n" +
                "    -fx-font: Champions;\n" +
                "    -fx-font-style: Bold;");
        noButton.setOnAction(e -> window.close());
        yesButton.setOnAction(e -> {
            _viewModel.closeProgram();
            System.exit(0);
        });
        AnchorPane layout = new AnchorPane();//Platform.exit();
        layout.setStyle("-fx-background-image: url('images/Webp.net-resizeimage.jpg');");
        layout.getChildren().addAll(yesButton, noButton, text);
        // layout.setAlignment(Pos.CENTER);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    /**
     * start music
     */
    public void ControlStartMusic()
    {
        onOffStartSong = true;
        startSong = new Media(getClass().getClassLoader().getResource("songs/uefa.mp3").toExternalForm());
        _viewModel.setMediaPlayer(new MediaPlayer(startSong));
        player=_viewModel.getMediaPlayer();
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.play();
    }

    /**
     * load old game
     * @param e
     */
    public void load(ActionEvent  e)
    {
        e.consume();
        load.setDisable(true);
        start.setDisable(true);
        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(null);
        if(selectedFile!=null)
        {
            getMenu().getMenus().get(0).getItems().get(0).setDisable(true);
            getMenu().getMenus().get(0).getItems().get(2).setDisable(true);
            String path = selectedFile.getAbsolutePath();
            addToListen("ChooseCharacter.fxml");
            try
            {
                _viewModel.loadFile(path);
                Stage sg=(Stage)load.getScene().getWindow();
                sg.close();
            }
            catch (IOException exception)
            {
                Alert alert = new Alert(Alert.AlertType.WARNING , "you try to load invalid file" +
                        " please try again");
                alert.show();
                getMenu().getMenus().get(0).getItems().get(0).setDisable(false);
                getMenu().getMenus().get(0).getItems().get(2).setDisable(false);
                load.setDisable(false);
                start.setDisable(false);

            }
            catch (ClassNotFoundException ex)
            {
                Alert alert = new Alert(Alert.AlertType.WARNING , "you try to load invalid file" +
                        " please try again");
                alert.show();
                getMenu().getMenus().get(0).getItems().get(0).setDisable(false);
                getMenu().getMenus().get(0).getItems().get(2).setDisable(false);
                load.setDisable(false);
                start.setDisable(false);
            }
        }
        else
        {
            getMenu().getMenus().get(0).getItems().get(0).setDisable(false);
            getMenu().getMenus().get(0).getItems().get(2).setDisable(false);
            load.setDisable(false);
            start.setDisable(false);
        }
    }

    /**
     * save the game
     * @param e
     */
    public void save(ActionEvent e)
    {
        e.consume();
        FileChooser fc;
        fc = new FileChooser();
        File savedFile = fc.showSaveDialog(null);
        if (savedFile != null)
        {
            savedFile.getAbsolutePath();
            _viewModel.saveMaze(savedFile.getAbsolutePath(), _viewModel.getPlayerChoose());
        }
    }

    /**
     * pop help window
     * @param e
     */
    public void help(ActionEvent e)
    {
        newPopScene("Help.fxml");
        e.consume();
    }

    /**
     * swith scenes in the game
     * @param string
     * @param container
     */
    public void switchScene(String string, AnchorPane container) {
        try {
            myStage = (Stage) container.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    string));
            Parent root = (Parent) loader.load();
            Scene scene = new Scene(root);
            myStage.setScene(scene);
            myStage.setResizable(true);
            myStage.setTitle(getStageTitle(string));
            MyViewController view = loader.getController();
            view.setViewModel(this._viewModel);
            myStage.setOnCloseRequest(e->{
                e.consume();
                view.exitPopWindow("Quit","Are you sure you want to exit?");
            });
            myStage.centerOnScreen();
            myStage.show();
            _viewModel.addObserver(view);
            view.setSettings(getPlayerChoose(),getMenu(),_viewModel.getMediaPlayer());
        } catch (IOException e) {
        }
    }

    /**
     * move to the view model if generate algorithm as change
     * @param e
     */
    public void selectGenerateAlgo(ActionEvent e)
    {
        e.consume();
        if(e.getSource().toString().contains("empty"))
        {
            _viewModel.setGenerateAlgo("Empty");
            return;
        }
        if(e.getSource().toString().contains("Simple"))
        {
            _viewModel.setGenerateAlgo("simple");
            return;
        }
        if(e.getSource().toString().contains("complicated"))
        {
            _viewModel.setGenerateAlgo("MyMazeGenerator");
            return;
        }
    }

    /**
     * move to the view model if searching algorithm as change
     * @param e
     */
    public void selectSearchingAlgo(ActionEvent e)
    {
        e.consume();
        if(e.getSource().toString().contains("BFS"))
        {
            _viewModel.setSearchAlgo("BFS");
            return;
        }
        if(e.getSource().toString().contains("DFS"))
        {
            _viewModel.setSearchAlgo("DFS");
            return;
        }
        if(e.getSource().toString().contains("Best"))
        {
            _viewModel.setSearchAlgo("Best");
            return;
        }
    }

    /**
     * change the stage title in switch scene
     * @param fxml
     * @return
     */
    public String getStageTitle(String fxml)
    {
        String ans="";
        if(fxml=="EndGame.fxml")
        {
            ans="finish game";
            return ans;
        }
        if(fxml=="Help.fxml")
        {
            ans="HELP";
            return ans;
        }
        if(fxml=="inputDetailsOfMaze.fxml")
        {
            ans="insert maze details";
            return ans;
        }
        if(fxml=="About.fxml")
        {
            ans="About";
            return ans;
        }
        ans="Uefa Champions Maze";
        return ans;
    }

    /**
     * make a new pop scene by fxml
     * @param string
     */
    public void newPopScene(String string)
    {
        try {
            FXMLLoader root = new FXMLLoader(getClass().getResource(string));
            Parent rootl = null;
            rootl = (Parent) root.load();
            Stage stage = new Stage();
            String title=getStageTitle(string);
            stage.setTitle(title);
            Scene scene = new Scene(rootl);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setMaximized(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            MyViewController view=root.getController();
            view.setViewModel(_viewModel);
            _viewModel.addObserver(view);
            view.setSettings(getPlayerChoose(),getMenu(),_viewModel.getMediaPlayer());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * about window
     * @param actionEvent
     * @throws IOException
     */

    public void About(ActionEvent actionEvent ) throws IOException
    {
        newPopScene("About.fxml");
        actionEvent.consume();
    }

    public void addToListen(String string)
    {
        FXMLLoader fxmlLoader = new FXMLLoader();
        try {
         Parent   root = (Parent)fxmlLoader.load(getClass().getResource(string).openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        MyViewController view = fxmlLoader.getController();
        view.setViewModel(this._viewModel);
        _viewModel.addObserver(view);
        view.setSettings(PlayerChoose,menuBar,_viewModel.getMediaPlayer());
    }

    public void FromMainSceneToOthers(String string, Pane rootPane,String css)
    {
        FXMLLoader fxmlLoader = new FXMLLoader();
        AnchorPane root = null;
        try {
            root = fxmlLoader.load(getClass().getResource(string).openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        rootPane.getChildren().setAll(root);
        if(!css.equals(""))
        {
            rootPane.getStylesheets().add(getClass().getResource(css).toExternalForm());
        }
        MyViewController view = fxmlLoader.getController();
        view.setViewModel(this._viewModel);
        _viewModel.addObserver(view);
        view.setSettings(PlayerChoose,menuBar,_viewModel.getMediaPlayer());
    }

    public void setSettings(String choose,MenuBar menuBar,MediaPlayer player)
    {
        PlayerChoose=choose;
        this.menuBar=menuBar;
        this.player=player;
    }

    public String getPlayerChoose()
    {
        return PlayerChoose;
    }

    public MenuBar getMenu()
    {
        return menuBar;
    }

    public void OnOffStartMusic(ActionEvent e)
    {
        e.consume();
       if(onOffStartSong==false) {
           onOffStartSong = true;
           player.play();
       }
       else {
           onOffStartSong=false;
           player.pause();
       }
    }


    public void characterSelect(ActionEvent e) throws IOException
    {
        e.consume();
        FromMainSceneToOthers("ChooseCharacter.fxml",this.rootPane,"");
    }

    /**
     * pop Exit window
     * @param actionEvent
     * @throws IOException
     */
    public void Exit(ActionEvent actionEvent ) throws IOException
    {
      exitPopWindow("Quit","Are you sure you want to exit?");
    }


    @Override
    public void update(Observable o, Object arg )
    {

    }
}