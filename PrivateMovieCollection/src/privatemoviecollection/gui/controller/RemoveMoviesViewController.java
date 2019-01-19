/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.gui.controller;

import java.net.URL;
import java.util.EventObject;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import privatemoviecollection.be.Movie;
import privatemoviecollection.gui.exceptions.ModelException;
import privatemoviecollection.gui.model.MainModel;
import privatemoviecollection.gui.util.WarningDisplayer;

/**
 * FXML Controller class
 *
 * @author Acer
 */
public class RemoveMoviesViewController implements Initializable {
    
    private MainModel model;
    private WarningDisplayer warningDisplayer;

    @FXML
    private TableView<Movie> tblMovies;
    @FXML
    private TableColumn<Movie, String> colTitle;
    @FXML
    private Button btnDelete;
    @FXML
    private TableColumn<Movie, ImageView> colImage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        warningDisplayer = new WarningDisplayer();
        try 
        {
            model = MainModel.createInstance();
        } 
        catch (ModelException e) 
        {
            Stage currentStage = (Stage) tblMovies.getScene().getWindow();
            warningDisplayer.displayError(currentStage, "Error", e.getMessage());
        }
        loadData();
        btnDelete.setDisable(true);
    }    
    
    private void loadData()
    {
        colTitle.setCellValueFactory(new PropertyValueFactory("title"));
        colImage.setCellValueFactory(new PropertyValueFactory("image"));
        tblMovies.setItems(model.getMoviesToDelete());
    }

    @FXML
    private void clickCancel(ActionEvent event) {
        Stage currentStage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
        currentStage.close();
    }

    @FXML
    private void clickDelete(ActionEvent event) {
        Stage currentStage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
        try
        {
            Movie selectedMovie = tblMovies.getSelectionModel().getSelectedItem();
            model.deleteMovie(selectedMovie);
            tblMovies.getItems().remove(selectedMovie);
            if(tblMovies.getItems().isEmpty())
            {
                currentStage.close();
            }
        }
        catch(ModelException e)
        {
            warningDisplayer.displayError(currentStage, "Error", e.getMessage());
        }
    }

    @FXML
    private void clickOnMovies(MouseEvent event) {
        if(tblMovies.getSelectionModel().getSelectedItem()!=null)
        {
            btnDelete.setDisable(false);
        }
    }

    @FXML
    private void clickClose(ActionEvent event) {
        Stage currentStage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
        currentStage.close();
    }
    
}
