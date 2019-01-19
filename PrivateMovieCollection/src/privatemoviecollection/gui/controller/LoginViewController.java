package privatemoviecollection.gui.controller;

import java.io.IOException;
import java.net.URL;
import java.util.EventObject;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import privatemoviecollection.be.User;
import privatemoviecollection.gui.exceptions.ModelException;
import privatemoviecollection.gui.model.MainModel;
import privatemoviecollection.gui.model.UserModel;
import privatemoviecollection.gui.util.WarningDisplayer;
import privatemoviecollection.gui.util.WindowDecorator;

/**
 * The {@code LoginViewController} class is a controller for
 * {@code LoginView}. It is responsible for getting validation of
 * users data from database through the {@code UserModel} instance and
 * for passing to {@code UserModel} informations about logged in user.
 * If logging in went correct it is displaying {@code MainView} of our
 * application.
 * 
 * @author schemabuoi
 * @author kiddo
 */

public class LoginViewController implements Initializable {
    
    private UserModel userModel;
    private double xOffset;
    private double yOffset;
    private WarningDisplayer warningDisplayer;

    @FXML
    private Button btnLogin;
    @FXML
    private TextField txtEmail;
    @FXML
    private PasswordField txtPassword;

    /**
     * Creates a connection with {@code UserModel} instance 
     * and initializes WarningDisplayer object.
     */
    public LoginViewController()
    {
        userModel = UserModel.createInstance();
        warningDisplayer = new WarningDisplayer();
    }
    
    /**
     * Initializes the controller class. Invokes method for creating text fields
     * listeners.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        createTextFieldListeners();
    }    
    
    /**
     * Creates a listeners for text fields with password and e-mail address.
     * Listeners check if enter button was pressed when one of this field was selected
     * and performs specific action according to selected text field.
     */
    private void createTextFieldListeners()
    {
        txtEmail.setOnKeyPressed(new EventHandler<KeyEvent>()
                {
                    @Override
                    public void handle(KeyEvent key) 
                    {
                        if(key.getCode() == KeyCode.ENTER)
                        {
                            txtPassword.requestFocus();
                        }
                    }

                }
            );
        txtPassword.setOnKeyPressed(new EventHandler<KeyEvent>()
                {
                    @Override
                    public void handle(KeyEvent key) 
                    {
                        if(key.getCode() == KeyCode.ENTER)
                        {
                            btnLogin.fire();
                        }
                    }

                }
            );
    }

    /**
     * Method which is invoked after clicking Sign In button on {@code LoginView}.
     * It opens a {@code CreateUserView}.
     */
    @FXML
    private void clickCreateAccount(ActionEvent event) throws IOException {
        Stage currentStage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
        WindowDecorator.fadeOutStage(currentStage);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/privatemoviecollection/gui/view/CreateUserView.fxml"));
        Parent root = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Sign In");
        stage.setScene(new Scene(root));  
        stage.showAndWait();
        WindowDecorator.fadeInStage(currentStage);
    }

    /**
     * Method which is invoked after clicking Log In button on {@code loginView}.
     * It gets a information about user with data typed on the text field from database
     * through the {@code UserModel} instance. If data are correct it openes the {@code MainView}
     * otherwise it displays an error. It also ivokes {@code showRemoveMoviesView} method at the end.
     */
    @FXML
    private void clickLogin(ActionEvent event) throws IOException {
        try
        {
            User user = userModel.getUser(txtEmail.getText(), txtPassword.getText());
            Stage mainStage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
            mainStage.hide();
            userModel.setUser(user);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/privatemoviecollection/gui/view/MainView.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            mainStage.setScene(new Scene(root));  
            mainStage.centerOnScreen();
            WindowDecorator.showStage(mainStage);
            
            showRemoveMoviesView(mainStage);
            
        }
        catch(ModelException e)
        {
            Stage currentStage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
            warningDisplayer.displayError(currentStage, "Error", e.getMessage());
        }
    }
    
    /**
     * Gets an information if user have some movies recommended
     * to delete - if yes, it opens {@code RemoveMoviesView}.
     */
    private void showRemoveMoviesView(Stage mainStage) throws ModelException, IOException
    {
        MainModel mainModel = MainModel.createInstance();
        if(!mainModel.getMoviesToDelete().isEmpty())
        {
            WindowDecorator.fadeOutStage(mainStage);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/privatemoviecollection/gui/view/RemoveMoviesView.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(new Scene(root));
            stage.showAndWait();
            WindowDecorator.fadeInStage(mainStage);
        }
    }

    /**
     * Method which is invoked after clicking on Close
     * button on {@code LoginView}. It is closing the
     * current stage.
     */
    @FXML
    private void clickClose(ActionEvent event) {
        Platform.exit();
    }

    /**
     * Method which is invoked after clicking on Minimalize
     * button on {@code LoginView}. It is minimalizing the
     * current stage.
     */
    @FXML
    private void clickMinimalize(ActionEvent event) {
        Stage stage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    /**
     * Method which is invoked after clicking on a top stage bar on {@code LoginView}. It performs a moving stage
     * while dragging top stage bar.
     */
    @FXML
    private void clickMouseDragged(MouseEvent event) 
    {
        Stage stage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() + xOffset);
        stage.setY(event.getScreenY() + yOffset);
    }

    /**
     * Method which is invoked after clicking on a top stage bar. It sets the data necessary for moving
     * a stage.
     */
    @FXML
    private void clickMousePressed(MouseEvent event) 
    {
        Stage stage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
        xOffset = stage.getX() - event.getScreenX();
        yOffset = stage.getY() - event.getScreenY();
    }
    
}
