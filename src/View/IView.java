package View;

import ViewModel.MyViewModel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public interface IView
{
    public void setViewModel(MyViewModel viewModel);
    public void  exitPopWindow(String title, String message);

}
