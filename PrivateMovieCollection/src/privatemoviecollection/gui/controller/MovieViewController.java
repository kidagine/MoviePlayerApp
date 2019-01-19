/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.gui.controller;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import privatemoviecollection.be.Category;
import privatemoviecollection.be.Movie;
import privatemoviecollection.bll.util.TimeConverter;
import privatemoviecollection.gui.exceptions.ModelException;
import privatemoviecollection.gui.model.CategoriesModel;
import privatemoviecollection.gui.model.MainModel;
import privatemoviecollection.gui.util.WarningDisplayer;

/**
 * FXML Controller class
 *
 * @author Acer
 */
public class MovieViewController implements Initializable {
    
    private MainModel mainModel;
    private CategoriesModel categoriesModel;
    private boolean editing;
    private Movie editingMovie;
    private  WarningDisplayer warningDisplayer;
    
    @FXML
    private TextField txtTitle;
    @FXML
    private TextField txtFile;
    @FXML
    private TextField txtTime;
    @FXML
    private ComboBox<String> cmbRating;
    @FXML
    private ListView<Category> lstSelectedCategories;
    @FXML
    private ComboBox<Category> cmbCategories;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;
    @FXML
    private TextField txtImage;

    public MovieViewController()
    {
        warningDisplayer = new WarningDisplayer();
        try
        {
            mainModel = MainModel.createInstance();
            categoriesModel = CategoriesModel.createInstance();
        }
        catch(ModelException e)
        {
            Stage currentStage = (Stage) txtTitle.getScene().getWindow();
            warningDisplayer.displayError(currentStage, "Error", e.getMessage());
        }
        editing = false;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbCategories.setItems(categoriesModel.getCategories());
        createRatingCombo();
        disableElements();
    } 
    
    public void disableElements()
    {
        btnSave.setDisable(true);
        txtTime.setDisable(true);
        txtFile.setDisable(true);
        txtImage.setDisable(true);

    }
    
    private void createRatingCombo()
    {
        ObservableList<String> ratings = FXCollections.observableArrayList();
        ratings.add("-");
        for(int i = 1; i <= 10; i++) 
        {
            ratings.add(Integer.toString(i));          
        }
        cmbRating.setItems(ratings);
        
    }
    
    public void setEditingMode(Movie editingMovie)
    {
        editing = true;
        this.editingMovie = editingMovie;
        txtTitle.setText(editingMovie.getTitle());
        for(Category category : editingMovie.getCategories())
        {
            lstSelectedCategories.getItems().add(category);
        }
        cmbRating.getSelectionModel().select(editingMovie.getRatingInString());
        txtFile.setText(editingMovie.getPath());
        txtTime.setText(TimeConverter.convertToString(editingMovie.getTime()));
        txtImage.setText(editingMovie.getImagePath());
        txtTitle.setFocusTraversable(false);
    }

    @FXML
    private void clickChooseFilePath(ActionEvent event) {
        FileChooser fileChooser = createMovieChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if(selectedFile != null)
        {
            txtFile.setText(selectedFile.getPath());
            setTimeField(selectedFile);
        }
        checkInputs();
    }
    
    private FileChooser createMovieChooser()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a movie");
        FileChooser.ExtensionFilter generalFilter = new FileChooser.ExtensionFilter("All Video Files", "*.mpeg4", "*.mp4", "*.m4a", "*.m4v");
        FileChooser.ExtensionFilter mpeg4Filter = new FileChooser.ExtensionFilter("MPEG4 (*.mpeg4)", "*.mpeg4");
        FileChooser.ExtensionFilter mp4Filter = new FileChooser.ExtensionFilter("MP4 (*.mp4, *.m4a, *.m4v)","*.mp4", "*.m4a", "*.m4v");
        fileChooser.getExtensionFilters().add(generalFilter);
        fileChooser.getExtensionFilters().add(mpeg4Filter);
        fileChooser.getExtensionFilters().add(mp4Filter);       
        return fileChooser;
    }
    
    private FileChooser createImageChooser()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an image");
        FileChooser.ExtensionFilter generalFilter = new FileChooser.ExtensionFilter("All Image Files", "*.png", "*.jpg", "*.jpeg");
        FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("PNG (*.png)", "*.png");
        FileChooser.ExtensionFilter jpegFilter = new FileChooser.ExtensionFilter("JPEG (*.jpeg, *.jpg)","*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(generalFilter);
        fileChooser.getExtensionFilters().add(pngFilter);
        fileChooser.getExtensionFilters().add(jpegFilter);  
        return fileChooser;

    }
    
    public void setTimeField(File selectedFile)
    {
        Media mediaFile = new Media(selectedFile.toURI().toString());
        MediaPlayer player = new MediaPlayer(mediaFile);
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        Runnable task = new Runnable() {
            @Override
            public void run() {
                int time = (int) player.getMedia().getDuration().toSeconds();
                txtTime.setText(TimeConverter.convertToString(time));
            }
            
        };
        scheduler.schedule(task, 200, TimeUnit.MILLISECONDS);
        scheduler.shutdown();
    }

    @FXML
    private void clickSelectCategory(ActionEvent event) 
    {
        Category selectedCategory = cmbCategories.getSelectionModel().getSelectedItem();
        if(!lstSelectedCategories.getItems().contains(selectedCategory))
        {
            lstSelectedCategories.getItems().add(selectedCategory);
        }
        checkInputs();
    }
    
    public void checkInputs()
    {
        if(txtTitle.getText().isEmpty() || txtFile.getText().isEmpty() || txtImage.getText().isEmpty())
        {
            btnSave.setDisable(true);
        }
        else
        {
            btnSave.setDisable(false);
        }
    }

    @FXML
    private void clickCancel(ActionEvent event) 
    {
        Stage stage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void clickSave(ActionEvent event) 
    {
        String movieTitle = txtTitle.getText();
        List<Category> movieCategories = new ArrayList();
        for(Category category : lstSelectedCategories.getItems())
        {
            movieCategories.add(category);
        }
        String moviePath = txtFile.getText();
        int time = TimeConverter.convertToInt(txtTime.getText());
        Integer movieRating = parseRating(cmbRating.getSelectionModel().getSelectedItem());
        String imagePath = txtImage.getText();
        Stage stage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
        if(editing)
        {
            try
            {
                mainModel.updateMovie(editingMovie, movieTitle, movieCategories, moviePath, time, movieRating, imagePath);
            }
            catch(ModelException e)
            {
                warningDisplayer.displayError(stage, "Error", e.getMessage());
            }
        }
        else
        {
            try
            {
                mainModel.createMovie(movieTitle, movieCategories, moviePath, time, movieRating, imagePath);
            }
            catch(ModelException e)
            {
                warningDisplayer.displayError(stage, "Error", e.getMessage());
            }
        }
        stage.close();
    }
    
    private Integer parseRating(String ratingInString)
    {
        if(ratingInString == null || ratingInString.equals("-"))
        {
            return null;
        }
        else
        {
            return Integer.parseInt(ratingInString);
        }
    }

    @FXML
    private void keyTitleTyped(KeyEvent event) 
    {
        checkInputs();
    }

    @FXML
    private void clickSelectRating(ActionEvent event) {
        checkInputs();
    }

    @FXML
    private void clickClose(ActionEvent event) {
        Stage stage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void clickChooseImagePath(ActionEvent event) 
    {
         FileChooser fileChooser = createImageChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if(selectedFile != null)
        {
            txtImage.setText(selectedFile.getPath());
        }
        checkInputs();
    }

    @FXML
    private void clickOnCategories(MouseEvent event) {
        MouseButton button = event.getButton();
        if(button==MouseButton.SECONDARY)
        {
            if (lstSelectedCategories.getItems() != null)
            {
                lstSelectedCategories.getItems().remove(lstSelectedCategories.getSelectionModel().getSelectedItem());
                lstSelectedCategories.getSelectionModel().clearSelection();
            }
        }
        else if(button==MouseButton.PRIMARY)
        {
            lstSelectedCategories.getSelectionModel().clearSelection();
        }
    }
    
}
