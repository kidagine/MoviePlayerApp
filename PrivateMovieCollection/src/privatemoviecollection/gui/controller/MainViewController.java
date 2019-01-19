package privatemoviecollection.gui.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.EventObject;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import privatemoviecollection.be.Category;
import privatemoviecollection.be.Movie;
import privatemoviecollection.gui.exceptions.ModelException;
import privatemoviecollection.gui.model.CategoriesModel;
import privatemoviecollection.gui.model.MainModel;
import privatemoviecollection.gui.util.WarningDisplayer;
import privatemoviecollection.gui.util.WindowDecorator;

/**
 *
 * @author Acer
 */
public class MainViewController implements Initializable {
    
    private MainModel mainModel;
    private CategoriesModel categoriesModel;
    private double xOffset;
    private double yOffset;
    private WarningDisplayer warningDisplayer;
    
    @FXML
    private TableView<Movie> tblMovies;
    @FXML
    private TableColumn<Movie, String> colTitle;
    @FXML
    private TableColumn<Movie, String> colCategories;
    @FXML
    private TableColumn<Movie, Integer> colTime;
    @FXML
    private TableColumn<Movie, String> colRating;
    @FXML
    private TableColumn<Movie, ImageView> colImage;
    @FXML
    private Button btnRemoveMovie;
    @FXML
    private Button btnEditMovie;
    @FXML
    private ComboBox<Category> cmbCategories;
    @FXML
    private ListView<Category> lstSelectedCategories;
    @FXML
    private ComboBox<String> cmbRating;
    @FXML
    private TextField txtSearchMovies;
    
    public MainViewController()
    {
        warningDisplayer = new WarningDisplayer();
        try
        {
            mainModel = MainModel.createInstance();
            categoriesModel = CategoriesModel.createInstance();
        }
        catch(ModelException e)
        {
            Stage currentStage = (Stage) tblMovies.getScene().getWindow();
            warningDisplayer.displayError(currentStage, "Error", e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        disableElements();
        loadData();
        createRatingComboBox();        
    }    
    
    private void createRatingComboBox()
    {
        ObservableList<String> ratings = FXCollections.observableArrayList();
        ratings.add("-");
        for(int i = 1; i < 10; i++) 
        {
            ratings.add("≥ " + i + ".0");          
        }
        cmbRating.setItems(ratings);
        
    }
        
    
    private void disableElements()
    {
        btnEditMovie.setDisable(true);
        btnRemoveMovie.setDisable(true);
    }
    
    private void loadData()
    {
        //sets table view with movies
        colTitle.setCellValueFactory(new PropertyValueFactory("title"));
        colCategories.setCellValueFactory(new PropertyValueFactory("categoriesInString"));
        colTime.setCellValueFactory(new PropertyValueFactory("timeInString"));
        colRating.setCellValueFactory(new PropertyValueFactory("ratingInString"));
        colImage.setCellValueFactory(new PropertyValueFactory("image"));
        tblMovies.setItems(mainModel.getMovies());
        
        //sets combobox with categories
        cmbCategories.setItems(categoriesModel.getCategories());
        
    }
    
    @FXML
    private void clickEditCategories(ActionEvent event) throws IOException {
        Stage currentStage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
        WindowDecorator.fadeOutStage(currentStage);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/privatemoviecollection/gui/view/CategoriesView.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Edit Categories");
        stage.setScene(new Scene(root));
        stage.showAndWait();
        WindowDecorator.fadeInStage(currentStage);
    }

    @FXML
    private void clickAddMovie(ActionEvent event) throws IOException {
        Stage currentStage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
        WindowDecorator.fadeOutStage(currentStage);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/privatemoviecollection/gui/view/MovieView.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("New Movie");
        stage.setScene(new Scene(root));
        stage.showAndWait();
        WindowDecorator.fadeInStage(currentStage);
    }

    @FXML
    private void clickEditMovie(ActionEvent event) throws IOException {
        Movie selectedMovie = tblMovies.getSelectionModel().getSelectedItem();
        Stage currentStage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
        WindowDecorator.fadeOutStage(currentStage);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/privatemoviecollection/gui/view/MovieView.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        MovieViewController controller = (MovieViewController) fxmlLoader.getController();
        controller.setEditingMode(selectedMovie);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Edit Movie");
        stage.setScene(new Scene(root));
        stage.showAndWait();
        WindowDecorator.fadeInStage(currentStage);
    }

    @FXML
    private void clickOnMovies(MouseEvent event) throws IOException {
        Movie selectedMovie = tblMovies.getSelectionModel().getSelectedItem();
        if(selectedMovie != null)
        {
            if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2)
            {
                Stage currentStage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
                try
                {
                    tryToCreateMediaPlayer(selectedMovie);
                    
                    mainModel.updateMovieLastView(selectedMovie);
                    WindowDecorator.fadeOutStage(currentStage);
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/privatemoviecollection/gui/view/ChoosePlayerView.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    ChoosePlayerViewController controller = fxmlLoader.getController();
                    controller.setMovieToPlayer(selectedMovie);
                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setTitle("Choose player");
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.setScene(new Scene(root));              
                    stage.showAndWait();
                    WindowDecorator.fadeInStage(currentStage);
                }
                catch(ModelException e)
                {
                    warningDisplayer.displayError(currentStage, "Error", e.getMessage());
                }
                catch(MediaException e)
                {
                    warningDisplayer.displayError(currentStage, "Error", e.getMessage());
                }
            }
            btnEditMovie.setDisable(false);
            btnRemoveMovie.setDisable(false);
        }
    }

    private void tryToCreateMediaPlayer(Movie selectedMovie) throws MediaException
    {
        File file = new File(selectedMovie.getPath());
        Media mediaFile = new Media(file.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(mediaFile); 
    }
    
    @FXML
    private void clickRemoveMovie(ActionEvent event) {
        Movie selectedMovie = tblMovies.getSelectionModel().getSelectedItem();
        Stage currentStage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
        Optional<ButtonType> action = warningDisplayer.displayConfirmation(currentStage, "Confirmation", "Are you sure you want to delete \"" + selectedMovie.getTitle() + " from your movies?");
        if(action.get() == ButtonType.OK)
        {
            try
            {
                mainModel.deleteMovie(selectedMovie);
            }
            catch(ModelException e)
            {
                warningDisplayer.displayError(currentStage, "Error", e.getMessage());
            }
        }
    }

    @FXML
    private void clickSelectCategory(ActionEvent event) 
    {
        Category selectedCategory = cmbCategories.getSelectionModel().getSelectedItem();
        if(!lstSelectedCategories.getItems().contains(selectedCategory))
        {
            lstSelectedCategories.getItems().add(selectedCategory);
            filterMovies();
        }
    }

    @FXML
    private void inputSearchMovies(KeyEvent event) 
    {
        filterMovies();
    }
    
    public ObservableList<Movie> getFilteredMovies()
    {
        List<Category> categories = lstSelectedCategories.getItems();
        String filter = txtSearchMovies.getText().trim();
        Integer rating = parseRatingFromSearchingComboBox(cmbRating.getSelectionModel().getSelectedItem());
        return mainModel.getFilteredMovies(categories, filter, rating);
    }
    
    private Integer parseRatingFromSearchingComboBox(String rating)
    {
        if(rating == null || rating.equals("-"))
        {
            return null;
        }
        else
        {
            return Integer.parseInt(rating.substring(2, 3));
        }
    }

    @FXML
    private void clickSearchByRating(ActionEvent event) 
    {
        filterMovies();
    }
    
    @FXML
    private void clickCategory(MouseEvent event) 
    {
        MouseButton button = event.getButton();
        if(button==MouseButton.SECONDARY)
        {
            if (lstSelectedCategories.getItems() != null)
            {
                lstSelectedCategories.getItems().remove(lstSelectedCategories.getSelectionModel().getSelectedItem());
                lstSelectedCategories.getSelectionModel().clearSelection();
                filterMovies();
            }
        }
        else if(button==MouseButton.PRIMARY)
        {
            lstSelectedCategories.getSelectionModel().clearSelection();
        }
    }
    
    private void filterMovies()
    {
        tblMovies.setItems(getFilteredMovies());
        clearMoviesTableSelection();
    }
    
    private void clearMoviesTableSelection()
    {
        tblMovies.getSelectionModel().clearSelection();
        btnEditMovie.setDisable(true);
        btnRemoveMovie.setDisable(true);
    }

    @FXML
    private void clickClose(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void clickMinimize(ActionEvent event) {
        Stage stage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
        stage.setIconified(true);
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
    
}
