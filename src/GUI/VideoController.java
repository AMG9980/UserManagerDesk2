/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class VideoController implements Initializable {
    

    @FXML
    private MediaView mediaview;
private MediaPlayer mediaPlayer;
    private Media media;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
   
    
        String path = new File("C:\\xampp\\htdocs\\Gestion_Pack\\src\\Media\\ayoub.mp4").getAbsolutePath();
        media=new Media(new File(path).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaview.setMediaPlayer(mediaPlayer);
        mediaPlayer.setAutoPlay(true);
    }    
    
}