/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.gui.controller;

import java.io.File;
import java.net.URL;
import java.util.EventObject;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import privatemoviecollection.be.Movie;
import privatemoviecollection.bll.util.TimeConverter;

/**
 * FXML Controller class
 *
 * @author Acer
 */
public class MoviePlayerViewController implements Initializable {
    
    private Movie movieToPlay;
    private MediaPlayer mediaPlayer;
    private double previousVolume;
    private boolean movieTimeChanged = false;
    private boolean buttonPlaySelected;
    private double xOffset;
    private double yOffset;
    
    @FXML
    private MediaView mdvPlayer;
    @FXML
    private Slider sldTime;
    @FXML
    private Slider sldVolume;
    @FXML
    private ToggleButton btnMute;
    @FXML
    private Label lblMovieCurrentTime;
    @FXML
    private Label lblMovieEndTime;
    @FXML
    private ToggleButton btnPlay;
    @FXML
    private Button btnClose;
    @FXML
    private Label lblTitle;
    @FXML
    private Label lblSlash;
    @FXML
    private Rectangle rctBottom;
    @FXML
    private Rectangle rctTop;
    @FXML
    private ToggleButton btnFullscreen;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        createVolumeSliderListener();
        createTimeSliderListener();
        setPlayButton();
        setMediaView();
    }   
    
    private void createTimeSliderListener()
    {
        sldTime.valueProperty().addListener(new ChangeListener()
            {
                @Override
                public void changed(ObservableValue arg0, Object arg1, Object arg2)
                {
                    lblMovieCurrentTime.setText(TimeConverter.convertToString((int)sldTime.getValue()));
                }
            }     
        );
    }
    
    private void createVolumeSliderListener()
    {
        sldVolume.valueProperty().addListener(new ChangeListener()
            {
                @Override
                public void changed(ObservableValue arg0, Object arg1, Object arg2)
                {
                    if(btnMute.isSelected() && sldVolume.getValue() != 0)
                    {
                        btnMute.setSelected(false);
                    }
                    else if(!btnMute.isSelected() && sldVolume.getValue() == 0)
                    {
                        previousVolume=0;
                        btnMute.setSelected(true);
                    }
                    if(mediaPlayer != null)
                    {
                        mediaPlayer.setVolume(sldVolume.getValue());
                    }
                }
            }     
        );
    }
    
    public void playMovie(Movie movie)
    {
        movieToPlay = movie;
        setMediaPlayer(movie);
        setMediaElements(movie);
        mediaPlayer.play();
    }
    
    private void setMediaView()
    {
        DoubleProperty mvw = mdvPlayer.fitWidthProperty();
        DoubleProperty mvh = mdvPlayer.fitHeightProperty();
        mvw.bind(Bindings.selectDouble(mdvPlayer.sceneProperty(), "width"));
        mvh.bind(Bindings.selectDouble(mdvPlayer.sceneProperty(), "height"));
        mdvPlayer.setPreserveRatio(true);
    }
    
    private void setMediaPlayer(Movie movie)
    {
        File file = new File(movie.getPath());
        Media mediaFile = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(mediaFile);
        mediaPlayer.setVolume(sldVolume.getValue());
    }
    
    private void setMediaElements(Movie movie)
    {
        setTimeListener();
        sldTime.setMax(movie.getTime());
        lblTitle.setText(movie.getTitle());
        lblMovieEndTime.setText(movie.getTimeInString());
        mdvPlayer.setMediaPlayer(mediaPlayer);
    }
    
    private void setTimeListener()
    {
        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>()
            {
                @Override
                public void changed(ObservableValue arg0, Duration arg1, Duration arg2)
                {
                    if(!sldTime.isPressed() && !movieTimeChanged)
                    {
                        sldTime.setValue(arg2.toSeconds());
                    }
                    else if(movieTimeChanged)
                        movieTimeChanged=false;
                }
            }
        );
    }

    /**
     * Switches the image of Play/Stop button.
     */
    private void switchPlayButton(boolean buttonClicked)
    {
        if(buttonClicked)
        {
            if(buttonPlaySelected)
            {
                btnPlay.setGraphic(new ImageView("/privatemoviecollection/gui/images/PlayButtonLarge.png"));
                buttonPlaySelected = false;
            }
            else
            {
                btnPlay.setGraphic(new ImageView("/privatemoviecollection/gui/images/StopButtonLarge.png"));
                buttonPlaySelected = true;
            }
        }
        else
        {
            if(buttonPlaySelected)
            {
                btnPlay.setGraphic(new ImageView("/privatemoviecollection/gui/images/PlayButtonSmall.png"));
                buttonPlaySelected = false;
            }
            else
            {
                btnPlay.setGraphic(new ImageView("/privatemoviecollection/gui/images/StopButtonSmall.png"));
                buttonPlaySelected = true;
            }
        }
    }
    
    /**
     * Sets the image and invokes methods for setting animations of play/stop button.
     */
    private void setPlayButton()
    {
        btnPlay.setGraphic(new ImageView("/privatemoviecollection/gui/images/StopButtonSmall.png"));
        buttonPlaySelected = true;
        setPlayButtonHoverIn();
        setPlayButtonHoverOut();
    }
    
     /**
     * Sets the Hover in animation of play/stop button.
     */
    private void setPlayButtonHoverIn()
    {
        btnPlay.setOnMouseEntered(new EventHandler() 
            {
                @Override
                public void handle(Event event) 
                {
                    if(buttonPlaySelected)
                    {
                        btnPlay.setGraphic(new ImageView("/privatemoviecollection/gui/images/StopButtonLarge.png"));
                    }
                    else
                    {
                        btnPlay.setGraphic(new ImageView("/privatemoviecollection/gui/images/PlayButtonLarge.png"));
                    }
                }
            
            }
        );
    }
    
    /**
     * Sets the Hover out animation of play/stop button
     */
    private void setPlayButtonHoverOut()
    {
        btnPlay.setOnMouseExited(new EventHandler() 
            {
                @Override
                public void handle(Event event) 
                {
                    if(buttonPlaySelected)
                    {
                        btnPlay.setGraphic(new ImageView("/privatemoviecollection/gui/images/StopButtonSmall.png"));
                    }
                    else
                    {
                        btnPlay.setGraphic(new ImageView("/privatemoviecollection/gui/images/PlayButtonSmall.png"));
                    }
                }
            
            }
        );
    }
    
    @FXML
    private void clickPlay(ActionEvent event) 
    {
        switchPlayButton(true);
        if(mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING))
        {
            mediaPlayer.pause();
        }
        else
        {
            mediaPlayer.play();
        }
    }

    @FXML
    private void clickMute(ActionEvent event) {
        if(btnMute.isSelected())
        {
            previousVolume = sldVolume.getValue();
            sldVolume.setValue(0);
        }
        else
        {
            if(sldVolume.getValue() == 0)
            {
                btnMute.setSelected(true);
            }
            sldVolume.setValue(previousVolume);
        }
    }

    @FXML
    private void dropTimeSlider(MouseEvent event) {
        mediaPlayer.seek(Duration.seconds(sldTime.getValue()));
        movieTimeChanged=true;
    }

    @FXML
    private void clickClose(ActionEvent event) {
        mediaPlayer.stop();
        Stage stage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void hoverOnHud(MouseEvent event) {
        showPlayerElements();

    }

    @FXML
    private void hoverOffHud(MouseEvent event) {
        if(!mediaPlayer.getStatus().equals(MediaPlayer.Status.PAUSED))
        {
            hidePlayerElements();
        }
    }

    @FXML
    private void clickOnMediaView(MouseEvent event) 
    {
        if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2)
        {
            btnFullscreen.fire();
        }
        if(mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING))
        {
            mediaPlayer.pause();
            switchPlayButton(false);
            showPlayerElements();
        }
        else
        {
            mediaPlayer.play();
            switchPlayButton(false);
            hidePlayerElements();
        }
    }
    
    private void showPlayerElements()
    {
        sldTime.setVisible(true);
        sldVolume.setVisible(true);
        btnMute.setVisible(true);
        btnPlay.setVisible(true);
        btnFullscreen.setVisible(true);
        lblMovieCurrentTime.setVisible(true);
        lblMovieEndTime.setVisible(true);
        lblTitle.setVisible(true);
        lblSlash.setVisible(true);
        rctTop.setVisible(true);
        rctBottom.setVisible(true);
    }
    
    private void hidePlayerElements()
    {
        sldTime.setVisible(false);
        sldVolume.setVisible(false);
        btnMute.setVisible(false);
        btnPlay.setVisible(false);
        btnFullscreen.setVisible(false);
        lblMovieCurrentTime.setVisible(false);
        lblMovieEndTime.setVisible(false);
        lblTitle.setVisible(false);
        lblSlash.setVisible(false);
        rctTop.setVisible(false);
        rctBottom.setVisible(false);
    }

    @FXML
    private void clickMouseDragged(MouseEvent event) {
        Stage stage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() + xOffset);
        stage.setY(event.getScreenY() + yOffset);
    }

    @FXML
    private void clickMousePressed(MouseEvent event) {
        Stage stage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
        xOffset = stage.getX() - event.getScreenX();
        yOffset = stage.getY() - event.getScreenY();
    }

    @FXML
    private void clickFullscreen(ActionEvent event) {
        Stage stage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
        if (stage.isFullScreen())
        {
            stage.setFullScreen(false);
        }
        else
        {
            stage.setFullScreen(true);
        }

    }

    
}
