package privatemoviecollection.gui.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import privatemoviecollection.be.User;
import privatemoviecollection.gui.exceptions.ModelException;
import privatemoviecollection.gui.model.UserModel;
import privatemoviecollection.gui.util.WarningDisplayer;

/**
 * The {@code CreateUserViewController} class is a controller for
 * {@code CreateUserView}. It is responsible for sending request to model
 * to create new user and for checking conditions for users address e-mail
 * and password.
 * 
 * @author schemabuoi
 * @author kiddo
 */

public class CreateUserViewController implements Initializable {
    
    private UserModel model;
    private WarningDisplayer warningDisplayer;
    
    @FXML
    private TextField txtEmail;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private PasswordField txtRepeatPassword;
    
    /**
     * Creates connection with {@code UserModel} instance.
     */
    public CreateUserViewController()
    {
        model = UserModel.createInstance();
        warningDisplayer = new WarningDisplayer();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    /**
     * Method which is invoked after clicking Create button on 
     * {@code CreateUserView}. Its responsibility is creating a new user - it is
     * displaying warnings if user data are incorrect and sending request 
     * to model to create a new user.
     */
    @FXML
    private void clickCreate(ActionEvent event) {
        Stage currentStage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
        if(!isEmailCorrect(txtEmail.getText()))
        {
            warningDisplayer.displayError(currentStage, "Cannot create user", "Address e-mail is invalid");
        }       
        else
        {
            List<String> faults = getPasswordFaults(txtPassword.getText());
            if(!faults.isEmpty())
            {
                String errorText = "";
                for(String s : faults)
                {
                    errorText += "- " + s + "\n";
                }
                warningDisplayer.displayError(currentStage, "Cannot create user", errorText);
            }
            else if(!txtPassword.getText().equals(txtRepeatPassword.getText()))
            {
                warningDisplayer.displayError(currentStage, "Cannot create user", "Your passwords do not match");
            }
            else
            {
                try
                {
                    User user = model.createUser(txtEmail.getText(), txtPassword.getText());
                    currentStage.close();
                }
                catch(ModelException e)
                {
                    warningDisplayer.displayError(currentStage, "Error", e.getMessage());
                }
            }
        }
    }
    
    /**
     * Checks if given e-mail address is correct.
     * 
     * @param email The e-mail to check
     * @return true if e-mail is correct.
     */
    private boolean isEmailCorrect(String email)
    {
        if(email.length()<5 || !email.contains("@") || !email.contains("."))
        {
            return false;
        }
        return true;
    }
    
    /**
     * Returns all faults of given password as a list of strings.
     * 
     * @param password The password to check.
     * @return List of strings.
     */
    private List<String> getPasswordFaults(String password)
    {
        List<String> faults = new ArrayList();
        if(password.length()<7)
        {
            faults.add("Your password has to contain at least 8 characters");
        }
        if(password.equals(password.toLowerCase()) || password.equals(password.toUpperCase()))
        {
            faults.add("Your password has to contain at least one upper case and one lower case letter");
        }
        if(!password.matches(".*\\d+.*"))
        {
            faults.add("Your password has to contain at least one number");
        }
        if(password.matches("[a-zA-Z0-9]*"))
        {
            faults.add("Your password has to contain at least one special character");
        }
        return faults;

    }
    
    /**
     * Method which is invoked after clicking on Close
     * button on {@code CreateUserView}. It is closing the
     * current stage.
     */
    @FXML
    private void clickClose(ActionEvent event) {
        Stage stage = (Stage)((Node)((EventObject) event).getSource()).getScene().getWindow();
        stage.close();
    }
    
}
