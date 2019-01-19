package privatemoviecollection.gui.util;

import java.util.Optional;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * The {@code WarningDisplayer} class is responsible for
 * displaying warning windows.
 * @author schemabuoi
 * @author kiddo
 */

public class WarningDisplayer {
    
     /**
     * Displays an error window in the given stage
     * with a specified header and text content.
     * 
     * @param currentStage the stage to display this error window to.
     * @param header the header that will be set on the error window's title.
     * @param content the content that will be set on the error window's text area.
     */
    
    public void displayError(Stage currentStage, String header, String content)
    {
        WindowDecorator.fadeOutStage(currentStage);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(header);
        alert.setHeaderText(null);
        alert.setContentText(content);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/privatemoviecollection/gui/css/Alert.css").toExternalForm());  
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setOnCloseRequest(new EventHandler()
            {
                @Override
                public void handle(Event event) 
                {
                    WindowDecorator.fadeInStage(currentStage);
                }
            }
        );
        Optional<ButtonType> action = alert.showAndWait();
    }
    
     /**
     * Displays a confirmation window in the given stage
     * with a specified header and text content.
     * 
     * @param currentStage the stage to display this confirmation window to.
     * @param header the header that will be set on the confirmation window's title.
     * @param content the content that will be set on the confirmation window's text area.
     */
     
    public Optional<ButtonType> displayConfirmation(Stage currentStage, String header, String content)
    {
        WindowDecorator.fadeOutStage(currentStage);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(header);
        alert.setHeaderText(null);
        alert.setContentText(content);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/privatemoviecollection/gui/css/Alert.css").toExternalForm()); 
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setOnCloseRequest(new EventHandler()
            {
                @Override
                public void handle(Event event) 
                {
                    WindowDecorator.fadeInStage(currentStage);
                }
            }
        );
        return alert.showAndWait();
    }
    
}
