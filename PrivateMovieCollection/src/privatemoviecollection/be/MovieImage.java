/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package privatemoviecollection.be;

import java.io.File;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Acer
 */
public class MovieImage {
    
    private ImageView image;
    private String imagePath;

    public MovieImage(String pathToImage)
    {
        imagePath = pathToImage;
        File file = new File(pathToImage);
        image = new ImageView(new Image(file.toURI().toString()));
        image.setFitHeight(73);
        image.setFitWidth(47);
    }
    
    public ImageView getImage() {
        ImageView image = new ImageView(new File(imagePath).toURI().toString());
        image.setFitHeight(73);
        image.setFitWidth(47);
        return image;
    }
    
    public void setImage(String pathToImage)
    {
        imagePath = pathToImage;
        File file = new File(pathToImage);
        image = new ImageView(new Image(file.toURI().toString()));
        image.setFitHeight(73);
        image.setFitWidth(47);
    }
    
    public String getImagePath()
    {
        return imagePath;
    }
    
    
}
