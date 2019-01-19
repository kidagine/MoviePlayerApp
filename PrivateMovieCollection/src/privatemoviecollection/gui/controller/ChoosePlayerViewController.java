/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.EventObject;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import privatemoviecollection.be.Movie;

/**
 * FXML Controller class
 *
 * @author Acer
 */
public class ChoosePlayerViewController implements Initializable {
    
    private Movie movieToPlay;
    
    @FXML
    private ListView<String> lstPlayerSelection;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lstPlayerSelection.getItems().add("Windows Media Player");
        lstPlayerSelection.getItems().add("Bonereel Player");
        lstPlayerSelection.getSelectionModel().select("Windows Media Player");

    }    

    @FXML
    private void clickOk(ActionEvent event) throws IOException 
    {
        if(lstPlayerSelection.getSelectionModel().getSelectedItem().equals("Windows Media Player"))
        {
            Stage currentStage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
            currentStage.close();
            Process process = new ProcessBuilder("C:\\Program Files (x86)\\Windows Media Player\\wmplayer.exe", movieToPlay.getPath(), "/fullscreen").start();

        }
        else
        {
            Stage currentStage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
            currentStage.close();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/privatemoviecollection/gui/view/MoviePlayerView.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
            MoviePlayerViewController controller = (MoviePlayerViewController) fxmlLoader.getController();
            controller.playMovie(movieToPlay);
        }
    }
    
    public void setMovieToPlayer(Movie movie)
    {
        movieToPlay = movie;
    }

    @FXML
    private void clickClose(ActionEvent event) {
        Stage currentStage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
        currentStage.close();
    }
    
}
