/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.SwipeEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class BackHomeController implements Initializable {


    @FXML
    private Button goPacks;
    @FXML
    private TextField T0;
    private TextField F1;
    @FXML
    private TextArea A1;
    @FXML
    private Button GoQuestion;
    @FXML
    private ImageView I1;
    private TextField F2;
    @FXML
    private TextArea A2;
    @FXML
    private ImageView I2;
    @FXML
    private ToggleButton Tbutton;
    @FXML
    private ToggleButton Tbutton1;
    @FXML
    private Button video;
    
    


    /**
     * Initializes the controller class.
     */
    @Override
    
    public void initialize(URL url, ResourceBundle rb) {
        
          
       A1.setVisible(true);
       I1.setVisible(true);
       goPacks.setVisible(true);
       Tbutton.setVisible(true);
       A2.setVisible(false);
       I2.setVisible(false);
       GoQuestion.setVisible(false);
       Tbutton1.setVisible(false);

      
       

    }    


    @FXML
    private void ongoPacksCLicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Packs.fxml"));
        Parent root = loader.load();
        goPacks.getScene().setRoot(root);
    }

    @FXML
    private void OnQuestionClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Chat.fxml"));
        Parent root = loader.load();
        goPacks.getScene().setRoot(root); 
    }

   

    @FXML
    private void OnSR(ActionEvent event) {
        
         
           A1.setVisible(false);
           I1.setVisible(false);
           goPacks.setVisible(false);
           Tbutton.setVisible(false);
           A2.setVisible(true);
           I2.setVisible(true);
           GoQuestion.setVisible(true);
           Tbutton1.setVisible(true);

    }

    @FXML
    private void OnCommencer(ActionEvent event) {
         A1.setVisible(true);
       I1.setVisible(true);
       goPacks.setVisible(true);
       Tbutton.setVisible(true);
       A2.setVisible(false);
       I2.setVisible(false);
       GoQuestion.setVisible(false);
       Tbutton1.setVisible(false);
       
    }

    @FXML
    private void OnVideoClicked(ActionEvent event) throws IOException {
        
         FXMLLoader loader = new FXMLLoader(getClass().getResource("video.fxml"));
        Parent root = loader.load();
        goPacks.getScene().setRoot(root); 
    }



    
}
