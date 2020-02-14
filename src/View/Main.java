package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import com.sun.media.jfxmedia.AudioClip;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sun.applet.AppletAudioClip;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Optional;

public class Main extends Application {
MyViewController view;
MyViewModel viewModel;
MyModel model;
static boolean letsPlay=false;
static Stage myStage;
    Button Exit = new Button ();
    Button load = new Button();
    Button save = new Button();
    @Override
    public void start(Stage primaryStage) throws Exception{
        model=new MyModel();
        myStage=primaryStage;
        viewModel=new MyViewModel(model);
        model.addObserver(viewModel);
        save.setDisable(false);
        save.setOnAction(e->
        {
            save.setDisable(true);
            view.save(e);
        });
        FXMLLoader fxm = new  FXMLLoader(getClass().getResource("MyView.fxml"));
        Parent root = (Parent) fxm.load();

        primaryStage.setTitle("Uefa Champions Maze");
        primaryStage.setResizable(true);
        Scene scene=new Scene(root);
        view = fxm.getController();
        primaryStage.setResizable(true);
        //scene.getStylesheets().add
        // (Main.class.getResource("mainBackGround.css").toExternalForm());
        primaryStage.setScene(scene);
        view.setViewModel(viewModel);
        letsPlay=true;
        load.setOnAction(e-> {
            load.setDisable(true);
            view.load(e);
        });
       // view.SwitchScene(primaryStage,"MyView");
        primaryStage.setOnCloseRequest(e->{
            e.consume();
            view.exitPopWindow("Quit","Are you sure you want to exit?");
        });
        primaryStage.show();
    }




    public static void main(String[] args) {
        launch(args);
    }
}
