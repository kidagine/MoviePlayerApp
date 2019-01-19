package privatemoviecollection.gui.util;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * The {@code WindowDecorator} class is responsible for
 * visual effects for windows.
 * @author schemabuoi
 * @author kiddo
 */

public class WindowDecorator {
    
    /**
     * Lowers the opacity of the given stage in a specified timeframe.
     * 
     * @param stage the stage that this effect is applied to.
     */
    public static void fadeOutStage(Stage stage)
    {
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(500),
                new KeyValue (stage.getScene().getRoot().opacityProperty(), 0.9))); 
            timeline.play();
    }
    
    /**
     * Resets the opacity of the given stage in a specified timeframe.
     * 
     * @param stage the stage that this effect is applied to.
     */
    public static void fadeInStage(Stage stage)
    {
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.millis(1000),
                new KeyValue (stage.getScene().getRoot().opacityProperty(), 1)));   
        timeline.play();
    }
    
    /**
     * It sets the given stage's opacity to 0 on start-up
     * and then resets it in a specified timeframe.
     * 
     * @param stage the stage that this effect is applied to.
     */
    public static void showStage(Stage stage)
    {
        stage.setOpacity(0);
        stage.setOnShowing(new EventHandler() 
            {
                @Override
                public void handle(Event event) 
                {
                    Timeline opacity = new Timeline(
                        new KeyFrame(Duration.millis(800),
                            new KeyValue (stage.opacityProperty(), 1)));   
                    opacity.play();                 
                }     
            }   
        );
        stage.show();
    }
    
}
