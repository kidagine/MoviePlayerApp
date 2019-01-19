package privatemoviecollection.gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.EventObject;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import privatemoviecollection.be.Category;
import privatemoviecollection.gui.exceptions.ModelException;
import privatemoviecollection.gui.model.CategoriesModel;
import privatemoviecollection.gui.util.WarningDisplayer;
import privatemoviecollection.gui.util.WindowDecorator;

/**
 * FXML Controller class
 *
 * @author Acer
 */
public class CategoriesViewController implements Initializable {

    private CategoriesModel model;
    private WarningDisplayer warningDisplayer;
    
    @FXML
    private Button btnDelete;
    
    public CategoriesViewController()
    {
        warningDisplayer = new WarningDisplayer();
        try
        {
            model = CategoriesModel.createInstance();
        }
        catch(ModelException e)
        {
            Stage currentStage = (Stage) btnDelete.getScene().getWindow();
            warningDisplayer.displayError(currentStage, "Error", e.getMessage());
        }
    }
    
    @FXML
    private ListView<Category> lstCategories;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lstCategories.setItems(model.getCategories());
        btnDelete.setDisable(true);
    }    

    @FXML
    private void clickNewCategory(ActionEvent event) throws IOException {
        Stage currentStage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
        WindowDecorator.fadeOutStage(currentStage);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/privatemoviecollection/gui/view/NewCategoryView.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("New Category");
        stage.setScene(new Scene(root));
        stage.showAndWait();
        WindowDecorator.fadeInStage(currentStage);
    }

    @FXML
    private void clickOnCategories(MouseEvent event) {
        if(lstCategories.getSelectionModel().getSelectedItem() != null)
        {
            btnDelete.setDisable(false);
        }
    }

    @FXML
    private void clickDelete(ActionEvent event) 
    {
        Category selectedCategory = lstCategories.getSelectionModel().getSelectedItem();
        Stage currentStage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
        Optional<ButtonType> action = warningDisplayer.displayConfirmation(currentStage, "Confirmation", "Are you sure you want to delete \"" + selectedCategory.getName() + "\" from your categories?" + 
                                                                                                                "\nDeleting this category will remove it from all the movies that belong to it.");
        if(action.get() == ButtonType.OK)
        {
            try
            {
                model.deleteCategory(selectedCategory);
            }
            catch(ModelException e)
            {
                warningDisplayer.displayError(currentStage, "Error", e.getMessage());
            }
        }
        lstCategories.getSelectionModel().clearSelection();
        btnDelete.setDisable(true);
    }

    @FXML
    private void clickClose(ActionEvent event) {
        Stage stage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
        stage.close();
    }
    
}
