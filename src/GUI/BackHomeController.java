/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author HP
 */
public class BackHomeController implements Initializable {

    @FXML
    private Button GoBonus;
    @FXML
    private Button goPacks;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void OngoBonusClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Bonuss.fxml"));
        Parent root;
        root = loader.load();
        GoBonus.getScene().setRoot(root);
        
    }

    @FXML
    private void ongoPacksCLicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Packs.fxml"));
        Parent root = loader.load();
        goPacks.getScene().setRoot(root);
    }
    
}
